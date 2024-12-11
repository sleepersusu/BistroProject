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

import com.example.bistro.backstage.campaign.Campaign;

@RestController
public class CampaignRestController {

	@Autowired
	CampaignFrontService campaignService;

	@GetMapping("/api/campaign")
	public ResponseEntity<List<Campaign>> getCampaigns() {
		List<Campaign> campaigns = campaignService.findAllCampaign();

		return ResponseEntity.ok(campaigns);
	}

	@GetMapping("/api/campaign/{id}")
	public ResponseEntity<?> getCampaignById(@PathVariable Integer id) {
		Campaign campaign = campaignService.findCampaignById(id);

		if (campaign != null) {
			return ResponseEntity.ok(campaign);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到活動資訊");
		}
	}

	@GetMapping("/api/campaign/image/{id}")
	public ResponseEntity<?> getMethodName(@PathVariable Integer id) {
		try {
			Campaign campaign = campaignService.findCampaignById(id);
			if (campaign == null || campaign.getCampaignImg() == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到活動或者圖片");
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_JPEG);

			return ResponseEntity.ok().headers(headers).body(campaign.getCampaignImg());
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@GetMapping("/api/campaign/active")
	public ResponseEntity<?> getActiveCampaigns() {
		List<Campaign> activeCampaigns = campaignService.findActiveCampaign();
		if (activeCampaigns != null) {
			return ResponseEntity.ok(activeCampaigns);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到正在活躍的活動");
		}
	}

}
