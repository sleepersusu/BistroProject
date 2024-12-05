package com.example.bistro.frontstage.cart;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.frontstage.cartId.CartId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class CartService {

    //表示 CartService 類別的兩個必需依賴對象，它們被標記為：
        //private：只能在 CartService 類別內部使用，符合封裝原則。
        //final：這些依賴在類別實例化後不能被修改，保證了類別的穩定性和線程安全性。
            private final CartRepository cartRepository;
            private final MembersRepository membersRepository;

    //使用建構函式註入，省略 @Autowired（如果你喜歡明確標示註解，可以保留 @Autowired）。
        //好處：
        //避免了反射注入欄位的潛在問題。
        //容易進行依賴管理和單元測試。
        //明確表明依賴關係，程式碼更清晰。
            public CartService(CartRepository cartRepository, MembersRepository membersRepository) {
                this.cartRepository = cartRepository;
                this.membersRepository = membersRepository;
            }
    //打開交易
            public void addToCart(String memberName, String memberPhone, Integer menuId, Integer count) {
                //檢查會員是否存在
                Members members = membersRepository.findByNameAndPhone(memberName, memberPhone)
                        .orElseGet(() -> {
                            // 如果不存在，新增非會員紀錄
                            Members newMembers = new Members();
                            newMembers.setMemberName(memberName);
                            newMembers.setMemberPhone(memberPhone);
                            newMembers.setMembership("非會員"); //給他非會員的註記
                            return membersRepository.save(newMembers);
                        });
                //還在研究中............................................................................
                // 使用 memberId 處理邏輯
                    CartId cartId = new CartId(members.getId(), menuId);
                    Cart existingCart = cartRepository.findById(cartId).orElse(null);

                    if (existingCart != null) {
                        // 商品已存在，累加数量
                        existingCart.setCartCount(existingCart.getCartCount() + count);
                        cartRepository.save(existingCart);
                    } else {
                        // 商品不存在，插入新紀錄
                        Cart newCart = new Cart();
                        newCart.setCartCount(1);
                        newCart.setCartId(cartId);
                        newCart.setCreatedAt(new Date());
                        cartRepository.save(newCart);
                    }

            }






    }
