package com.example.bistro.backstage.shippingDetails;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.example.bistro.backstage.lotteryWinners.LotteryWinners;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ShippingDetails")
@Getter
@Setter
@NoArgsConstructor
public class ShippingDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "lotteryWinnerId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private LotteryWinners lotteryWinner;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String address;
    
    @Column(length = 500)
    private String notes;
    
    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isSend;
}
