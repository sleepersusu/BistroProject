package com.example.bistro.frontstage.members;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/members")
public class MembersRestController {
	
	@Autowired
	private MemberFrontService memberFronetService;
	
	//新增會員帳號
	@PostMapping("/create")
	public ResponseEntity<Map<String, String>> createMember(@RequestBody MembersDTO membersDTO) {
		String userName = membersDTO.getUserName();
		String userPhone = membersDTO.getUserPhone();
		String userAccount = membersDTO.getUserAccount();
		String userPassword = membersDTO.getUserPassword();
		boolean result = memberFronetService.findMemberByAccount(userAccount);
		Map<String, String> response = new HashMap<>();
		if(result) {
			response.put("status", "資料已存在");
			return ResponseEntity.status(404).body(response);
		}else {

			Members memberBean = new Members();
			memberBean.setMemberName(userName);
			memberBean.setMemberPhone(userPhone);
			memberBean.setMemberAccount(userAccount);
			memberBean.setMemberPassword(userPassword);
			memberBean.setMemberEmail(userAccount);

			Members memberData = memberFronetService.createMember(memberBean);
			
			response.put("status", "success");
			response.put("memberId", memberData.getId().toString());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}


	}
	
}
