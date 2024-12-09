package com.example.bistro.backstage.lotteryChance;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.orders.Orders;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LotteryChance")
public class LotteryChance {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "lotteryChances")
	private Integer lotteryChances;		
	
	@Column(name = "usedChances")
    private Integer usedChances;
	
	@Column(name = "remainingChances")
    private Integer remainingChances;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt")	
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "memberId")
	private Members member;
	
	@ManyToOne
	@JoinColumn(name = "campaignId")
	private Campaign campaign;
	
	@PrePersist 
	public void onCreate() {
		if(createdAt == null) {
			createdAt = new Date();
		}
		
		if(remainingChances == null) {
	        remainingChances = lotteryChances;
	    }
		
	    if(usedChances == null) {
	        usedChances = 0;
	    }
	}
	

	public LotteryChance() {
	}
	

}
