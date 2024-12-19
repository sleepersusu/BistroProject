package com.example.bistro.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.bistro.config.GoogleConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class GoogleOAuthController {
	
	@Autowired
	private GoogleConfig googleConfig;
	
	@GetMapping("/google/login")
	public String googleLonin() {
		System.out.println("近來GOOGLE");
		String oauthUrl="https://accounts.google.com/o/oauth2/v2/auth?"
		+ "client_id" + googleConfig.getClientId() 
		+ "&response_type=code"
		+ "&scope=email%20profile"
		+ "&redirect_uri="+googleConfig.getRedirectUris()
		+ "&state=state";
		return "redirect:"+oauthUrl;
	}
	@GetMapping("/google")
	public String googleFeeback(@RequestParam String code) throws JsonMappingException, JsonProcessingException {
		RestClient restClient = RestClient.create();
		
		String queryBody = UriComponentsBuilder.newInstance()
				.queryParam("code", code)
				.queryParam("grant_type", "authorization_code")
				.queryParam("client_id", googleConfig.getClientId())
				.queryParam("client_secret", googleConfig.getClientSecret())
				.queryParam("redirect_uri", googleConfig.getRedirectUris())
				.build().getQuery();
		String credentials=restClient.post()
				.uri("https://oauth2.googleapis.com/token")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.body(queryBody).retrieve().body(String.class);
		System.out.println("證書接收:"+credentials);
		
		JsonNode jsonNode = new ObjectMapper().readTree(credentials);
		String accessToken = jsonNode.get("access_token").asText();
		String idToken = jsonNode.get("id_token").asText();
		
		String result = restClient.get()
		.uri("https://www.googleapis.com/drive/v2/files?access_token="+accessToken)
		.header("Authorization", "Bearer"+accessToken)
		.retrieve().body(String.class);
		System.out.println("再發TOKEN結果返回:"+result);
		JsonNode jsonResultNode = new ObjectMapper().readTree(result);
		String userEmail=jsonResultNode.get("email").asText();
		System.out.println("取得信箱:"+userEmail);
		return null;
	}
	

}
