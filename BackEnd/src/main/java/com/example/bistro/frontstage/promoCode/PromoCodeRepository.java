package com.example.bistro.frontstage.promoCode;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromoCodeRepository extends JpaRepository<PromoCodeBean, Integer> {
	
	@Query(value="select * from Promo where memberId = :memberId", nativeQuery = true)
	List<PromoCodeBean> findMemberPromoCode(@Param("memberId") int memberId);
	
}