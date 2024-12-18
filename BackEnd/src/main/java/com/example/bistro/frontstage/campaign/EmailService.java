package com.example.bistro.frontstage.campaign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.shippingDetails.ShippingDetails;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendShippingNotification(ShippingDetails details) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(details.getEmail());
            message.setSubject("您的獎品已出貨通知");
            message.setText(String.format(
                "親愛的 %s 您好，\n\n" +
                "您的獎品已經出貨了！\n" +
                "獎品名稱：%s\n" +
                "配送地址：%s\n\n" +
                "如有任何問題，請隨時與我們聯繫。\n\n" +
                "祝您有愉快的一天！\n" +
                "Nightly Sips 酌夜語 敬上",
                details.getName(),
                details.getLotteryWinner().fetchPrizeName(),
                details.getAddress()
            ));
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("寄送郵件失敗");
        }
    }
}
