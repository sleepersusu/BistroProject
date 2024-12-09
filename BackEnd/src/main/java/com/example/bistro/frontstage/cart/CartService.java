package com.example.bistro.frontstage.cart;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuRepository;
import com.example.bistro.frontstage.cartId.CartId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    //表示 CartService 類別的兩個必需依賴對象，它們被標記為：
        //private：只能在 CartService 類別內部使用，符合封裝原則。
        //final：這些依賴在類別實例化後不能被修改，保證了類別的穩定性和線程安全性。
            private final CartRepository cartRepository;
            private final MembersRepository membersRepository;
            private final MenuRepository menuRepository;

    //使用建構函式註入，省略 @Autowired（如果你喜歡明確標示註解，可以保留 @Autowired）。
    //好處：
        //避免了反射注入欄位的潛在問題。
        //容易進行依賴管理和單元測試。
        //明確表明依賴關係，程式碼更清晰。
            public CartService(CartRepository cartRepository, MembersRepository membersRepository, MenuRepository menuRepository) {
                this.cartRepository = cartRepository;
                this.membersRepository = membersRepository;
                this.menuRepository = menuRepository;
            }

            private String generateGuestAccount() {
                // 生成 UUID 並取前 10 個字
                String uuid = UUID.randomUUID().toString().replace("-", ""); // 去掉 "-" 符號
                return uuid.substring(0, 10) + "@guest.com";
            }



        //打開交易
            public Cart addToCart(String memberName, String memberPhone, Integer menuId, Integer cartCount) {
                //檢查會員是否存在，根據電話去找
                    Members members = membersRepository.findByMemberPhone(memberPhone)
                            .orElseGet(() -> {
                            // 如果不存在，新增非會員紀錄，請他填寫姓名和電話(起碼)
                                Members newMembers = new Members();
                                newMembers.setMemberName(memberName);
                                newMembers.setMemberPhone(memberPhone);
                                newMembers.setMembership("非會員");                    //給他非會員的註記
                                String generatedAccount = generateGuestAccount();   //自動生成 memberAccount
                                newMembers.setMemberAccount(generatedAccount);
                                newMembers.setMemberStatus("啟用");
                                newMembers.setCreatedAt(new java.util.Date());
                                return membersRepository.save(newMembers);
                            });

                    Integer membersId = members.getId();


                    // 要先判斷該配對有沒有已經存在
                        Cart dbCart = cartRepository.findByMemberIdAndMenuId(membersId, menuId);

                    // 如果存在
                        if(dbCart != null) {
                            dbCart.setCartCount(dbCart.getCartCount() +1 );
                            return cartRepository.save(dbCart);  // 保存更新后的购物车
                        }

                    // 如果不存在
                        CartId cartId = new CartId(membersId,menuId);

                        Optional<Members> optionalMembers = membersRepository.findById(membersId);
                        Optional<Menu> optionalMenu = menuRepository.findById(menuId);

                        Cart cart = new Cart();
                        cart.setCartId(cartId);
                        cart.setCartCount(1);
                        cart.setMembers(optionalMembers.get());
                        cart.setMenu(optionalMenu.get());

                    return cartRepository.save(cart);

            }


            // 根據手機號碼查找會員的所有購物車
                public List<Cart> findCartByPhone(String memberPhone,Integer loginUserId) {
                // 根據手機號碼查找會員，如果存在
                    Members member = membersRepository.findByMemberPhone(memberPhone).orElse(null);
                        if (member != null) {
                            // 如果會員存在，根據會員id去找購物車
                                return cartRepository.findCartByMemberId(member.getId());
                        }
                        else {
                            // 如果會員不存在，就根據電話號碼去找購物車
                                throw new IllegalArgumentException("此電話沒有紀錄，請重新輸入電話號碼");
                        }
                }

//-----------------------------------------------------------------------------------------------------------------------





                //會員新增購物車
                    //1.確定有沒有會員，沒有要請他登入(可以在controller做)
                    //2.判斷memberId和menuId是否有存在了，代表這個會員已經有這個產品了
                        //如果存在    就將購物車加一
                        //如果不存在   就set cardId(複合主鍵給他)，建立一筆新的資料
                @Transactional
                public Cart addCartNew(Integer membersId, Integer menuId) {
                    Cart dbCart = cartRepository.findByMemberIdAndMenuId(membersId, menuId);
                    // 如果存在
                        if(dbCart != null) {
                            dbCart.setCartCount(dbCart.getCartCount() +1 );
                            return cartRepository.save(dbCart);  // 保存更新後的購物車
                        }
                    // 如果不存在
                        CartId cartId = new CartId(membersId,menuId);
                        Optional<Members> optionalMembers = membersRepository.findById(membersId);
                        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
                        Cart cart = new Cart();
                        cart.setCartId(cartId);
                        cart.setCartCount(1);
                        cart.setMembers(optionalMembers.get());
                        cart.setMenu(optionalMenu.get());
                    //返回新的賦值
                        return cartRepository.save(cart);
                }



                //查找所有會員的購物車

                public List<Cart> findMemberCart(Integer membersId) {
                    return cartRepository.findCartByMemberId(membersId);
                }

                //會員減少購物車
                    //1.確定有沒有會員，沒有要請他登入
                    //2.判斷memberId和menuId是否有存在了，代表這個會員已經有這個產品了
                        //如果存在    就將購物車加一
                        //如果不存在   就set cardId(複合主鍵給他)，建立一筆新的資料

                @Transactional
                public void minusOneCount(Integer membersId, Integer menuId) {
                    Cart dbCart = cartRepository.findByMemberIdAndMenuId(membersId, menuId);
                    //如果已經是1扣到剩0就刪除
                    if(dbCart.getCartCount() == 1) {
                        cartRepository.delete(dbCart);
                        //如果不是1，就直接扣1就好，先植入count數字然後，取得目前數字，然後進行-1
                    }else {
                        dbCart.setCartCount(dbCart.getCartCount() - 1);
                    }
                }



    }
