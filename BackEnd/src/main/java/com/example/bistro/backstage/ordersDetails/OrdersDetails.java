package com.example.bistro.backstage.ordersDetails;


import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.orders.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "OrderDetails")
public class OrdersDetails {

    //PK
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "ID")
            private Integer ID;

    //欄位名稱
            private String  odName;
            private Integer odQuantity;
            private Integer odPrice;
            private Integer odSumPrice;

    //FK
        // 多對一：多筆訂單詳情來自同一筆訂單
            @ManyToOne
            @JoinColumn(name = "ordersId")
            @JsonIgnore
            private Orders orders;  // 訂單明細與訂單關聯
        // 多對一： 一個商品項目會出現在多筆訂單
            @ManyToOne
            @JoinColumn(name = "menuId")
            @JsonIgnore
            private Menu menu;  // 訂單明細與菜單商品關聯





}
