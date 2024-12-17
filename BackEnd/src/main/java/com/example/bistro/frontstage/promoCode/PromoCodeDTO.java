package com.example.bistro.frontstage.promoCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoCodeDTO {
    private Integer memberId;
    private Integer pointPrizesId;
    private String promoCode;
}