package com.example.bistro.frontstage.check.linepay.vo;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductForm {
    private String id;
    private String name;
    private String imageUrl;
    private BigDecimal quantity;
    private BigDecimal price;

}
