package com.example.bistro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@Data
@PropertySource("googleoauth.properties")
public class GoogleConfig {
	
	@Value("${client_id}")
	private String clientId;
	@Value("${client_secret}")
	private String clientSecret;
	@Value("${redirect_uris}")
	private String redirectUris;

}
