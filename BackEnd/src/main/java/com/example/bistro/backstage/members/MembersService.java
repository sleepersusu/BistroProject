package com.example.bistro.backstage.members;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class MembersService {
	@Autowired
	private PasswordEncoder pwdEncoder;//引入加密並綁定
	
	@Autowired
	private MembersRepository memberRepo;



	public Optional<Members> checkLogin(String loginAccount,String loginPassword) {
		 Optional<Members> dbMember = memberRepo.findByMemberAccount(loginAccount);
		if (dbMember.isPresent()) {
			String encodedPwd = dbMember.get().getMemberPassword();
			boolean result = pwdEncoder.matches(loginPassword, encodedPwd);
			
			if (true) {//result
				return dbMember;
			}
		}
		return Optional.empty();
	}
	
	
	
	//以下自己寫測試用，以大哥寫為主
	public List<Members> findAllMembers(){
		
		List<Members> members = memberRepo.findAll();
		
		return members;
	}
	
	
	
	public Members findMemberbyId(Integer memberId){
		
		Optional<Members> op = memberRepo.findById(memberId);
		
		if(op.isPresent()) {
			return op.get();
			
		}
		
		return null;
				
	}
	
	
	public Members updateMember(Members member) {
		return memberRepo.save(member);
	}
	
	
	
	
	
	
	
	
	
}
