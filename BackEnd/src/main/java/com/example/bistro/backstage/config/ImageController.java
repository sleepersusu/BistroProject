package com.example.bistro.backstage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class ImageController {
		
	@Autowired
	private ImageService imageService;
	
	//
	//上傳
	
	
	//下載
	
	@GetMapping("/Bistro/download")
	public ResponseEntity<byte[]> downloadBackEndPhoto(@RequestParam String type,@RequestParam Integer ID) {
		
		return imageService.imageDownload(type,ID);
		
	}
	
	
	//客戶端	
	@GetMapping("/api/{type}/photo/{id}")
	public ResponseEntity<byte[]> downloadPhoto(@PathVariable String type,@PathVariable Integer id) {
		
		return imageService.imageDownload(type,id);
		
	}
	
}
