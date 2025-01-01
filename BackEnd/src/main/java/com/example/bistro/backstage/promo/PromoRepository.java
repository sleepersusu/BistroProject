package com.example.bistro.backstage.promo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bistro.frontstage.promoCode.PromoCodeBean;

public interface PromoRepository extends JpaRepository<PromoCodeBean, Integer> {

}