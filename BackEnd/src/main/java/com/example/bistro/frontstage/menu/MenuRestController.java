package com.example.bistro.frontstage.menu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuRepository;
import com.example.bistro.backstage.menu.MenuService;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class MenuRestController {
	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepo;

	@GetMapping("/api/{ID}/menu")
	public ResponseEntity<Menu> findMenuByMenuId(@PathVariable Integer ID) {

		Menu menu = menuService.findMenuById(ID);

		if (menu == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(menu);
	}

	@GetMapping("/api/menu")
	public ResponseEntity<List<Menu>> findMenuByStatusIsSold() {
		List<Menu> menuIsSold = menuService.findMenuByStatusIsSold();
		return ResponseEntity.ok(menuIsSold);
	}

	@GetMapping("/api/menu/category") // 找出所有分類
	public ResponseEntity<List<String>> findAllCategory() {
		List<String> allCategories = menuService.findAllCategories();
		if (allCategories.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(allCategories);
	}

	@GetMapping("/api/menu/{productCategory}") // 依照分類找尋正在賣的商品
	public ResponseEntity<List<Menu>> findMenuByCategoryAndIsSold(@PathVariable String productCategory) {
		List<Menu> menuIsSold = menuService.findMenuByCategoryAndIsSold(productCategory);

		if (menuIsSold.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		return ResponseEntity.ok(menuIsSold);

	}

	@GetMapping("/api/menu/topThree") // 前三名產品
	public ResponseEntity<List<Menu>> findTopThreeMenu() {

		List<Menu> topThreeMenu = menuService.findTopThreeMenu();

		if (topThreeMenu.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(topThreeMenu);
	}


	@Transactional
	@PutMapping("/api/menu/minusCartStock/{id}")
	public ResponseEntity<?> minusCartStock(@RequestBody CartItemDTO dto, @PathVariable Integer id) {
	    try {
	        // 檢查產品是否存在
	        Menu product = menuService.findMenuById(id);
	        if (product == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID " + id + " not found.");
	        }

	        // 檢查庫存是否足夠
	        if (product.getProductCount() <= 0) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("Product ID " + id + " is out of stock.");
	        }

	        // 檢查庫存是否足夠扣減
	        if (product.getProductCount() < dto.getCartCount()) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body("Product ID " + id + " has insufficient stock.");
	        }

	        // 扣減庫存
	        int remainingStock = product.getProductCount() - dto.getCartCount();
	        
	        if(remainingStock<0) {
	        	
	        	 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("庫存不足");
	        }
	        
	        product.setProductCount(remainingStock);  // 確保庫存不會低於 0
	        menuService.updateMenu(product);

	        // 返回成功結果
	        return ResponseEntity.ok(Map.of("success", true, "productCount", remainingStock));
	    } catch (Exception e) {
	        
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error processing request: " + e.getMessage());
	    }
	}

}
