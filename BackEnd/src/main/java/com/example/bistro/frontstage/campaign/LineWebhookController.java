package com.example.bistro.frontstage.campaign;



import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.example.bistro.backstage.PointsTotal.PointsTotalBean;
import com.example.bistro.backstage.PointsTotal.PointsTotalRepository;
import com.example.bistro.backstage.PointsTotal.PointsTotalService;
import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.line.LineMember;
import com.example.bistro.backstage.line.LineMemberRepository;
import com.example.bistro.backstage.lotteryWinners.LotteryWinners;
import com.example.bistro.backstage.lotteryWinners.LotteryWinnersRepository;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.reservations.Reservations;
import com.example.bistro.backstage.reservations.ReservationsRepository;
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
    
    @Autowired 
    private CampaignFrontService campaignFrontService;
    
    @Autowired
    private ReservationsRepository reservationsRepo;
    
    @Autowired
    private PointsTotalRepository pointsTotalRepo;
    

    @PostMapping("/line/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String body) {
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            JsonNode events = jsonNode.get("events");

            if (events.isArray() && events.size() > 0) {
                JsonNode event = events.get(0);
                String type = event.get("type").asText();
                String userId = event.get("source").get("userId").asText();

                if ("follow".equals(type)) {
                    String message = "感謝您追蹤我們！\n" +
                                   "請輸入您註冊會員時使用的手機號碼，\n" +
                                   "我們將為您進行會員綁定。";
                    
                    TextMessage textMessage = new TextMessage(message);
                    lineMessagingClient.pushMessage(
                        new PushMessage(userId, textMessage)
                    ).get();
                } else if ("message".equals(type)) {
                    JsonNode messageNode = event.get("message");
                    if ("text".equals(messageNode.get("type").asText())) {
                        String text = messageNode.get("text").asText().trim();
                        if (text.matches("^09\\d{8}$")) {
                            handleMemberBinding(text, userId);
                        }else if ("#中獎紀錄".equals(text)) {
                            handleViewPrizeHistory(userId);
                        }else if ("#最新活動".equals(text)) {
                        	handleViewActiveCampaign(userId);
                        }else if ("#我的訂位".equals(text)){
                        	handleViewReservations(userId);
                        }else if ("#紅利點數".equals(text)) {
                        	handleViewPoints(userId);
                        }
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
    public ResponseEntity<String> sendShipMessageToUser(@PathVariable Integer winnerId) {
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
    
    private void handleMemberBinding(String phone, String userId) {
        try {
            Members member = membersRepo.findByMemberPhone(phone)
                .orElse(null);
            
            if (member != null) {
            	Optional<LineMember> existingBinding = lineMemberRepo.findByLineUserId(userId);
                if (existingBinding.isPresent()) {
                    String memberName = existingBinding.get().getMember().getMemberName();
                    
                    if (existingBinding.get().getMember().getId().equals(member.getId())) {
                        String message = String.format(
                            "親愛的 %s，此手機號碼對應的會員已經與您的 LINE 帳號綁定了！",
                            memberName != null ? memberName : "會員"
                        );
                        lineMessagingClient.pushMessage(
                            new PushMessage(userId, new TextMessage(message))
                        ).get();
                    } else {
                        String message = String.format(
                            "親愛的 %s，您的 LINE 帳號已經綁定其他會員了！如需更換綁定，請聯繫客服協助。",
                            memberName != null ? memberName : "會員"
                        );
                        lineMessagingClient.pushMessage(
                            new PushMessage(userId, new TextMessage(message))
                        ).get();
                    }
                    return;
                }

                LineMember lineMember = new LineMember();
                lineMember.setMember(member);
                lineMember.setLineUserId(userId);
                lineMemberRepo.save(lineMember);

                String memberName = member.getMemberName();
                String message = String.format(
                    "歡迎 %s！\n會員綁定成功 🎉\n現在您可以使用所有功能了。",
                    memberName != null ? memberName : "會員"
                );
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            } else {
                String message = "找不到此手機號碼對應的會員資料，請確認號碼是否正確，或聯繫客服協助。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            }
        } catch (Exception e) {
            log.error("會員綁定失敗", e);
            String message = "綁定過程發生錯誤，請稍後再試或聯繫客服協助。";
            try {
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            } catch (Exception ex) {
                log.error("發送錯誤訊息失敗", ex);
            }
        }
    }
    
    private void handleViewPrizeHistory(String userId) {
        try {
            Optional<LineMember> lineMember = lineMemberRepo.findByLineUserId(userId);
            
            if (lineMember.isPresent()) {
                List<LotteryWinners> winners = winnersRepo.findByMemberOrderByCreatedAtDesc(lineMember.get().getMember());
                
                if (winners.isEmpty()) {
                    String message = "您目前還沒有中獎紀錄喔！\n" +
                                   "繼續參加活動，增加中獎機會 🎉";
                    lineMessagingClient.pushMessage(
                        new PushMessage(userId, new TextMessage(message))
                    ).get();
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("您的中獎紀錄如下：\n\n");
                
                int count = 0;
                for (LotteryWinners winner : winners) {
                	if(count >= 3) {
                		break;
                	}
                	sb.append("🎈活動名稱：").append(winner.fetchCampaignName()).append("\n");
                    sb.append("🎁 獎品：").append(winner.fetchPrizeName()).append("\n");
                    
                    ShippingDetails details = winner.getShippingDetails();
                    if (details != null) {
                        sb.append("📦 狀態：已出貨\n");
                        sb.append("🏠 配送地址：").append(details.getAddress()).append("\n");
                    } else {
                        sb.append("⏳ 狀態：待處理\n");
                    }
                    sb.append("\n");
                    count++;
                }
                
                if (winners.size() > 3) {
                    sb.append("還有 ").append(winners.size() - 3).append(" 筆更早的中獎紀錄\n");
                }
                
                sb.append("感謝您的參與！");
                
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(sb.toString()))
                ).get();
                
            } else {
                String message = "您尚未綁定會員！\n" +
                               "請先輸入您註冊時使用的手機號碼進行綁定。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            }
        } catch (Exception e) {
            log.error("查詢中獎紀錄失敗", e);
            String message = "查詢過程發生錯誤，請稍後再試或聯繫客服協助。";
            try {
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            } catch (Exception e2) {
                log.error("發送錯誤訊息失敗", e2);
            }
        }
    }
    
    private void handleViewActiveCampaign(String userId) {
        try {
            List<Campaign> activeCampaigns = campaignFrontService.findActiveCampaign();
            
            if (activeCampaigns.isEmpty()) {
                String message = "目前沒有進行中的活動，請持續關注我們的最新消息！";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("🌟 進行中的活動 🌟\n\n");

            for (Campaign campaign : activeCampaigns) {
                sb.append("📢 ").append(campaign.getCampaignTitle()).append("\n\n");            
                sb.append(campaign.getCampaignDescription()).append("\n\n");
                sb.append("💰 最低消費：").append(campaign.getMinOrderAmount()).append(" 元\n");
                sb.append("⏰ 活動期間：\n");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                sb.append(sdf.format(campaign.getStartDate()))
                  .append(" ~ ")
                  .append(sdf.format(campaign.getEndDate()))
                  .append("\n");  
                if (campaign.getNote() != null && !campaign.getNote().isEmpty()) {
                    sb.append("📌 備註：").append(campaign.getNote()).append("\n");
                }                
                sb.append("\n"); 
            }
            sb.append("💡 詳細活動內容請至官網查看：\n");
            sb.append("http://localhost:5173/campaign");

            lineMessagingClient.pushMessage(
                new PushMessage(userId, new TextMessage(sb.toString()))
            ).get();
            
        } catch (Exception e) {
            log.error("查詢活動資訊失敗", e);
            try {
                String errorMessage = "查詢活動資訊時發生錯誤，請稍後再試。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(errorMessage))
                ).get();
            } catch (Exception ex) {
                log.error("發送錯誤訊息失敗", ex);
            }
        }
    }
    
    private void handleViewReservations(String userId) {
        try {
            Optional<LineMember> lineMember = lineMemberRepo.findByLineUserId(userId);
            if (lineMember.isPresent()) {
                List<Reservations> reservations = reservationsRepo.findByContactPhone(
                    lineMember.get().getMember().getMemberPhone()
                );
                
                if(reservations.isEmpty()) {
                    String message = "目前查無您的訂位紀錄，立即預訂，享受專屬座位的貼心服務吧！\n" +
                                   "👉 線上訂位：http://localhost:5173/reservations";
                    lineMessagingClient.pushMessage(
                        new PushMessage(userId, new TextMessage(message))
                    ).get();
                    return;
                }

                StringBuilder sb = new StringBuilder();
                sb.append("📅 您的訂位紀錄：\n\n");

                int count = 0;
                for (Reservations reservation : reservations) {
                    if (count >= 3) break;

                    sb.append("🕒 訂位時間：")
                      .append(reservation.getReservationDate())
                      .append(" ")
                      .append(reservation.getStartTime())
                      .append("\n");

                    sb.append("👥 訂位人數：")
                      .append(reservation.getNumberPeople())
                      .append(" 位\n");

                    sb.append("👤 訂位姓名：")
                      .append(reservation.getCustomerName())
                      .append("\n");

                    sb.append("📞 聯絡電話：")
                      .append(reservation.getContactPhone())
                      .append("\n");

                    sb.append("✨ 訂位狀態：")
                      .append(reservation.getReservationStatus())
                      .append("\n");

                    if (reservation.getNotes() != null && !reservation.getNotes().isEmpty()) {
                        sb.append("📝 備註：")
                          .append(reservation.getNotes())
                          .append("\n");
                    }

                    sb.append("\n");
                    count++;
                }

                if (reservations.size() > 3) {
                    sb.append("還有 ").append(reservations.size() - 3).append(" 筆更早的訂位紀錄\n");
                    sb.append("完整訂位紀錄請至官網查看\n");
                }

                sb.append("\n📌 如需修改訂位，請來電 02-2345-6789");

                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(sb.toString()))
                ).get();

            } else {
                String message = "您尚未綁定會員！\n" +
                               "請先輸入您註冊時使用的手機號碼進行綁定。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            }
        } catch (Exception e) {
            log.error("查詢訂位紀錄失敗", e);
            try {
                String errorMessage = "查詢訂位紀錄時發生錯誤，請稍後再試或聯繫客服協助。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(errorMessage))
                ).get();
            } catch (Exception ex) {
                log.error("發送錯誤訊息失敗", ex);
            }
        }
    }
    
    private void handleViewPoints(String userId) {
        try {
            Optional<LineMember> lineMember = lineMemberRepo.findByLineUserId(userId);
            if (lineMember.isPresent()) {
                Members member = lineMember.get().getMember();
                PointsTotalBean pointTotals = pointsTotalRepo.findByMembers(member);
                

                if(pointTotals == null) {
                    String message = "您目前還沒有紅利點數！\n" +
                                   "消費即可累積點數，快來享受會員專屬優惠 🛍\n" +
                                   "👉 立即訂位：http://localhost:5173/reservations";
                    lineMessagingClient.pushMessage(
                        new PushMessage(userId, new TextMessage(message))
                    ).get();
                    return;
                }
                
                Integer points = pointTotals.getPointsTotal();
                StringBuilder sb = new StringBuilder();
                sb.append("💰 您的紅利點數資訊\n\n");
                sb.append("目前點數：").append(points).append(" 點\n\n");
                
             
                sb.append("📌 點數說明：\n");
                sb.append("- 每消費 100 元可獲得 1 點\n");
                
                
                if (points >= 100) {
                    sb.append("\n🎉 您的點數已經可以兌換優惠了！\n");
                    sb.append("下次消費時可以折抵 ").append(points).append(" 元");
                }
                
                sb.append("\n\n💡 更多優惠資訊請至官網查看");

                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(sb.toString()))
                ).get();

            } else {
                String message = "您尚未綁定會員！\n" +
                               "請先輸入您註冊時使用的手機號碼進行綁定。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(message))
                ).get();
            }
        } catch (Exception e) {
            log.error("查詢紅利點數失敗", e);
            try {
                String errorMessage = "查詢紅利點數時發生錯誤，請稍後再試或聯繫客服協助。";
                lineMessagingClient.pushMessage(
                    new PushMessage(userId, new TextMessage(errorMessage))
                ).get();
            } catch (Exception ex) {
                log.error("發送錯誤訊息失敗", ex);
            }
        }
    }
}