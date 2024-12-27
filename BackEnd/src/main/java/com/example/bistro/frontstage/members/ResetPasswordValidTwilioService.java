package com.example.bistro.frontstage.members;

import java.util.UUID;

import org.springframework.stereotype.Service;
import com.example.bistro.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class ResetPasswordValidTwilioService {
	private final String accountSid;
	private final String authToken;
	private final String fromPhoneNumber;
	private final String phoneNumberTest;

	public ResetPasswordValidTwilioService(TwilioConfig twilioConfig) {
		this.accountSid = twilioConfig.getAccountSid();
		this.authToken = twilioConfig.getAuthToken();
		this.fromPhoneNumber = twilioConfig.getPhoneNumber();
		this.phoneNumberTest = twilioConfig.getPhoneNumberTest();
		Twilio.init(accountSid, authToken);
	}

	public void sendSms(String toPhoneNumber, String messageContent) {
		Message message = Message
				.creator(new PhoneNumber(phoneNumberTest), new PhoneNumber(fromPhoneNumber), messageContent).create();

		System.out.println("Message sent! SID: " + message.getSid());
	}

	public String phoneConfirm(String toPhoneNumber) {
		String contactPhone = "+886" + toPhoneNumber.replaceFirst("^0", "");
		if (phoneNumberTest.equals(contactPhone)) {
			
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String messageContent = String.format(
				"您的訂位成功！\n" 
				+ "驗證碼為：%s\n" 
				+ "請輸入正確驗證碼，核對身分！", uuid);
			System.out.println("準備發送驗證簡訊");
			sendSms(phoneNumberTest, messageContent);
			return uuid;
		} else {
			System.out.println("No SMS sent. The input phone number is not the test number.");
			return null;
		}
	}
}
