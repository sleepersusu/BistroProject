package com.example.bistro.frontstage.campaign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
		LotteryWinners winner = lotteryWinnersService.findById(id);
		if(winner == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到得獎者");
		}
		
		return winner;
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
	    if(prizes == null || prizes.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到活動獎品..");
	    }

	    CampaignPrizes winnerPrize = drawPrizeByProbability(prizes);

	   
	    lotteryChanceService.useChance(chance.getId());

	    Map<String, Object> result = new HashMap<>();
	    result.put("isWin", !winnerPrize.getPrizeName().equals("銘謝惠顧"));
	    result.put("remainingChances", chance.getRemainingChances());
	    
	    if (winnerPrize != null) {
	        LotteryWinners winner = new LotteryWinners();
	        winner.setCampaign(chance.getCampaign());
	        winner.setCampaignPrizes(winnerPrize);
	        winner.setMember(chance.getMember());
	        campaignPrizeFrontService.reducePrize(winnerPrize.getId());
	        
	        result.put("prize", lotteryWinnersRepo.save(winner));
	    }

	    return result;
	}

	private CampaignPrizes drawPrizeByProbability(List<CampaignPrizes> prizes) {
	    if (prizes == null || prizes.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "獎品列表為空");
	    }
	    
	    List<CampaignPrizes> availablePrizes = prizes.stream()
	        .filter(prize -> prize.getPrizeQuantity() > 0)
	        .collect(Collectors.toList());

	    if (availablePrizes.isEmpty()) {
	        return prizes.stream()
	            .filter(prize -> prize.getPrizeName().equals("銘謝惠顧"))
	            .findFirst()
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "抽獎過程發生錯誤"));
	    }

	    double random = Math.random() * 100;
	    double cumulative = 0.00;

	    for (CampaignPrizes prize : availablePrizes) {
	        if(prize.getProbability() == null) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "獎品機率設定錯誤");
	        }
	        cumulative += prize.getProbability().doubleValue();
	        if (random <= cumulative) {
	            return prize;
	        }
	    }

	    return availablePrizes.get(availablePrizes.size() - 1);
	}
	
	public LotteryWinners findByMemberId(Integer memberId){
		LotteryWinners winner = lotteryWinnersRepo.findByMemberId(memberId);
		if(winner == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到此會員的中獎紀錄");
		}
		return winner;
	}
}
