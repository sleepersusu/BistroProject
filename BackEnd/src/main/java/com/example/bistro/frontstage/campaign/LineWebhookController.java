package com.example.bistro.frontstage.campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import jakarta.persistence.EntityNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@LineMessageHandler
public class LineWebhookController {

	@Autowired
	private final LineMessageService lineMessageService;

	public LineWebhookController(LineMessageService lineMessageService) {
		this.lineMessageService = lineMessageService;
	}

	@PostMapping("/line/webhook")
	public ResponseEntity<String> handleWebhook(@RequestBody String body) {
		try {
			lineMessageService.processWebhookEvent(body);
			return ResponseEntity.ok("OK");
		} catch (Exception e) {
			log.error("Webhook處理失敗", e);
			return ResponseEntity.ok("Error");
		}
	}

	@PostMapping("/line/send-message/{winnerId}")
	public ResponseEntity<String> sendShipMessageToUser(@PathVariable Integer winnerId) {
		try {
			lineMessageService.sendShippingNotification(winnerId);
			return ResponseEntity.ok("訊息發送成功");
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到用戶");
		} catch (Exception e) {
			log.error("發送出貨通知失敗", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發送訊息時發生錯誤");
		}
	}
}