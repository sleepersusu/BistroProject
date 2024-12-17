package com.example.bistro.backstage.pointsGet;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.orders.Orders;

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
@Table(name = "PointGet")
public class PointsGetBean {
	
    //PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    
    @ManyToOne
    @JoinColumn(name = "memberId")
    private Members members;
    
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders orders;
    
    private Integer pointGetted;

}