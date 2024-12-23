package com.example.bistro.frontstage.members;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;




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
		Optional<Members> result = memberFronetService.findMemberByAccount(userAccount);
		Map<String, String> response = new HashMap<>();
		if(result.isPresent()) {
			response.put("status", "資料已存在");
			return ResponseEntity.status(404).body(response);
		}else {

			Members memberBean = new Members();
			memberBean.setMemberName(userName);
			memberBean.setMemberPhone(userPhone);
			memberBean.setMemberAccount(userAccount);
			memberBean.setMemberPassword(userPassword);
			memberBean.setMemberEmail(userAccount);

			Members memberData = memberFronetService.insertMember(memberBean);
			
			response.put("status", "success");
			response.put("memberId", memberData.getId().toString());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Members> getMethodName(@PathVariable Integer id) {
		Optional<Members> result = memberFronetService.findMemberById(id);
		if(result.isPresent()) {
			Members memberData = result.get();
			return ResponseEntity.ok(memberData);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Map<String, String>> updateMemberProfile(@PathVariable Integer id, @RequestBody memberProfileDTO memberProfileDTO) {
		Optional<Members> resultData = memberFronetService.findMemberById(id);
		Map<String, String> response = new HashMap<>();
		if(resultData.isPresent()) {
			Members memberData = resultData.get();
			memberData.setMemberName(memberProfileDTO.getUserName());
			memberData.setMemberEmail(memberProfileDTO.getUserEmail());
			memberData.setMemberPhone(memberProfileDTO.getUserPhone());
			memberData.setMemberFavor(memberProfileDTO.getUserFavor());
			memberData.setMemberSex(memberProfileDTO.getUserGender());
			memberData.setMemberAddress(memberProfileDTO.getUserAddress());
			memberData.setMemberBirthday(memberProfileDTO.getUserBirthdate());
			memberFronetService.updateMember(memberData);
			response.put("status", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		response.put("status", "fail");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@DeleteMapping("/{id}")
	public String name() {
		return null;
	}
	
	
}
