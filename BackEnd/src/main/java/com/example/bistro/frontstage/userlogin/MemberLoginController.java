package com.example.bistro.frontstage.userlogin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class MemberLoginController {
	
	@Autowired
	private MembersService membersService;
	
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginPost(@RequestBody Map<String,String> userRequest,HttpSession httpSession) {
    	String memberAccount = userRequest.get("account");
    	String memberPassword = userRequest.get("password");
    	Optional<Members> checkResult = membersService.checkLogin(memberAccount, memberPassword);//撈這筆資料
    	Optional<Members> result = membersService.findMemberByAccount(memberAccount);
    	Map<String, Object> response = new HashMap<>();
		if (checkResult.isPresent()) {
			Members memberData = checkResult.get();
			if(memberData.getMemberStatus().equals("註銷")) {
				System.out.println("核對Status");
				response.put("status", "fail");
		        response.put("message", "帳號已註銷");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
			}
			long currentTime = System.currentTimeMillis();
			System.out.println("登入成功，建立Session");
			httpSession.setMaxInactiveInterval(10800);//session存活時間sec
			httpSession.setAttribute("lastAccessTime", currentTime);
			httpSession.setAttribute("membersId", memberData.getId());
			//Session紀錄資訊
			System.out.println("前台的session有取到"+httpSession.getAttribute("membersId"));
			response.put("status", "success");
			response.put("memberId", memberData.getId().toString());
			response.put("memberName", memberData.getMemberName());
			if(memberData.getMemberPoint()==null) {
				response.put("memberPoint", 0);
			}else {
				response.put("memberPoint", memberData.getMemberPoint().toString());
			}
			return ResponseEntity.ok(response);
		}else if(result.isPresent()){
			response.put("status", "fail");
	        response.put("message", "輸入密碼錯誤");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}else {
			System.out.println("會員登入失敗");
			response.put("status", "fail");
			return ResponseEntity.status(404).body(response);
		}
    }
    
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Map<String, Object> response = new HashMap<>();
        if (session != null) {
            session.invalidate();  // 销毁 Session
            System.out.println("登出銷毀Session");
            response.put("status", "success");
        }
        return ResponseEntity.ok(response);
    }
}
