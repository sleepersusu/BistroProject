package com.example.bistro.backstage.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartRepositoryDao extends JpaRepository<Cart, Integer> {


    @Query("SELECT c FROM Cart c WHERE c.members.id = :memberId")
    List<Cart> findByMemberId(@Param("memberId") Integer memberId);


    @Modifying
    @Transactional
    @Query("DELETE FROM Cart c WHERE c.members.id = :membersId")
    void deleteByMemberId(@Param("membersId") Integer membersId);

}
