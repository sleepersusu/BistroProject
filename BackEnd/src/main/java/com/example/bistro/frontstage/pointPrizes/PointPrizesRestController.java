package com.example.bistro.frontstage.pointPrizes;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.pointPrizes.PointPrizesBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class PointPrizesRestController {

	@Autowired
	PointPrizesFrontService pointPrizesService;
	
	
	@GetMapping("/api/pointPrizes")
	public ResponseEntity <List<PointPrizesBean>> getPointPrizes() {
		List<PointPrizesBean> pointPrizesBean = pointPrizesService.findAllPointPrizes();
		
		return ResponseEntity.ok(pointPrizesBean);
	}
	
	@PostMapping("/api/MinusOnePrizesCount")
	public void MinusOnePrizesCount(@RequestBody Map<String, Object> requestData) {
	    try {
	        Integer pointPrizesId = Integer.valueOf(requestData.get("pointPrizesId").toString());
	        System.out.println("收到的獎品ID: " + pointPrizesId);
	        
	        pointPrizesService.deletePointPrizeCount(pointPrizesId);
	        
	    } catch (NumberFormatException e) {
	        System.out.println("無效的獎品ID");
	    }
	}
	
}