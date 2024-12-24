package com.example.bistro.ECPayDemo.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String name;
    private int quantity;
    private double price;
}