package com.example.bistro.frontstage.campaign;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaign.CampaignRepository;
import com.example.bistro.backstage.campaignPrize.CampaignPrizeService;
import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.campaignPrize.CampaignPrizesRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CampaignPrizeFrontService {
	
	@Autowired
	CampaignPrizeService campaignPrizeService;
	
	@Autowired
	CampaignRepository campaignRepo;
	
	@Autowired
	CampaignPrizesRepository campaignPrizesRepo;
		
	
	public CampaignPrizes findPrizeById(Integer id) {
		return campaignPrizeService.findPrizeById(id);
	}
	
	public List<CampaignPrizes> findAllCampaignPrizes(){
		return campaignPrizeService.findAllCampaignPrizes();
	}
	
	public List<CampaignPrizes> findPrizesByCampaignId(Integer id) {
		Campaign campaign = campaignRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到該活動"));

		if (!campaign.isActive()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "活動尚未開始或已結束");
		}

		List<CampaignPrizes> prizes = campaignPrizesRepo.findPrizesFromCampaignId(id);

		if (prizes.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "該活動尚未設置獎品");
		}

		return prizes;
	}
	
	public CampaignPrizes reducePrize(Integer id) {
		CampaignPrizes prize = campaignPrizeService.findPrizeById(id);
		prize.setPrizeQuantity(prize.getPrizeQuantity() - 1);
		return campaignPrizeService.updatePrize(prize);
	}

}
