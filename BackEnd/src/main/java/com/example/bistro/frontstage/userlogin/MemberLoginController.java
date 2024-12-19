package com.example.bistro.frontstage.userlogin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class MemberLoginController {
	
	@Autowired
	private MembersService membersService;
	
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginPost(@RequestBody Map<String,String> userRequest,HttpSession httpSession) {
    	String memberAccount = userRequest.get("Account");
    	String memberPassword = userRequest.get("Password");
    	Optional<Members> checkResult = membersService.checkLogin(memberAccount, memberPassword);//撈這筆資料

    	Map<String, String> response = new HashMap<>();
		if (checkResult.isPresent()) {
			Members memberData = checkResult.get();
			long currentTime = System.currentTimeMillis();
			System.out.println("登入成功，建立Session");
			httpSession.setMaxInactiveInterval(3600);//session存活時間sec
			httpSession.setAttribute("lastAccessTime", currentTime);
			httpSession.setAttribute("membersId", memberData.getId());
			//Session紀錄資訊
			System.out.println(httpSession.hashCode());
			System.out.println("session有取到"+httpSession.getAttribute("membersId"));
			response.put("status", "success");
			response.put("memberId", memberData.getId().toString());
			response.put("memberName", memberData.getMemberName());
			return ResponseEntity.ok(response);
		}else {
			System.out.println("登入失敗");
			response.put("status", "fail");
			return ResponseEntity.status(404).body(response);
		}
    }		
}
