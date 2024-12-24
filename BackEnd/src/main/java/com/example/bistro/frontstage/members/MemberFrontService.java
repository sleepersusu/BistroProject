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
	public Members updateMember(Members memberBean) {	
		Members resultData = memberRepo.save(memberBean);
		return resultData;
	}

	
}
