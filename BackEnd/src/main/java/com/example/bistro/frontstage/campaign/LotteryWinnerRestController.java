package com.example.bistro.frontstage.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.lotteryWinners.LotteryWinners;

@RestController
public class LotteryWinnerRestController {
	
	@Autowired
	LotteryWinnerFrontService lotteryWinnerFrontService;
	
	@GetMapping("/api/winner/{id}")
	public ResponseEntity<?> getMethodName(@PathVariable Integer id) {
		LotteryWinners winner = lotteryWinnerFrontService.findWinnerById(id);
		return ResponseEntity.ok(winner);
	}
	

}
