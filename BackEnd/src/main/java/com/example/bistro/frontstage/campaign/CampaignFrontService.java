package com.example.bistro.frontstage.campaign;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaign.CampaignRepository;
import com.example.bistro.backstage.campaign.CampaignService;
import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.campaignPrize.CampaignPrizesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CampaignFrontService {

	@Autowired
	CampaignRepository campaignRepo;	
	
	@Autowired
	CampaignService campaignService;
	
	public List<Campaign> findAllCampaign() {
		return campaignService.findAllCampaign();
	}
	
	public Campaign findCampaignById(Integer id) {
		return campaignService.findCampaignById(id);
	}

	public List<Campaign> findActiveCampaign() {
		Date now = new Date();
		return campaignRepo.findActiveCampaign(now);
	}

	
	
	

}
