package com.example.bistro.backstage.lotteryWinners;

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
import com.example.bistro.backstage.campaignPrize.CampaignPrizeService;
import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.lotteryChance.LotteryChance;
import com.example.bistro.backstage.lotteryChance.LotteryChanceService;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;

@Controller
public class LotteryWinnersController {
	
	@Autowired
	private LotteryWinnersService lotteryWinnersService;
	
	@Autowired
	private MembersRepository memberRepo;
	
	@Autowired
	private CampaignService campaignService;
	
	@Autowired
	private CampaignPrizeService campaignPrizeService;
	
	
	@GetMapping("/Bistro/campaign/winner/findAll")
	public String findAllWinner(Model model) {		
		List<Object[]> results = lotteryWinnersService.findAllWinner();
		List<CampaignPrizes> allCampaignPrizes = campaignPrizeService.findAllCampaignPrizes();
		
		model.addAttribute("allWinners", results);
		model.addAttribute("allCampaignPrizes", allCampaignPrizes);
		return "campaign/lotteryWinnersView";
	}
	
	@PostMapping("/Bistro/campaign/winner/create")
	public String insertWinner(@RequestParam Integer campaignId,
								@RequestParam Integer prizeId,
								@RequestParam Integer memberId) {
		Campaign campaign = campaignService.findCampaignById(campaignId);
		CampaignPrizes prize = campaignPrizeService.findPrizeById(prizeId);
		Optional<Members> member = memberRepo.findById(memberId);
		
		LotteryWinners lotteryWinner = new LotteryWinners();
		lotteryWinner.setCampaign(campaign);
		lotteryWinner.setCampaignPrizes(prize);
		lotteryWinner.setMember(member.get());
		
		lotteryWinnersService.insertLotteryChance(lotteryWinner);
		
		
		return "redirect:/Bistro/campaign/winner/findAll";
	}
	
	@PostMapping("/Bistro/campaign/winner/delete")
	public String deleteWinner(@RequestParam Integer id) {
		lotteryWinnersService.deleteChanceById(id);
		
		return "redirect:/Bistro/campaign/winner/findAll";
	}
	
	
	

}
