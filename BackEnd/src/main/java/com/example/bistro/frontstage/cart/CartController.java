package com.example.bistro.frontstage.cart;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;


    //列出會員所擁有的購物車
        @GetMapping("/list")
        public ResponseEntity<List<Cart>>shoppingCart(HttpSession httpSession) {

            Integer loginUserId = (Integer) httpSession.getAttribute("membersId");

            // 如果是空的，請他先登入
            if (loginUserId == null) {
                return ResponseEntity.status(401).body(null);
            }
            List<Cart> shoppingCart = cartService.findMemberCart(loginUserId);
            return ResponseEntity.ok(shoppingCart);

        }
    //會員新增購物車
        //1.確定有沒有會員，沒有要請他登入(可以在controller做)
        //2.判斷memberId和menuId是否有存在了，代表這個會員已經有這個產品了
            //如果存在    就將購物車加一
            //如果不存在   就set cardId(複合主鍵給他)，建立一筆新的資料
        @PostMapping("/add/{menuId}")
        public ResponseEntity<String> addCart(@PathVariable Integer menuId,HttpSession httpSession) {
            Integer loginUserId = (Integer) httpSession.getAttribute("membersId");
            // 如果是空的，請他先登入
                if (loginUserId == null) {
                    return ResponseEntity.status(401).body("Please log in first.");
                }
            cartService.addCartNew(loginUserId,menuId);
            List<Cart> shoppingCart = cartService.findMemberCart(loginUserId);
            return ResponseEntity.ok("Added cart successfully.");
        }

    //會員減少購物車
        //1.確定有沒有會員，沒有要請他登入
        //2.判斷memberId和menuId是否有存在了，代表這個會員已經有這個產品了
            //如果存在    就將購物車加一
            //如果不存在   就set cardId(複合主鍵給他)，建立一筆新的資料
        @PostMapping("/minusCart/{menuId}")
        public ResponseEntity<String> minusCart(@PathVariable Integer menuId,HttpSession httpSession) {
            Integer loginUserId = (Integer) httpSession.getAttribute("membersId");
            // 如果是空的，請他先登入
                if (loginUserId == null) {
                    return ResponseEntity.status(401).body("Please log in first.");
                }
            cartService.minusOneCount(loginUserId,menuId);
            List<Cart> shoppingCart = cartService.findMemberCart(loginUserId);
            return ResponseEntity.ok("minus cart successfully.");
        }



























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
