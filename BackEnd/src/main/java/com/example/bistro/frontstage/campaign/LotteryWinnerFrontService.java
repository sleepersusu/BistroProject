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
    LotteryChancesFrontService lotteryChanceService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	TimeBonus timeBonus;
	
	
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

	    CampaignPrizes winnerPrize = drawPrizeByProbability(prizes, chance.getMember().getId());	   
	    lotteryChanceService.useChance(chance.getId());

	    Map<String, Object> result = new HashMap<>();
	    result.put("isWin", !winnerPrize.getPrizeName().equals("銘謝惠顧"));
	    result.put("remainingChances", chance.getRemainingChances());
	    
	    int unluckyCount = redisService.getUnluckyCount(chance.getMember().getId());
	    result.put("unluckyCount", unluckyCount);
        result.put("unluckyBonus", unluckyCount * 5.0);
        
        Map<String, Object> timeBonusInfo = timeBonus.getCurrentBonusInfo();
        if ((Boolean) timeBonusInfo.get("isBonusTime")) {
            result.putAll(timeBonusInfo);
        }
        
        if (unluckyCount >= 3) {
            result.put("bonusMessage", "手氣不好沒關係，中獎機率大幅提升中！");
        }
	    
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

	private CampaignPrizes drawPrizeByProbability(List<CampaignPrizes> prizes, Integer memberId) {
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
	    
	    CampaignPrizes jackpot = timeBonus.calculateActualProbability(prizes);
	    
	    int unluckyCount = redisService.getUnluckyCount(memberId);
	    double unluckyBonus = 1.0 + Math.min(unluckyCount * 0.05, 0.5);

	    double random = Math.random() * 100 / unluckyBonus;
	    double cumulative = 0.00;

	    for (CampaignPrizes prize : availablePrizes) {
	        if(prize.getProbability() == null) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "獎品機率設定錯誤");
	        }
	        
	        double probability = prize.getProbability().doubleValue();
	        
	        if (prize.equals(jackpot) && timeBonus.isBonusTime()) {
                probability *= 1.67;  
            }
	        
	        cumulative += probability;
	        
	        if (random <= cumulative) {
	        	if (!prize.getPrizeName().equals("銘謝惠顧")) {
                    redisService.resetUnluckyCount(memberId);
                } else {
                    redisService.incrementUnluckyCount(memberId);
                }
	            return prize;
	        }
	    }

	    return availablePrizes.get(availablePrizes.size() - 1);
	}
	
	public List<LotteryWinners> findByMemberId(Integer memberId){
		List<LotteryWinners> winner = lotteryWinnersRepo.findByMemberId(memberId);
		if(winner == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到此會員的中獎紀錄");
		}
		return winner;
	}
}
