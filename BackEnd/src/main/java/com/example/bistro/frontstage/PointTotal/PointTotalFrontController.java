package com.example.bistro.frontstage.PointTotal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.PointsTotal.PointsTotalBean;
import com.example.bistro.backstage.pointPrizes.PointPrizesBean;

@RestController
public class PointTotalFrontController {
    @Autowired
    PointTotalFrontService pointTotalFrontService;
    
    @PostMapping("/api/updateMemberPoint")
    public ResponseEntity<?> updateMemberPoint(@RequestBody PointTotalFrontDTO request) {
        try {
        	pointTotalFrontService.updateMemberPoints(request.getMemberId(), request.getPointGetted());
            return ResponseEntity.ok("Points updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating points: " + e.getMessage());
        }
    }
    
    @GetMapping("/api/getMemberPoint")
    public ResponseEntity <List<PointsTotalBean>> getMemberPointTotal() {
    	List<PointsTotalBean> pointsTotalBean = pointTotalFrontService.findAllMemberPoint();
    	
    	return ResponseEntity.ok(pointsTotalBean);
    }
    
}