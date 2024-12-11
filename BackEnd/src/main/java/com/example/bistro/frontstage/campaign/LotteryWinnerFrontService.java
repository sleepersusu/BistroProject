package com.example.bistro.frontstage.campaign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.lotteryChance.LotteryChance;
import com.example.bistro.backstage.lotteryWinners.LotteryWinners;
import com.example.bistro.backstage.lotteryWinners.LotteryWinnersRepository;
import com.example.bistro.backstage.lotteryWinners.LotteryWinnersService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LotteryWinnerFrontService {
	
	@Autowired
	CampaignPrizeFrontService campaignPrizeFrontService;
	
	@Autowired
	LotteryWinnersRepository lotteryWinnersRepo;
	
	@Autowired
	LotteryWinnersService lotteryWinnersService;
	
	@Autowired
    private LotteryChancesFrontService lotteryChanceService;
	
	
	public LotteryWinners findWinnerById(Integer id) {
		return lotteryWinnersService.findById(id);
	}
	
	
	public Map<String, Object> drawPrize(Integer id) {
		LotteryChance chance = lotteryChanceService.findChanceById(id);
		if(chance == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到抽獎機會..");
		}
		
	    if(chance.getRemainingChances() <= 0) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "已經用完剩餘的抽獎機會..");
	    }

	    List<CampaignPrizes> prizes = campaignPrizeFrontService.findPrizesByCampaignId(chance.getCampaign().getId());

	    CampaignPrizes winnerPrize = drawPrizeByProbability(prizes);

	   
	    lotteryChanceService.useChance(chance.getId());

	    Map<String, Object> result = new HashMap<>();
	    result.put("isWin", winnerPrize != null);
	    result.put("remainingChances", chance.getRemainingChances());
	    
	    if (winnerPrize != null) {
	        LotteryWinners winner = new LotteryWinners();
	        winner.setCampaign(chance.getCampaign());
	        winner.setCampaignPrizes(winnerPrize);
	        winner.setLotteryChance(chance);
	        winner.setMember(chance.getMember());
	        
	        result.put("prize", lotteryWinnersRepo.save(winner));
	    }

	    return result;
	}

	private CampaignPrizes drawPrizeByProbability(List<CampaignPrizes> prizes) {
	    double random = Math.random() * 100;
	    double cumulative = 0.00;
	    
	    for (CampaignPrizes prize : prizes) {
	        cumulative += prize.getProbability().doubleValue();
	        if (random <= cumulative) {
	            return prize;
	        }
	    }
	    
	    return null;
	}
	
	public List<LotteryWinners> findByMemberId(Integer memberId){
		return lotteryWinnersRepo.findByMemberId(memberId);
	}
}
