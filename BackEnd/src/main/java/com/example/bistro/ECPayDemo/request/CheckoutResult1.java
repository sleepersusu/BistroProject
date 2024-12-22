package com.example.bistro.ECPayDemo.request;

import lombok.Data;

@Data
public class CheckoutResult {
    String amount;
    String ordersName;
    String ordersTel;
}