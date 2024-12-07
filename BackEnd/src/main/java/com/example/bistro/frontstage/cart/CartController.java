package com.example.bistro.frontstage.cart;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;



    //shoppingCartAll
    //Add to Cart
        @PostMapping("/add")
        public String addToCart(@RequestBody JsonNode jsonNode) {

            // 提取JSON中的数据
                String memberName = jsonNode.get("memberName").asText();
                String memberPhone = jsonNode.get("memberPhone").asText();
                Integer menuId = jsonNode.get("menuId").asInt();
                Integer cartCount = jsonNode.get("cartCount").asInt();

                Cart cart = cartService.addToCart(memberName, memberPhone, menuId, cartCount);

                List<Cart> shoppingCart = cartService.findMembersCart(Integer.valueOf(memberPhone));

                return "成功";
        }
    //MinusOne


}
