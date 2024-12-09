package com.example.bistro.backstage.lotteryWinners;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.lotteryChance.LotteryChance;
import com.example.bistro.backstage.members.Members;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LotteryWinners")
public class LotteryWinners {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name = "campaignId")
	private Campaign campaign;
	
	@ManyToOne
	@JoinColumn(name = "memberId")
	private Members member;
	
	@ManyToOne
	@JoinColumn(name = "prizeId")
	private CampaignPrizes campaignPrizes;
	
	@OneToOne
	@JoinColumn(name = "lotteryChanceId")
	private LotteryChance lotteryChance;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt")
	private Date createdAt;
	
	@PrePersist 
	public void onCreate() {
		if(createdAt == null) {
			createdAt = new Date();
		}		
	}

	public LotteryWinners() {
	}

}
