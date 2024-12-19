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
		} catch (IllegalArgumentException e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
			List<String> availableTimeslots = reservationsFrontstageService
					.getAvailableTimeSlots(reservation.getReservationDate(), reservation.getNumberPeople());

			if (!availableTimeslots.isEmpty()) {
				Map<String, Object> response = new HashMap<>();
				response.put("success", true);
				response.put("availableTimeslots", availableTimeslots);
				return ResponseEntity.ok(response);
			} else {
				throw new RuntimeException("沒有足夠的座位，請選擇其他時段。");
			}
		} catch (Exception e) {
			Map<String, Object> response = new HashMap<>();
			response.put("success", false);
			response.put("message", "查詢可用時間段時發生錯誤，請稍後再試。");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
