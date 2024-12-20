package com.example.bistro.frontstage.members;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;

@RestController
public class PasswordResetController {

	@Autowired
	private MembersRepository membersRepo;

	@Autowired
	private JavaMailSender mailSender;


	@PostMapping("/api/forgot-password/{email}")
	public ResponseEntity<?> forgotPassword(@PathVariable String email) {
	    Optional<Members> op = membersRepo.findByMemberEmail(email);
	    if (op.isEmpty()) {
	        return ResponseEntity.badRequest().body("找不到該電子信箱");
	    }
	    
	    String resetUrl = "http://localhost:5173/reset-password/" + email;  
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(email);
	    message.setSubject("密碼重設請求");
	    message.setText("請點擊以下連結重設密碼：\n" + resetUrl);
	    
	    mailSender.send(message);
	    return ResponseEntity.ok("重設密碼郵件已發送");
	}

	@PostMapping("/api/reset-password/{email}/{password}")
	public ResponseEntity<?> resetPassword(@PathVariable String email, @PathVariable String password) {
	    Optional<Members> op = membersRepo.findByMemberEmail(email);
	    if (op.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到會員資訊");
	    }
	    Members member = op.get();
	    member.setMemberPassword(password);
	    membersRepo.save(member);
	    return ResponseEntity.ok("密碼重設成功");
	}
}
