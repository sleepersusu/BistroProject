package com.example.bistro.frontstage.promoCodeRecord;

import com.example.bistro.backstage.members.Members;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "PromoRecord")
public class PromoCodeRecordBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Members members;
    
    private String pointPrizesName;
    
    private String promoCode;
}
