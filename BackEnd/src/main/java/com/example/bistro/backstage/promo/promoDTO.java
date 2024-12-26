package com.example.bistro.backstage.promo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class promoDTO {
    private Integer id;
    private String promoCode;
    private Integer memberId;
    private String memberName;
    private Integer pointPrizesId;
    private String pointPrizesName;
}