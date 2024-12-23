package com.example.bistro.frontstage.pointRecord;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.PointsRecords.PointsRecordsBean;
import com.example.bistro.backstage.PointsRecords.PointsRecordsRepository;
import com.example.bistro.backstage.PointsRecords.PointsRecordsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PointRecordRestController {

	@Autowired
	PointRecordFrontService pointRecordFrontService;
	
	@PostMapping("/api/pointRecord")
	public ResponseEntity<?> CreatePointRecord(@RequestBody PointRecordDTO requestDto) {
	    // 呼叫 Service 層，並獲取返回的結果
	    Map<String, Boolean> map = pointRecordFrontService.createPointsRecords(requestDto.getMemberId(),requestDto.getPointPrizesId(),requestDto.getRecordsDate());
	    
	    return ResponseEntity.ok(map);
	}
	
//	@PostMapping("/api/minusMemberPoint")
//	public ResponseEntity<?> MinusMemberPoint(@RequestBody PointRecordDTO requestDto) {
//			
//		PointsRecordsBean pointsRecordsBean = pointRecordFrontService.minusMemberPoint(requestDto.getPointPrizesId(), requestDto.getMemberId());
//		
//		return ResponseEntity.ok(pointsRecordsBean);
//	}
	
	@PostMapping("/api/minusMemberPoint")
	public ResponseEntity<?> MinusMemberPoint(@RequestBody PointRecordDTO requestDto) {
			
		pointRecordFrontService.minusMemberPoint(requestDto.getPointPrizesId(), requestDto.getMemberId());
		
		return ResponseEntity.ok("已刪除相對應點數");
	}
	
}
