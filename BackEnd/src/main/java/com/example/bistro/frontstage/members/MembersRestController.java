package com.example.bistro.frontstage.members;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.PointsTotal.PointsTotalBean;
import com.example.bistro.backstage.PointsTotal.PointsTotalRepository;
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
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api")
public class MembersRestController {
	
	@Autowired
	private MemberFrontService memberFronetService;
	
	@Autowired
	private PointsTotalRepository PTRepo;
	
	@Autowired
	private ResetPasswordValidTwilioService ResetTwilioService;
	
	private String verifyCode=null;
	//新增會員帳號
	@PostMapping("/members/create")
	public ResponseEntity<Map<String, String>> createMember(@RequestBody MembersDTO membersDTO) {
		String userName = membersDTO.getUserName();
		String userPhone = membersDTO.getUserPhone();
		String userAccount = membersDTO.getUserEmail();
		String userPassword = membersDTO.getUserPassword();
		Optional<Members> result = memberFronetService.findMemberByAccount(userAccount);
		Map<String, String> response = new HashMap<>();
		if(result.isPresent()) {
			response.put("status", "Fail");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
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

	//查詢會員
	@GetMapping("/frontend/members/{id}")
	public ResponseEntity<Members> getMethodName(@PathVariable Integer id) {
		Optional<Members> result = memberFronetService.findMemberById(id);
		if(result.isPresent()) {
			Members memberData = result.get();
			Optional<PointsTotalBean> pointResult = PTRepo.findByMembersId(id);
			if(pointResult.isPresent()) {
				PointsTotalBean pointsData = pointResult.get();
				memberData.setMemberPoint(pointsData.getPointsTotal());
			}else {
				memberData.setMemberPoint(0);
			}
			return ResponseEntity.ok(memberData);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	//修改會員密碼
	@PutMapping("/frontend/members/{id}/password")
	public ResponseEntity<Map<String, String>> updateMemberPassword(@PathVariable Integer id,@RequestBody MemberPasswordDTO memberPWD) {
		verifyCode=null;
		System.out.println("接收密碼開始");
		Map<String, String> response = new HashMap<>();
		Optional<Members> resultData = memberFronetService.findMemberById(id);
		if(resultData.isPresent()) {
			System.out.println();
			String newPassword = memberPWD.getNewPassword();
			Members memberBean = resultData.get();
			memberBean.setMemberPassword(newPassword);
			memberFronetService.changeMemberPassword(memberBean);
		}else {
			response.put("status", "fail");
			response.put("message", "無此帳號");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
		System.out.println(memberPWD.getNewPassword());

		response.put("status", "success");
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	//修改會員
	@PutMapping("/frontend/members/{id}")
	public ResponseEntity<Map<String, String>> updateMemberProfile(@PathVariable Integer id, @RequestBody MemberProfileDTO memberProfileDTO) {
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
	//註銷會員
	@DeleteMapping("/frontend/members/{id}")
	public ResponseEntity<?> cancelMemberAccount(@PathVariable Integer id) {
		Optional<Members> resultData = memberFronetService.findMemberById(id);
		System.out.println("準備註銷");
		if(resultData.isPresent()) {
			System.out.println("找到對象");
			Members memberBean = resultData.get();
			String status="註銷";
			memberBean.setMemberStatus(status);
			memberFronetService.updateMember(memberBean);
		}
		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		return null;
	}
	
	//發送簡訊
	@PutMapping("/frontend/members/{id}/sms")
	public ResponseEntity<Map<String, String>> sendSMS(@PathVariable Integer id,@RequestBody MemberPasswordDTO memberPWD) {
		
		String uuid = ResetTwilioService.sendPhoneConfirm(memberPWD.getPhone());
		verifyCode=uuid;
		Map<String, String> response = new HashMap<>();
		if(uuid!=null) {
			response.put("status", "success");
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}else {
			response.put("status", "fail");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	//核對驗證碼
	@PutMapping("/frontend/members/{id}/verify")
	public ResponseEntity<Map<String, String>> checkVerifyCode(@PathVariable Integer id,@RequestBody MemberPasswordDTO memberPWD) {
		String verificationCode = memberPWD.getVerificationCode();
		Map<String, String> response = new HashMap<>();
		try {
			if(verifyCode.equals(verificationCode)) {
				response.put("status", "success");
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			else {
				response.put("status", "fail");
				response.put("message", "驗證碼不符合");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
		} catch (NullPointerException e) {
			response.put("status", "fail");
			response.put("message", "驗證碼已棄置，請重新發送");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	
}
