package com.example.bistro.backstage.PointsRecords;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PointsRecordsRepository extends JpaRepository<PointsRecordsBean, Integer>{

	@Query(value = "SELECT PointsRecords.ID, " +
            "Members.ID, " +
            "Members.memberName, " +
            "PointPrizes.ID, " +
            "PointPrizes.pointPrizesName, " +
            "PointPrizes.pointPrizesPoints, " +
            "PointsRecords.recordsDate " +
            "FROM PointsRecords " +
            "JOIN Members ON PointsRecords.memberId = Members.ID " +
            "JOIN PointPrizes ON PointsRecords.pointPrizesId = PointPrizes.ID", 
    nativeQuery = true)
	List<Object[]> findMembersAllPointRecord();
	
	@Modifying
	@Transactional
	@Query(value="UPDATE PointsTotal "
			+ "SET PointsTotal = PointsTotal - ? "
			+ "WHERE memberId = ?",nativeQuery = true)
	void minusMemberPoint(int PointPrizesPoints, int memberId);
	

//	UPDATE PointsTotal
//	SET MemberPointTotal = MemberPointTotal - 20
//	FROM PointsTotal
//	JOIN PointPrizes ON PointPrizes.ID = PointsTotal.pointPrizesId
//	WHERE PointsTotal.memberId = 1;

}
