package com.example.bistro.backstage.lotteryChance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LotteryChanceRepository extends JpaRepository<LotteryChance, Integer> {

	@Query(value = "SELECT " +
			"lc.ID, "+
		    "m.ID AS MemberId, " +
		    "m.memberName, " +
		    "c.campaignTitle, " +
		    "lc.lotteryChances, " +
		    "lc.usedChances, " +
		    "lc.remainingChances, " +
		    "CASE " +
		        "WHEN c.endDate < GETDATE() THEN '已過期' " +
		        "ELSE '可使用' " +
		    "END AS status " +
		    "FROM Members m " +
		    "INNER JOIN LotteryChance lc ON m.ID = lc.memberId " +
		    "INNER JOIN Campaign c ON lc.campaignId = c.ID", 
		    nativeQuery = true)
		List<Object[]> findMembersAllChance();
		
		Optional<LotteryChance> findByMemberIdAndCampaignId(Integer memberId,Integer campaignId);


}
