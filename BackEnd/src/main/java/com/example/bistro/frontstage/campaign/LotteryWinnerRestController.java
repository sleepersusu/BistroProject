package com.example.bistro.frontstage.campaign;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.lotteryWinners.LotteryWinners;


@RestController
public class LotteryWinnerRestController {
	
	@Autowired
	LotteryWinnerFrontService lotteryWinnerFrontService;
	
	@GetMapping("/api/winner/{id}")
	public ResponseEntity<?> getWinnerById(@PathVariable Integer id) {
		LotteryWinners winner = lotteryWinnerFrontService.findWinnerById(id);
		return ResponseEntity.ok(winner);
	}
	
	@GetMapping("/api/winner/member/{memberId}")
	public ResponseEntity<?> getWinnerByMemberId(@PathVariable Integer memberId){
		List<LotteryWinners> winner = lotteryWinnerFrontService.findByMemberId(memberId);
		return ResponseEntity.ok(winner);
	}
	
	@PostMapping("/api/frontend/winner/{chanceId}")
	public ResponseEntity<?> drawPrizeByChanceId(@PathVariable Integer chanceId) {
		Map<String, Object> map = lotteryWinnerFrontService.drawPrize(chanceId);
		return ResponseEntity.ok(map);
	}
	
	
	

}
