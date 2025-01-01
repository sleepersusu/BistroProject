package com.example.bistro.frontstage.promoCodeRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PromoCodeRecordRepository extends JpaRepository<PromoCodeRecordBean, Integer>{
		
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Promo WHERE promoCode = :promoCode", nativeQuery = true)
    int deleteMemberPromoCode(@Param("promoCode") String promoCode);
}
