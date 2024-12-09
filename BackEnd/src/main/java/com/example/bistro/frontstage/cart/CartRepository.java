package com.example.bistro.frontstage.cart;

import com.example.bistro.frontstage.cartId.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, CartId> {


    @Query("from Cart c where c.members.id = :mm and c.menu.ID = :pp")
    Cart findByMemberIdAndMenuId(@Param("mm") Integer membersId, @Param("pp") Integer menuId);

    //根據memberId 查找某個會員的購物車所有紀錄
    @Query("from Cart c where c.members.id = :mid")
    List<Cart> findCartByMemberId(@Param("mid") Integer membersId);

    //根據memberPhone 查找某個會員的購物車所有紀錄
    @Query("from Cart c where c.members.memberPhone = :mp")
    List<Cart> findCartByMemberPhone(@Param("mp") String memberPhone);


    @Query("SELECT c FROM Cart c WHERE c.cartId.membersId = :membersId")
    List<Cart> findCartsByMemberId(@Param("membersId") Integer membersId);

//    @Query("SELECT c FROM Cart c WHERE c.members.id = :memberId")
//    List<Cart> findByMemberId(@Param("memberId") Integer memberId);

}
