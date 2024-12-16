package com.example.bistro.frontstage.pointRecord;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRecordDTO {
    private Integer memberId;
    private Integer pointPrizesId;
    private Date recordsDate;
    private Date promoCode;
}
