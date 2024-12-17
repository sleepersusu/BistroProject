package com.example.bistro.frontstage.orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    private Integer paymentPrice;
    private String paymentWay;
    private String paymentStatus;
}
