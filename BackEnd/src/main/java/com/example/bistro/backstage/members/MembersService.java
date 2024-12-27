package com.example.bistro.backstage.members;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MembersService {
	@Autowired
	private PasswordEncoder pwdEncoder;//引入加密並綁定
	
	@Autowired
	private MembersRepository memberRepo;
	
	public Page<Members> findWithPagination(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return memberRepo.findByMemberNameOrMemberEmailContaining(
                    search, search, pageable);
        }
        return memberRepo.findAll(pageable);
    }
	
	public Members insertMember(Members memberBean) {
		String memberShip="會員";
		String memberStatus="啟用";
		memberBean.setMemberShip(memberShip);
		memberBean.setMemberPoint(0);
		memberBean.setMemberStatus(memberStatus);
		String password = memberBean.getMemberPassword();
		String encodedPwd = pwdEncoder.encode(password);
		memberBean.setMemberPassword(encodedPwd);
		return memberRepo.save(memberBean);
	}
	
    //findById
    public Members findMembersById(Integer id) {
        Optional<Members> op = memberRepo.findById(id);
        return op.isPresent() ? op.get() : null;
    }
    //確認密碼
    public boolean checkMembersPWD(Members memberBean) {
    	Optional<Members> resultData = memberRepo.findById(memberBean.getId());
    	if(resultData.isPresent()) {
    		Members memberData = resultData.get();
    		String encodedPwd = memberData.getMemberPassword();
    		boolean result = pwdEncoder.matches(memberBean.getMemberPassword(), encodedPwd);
    		if(!result) {
    			memberBean.getMemberPassword();
    		}
    		return result;
    	}else {
    		return false;
    	}
    }

	//根據姓名或電話查詢會員
	public Members findMembers(String name, String phone) {
	    // 如果有姓名，優先用姓名查詢
		// 如果姓名為空但有電話，則用電話查詢
		// 如果兩者都沒有提供，返回 null
	    if (name != null && !name.isEmpty()) {
	        return memberRepo.findByMemberName(name).orElse(null);
	    }else if (phone != null && !phone.isEmpty()) {
	        return memberRepo.findByMemberPhone(phone).orElse(null);
	    }
	    return null;
	}
	public List<Members> findAllMembers() {
		return memberRepo.findAll();
	}
	
	
	public Optional<Members> checkLogin(String loginAccount,String loginPassword) {
		 Optional<Members> dbMember = memberRepo.findByMemberAccount(loginAccount);
		if (dbMember.isPresent()) {
			String encodedPwd = dbMember.get().getMemberPassword();
			boolean result = pwdEncoder.matches(loginPassword, encodedPwd);
			
			if (result) {//result
				return dbMember;
			}else if (encodedPwd.equals(loginPassword)) {
				return dbMember;
			}else {
				return Optional.empty();
			}
		}else {
			return Optional.empty();
		}
	}
	
	public String updateMember(Members memberBean) {
		memberRepo.save(memberBean);
		return "更新完成";
	}
	public Optional<Members> findMemberByAccount(String loginAccount) {
		Optional<Members> memberData = memberRepo.findByMemberAccount(loginAccount);
	return memberData;
	}
	
	
}
