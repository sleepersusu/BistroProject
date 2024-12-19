package com.example.bistro.frontstage.campaign;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.example.bistro.backstage.line.LineMember;
import com.example.bistro.backstage.line.LineMemberRepository;
import com.example.bistro.backstage.lotteryWinners.LotteryWinners;
import com.example.bistro.backstage.lotteryWinners.LotteryWinnersRepository;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.shippingDetails.ShippingDetails;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@LineMessageHandler
public class LineWebhookController {

    @Autowired
    private LineMessagingClient lineMessagingClient;
    
    @Autowired
    private MembersRepository membersRepo;
    
    @Autowired
    private LineMemberRepository lineMemberRepo;
    
    @Autowired
    private LotteryWinnersRepository winnersRepo;
    
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/line/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String body) {
        System.out.println("=== 收到 webhook 請求 ===");
        try {
            JsonNode jsonNode = objectMapper.readTree(body); 
            JsonNode events = jsonNode.get("events");

            if (events.isArray() && events.size() > 0) {
                JsonNode event = events.get(0);
                String type = event.get("type").asText();
                String userId = event.get("source").get("userId").asText();

                System.out.println("事件類型: " + type);
                System.out.println("用戶 ID: " + userId);

                if ("follow".equals(type)) {
                    Members member = membersRepo.findById(1).orElse(null);
                    if (member != null) {
                        LineMember lineMember = new LineMember();
                        lineMember.setMember(member);
                        lineMember.setLineUserId(userId);
                        lineMemberRepo.save(lineMember);
                    }
                }
            }
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("Error");
        }
    }
    
    @PostMapping("/line/send-message/{winnerId}")
    public ResponseEntity<String> sendMessageToUser(@PathVariable("winnerId") Integer winnerId) {
    	Optional<LotteryWinners> op = winnersRepo.findById(winnerId);
    	LotteryWinners winner = op.get();
        Integer id = winner.getMember().getId();
        Optional<LineMember> op2 = lineMemberRepo.findByMemberId(id);
        
        if (op2.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到用戶");
        }
        ShippingDetails details = winner.getShippingDetails();
        String message = String.format(
        	    "親愛的 %s 您好 🎉\n\n" +
        	    "您的獎品已經出貨了！ 📦\n" +
        	    "獎品名稱：%s\n" +
        	    "配送地址：%s\n\n" +
        	    "如有任何問題，請隨時與我們聯繫 ☎️\n\n" +
        	    "祝您有愉快的一天！ 😊\n" +
        	    "Nightly Sips 酌夜語 敬上",
        	    details.getName(),
        	    details.getLotteryWinner().fetchPrizeName(),
        	    details.getAddress()
        	);
        
        try {
        	LineMember lineMember = op2.get();
            TextMessage textMessage = new TextMessage(message);
            PushMessage pushMessage = new PushMessage(lineMember.getLineUserId(), textMessage);
            
            lineMessagingClient.pushMessage(pushMessage).get();
            
            return ResponseEntity.ok("訊息發送成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發送訊息時發生錯誤");
        }
    }
}