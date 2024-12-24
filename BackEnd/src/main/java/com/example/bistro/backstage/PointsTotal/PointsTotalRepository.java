package com.example.bistro.backstage.PointsTotal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.bistro.backstage.members.Members;


public interface PointsTotalRepository extends JpaRepository<PointsTotalBean, Integer> {

	@Query(value = "SELECT PointsTotal.ID, PointsTotal.PointsTotal, PointsTotal.memberId, Members.memberName " +
            "FROM PointsTotal " +
            "JOIN Members ON PointsTotal.memberId = Members.id",
            nativeQuery = true)
	List<Object[]> findPointsTotal();
	

	PointsTotalBean findByMembers(Members members);
  
    // 增加根據會員ID查詢點數的方法
  Optional<PointsTotalBean> findByMembersId(Integer memberId);

}