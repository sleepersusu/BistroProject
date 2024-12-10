package com.example.bistro.frontstage.campaign;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaign.CampaignService;
import com.example.bistro.backstage.lotteryChance.LotteryChance;
import com.example.bistro.backstage.lotteryChance.LotteryChanceRepository;
import com.example.bistro.backstage.lotteryChance.LotteryChanceService;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LotteryChancesFrontService {

	@Autowired
	LotteryChanceRepository lotteryChanceRepo;

	@Autowired
	CampaignService campaignService;

	@Autowired
	MembersRepository membersRepo;

	@Autowired
	LotteryChanceService lotteryChanceService;

	public LotteryChance findChanceById(Integer id) {
		return lotteryChanceService.findById(id);
	}

	public Optional<LotteryChance> findMemberChanceByCampaign(Integer memberId, Integer campaignId) {
		return lotteryChanceRepo.findByMemberIdAndCampaignId(memberId, campaignId);
	}

	public LotteryChance calculateAndAddChances(Integer memberId, Integer campaignId, Integer orderAmount) {
		Optional<Members> member = membersRepo.findById(memberId);
		if (member.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到會員資料");
		}

		Campaign campaign = campaignService.findCampaignById(campaignId);

		if (!campaign.isActive()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "活動尚未開始或已結束");
		}

		if (orderAmount < campaign.getMinOrderAmount()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"訂單金額未達到最低抽獎門檻：" + campaign.getMinOrderAmount() + "元");
		}

		int newChances = orderAmount / campaign.getMinOrderAmount();

		Optional<LotteryChance> existingChance  = this.findMemberChanceByCampaign(memberId, campaignId);

		if (existingChance.isPresent()) {
			LotteryChance chance = existingChance.get();
			int totalChances = chance.getLotteryChances() + newChances;
			chance.setLotteryChances(totalChances);
			chance.setRemainingChances(chance.getRemainingChances() + newChances);
			return lotteryChanceRepo.save(chance);
		} else {
			LotteryChance chance = new LotteryChance();
			chance.setLotteryChances(newChances);
			chance.setCampaign(campaign);
			chance.setMember(member.get());
			chance.setUsedChances(0);
			chance.setRemainingChances(newChances);
			return lotteryChanceRepo.save(chance);
		}

	}

	public void useChance(LotteryChance chance) {
		if (chance.getRemainingChances() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "已無剩餘抽獎機會");
		}

		chance.setUsedChances(chance.getUsedChances() + 1);
		chance.setRemainingChances(chance.getLotteryChances() - chance.getUsedChances());
		lotteryChanceRepo.save(chance);
	}
}
