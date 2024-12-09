package com.example.bistro.frontstage.check.linepay.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CheckoutPaymentRequestForm {

    private BigDecimal amount;
    private String currency;
    private String orderId;
    private List<ProductPackageForm> packages;
    private RedirectUrls redirectUrls;
}
