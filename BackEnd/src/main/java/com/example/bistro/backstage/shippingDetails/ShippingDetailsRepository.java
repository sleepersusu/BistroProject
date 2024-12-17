package com.example.bistro.backstage.shippingDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingDetailsRepository extends JpaRepository<ShippingDetails, Integer> {
	Optional<ShippingDetails> findByLotteryWinnerId(Integer lotteryWinnerId);
}
