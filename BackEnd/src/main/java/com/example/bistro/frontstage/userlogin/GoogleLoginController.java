package com.example.bistro.frontstage.userlogin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.config.GoogleConfig;
import com.example.bistro.frontstage.members.MemberFrontService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleLoginController {

	@Autowired
	private GoogleConfig googleConfig;

	@Autowired
	private MemberFrontService membersFrontService;

	@GetMapping("/google/login")
	public String googleLonin() {
		System.out.println("進到這邊/google/login");
		String oauthUrl = "https://accounts.google.com/o/oauth2/v2/auth?" + "client_id=" + googleConfig.getClientId()
				+ "&response_type=code" + "&scope=email%20profile" + "&redirect_uri=" + googleConfig.getRedirectUris()
				+ "&state=state";
		return "redirect:" + oauthUrl;
	}

	@GetMapping("/google")
	public ResponseEntity<Map<String, String>> googleFeeback(@RequestParam("idToken") String code, HttpSession httpSession)
			throws JsonMappingException, JsonProcessingException {
		System.out.println("進到這邊/google");
		Map<String, String> response = new HashMap<>();
//		RestClient restClient = RestClient.create();
		System.out.println("取得前端的code: "+code);
		String idTokenn=code;

	    String CLIENT_ID = googleConfig.getClientId();
	    String useremail=null;
	    String username =null;
		try
		{
			JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), jsonFactory)
                    .setAudience(Collections.singletonList(CLIENT_ID))  // 設置合法的 Client ID
                    .build();

            // 驗證 ID Token
            GoogleIdToken idToken = verifier.verify(idTokenn);
			if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                System.out.println(payload);
                useremail = payload.getEmail();  // 提取 Email
                username = (String) payload.get("name");  // 提取 Name
			} else {
				System.out.println("Invalid ID token.");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		Optional<Members> Result = membersFrontService.findMemberByAccount(useremail);
		if (Result.isPresent()) {
			Members memberData = Result.get();
			long currentTime = System.currentTimeMillis();
			
			System.out.println("登入成功，建立Session");
			httpSession.setMaxInactiveInterval(10800);// session存活時間sec
			httpSession.setAttribute("lastAccessTime", currentTime);// Session紀錄資訊
			httpSession.setAttribute("membersId", memberData.getId());// Session紀錄資訊
			System.out.println("Google登入下session有取到" + httpSession.getAttribute("membersId"));
			
			response.put("status", "success");
			response.put("memberId", memberData.getId().toString());
			response.put("memberName", memberData.getMemberName());
			if (memberData.getMemberPoint() == null) {
				response.put("memberPoint", "0");
			} else {
				response.put("memberPoint", memberData.getMemberPoint().toString());
			}
			return ResponseEntity.ok(response);
		} else {
			System.out.println("GOOGLE登入，沒有此帳號，註冊帳號");
			Members memberBean = new Members();
			memberBean.setMemberAccount(useremail);
			memberBean.setMemberName(username);
			memberBean.setMemberEmail(useremail);
			Members resultData = membersFrontService.insertMember(memberBean);
			System.out.println("建立後開始登入，建立Session");
			long currentTime = System.currentTimeMillis();
			httpSession.setMaxInactiveInterval(10800);// session存活時間sec
			httpSession.setAttribute("lastAccessTime", currentTime);// Session紀錄資訊
			System.out.println("GOOGLE新增的ID"+resultData.getId());
			httpSession.setAttribute("membersId", resultData.getId());// Session紀錄資訊
			System.out.println("Google登入下session有取到" + httpSession.getAttribute("membersId"));
			response.put("status", "success");
			response.put("memberId", resultData.getId().toString());
			response.put("memberName", resultData.getMemberName());
			response.put("memberPoint", "0");
			return ResponseEntity.ok(response);
		}
	}

}

//		String queryBody = UriComponentsBuilder.newInstance().queryParam("code", code)
//				.queryParam("grant_type", "authorization_code").queryParam("client_id", googleConfig.getClientId())
//				.queryParam("client_secret", googleConfig.getClientSecret())
//				.queryParam("redirect_uri", googleConfig.getRedirectUris()).build().getQuery();
//		String credentials = restClient.post().uri("https://oauth2.googleapis.com/token")
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED).body(queryBody).retrieve().body(String.class);
//		System.out.println("證書接收:" + credentials);
//
//		JsonNode jsonNode = new ObjectMapper().readTree(credentials);
//		String accessToken = jsonNode.get("access_token").asText();
//		String idToken = jsonNode.get("id_token").asText();
//
//		String result = restClient.get().uri("https://www.googleapis.com/oauth2/v2/userinfo")
//				.header("Authorization", "Bearer " + idToken).retrieve().body(String.class);
//		System.out.println("再發TOKEN結果返回:" + result);
//		JsonNode jsonResultNode = new ObjectMapper().readTree(result);
//		String userEmail = jsonResultNode.get("email").asText();
//		System.out.println("取得信箱:" + userEmail);