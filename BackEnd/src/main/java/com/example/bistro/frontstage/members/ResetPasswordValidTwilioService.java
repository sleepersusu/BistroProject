package com.example.bistro.frontstage.members;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bistro.config.ResetPasswordTwilioConfig;
import com.example.bistro.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class ResetPasswordValidTwilioService {
	
	@Autowired
    private ResetPasswordTwilioConfig twilioConfig; // 注入 TwilioConfig
	
	private String accountSid;
	private String authToken;
	private String fromPhoneNumber;
	private String phoneNumberTest;
	
	public ResetPasswordValidTwilioService(ResetPasswordTwilioConfig twilioConfig) {
		this.accountSid = twilioConfig.getAccountSidVerify();
		this.authToken = twilioConfig.getAuthTokenVerify();
		this.fromPhoneNumber = twilioConfig.getPhoneNumberVerify();
		this.phoneNumberTest = twilioConfig.getPhoneMyNumberVerify();
		
//		Twilio.init(accountSid, authToken);
	}
	

	public void sendSms(String toPhoneNumber, String messageContent) {
		System.out.println("換我RESET初始化");
		Twilio.init(accountSid, authToken);
		Message message = Message
				.creator(new PhoneNumber(phoneNumberTest), new PhoneNumber(fromPhoneNumber), messageContent)
				.create();

		System.out.println("已發送twilio簡訊! SID: " + message.getSid());
	}

	public String sendPhoneConfirm(String toPhoneNumber) {
		String contactPhone = "+886" + toPhoneNumber.replaceFirst("^0", "");
		if (phoneNumberTest.equals(contactPhone)) {
			System.out.println("進sendPhoneConfirm這邊了");
			String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
			String messageContent = String.format(
				"\n驗證碼為：%s\n" 
				+ "請立即輸入驗證碼，核對身分!\n" 
				+ "如驗證碼無誤，可繼續修改密碼！", uuid);
			System.out.println("準備發送驗證簡訊");
			sendSms(phoneNumberTest, messageContent);
			return uuid;
		} else {
			System.out.println("No SMS sent. The input phone number is not the test number.");
			return null;
		}
	}
}
