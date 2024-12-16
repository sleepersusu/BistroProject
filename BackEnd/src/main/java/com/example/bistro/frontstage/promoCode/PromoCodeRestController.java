package com.example.bistro.frontstage.promoCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromoCodeRestController {
	
	@Autowired
	PromoCodeService promoCodeService;
	
	@PostMapping("/api/promoCode")
	public ResponseEntity<?> promoCode(@RequestBody PromoCodeDTO requestDto) {
	    System.out.println(requestDto.getPromoCode());
		promoCodeService.createPromoCode(requestDto);
		
		
		return ResponseEntity.ok("Promo code created successfully");
	}
	
}