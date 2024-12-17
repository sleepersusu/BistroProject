package com.example.bistro.frontstage.campaign;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.lotteryChance.LotteryChance;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class LotteryChanceRestController {
	
	@Autowired
	LotteryChancesFrontService chancesFrontService;
	
	@GetMapping("/api/lotteryChance/{id}")
	public ResponseEntity<?> findChanceById(@PathVariable Integer id) {
		LotteryChance chance = chancesFrontService.findChanceById(id);
		if(chance == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該會員抽獎機會");
		}
		return ResponseEntity.ok(chance);
	}
	
	@GetMapping("/api/lotteryChance/member/{id}")
	public ResponseEntity<?> findByMemberId(@PathVariable Integer id) {
		List<LotteryChance> memberChances = chancesFrontService.findByMemberId(id);		
		return ResponseEntity.ok(memberChances);
	}
	
	
	@GetMapping("/api/lotteryChance/{memberId}/{campaignId}")
	public ResponseEntity<?> findMemberChanceByCampaign(@PathVariable Integer memberId,@PathVariable Integer campaignId){
		Optional<LotteryChance> chance = chancesFrontService.findMemberChanceByCampaign(memberId, campaignId);
		if(chance.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("此活動中找不到抽獎機會");
		}		
		return ResponseEntity.ok().body(chance.get());
	}
	
	@PostMapping("/api/lotteryChance")
	public ResponseEntity<?> addChances(@RequestBody LotteryChanceDTO chance) {
		Map<String, Object> successChance = chancesFrontService.calculateAndAddChances(chance.getMemberId(), 
													chance.getCampaignId(), 
													chance.getOrderAmount());		
		
		return ResponseEntity.ok().body(successChance);
	}
	
	

}
