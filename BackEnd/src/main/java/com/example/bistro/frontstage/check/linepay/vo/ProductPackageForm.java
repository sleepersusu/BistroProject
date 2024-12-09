package com.example.bistro.frontstage.check.linepay.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductPackageForm {
    private String id;
    private String name;
    private BigDecimal amount;

    private List<ProductForm> products;


}
