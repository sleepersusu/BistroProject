package com.example.bistro.backstage.menu;

import java.io.IOException;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;



@Controller
public class MenuController {

	@Autowired
	private MenuService menuService;



	private static final long MAX_FILE_SIZE = 8 * 1024 * 1024;
	
	@PostMapping("/Bistro/postMenu")
	public String postMenu(@RequestParam String productCategory, @RequestParam String productName,
			@RequestParam MultipartFile productImage, @RequestParam Integer productPrice,
			@RequestParam String productDescribe, @RequestParam Integer productCount,
			@RequestParam Integer minproductCount, @RequestParam(defaultValue = "0.0") Double avgScore,
			@RequestParam String menuStatus) throws IOException {

		long maxSize = 8 * 1024 * 1024;

		// 檢查檔案大小
		if (productImage.getSize() > maxSize) {
			System.out.println("檔案大小超過限制！最大檔案大小為 8MB。");
			throw new IOException("檔案大小超過限制！最大檔案大小為 8MB。");
		}

		byte[] fileBytes = productImage.getBytes();
		String originalFilename = productImage.getOriginalFilename();

		Menu newMenu = new Menu();
		newMenu.setProductCategory(productCategory);
		newMenu.setProductName(productName);
		newMenu.setProductImg(fileBytes);
		newMenu.setProductPrice(productPrice);
		newMenu.setProductDescribe(productDescribe);
		newMenu.setProductImgUrl(originalFilename);
		newMenu.setProductCount(productCount);
		newMenu.setMinproductCount(minproductCount);

		newMenu.setMenuStatus(menuStatus);

		if (avgScore == null) {
			newMenu.setAvgScore(0.0);
		} else {
			newMenu.setAvgScore(avgScore);
		}

		newMenu.setMenuStatus(menuStatus);

		menuService.createMenu(newMenu);
		return "redirect:/Bistro/findAllMenu";

	}

	@GetMapping("/Bistro/findAllMenu")
	public String findAllMenu(Model model) {
		// 查詢所有菜單
		List<Menu> menuList = menuService.findAllMenu();
		// 查找庫存不足且狀態為"上架"的餐點
		List<String> lowStockItems = menuList.stream()
				.filter(menu -> menu.getProductCount() < menu.getMinproductCount() && "上架".equals(menu.getMenuStatus()))
				.map(Menu::getProductName).collect(Collectors.toList());
		// 將庫存不足的商品名稱傳遞給前端
		model.addAttribute("lowStockItems", lowStockItems);

		model.addAttribute("allMenu", menuList);

		return "menu/showAllMenuView";
	}

	@PostMapping("/Bistro/updateMenuPost")
	@Transactional
	public String updateMenuPost(@ModelAttribute Menu menu, 
			@RequestParam("productImage") MultipartFile file) {
	    try {
	        if (file.getSize() > MAX_FILE_SIZE) {
	            throw new IOException("檔案大小超過限制！最大檔案大小為 8MB。");
	        }
	        
	        Menu existingMenu = menuService.findMenuById(menu.getID());
	        
	        if (file.isEmpty()) {
	            if (existingMenu != null) {
	                menu.setProductImg(existingMenu.getProductImg());
	                menu.setProductImgUrl(existingMenu.getProductImgUrl());
	            }
	        } else {
	            byte[] imageData = file.getBytes();
	            menu.setProductImg(imageData);
	            menu.setProductImgUrl(file.getOriginalFilename());
	        }
	        
	        menuService.updateMenu(menu);
	        return "redirect:/Bistro/findAllMenu";
	    } catch (IOException e) {
	        
	        return "menu/errorPage";
	    }
	}


	@PostMapping("/Bistro/deleteMenu") // 下架商品
	public String deleteMenu(@RequestParam Integer ID) {

		menuService.deleteMenu(ID);

		return "redirect:/Bistro/findAllMenu";

	}

	@PostMapping("/Bistro/recoverMenu") // 下架商品恢復成上架
	public String recoverMenu(@RequestParam Integer ID) {

		menuService.recoverMenu(ID);

		return "redirect:/Bistro/findAllMenu";

	}

}
