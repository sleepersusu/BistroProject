package com.example.bistro.frontstage.cart;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;



    //shoppingCartAll
    // 查看購物車
    // 根據手機號碼查找會員的所有購物車
        @GetMapping("/allCart")
        public ResponseEntity<Map<String, Object>> shoppingCart(@RequestParam String memberPhone,HttpSession httpSession) {

            Integer loginUserId = (Integer) httpSession.getAttribute("membersId");


        // 查詢購物車所有資料，根據findCartByPhone的方法
            List<Cart> shoppingCart = cartService.findCartByPhone(memberPhone,loginUserId);
        // 返回结果
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("shoppingCart", shoppingCart);
        return ResponseEntity.ok(response);
    }

    //Add to Cart
        @PostMapping("/addCart")
        public ResponseEntity<Map<String, Object>> addToCart(@RequestBody JsonNode jsonNode,HttpSession httpSession) {

            Integer loginUserId = (Integer) httpSession.getAttribute("membersId");

            // 提取JSON中的數據
                String memberName = jsonNode.get("memberName").asText();
                String memberPhone = jsonNode.get("memberPhone").asText();
                Integer menuId = jsonNode.get("menuId").asInt();
                Integer cartCount = jsonNode.get("cartCount").asInt();

                Cart cart = cartService.addToCart(memberName, memberPhone, menuId, cartCount);

                List<Cart> shoppingCart = cartService.findCartByPhone(memberPhone,loginUserId);

                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("shoppingCart", shoppingCart);

                return ResponseEntity.ok(response);
        }


    //MinusOne


}
