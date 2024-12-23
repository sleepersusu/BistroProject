package com.example.bistro.frontstage.promoCodeRecord;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.frontstage.promoCode.PromoCodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class PromoCodeRecordController {

	
	@Autowired
	PromoCodeRecordService promoCodeRecordService;
	
	@Autowired
	PromoCodeService promoCodeService;
	
//	@PostMapping("/api/createPromoRecord")
//	public ResponseEntity<?> CreatePromoCodeRecord(@RequestBody PromoCodeRecordDTO requestDto) {
//		promoCodeRecordService.createPromoCodeRecord(requestDto);
//		
//		return ResponseEntity.ok("PromoCodeRecord created successfully");
//	}

	
	@PostMapping("/api/deletePromoRecord")
	public String deleteMemberPromoCode(@RequestBody Map<String, String> body) {
	    String promoCode = body.get("promoCode");
	    try {
	        promoCodeRecordService.deletePromoCodeRecord(promoCode);
	        return "刪除優惠碼成功";
	    } catch (RuntimeException e) {
	        return "刪除優惠碼失敗: " + e.getMessage();
	    }
	}
	
    @PostMapping("/api/createPromoRecord")
    public ResponseEntity<?> createPromoCodeRecord(@RequestBody PromoCodeRecordDTO requestDto) {
        try {
            promoCodeRecordService.createPromoCodeRecord(requestDto);
            return ResponseEntity.ok("優惠券記錄創建成功，並已刪除使用過的優惠碼");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("處理優惠券時發生錯誤: " + e.getMessage());
        }
    }
	
    @GetMapping("/api/showPromoRecord")
    public ResponseEntity <List<PromoCodeRecordBean>> showPromoRecord() {
    	List<PromoCodeRecordBean> promoCodeRecordBean = promoCodeRecordService.findAllPromoCodeRecord();
    	
    	return ResponseEntity.ok(promoCodeRecordBean);
    }
    
}
