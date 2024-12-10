package com.example.bistro.backstage.campaignPrize;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignPrizesRepository extends JpaRepository<CampaignPrizes, Integer> {
	
	@Query("from CampaignPrizes cp where cp.campaign.id = :id")
	List<CampaignPrizes> findPrizesFromCampaignId(@Param("id") Integer id);

}
