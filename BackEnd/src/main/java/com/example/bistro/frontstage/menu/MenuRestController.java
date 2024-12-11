package com.example.bistro.frontstage.menu;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuRepository;
import com.example.bistro.backstage.menu.MenuService;

@RestController
public class MenuRestController {
	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuRepository menuRepo;
	
	
	@GetMapping("api/menu/")
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
		
		if(topThreeMenu.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(topThreeMenu);
	}
	
	
	@GetMapping("/api/{ID}/menuphoto")
	public ResponseEntity<byte[]> downloadMenu(@PathVariable Integer ID) {
		Optional<Menu> op = menuRepo.findById(ID);

		if (op.isPresent()) {

			Menu menu = op.get();
			byte[] menubyte = menu.getProductImg();
			String originalFilename = menu.getProductImgUrl();

			String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

			MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM; // 默認為通用二進位流
			
			if (fileExtension.equalsIgnoreCase("png")) {
				mediaType = MediaType.IMAGE_PNG;
			} else if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
				mediaType = MediaType.IMAGE_JPEG;
			}

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(mediaType);

			return new ResponseEntity<byte[]>(menubyte, httpHeaders, HttpStatus.OK);

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
}
