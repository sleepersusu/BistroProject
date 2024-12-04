package com.example.bistro.backstage.cart;

import java.util.Date;
import java.util.Objects;

import com.example.bistro.backstage.cartId.CartId;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.menu.Menu;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Cart", uniqueConstraints = @UniqueConstraint(columnNames = {"membersId", "menuId"}))
public class Cart {

//序列化欄位，不是Pk
        @EmbeddedId
        private CartId cartId;


//欄位
        @Column(name = "cartCount", nullable = false)
        private Integer cartCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "createdAt", nullable = false)
        private Date createdAt;


//FK+PK雙主鍵
    // 多對一：一個會員只會有一台購物車
        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("membersId")
        private Members members;  // 與 Member 表的多對一關係，允許為 NULL
    //多對一：多個產品可以同時出現在一台購物車中
        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("menuId")
        private Menu menu;  // 與 Menu 表的多對一關係，不允許為 NULL


        public Cart() {}

}
