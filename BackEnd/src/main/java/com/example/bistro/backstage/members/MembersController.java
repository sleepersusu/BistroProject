package com.example.bistro.backstage.members;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.bistro.backstage.menu.Menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;



@Controller
public class MembersController {

	@Autowired
	private MembersService membersService;

	@Autowired
	private MembersService membersRepo;

	// 以下自己寫測試用，以大哥寫為主
	@GetMapping("/Bistro/member/findAllMembers")
	public String findAllMembers(Model model) {

		List<Members> allMembers = membersService.findAllMembers();

		model.addAttribute("allMembers", allMembers);

		return "member/membersView";
	}


	// 以下自己寫測試用，以大哥寫為主
	@Transactional
	@PostMapping("/Bistro/member/updateMemers")
	public String updateMemers(@ModelAttribute Members member, @RequestParam("edituserimg") MultipartFile file) {
		Members memberbyId = membersService.findMembersById(member.getId());
		if (file.isEmpty()) {

			if (memberbyId != null) {

				byte[] memberImg = memberbyId.getMemberImg();

				if (memberImg != null) {
					member.setMemberImg(memberImg);
				}
				membersService.updateMember(member);
				return "redirect:/Bistro/member/findAllMembers";

			}
		} else if (memberbyId != null) {

			byte[] memberImg = memberbyId.getMemberImg();

			if (memberImg != null) {
				member.setMemberImg(memberImg);
				membersService.updateMember(member);
				return "redirect:/Bistro/member/findAllMembers";

			}

			// 有傳新頭像 有這個會員 原本沒頭像

			// 有傳新頭像 有這個會員 原本有頭像

		} else {

			return "先註冊會員";

		}

		return null;

	}
	
	

}
