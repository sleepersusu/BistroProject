package com.example.bistro.ECPayDemo.request;

import lombok.Data;

@Data
public class CheckoutRequest extends BaseRequest {
    String amount;
    String ordersName;
    String ordersTel;
}