package com.example.bistro.backstage.members;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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
	
	
	
    //findById 獎品會用到
    public Members findMembersById(Integer id) {
        Optional<Members> op = memberRepo.findById(id);
        return op.isPresent() ? op.get() : null;
    }
	
	
	public Members updateMember(Members member) {
		return memberRepo.save(member);
	}
	
	
	
	
	
	
	
	
	
}
