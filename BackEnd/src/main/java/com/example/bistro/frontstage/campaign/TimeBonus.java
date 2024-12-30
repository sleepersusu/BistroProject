package com.example.bistro.frontstage.campaign;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bistro.backstage.campaignPrize.CampaignPrizes;

@Service
public class TimeBonus {
	
	
	public boolean isBonusTime() {
		LocalDateTime now = LocalDateTime.now();
		int hour = now.getHour();
		
		return hour >= 20 && hour < 24;
	}
	
	public CampaignPrizes calculateActualProbability(List<CampaignPrizes> prizes) {
	    if (prizes == null || prizes.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "獎品列表為空");
	    }
	  
	    CampaignPrizes jackpot = prizes.stream()
	        .filter(prize -> !prize.getPrizeName().equals("銘謝惠顧")) 
	        .min((p1, p2) -> Double.compare(
	            p1.getProbability().doubleValue(), 
	            p2.getProbability().doubleValue()
	        ))
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "找不到有效獎品"));
	    
	    return jackpot;
	}
	
	public Map<String, Object> getCurrentBonusInfo() {
        Map<String, Object> result = new HashMap<>();
        boolean isBonusTime = isBonusTime();
        
        result.put("isBonusTime", isBonusTime);
        if (isBonusTime) {
            result.put("message", "頭獎機率加成時段！");
            result.put("bonusRate", "5%");
        }
        
        return result;
    }
}
