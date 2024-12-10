package com.example.bistro.frontstage.campaign;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaign.CampaignService;

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
	public ResponseEntity<Campaign> getCampaignById(@PathVariable Integer id) {
	    Campaign campaign = campaignService.findCampaignById(id);
	    
	    if (campaign != null) {
	        return ResponseEntity.ok(campaign);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
}
