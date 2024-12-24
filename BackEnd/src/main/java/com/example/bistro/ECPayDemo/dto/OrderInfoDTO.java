package com.example.bistro.ECPayDemo.dto;

import java.util.List;
import lombok.Data;

@Data
public class OrderInfoDTO {
    private String totalAmount;
    private String ordersName;
    private String ordersTel;
    private List<OrderItemDTO> orderItems;
    private String seatType;
    private String ordersRequest;
}
