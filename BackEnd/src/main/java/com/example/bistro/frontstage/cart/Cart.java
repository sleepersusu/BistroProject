package com.example.bistro.frontstage.cart;

import java.util.Date;

import com.example.bistro.frontstage.cartId.CartId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "Cart")
public class Cart {

//序列化欄位，不是Pk
        @EmbeddedId
        private CartId cartId;


//欄位
        @Column(name = "cartCount", nullable = false)
        private Integer cartCount;  //商品數量

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "createdAt", nullable = false)
        private Date createdAt;

        //後面就不用set時間
        @PrePersist
        public void onCreate() {
            if(createdAt == null) {
                createdAt = new Date();
            }
        }

//FK+PK雙主鍵
    // 多對一：多台購物車可能來自同一個會員，不允許null，因為pk，所以如果是非會員要幫他新增一筆資料
        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("membersId")
        @JoinColumn(name = "membersId", referencedColumnName = "ID", nullable = false)
        @JsonIgnore
        private Members members;
    //多對一：多個產品可以同時出現在一台購物車中
        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("menuId")
        @JoinColumn(name = "menuId", referencedColumnName = "ID", nullable = false)
        @JsonIgnore
        private Menu menu;  // 與 Menu 表的多對一關係，不允許為 NULL


        public Cart() {}

        public Cart(CartId cartId, Integer cartCount, Date createdAt, Members members, Menu menu) {
            this.cartId = cartId;
            this.cartCount = cartCount;
            this.createdAt = createdAt;
            this.members = members;
            this.menu = menu;
        }

}
