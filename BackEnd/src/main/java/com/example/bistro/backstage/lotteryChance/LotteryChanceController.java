package com.example.bistro.backstage.lotteryChance;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaign.CampaignService;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersRepository;

@Controller
public class LotteryChanceController {
	
	@Autowired
	LotteryChanceService lotteryChanceService;
	
	@Autowired
	CampaignService campaignService;
	
	@Autowired
	MembersRepository memberRepo;
	
	@GetMapping("/Bistro/campaign/chance/findAll")
	public String findAll(Model model) {
	    List<Object[]> results = lotteryChanceService.findMembersAllChance();
	    model.addAttribute("lotteryChances", results);
	    
	    return "campaign/lotteryChanceView";
	}
	
	@PostMapping("/Bistro/campaign/chance/create")
	public String insertChance(@RequestParam Integer memberId,
								@RequestParam Integer campaignId,
								@RequestParam Integer lotteryChance ) {
		LotteryChance chance = new LotteryChance();
		Campaign campaign = campaignService.findCampaignById(campaignId);
		Optional<Members> member = memberRepo.findById(memberId);
		chance.setMember(member.get());
		chance.setCampaign(campaign);
		chance.setLotteryChances(lotteryChance);
		lotteryChanceService.insertLotteryChance(chance);
		
		return "redirect:/Bistro/campaign/chance/findAll";
	}
	
	@PostMapping("/Bistro/campaign/chance/update")
	public String updateChance(@RequestParam Integer lotteryChance,
									@RequestParam Integer id) {
		LotteryChance chance = lotteryChanceService.findById(id);
		chance.setLotteryChances(lotteryChance);
		chance.setRemainingChances(lotteryChance - chance.getUsedChances());
		
		lotteryChanceService.updateLotteryChance(chance);
		
		return "redirect:/Bistro/campaign/chance/findAll";
	}
	
	@PostMapping("/Bistro/campaign/chance/delete")
	public String deleteChance(@RequestParam Integer id) {
		lotteryChanceService.deleteChanceById(id);
		
		return "redirect:/Bistro/campaign/chance/findAll";
	}
	
	
	
	

}
