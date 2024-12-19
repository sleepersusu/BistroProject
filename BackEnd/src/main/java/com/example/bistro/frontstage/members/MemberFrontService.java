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
	
	public boolean findMemberByAccount(String Account) {
		Optional<Members> result = memberRepo.findByMemberAccount(Account);
		if(result.isPresent()) {
			return true;
		}else {
			return false;
		}
	}
	public Members createMember(Members memberBean) {
		String memberShip="會員";
		String memberStatus="啟用";
		memberBean.setMemberShip(memberShip);
		memberBean.setMemberStatus(memberStatus);
		String password = memberBean.getMemberPassword();
		String encodedPwd = pwdEncoder.encode(password);
		memberBean.setMemberPassword(encodedPwd);
		return memberRepo.save(memberBean);
	}
}
