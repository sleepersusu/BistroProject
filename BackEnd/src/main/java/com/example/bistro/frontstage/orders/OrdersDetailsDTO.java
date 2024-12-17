package com.example.bistro.frontstage.orders;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersDetailsDTO {


    private String  odName;
    private Integer odQuantity;
    private Integer odPrice;
    private Integer odSumPrice;
    private Integer menuId;

}
