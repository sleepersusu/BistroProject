package com.example.bistro.frontstage.members;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;

@Service
public class MemberFrontService {
	
	@Autowired
	private PasswordEncoder pwdEncoder;

	@Autowired
	private MembersRepository memberRepo;
	
	public Optional<Members> findMemberByAccount(String Account) {
		Optional<Members> memberData = memberRepo.findByMemberAccount(Account);
		return memberData;
	}
	public Members insertMember(Members memberBean) {
		String memberShip="會員";
		String memberStatus="啟用";
		memberBean.setMemberShip(memberShip);
		memberBean.setMemberPoint(0);
		memberBean.setMemberStatus(memberStatus);
		if(memberBean.getMemberPassword()!=null) {
		String password = memberBean.getMemberPassword();
		String encodedPwd = pwdEncoder.encode(password);
		memberBean.setMemberPassword(encodedPwd);
		}
		return memberRepo.save(memberBean);
	}
	
	public Optional<Members> findMemberById(Integer id) {
		Optional<Members> resultData = memberRepo.findById(id);
		return resultData;
	}
	public Optional<Members> updateMember(Members memberBean) {
		Optional<Members> resultData = memberRepo.findById(memberBean.getId());
		if(resultData.isPresent()) {
			Members memberData = resultData.get();
			memberBean.setCreatedAt(memberData.getCreatedAt());
			memberBean.setMemberStatus(memberData.getMemberStatus());
			memberBean.setMemberShip(memberData.getMemberShip());
			if(memberBean.getMemberPoint()==null) {
				memberBean.setMemberPoint(memberData.getMemberPoint());				
			}
			if(memberBean.getMemberImg()!=null) {
				
			}
			
		}
		return null;
	}

	
}
