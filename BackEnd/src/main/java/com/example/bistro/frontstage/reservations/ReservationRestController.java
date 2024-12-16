package com.example.bistro.frontstage.reservations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ReservationRestController {

	@Autowired
	private ReservationsFrontstageService reservationsFrontstageService;

	@PostMapping("/api/Bistro/insert")
	public ResponseEntity<Map<String, Object>> insert(@RequestBody ReservationDTO dto) {

		try {
			reservationsFrontstageService.insert(dto);
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "訂位成功");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "訂位失敗，請稍後再試。");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/api/Bistro/remaining")
	 public ResponseEntity<Map<String, Object>> remaining(@RequestBody ReservationDTO reservation) {
        try {
            // 呼叫 Service 層來檢查可用的時間段
            List<String> availableTimeslots = reservationsFrontstageService.getAvailableTimeSlots(reservation.getReservationDate(), reservation.getNumberPeople());

            // 根據可用時間段返回結果
            if (!availableTimeslots.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("availableTimeslots", availableTimeslots);
                return ResponseEntity.ok(response);
            } else {
                // 如果沒有可用的時間段
                throw new RuntimeException("没有足够的座位可用，请选择其他时间段。");
            }
        } catch (Exception e) {
            // 捕獲異常並返回錯誤信息
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "查詢可用時間段時發生錯誤，請稍後再試。");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
