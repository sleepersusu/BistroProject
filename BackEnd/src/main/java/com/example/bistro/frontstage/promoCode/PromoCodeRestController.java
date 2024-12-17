package com.example.bistro.frontstage.promoCode;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

	@GetMapping("/api/showPromoCode")
	public ResponseEntity<List<PromoCodeBean>> showPromoCode(@RequestParam int memberId){
		List<PromoCodeBean> promoCodeBean = promoCodeService.findMemberPromoCode(memberId);
		return ResponseEntity.ok(promoCodeBean);
	}
	
}