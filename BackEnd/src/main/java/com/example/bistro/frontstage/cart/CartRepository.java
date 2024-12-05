package com.example.bistro.frontstage.cart;

import com.example.bistro.frontstage.cartId.CartId;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface CartRepository extends JpaRepository<Cart, CartId> {
    }
