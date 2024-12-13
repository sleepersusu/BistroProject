package com.example.bistro.backstage.campaign;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CampaignRepository extends JpaRepository<Campaign, Integer> {
	
	@Query("select c from Campaign c where c.startDate <= :now and c.endDate >= :now")
	List<Campaign> findActiveCampaign(@Param("now") Date now);

}
