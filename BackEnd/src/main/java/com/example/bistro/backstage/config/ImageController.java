package com.example.bistro.backstage.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
public class ImageController {
		
	@Autowired
	private ImageService imageService;
	
	//上傳
	@PostMapping("/api/{type}/photo/{id}")
	public ResponseEntity<byte[]> uploadPhoto(@RequestParam MultipartFile file,@PathVariable String type,@PathVariable Integer id) {
		System.out.println("接圖片");
		System.out.println("上傳對象類型:"+type);
		System.out.println("對象:"+id);
		
		byte[] imgFile=null;
		try {
			imgFile = file.getBytes();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return imageService.imageUpload(type, id, imgFile);
	}
	
	
	
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
