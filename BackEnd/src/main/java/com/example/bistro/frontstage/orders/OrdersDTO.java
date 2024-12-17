package com.example.bistro.frontstage.orders;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrdersDTO {
    private String ordersName;         // 顧客姓名
    private String ordersTel;          // 顧客電話
    private String seatType;           // 內用或外帶
    private Integer ordersSumPrice;    // 訂單總價格
    private String ordersRequest;      // 特殊要求
    private String latestPaymentStatus;// 最新付款狀態
    private Integer memberId;           // 會員 ID (如果是非會員，可以為 null)


    // 訂單詳情列表 (購物車資訊)
    private List<OrdersDetailsDTO> ordersDetails;

    // 付款資訊
    private List<PaymentDTO> payments;
}
