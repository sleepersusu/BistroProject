package com.example.bistro.frontstage.members;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;

@RestController
public class PasswordResetController {
	@Autowired
	private PasswordEncoder pwdEncoder;//引入加密並綁定

	@Autowired
	private MembersRepository membersRepo;

	@Autowired
	private JavaMailSender mailSender;

	private SecretKeySpec secretKey = new SecretKeySpec("1234567890123456".getBytes(), "AES");
	@PostMapping("/api/forgot-password/{email}")
	public ResponseEntity<?> forgotPassword(@PathVariable String email) {
	    Optional<Members> op = membersRepo.findByMemberEmail(email);
	    if (op.isEmpty()) {
	        return ResponseEntity.badRequest().body("找不到該電子信箱");
	    }
	    
	    String encryptedEmail = null;
	    try {
			Cipher encryptCipher = Cipher.getInstance("AES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedEmailBytes = encryptCipher.doFinal(email.getBytes());
			encryptedEmail = Base64.getUrlEncoder().encodeToString(encryptedEmailBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	    
	    System.out.println("加密後的電子郵件: " + encryptedEmail);
		String resetUrl = "http://localhost:5173/reset-password/" + encryptedEmail;  
	    
	    SimpleMailMessage message = new SimpleMailMessage();
	    message.setTo(email);
	    message.setSubject("密碼重設請求");
	    message.setText("請點擊以下連結重設密碼：\n" + resetUrl);
	    
	    mailSender.send(message);
	    return ResponseEntity.ok("重設密碼郵件已發送");
	}

	@PostMapping("/api/reset-password/{encryptedEmail}/{password}")
	public ResponseEntity<?> resetPassword(@PathVariable String encryptedEmail, @PathVariable String password) {
		Cipher decryptCipher;
		String decryptedEmail=null;
		try {
			decryptCipher = Cipher.getInstance("AES");
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decryptedEmailBytes = decryptCipher.doFinal(Base64.getUrlDecoder().decode(encryptedEmail));
			decryptedEmail = new String(decryptedEmailBytes);
			System.out.println("解密後的電子郵件: " + decryptedEmail);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		
	    Optional<Members> op = membersRepo.findByMemberEmail(decryptedEmail);
	    if (op.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到會員資訊");
	    }
	    Members member = op.get();
	    String encode = pwdEncoder.encode(password);
	    member.setMemberPassword(encode);
	    membersRepo.save(member);
	    return ResponseEntity.ok("密碼重設成功");
	}
}
