package com.example.bistro.frontstage.campaign;



import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import com.example.bistro.backstage.PointsTotal.PointsTotalBean;
import com.example.bistro.backstage.PointsTotal.PointsTotalRepository;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class LineMessageService {
   private final LineMessagingClient lineMessagingClient;
   private final MembersRepository membersRepo;
   private final LineMemberRepository lineMemberRepo;
   private final LotteryWinnersRepository winnersRepo; 
   private final ObjectMapper objectMapper;
   private final CampaignFrontService campaignFrontService;
   private final ReservationsRepository reservationsRepo;
   private final PointsTotalRepository pointsTotalRepo;

   public void processWebhookEvent(String body) throws Exception {
       JsonNode jsonNode = objectMapper.readTree(body);
       JsonNode events = jsonNode.get("events");

       if (events.isArray() && events.size() > 0) {
           JsonNode event = events.get(0);
           String type = event.get("type").asText();
           String userId = event.get("source").get("userId").asText();

           switch (type) {
               case "follow":
                   handleFollow(userId);
                   break;
               case "message":
                   handleMessage(event, userId);
                   break;
           }
       }
   }

   private void handleFollow(String userId) throws Exception {
       String message = "感謝您追蹤我們！\n" +
                       "請輸入您註冊會員時使用的手機號碼，\n" +
                       "我們將為您進行會員綁定。";
       sendLineMessage(userId, message);
   }

   private void handleMessage(JsonNode event, String userId) throws Exception {
       JsonNode messageNode = event.get("message");
       if ("text".equals(messageNode.get("type").asText())) {
           String text = messageNode.get("text").asText().trim();
           
           if (text.matches("^09\\d{8}$")) {
               handleMemberBinding(text, userId);
           } else {
               switch (text) {
                   case "#中獎紀錄":
                       handleViewPrizeHistory(userId);
                       break;
                   case "#最新活動":
                       handleViewActiveCampaign(userId);
                       break;
                   case "#我的訂位":
                       handleViewReservations(userId);
                       break;
                   case "#紅利點數":
                       handleViewPoints(userId);
                       break;
               }
           }
       }
   }

   public void sendShippingNotification(Integer winnerId) throws Exception {
       LotteryWinners winner = winnersRepo.findById(winnerId)
           .orElseThrow(() -> new EntityNotFoundException("找不到中獎紀錄"));
           
       LineMember lineMember = lineMemberRepo.findByMemberId(winner.getMember().getId())
           .orElseThrow(() -> new EntityNotFoundException("找不到LINE會員"));

       ShippingDetails details = winner.getShippingDetails();
       String message = createShippingMessage(details);
       sendLineMessage(lineMember.getLineUserId(), message);
   }

   private void handleMemberBinding(String phone, String userId) {
       try {
           Members member = membersRepo.findByMemberPhone(phone)
               .orElse(null);
           
           if (member != null) {
               Optional<LineMember> existingBinding = lineMemberRepo.findByLineUserId(userId);
               if (existingBinding.isPresent()) {
                   handleExistingBinding(existingBinding.get(), member, userId);
                   return;
               }

               createNewBinding(member, userId);
           } else {
               String message = "找不到此手機號碼對應的會員資料，請確認號碼是否正確，或聯繫客服協助。";
               sendLineMessage(userId, message);
           }
       } catch (Exception e) {
           log.error("會員綁定失敗", e);
           handleError(userId, "綁定過程發生錯誤，請稍後再試或聯繫客服協助。");
       }
   }
   
   private void handleExistingBinding(LineMember existingBinding, Members member, String userId) throws Exception {
       String memberName = existingBinding.getMember().getMemberName();
       String message;
       
       if (existingBinding.getMember().getId().equals(member.getId())) {
           message = String.format(
               "親愛的 %s，此手機號碼對應的會員已經與您的 LINE 帳號綁定了！",
               memberName != null ? memberName : "會員"
           );
       } else {
           message = String.format(
               "親愛的 %s，您的 LINE 帳號已經綁定其他會員了！如需更換綁定，請聯繫客服協助。",
               memberName != null ? memberName : "會員"
           );
       }
       
       sendLineMessage(userId, message);
   }

   private void createNewBinding(Members member, String userId) throws Exception {
       LineMember lineMember = new LineMember();
       lineMember.setMember(member);
       lineMember.setLineUserId(userId);
       lineMemberRepo.save(lineMember);

       String memberName = member.getMemberName();
       String message = String.format(
           "歡迎 %s！\n會員綁定成功 🎉\n現在您可以使用所有功能了。",
           memberName != null ? memberName : "會員"
       );
       
       sendLineMessage(userId, message);
   }

   private void handleViewPrizeHistory(String userId) throws Exception {
       Optional<LineMember> lineMember = lineMemberRepo.findByLineUserId(userId);
       
       if (lineMember.isEmpty()) {
           sendNotBindMessage(userId);
           return;
       }

       List<LotteryWinners> winners = winnersRepo.findByMemberOrderByCreatedAtDesc(lineMember.get().getMember());
       
       if (winners.isEmpty()) {
           String message = "您目前還沒有中獎紀錄喔！\n" +
                          "繼續參加活動，增加中獎機會 🎉";
           sendLineMessage(userId, message);
           return;
       }

       String message = formatPrizeHistory(winners);
       sendLineMessage(userId, message);
   }

   private String formatPrizeHistory(List<LotteryWinners> winners) {
       StringBuilder sb = new StringBuilder();
       sb.append("您的中獎紀錄如下：\n\n");
       
       int count = 0;
       for (LotteryWinners winner : winners) {
           if (count >= 3) break;
           
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
       return sb.toString();
   }

   private void handleViewActiveCampaign(String userId) throws Exception {
       List<Campaign> activeCampaigns = campaignFrontService.findActiveCampaign();
       
       if (activeCampaigns.isEmpty()) {
           String message = "目前沒有進行中的活動，請持續關注我們的最新消息！";
           sendLineMessage(userId, message);
           return;
       }

       String message = formatActiveCampaigns(activeCampaigns);
       sendLineMessage(userId, message);
   }

   private String formatActiveCampaigns(List<Campaign> campaigns) {
       StringBuilder sb = new StringBuilder();
       sb.append("🌟 進行中的活動 🌟\n\n");

       SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
       
       for (Campaign campaign : campaigns) {
           sb.append("📢 ").append(campaign.getCampaignTitle()).append("\n\n");            
           sb.append(campaign.getCampaignDescription()).append("\n\n");
           sb.append("💰 最低消費：").append(campaign.getMinOrderAmount()).append(" 元\n");
           sb.append("⏰ 活動期間：\n");
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
       sb.append("http://nightlysips.com/campaign");
       
       return sb.toString();
   }

   private void handleViewReservations(String userId) throws Exception {
       Optional<LineMember> lineMember = lineMemberRepo.findByLineUserId(userId);
       if (lineMember.isEmpty()) {
           sendNotBindMessage(userId);
           return;
       }

       List<Reservations> reservations = reservationsRepo.findByContactPhone(
           lineMember.get().getMember().getMemberPhone()
       );
       
       if (reservations.isEmpty()) {
           String message = "目前查無您的訂位紀錄，立即預訂，享受專屬座位的貼心服務吧！\n" +
                          "👉 線上訂位：http://nightlysips.com/reservations";
           sendLineMessage(userId, message);
           return;
       }

       String message = formatReservations(reservations);
       sendLineMessage(userId, message);
   }

   private String formatReservations(List<Reservations> reservations) {
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
       
       return sb.toString();
   }

   private void handleViewPoints(String userId) throws Exception {
       Optional<LineMember> lineMember = lineMemberRepo.findByLineUserId(userId);
       if (lineMember.isEmpty()) {
           sendNotBindMessage(userId);
           return;
       }

       Members member = lineMember.get().getMember();
       PointsTotalBean pointTotals = pointsTotalRepo.findByMembers(member);
       
       if (pointTotals == null) {
           String message = "您目前還沒有紅利點數！\n" +
                          "消費即可累積點數，快來享受會員專屬優惠 🛍\n" +
                          "👉 立即訂位：http://nightlysips.com/reservations";
           sendLineMessage(userId, message);
           return;
       }
       
       String message = formatPointsInfo(pointTotals.getPointsTotal());
       sendLineMessage(userId, message);
   }

   private String formatPointsInfo(Integer points) {
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
       return sb.toString();
   }

   private void sendLineMessage(String userId, String message) throws Exception {
       TextMessage textMessage = new TextMessage(message);
       lineMessagingClient.pushMessage(new PushMessage(userId, textMessage)).get();
   }

   private void handleError(String userId, String errorMessage) {
       try {
           sendLineMessage(userId, errorMessage);
       } catch (Exception e) {
           log.error("發送錯誤訊息失敗", e);
       }
   }

   private void sendNotBindMessage(String userId) throws Exception {
       String message = "您尚未綁定會員！\n" +
                       "請先輸入您註冊時使用的手機號碼進行綁定。";
       sendLineMessage(userId, message);
   }

   private String createShippingMessage(ShippingDetails details) {
       return String.format(
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
   }
}