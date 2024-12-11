package com.example.bistro.frontstage.pointPrizes;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.pointPrizes.PointPrizesBean;
import com.example.bistro.backstage.pointPrizes.PointPrizesService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
public class PointPrizesRestController {

	@Autowired
	PointPrizesService pointPrizesService;
	
	@GetMapping("/api/pointPrizes")
	public ResponseEntity <List<PointPrizesBean>> getPointPrizes() {
		List<PointPrizesBean> pointPrizesBean = pointPrizesService.findAllPointPrizes();
		
		return ResponseEntity.ok(pointPrizesBean);
	}
	
	
}