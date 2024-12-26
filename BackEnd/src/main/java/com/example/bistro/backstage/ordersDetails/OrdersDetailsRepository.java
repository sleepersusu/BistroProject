package com.example.bistro.backstage.ordersDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails,Integer> {

    // Add this method for pagination
    Page<OrdersDetails> findByOdNameContainingOrOrders_OrdersNumberContaining(
            String odName, String ordersNumber, Pageable pageable);
    }
