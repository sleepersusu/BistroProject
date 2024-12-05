package com.example.bistro.frontstage.cart;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuRepository;
import com.example.bistro.frontstage.cartId.CartId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        //打開交易
            public Cart addToCart(String memberName, String memberPhone, Integer menuId, Integer cartCount, Integer membersId) {
                //檢查會員是否存在
                    Members members = membersRepository.findByMemberPhone(memberPhone)
                            .orElseGet(() -> {
                                // 如果不存在，新增非會員紀錄，請他填寫姓名和電話(起碼)
                                Members newMembers = new Members();
                                newMembers.setMemberName(memberName);
                                newMembers.setMemberPhone(memberPhone);
                                newMembers.setMembership("非會員"); //給他非會員的註記
                                return membersRepository.save(newMembers);
                            });

                    // 要先判斷該配對有沒有已經存在
                        Cart dbCart = cartRepository.findByMembersIdAndMenuId(membersId, menuId);

                    // 如果存在
                        if(dbCart != null) {
                            dbCart.setCartCount(dbCart.getCartCount() +1 );
                            return dbCart;
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

        //購物車減一或是當購物車扣到為0的時候，要刪除這項產品
            @Transactional
            public void minusOneCount(Integer membersId, Integer menuId) {
                Cart dbCart = cartRepository.findByMembersIdAndMenuId(membersId, menuId);

                if(dbCart.getCartCount() == 1) {
                    cartRepository.delete(dbCart);
                }else {
                    dbCart.setCartCount(dbCart.getCartCount() - 1);
                }
            }



            public List<Cart> findMembersCart(Integer membersId) {
                return cartRepository.findCartByMembers(membersId);
            }





    }
