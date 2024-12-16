package com.example.bistro.frontstage.promoCode;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.pointPrizes.PointPrizesBean;

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
@Table(name = "Promo")
public class PromoCodeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Members members;
    
    @ManyToOne
    @JoinColumn(name = "pointPrizesId")
    private PointPrizesBean pointPrizes;
    
    private String promoCode;
    
}