package com.example.bistro.frontstage.userlonin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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



    public ResponseEntity<String> loginPost(@RequestParam String memberAccount,@RequestParam String memberPassword,HttpSession httpSession) {
    	Optional<Members> checkResult = membersService.checkLogin(memberAccount, memberPassword);//撈這筆資料


		if (checkResult.isPresent()) {
			long currentTime = System.currentTimeMillis();
			System.out.println("登入成功，建立Session");
			httpSession.setMaxInactiveInterval(3600);//session存活時間sec
			httpSession.setAttribute("lastAccessTime", currentTime);
			httpSession.setAttribute("membersId", checkResult.get().getId());
			//Session紀錄資訊
			System.out.println("session有取到"+httpSession.getAttribute("membersId"));
			return ResponseEntity.ok("Login OK.");
		}else {
			System.out.println("登入失敗");
			
			return ResponseEntity.status(404).body("Users Not Found.");
		}
    }		

}

