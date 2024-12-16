package com.example.bistro.frontstage.campaign;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.lotteryWinners.LotteryWinners;
import com.example.bistro.backstage.lotteryWinners.LotteryWinnersRepository;
import com.example.bistro.backstage.shippingDetails.ShippingDetails;
import com.example.bistro.backstage.shippingDetails.ShippingDetailsRepository;

@RestController
public class ShippingDetailsRestController {
	
	@Autowired
	ShippingDetailsRepository shippingDetailsRepo;
	
	@Autowired
	LotteryWinnersRepository lotteryWinnersRepo;
	
	@Autowired
    private EmailService emailService;
	
	@GetMapping("/api/shippingDetails/{winnerId}")
	public ResponseEntity<?> getMethodName(@PathVariable Integer winnerId) {
		Optional<ShippingDetails> op = shippingDetailsRepo.findByLotteryWinnerId(winnerId);
		return op.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到配送資訊") : ResponseEntity.ok(op.get());
	}
	
	@PostMapping("/api/shippingDetails")
	public ResponseEntity<?> postMethodName(@RequestBody ShippingDetailsDTO reqDto) {
		Optional<LotteryWinners> op = lotteryWinnersRepo.findById(reqDto.getLotteryWinnerId());
		LotteryWinners winner = op.get();
		if(!winner.isShippingCompleted()) {
			winner.setShippingCompleted(true);	
			lotteryWinnersRepo.save(winner);
			
			ShippingDetails detail = new ShippingDetails();
			detail.setEmail(reqDto.getEmail());
			detail.setAddress(reqDto.getAddress());
			detail.setLotteryWinner(winner);
			detail.setName(reqDto.getName());
			detail.setNotes(reqDto.getNotes());
			detail.setPhone(reqDto.getPhone());
			return ResponseEntity.ok(shippingDetailsRepo.save(detail));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("已經填寫過了");
		
	}
	
	@PostMapping("api/shippingDetails/{id}/ship")
    public ResponseEntity<?> shipOrder(@PathVariable Integer id) {
        try {
            Optional<ShippingDetails> op = shippingDetailsRepo.findById(id);
            
            if(op.isEmpty()) {
            	throw new RuntimeException("找不到配送資訊");
            }
            
            emailService.sendShippingNotification(op.get());
            
            return ResponseEntity.ok("郵件發送成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
	
	

}
