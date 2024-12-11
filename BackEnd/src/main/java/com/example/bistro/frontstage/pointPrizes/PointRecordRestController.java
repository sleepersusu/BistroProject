package com.example.bistro.frontstage.pointPrizes;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.PointsRecords.PointsRecordsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PointRecordRestController {

	@Autowired
	PointsRecordsService pointRecordService;
	
	@PostMapping("/api/pointRecord")
	public ResponseEntity<?> pointRecord(@RequestBody PointRecordDTO requestDto) {
	    // 呼叫 Service 層，並獲取返回的結果
	    Map<String, Boolean> map = pointRecordService.createPointsRecords(requestDto.getMemberId(),requestDto.getPointPrizesId(),requestDto.getRecordsDate());
	    
	    return ResponseEntity.ok(map);
	}
	
}
