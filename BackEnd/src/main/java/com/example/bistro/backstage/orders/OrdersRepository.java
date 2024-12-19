package com.example.bistro.backstage.orders;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    Optional<Orders> findByOrdersTel(String ordersTel);  // 根據電話查詢訂單

}
