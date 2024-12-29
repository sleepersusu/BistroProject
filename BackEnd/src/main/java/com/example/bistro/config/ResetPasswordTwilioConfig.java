package com.example.bistro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;

@Configuration
@Getter
@PropertySource("twilio.properties")
public class ResetPasswordTwilioConfig {
   
    @Value("${twilio.account.sid.verify}")
    private String accountSidVerify;

    @Value("${twilio.auth.token.verify}")
    private String authTokenVerify;

    @Value("${twilio.phone.number.verify}")
    private String phoneNumberVerify;
    
    @Value("${twilio.phone.mynumber.verify}")
    private String phoneMyNumberVerify;

}
