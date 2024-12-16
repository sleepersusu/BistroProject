package com.example.bistro.backstage.pointPrizes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PointPrizesRepository extends JpaRepository<PointPrizesBean, Integer> {

	@Modifying
	@Transactional
	@Query(value = "UPDATE PointPrizes\r\n"
			+ "SET pointPrizesCount = pointPrizesCount - 1\r\n"
			+ "WHERE ID = ?;"
			,nativeQuery = true)
	public void deletePointPrizeCount(int memberId);
	
}
