package com.example.bistro.backstage.lotteryWinners;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.bistro.backstage.members.Members;


public interface LotteryWinnersRepository extends JpaRepository<LotteryWinners, Integer> {
	
	@Query(value = "SELECT " +
			   "lw.ID AS winnerId, " +
			   "m.memberName as memberName, " +
			   "c.campaignTitle as campaignTitle, " +
			   "cp.prizeName as prizeName, " +
			   "lw.createdAt AS winningTime, " +
			   "CASE " +
			       "WHEN c.endDate < GETDATE() THEN '已過期' " +
			       "ELSE '進行中' " +
			   "END AS campaignStatus " +
			   "FROM LotteryWinners lw " +
			   "INNER JOIN Members m ON lw.memberId = m.ID " +
			   "INNER JOIN Campaign c ON lw.campaignId = c.ID " +
			   "INNER JOIN CampaignPrizes cp ON lw.prizeId = cp.ID " +
			   "ORDER BY lw.createdAt DESC", 
			   nativeQuery = true)
			List<Object[]> findAllWinners();
			
			@Query("from LotteryWinners where member.id = :memberId")
			LotteryWinners findByMemberId(Integer memberId);
			
}
