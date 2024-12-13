package com.example.bistro.backstage.members;

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

}
