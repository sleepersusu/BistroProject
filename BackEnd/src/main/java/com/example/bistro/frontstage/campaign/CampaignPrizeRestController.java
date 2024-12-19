	package com.example.bistro.frontstage.campaign;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.campaignPrize.CampaignPrizeService;
import com.example.bistro.backstage.campaignPrize.CampaignPrizes;

@RestController
public class CampaignPrizeRestController {
	
	@Autowired

	CampaignPrizeFrontService prizeService;

	
	@GetMapping("/api/campaignPrize")
	public ResponseEntity<?> getCampaignPrizes() {
		List<CampaignPrizes> prizes = prizeService.findAllCampaignPrizes();
		return ResponseEntity.ok(prizes);
	}
	
	@GetMapping("/api/campaignPrize/{id}")
	public ResponseEntity<?> getCampaignPrize(@PathVariable Integer id) {
		CampaignPrizes prize = prizeService.findPrizeById(id);
		if(prize == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到獎品");
		}
		return ResponseEntity.ok(prize);
	}
	
	@GetMapping("/api/campaignPrize/image/{id}")  
	public ResponseEntity<?> getPrizeImage(@PathVariable Integer id) {
	    try {
	        CampaignPrizes prize = prizeService.findPrizeById(id);	        
	        if (prize == null || prize.getPrizeImg() == null) {
	        	return ResponseEntity
	                    .status(HttpStatus.NOT_FOUND)
	                    .body("找不到圖片");
	        }
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_JPEG);
	        
	        return ResponseEntity
	            .ok()
	            .headers(headers)
	            .body(prize.getPrizeImg());
	            
	    } catch (Exception e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}
	

	@GetMapping("/api/campaignPrize/prizeByCampaign/{campaignId}")
	public ResponseEntity<?> getPrizesByCampaign(@PathVariable Integer campaignId) {
		List<CampaignPrizes> prizes = prizeService.findPrizesByCampaignId(campaignId);
		if(prizes == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("該活動還沒有設定產品");
		}
		return ResponseEntity.ok(prizes);
	}	


}
