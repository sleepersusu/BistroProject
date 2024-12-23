package com.example.bistro.frontstage.reservations;



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import com.example.bistro.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioService {
	 	private final String accountSid;
	    private final String authToken;
	    private final String fromPhoneNumber;
	    private final String phoneNumberTest;

	    public TwilioService(TwilioConfig twilioConfig) {
	        this.accountSid = twilioConfig.getAccountSid();
	        this.authToken = twilioConfig.getAuthToken();
	        this.fromPhoneNumber = twilioConfig.getPhoneNumber();
	        this.phoneNumberTest = twilioConfig.getPhoneNumberTest();
	        Twilio.init(accountSid, authToken);
	    }

	    public void sendSms(String toPhoneNumber, String messageContent) {
	        Message message = Message.creator(
	                new PhoneNumber(phoneNumberTest),
	                new PhoneNumber(fromPhoneNumber),
	                messageContent
	        ).create();

	        System.out.println("Message sent! SID: " + message.getSid());
	    }
	    	    
	    public void sendReservationConfirmation(String toPhoneNumber, String reservationDate, String reservationTime, int numberPeople) {
    	 
	    	LocalDate Date = LocalDate.now();   //要把格式改成
			
			 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		        String formattedDate = Date.format(formatter);
	    	
	    	 String messageContent = String.format("您的訂位成功！\n" +
	                 "訂位日期：%s\n" +
	                 "訂位時間：%s\n" +
	                 "人數：%d\n" +
	                 "感謝您的預約，期待您的光臨！", formattedDate, reservationTime, numberPeople);

	        sendSms(phoneNumberTest, messageContent);
	    }
}
