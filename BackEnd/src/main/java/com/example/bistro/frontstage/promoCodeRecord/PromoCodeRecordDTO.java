package com.example.bistro.frontstage.promoCodeRecord;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromoCodeRecordDTO {
    private Integer memberId;
//    private String promoCode;
    private List<String> promoCodes;
    
    private List<String> pointPrizesName;
}
