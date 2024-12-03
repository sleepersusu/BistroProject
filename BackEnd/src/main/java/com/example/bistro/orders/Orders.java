package com.example.bistro.orders;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.members.Members;
import com.example.bistro.ordersDetails.OrdersDetails;
import com.example.bistro.payment.Payment;
import com.example.bistro.seats.Seats;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "Orders")
public class Orders {



   //PK
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "ID")
        private Integer ID;


  //欄位
        //訂單資訊
            private String  ordersName;         //顧客姓名
            private String  ordersTel;          //顧客電話
            private String  seatType;         //內用和外帶
            private Integer ordersSumPrice;     //整筆訂單總價格
            private Integer pointGetted;        //獲得積分
            private String  ordersStatus;       //訂單狀態
            private String  ordersRequest;      //特殊要求
            private String latestPaymentStatus; //最新付款資訊

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @Temporal(TemporalType.TIMESTAMP)
            private Date createdAt;             //訂單建立時間

        //後面就不用set時間
            @PrePersist
            public void onCreate() {
                if(createdAt == null) {
                    createdAt = new Date();
                }
            }


   //FK
        // 多對一：多個訂單可以來自同一個會員
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "memberId")
            private Members members;

        // 多對一：多個訂單可以來自同一個桌號
            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "seatsId")
            private Seats seats;

        // 多對一：多個訂單可以來自同一個員工處理
        //    @ManyToOne
        //    @JoinColumn(name = "employeeId")
        //    private Employee employee;

        // 一對多：一筆訂單會有多筆訂單詳情
            @OneToMany(mappedBy = "orders",cascade = CascadeType.ALL, orphanRemoval = true)
            private List<OrdersDetails> ordersDetails = new ArrayList<>();  // 訂單明細

        // 一對多：一筆訂單可能有多筆付款紀錄(失敗重來之類)
            @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
            private List<Payment> payment = new ArrayList<>();  // 訂單與付款紀錄的一對多關聯



    public Orders() {}





}
