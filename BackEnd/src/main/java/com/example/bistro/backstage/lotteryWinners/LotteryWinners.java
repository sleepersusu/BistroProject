package com.example.bistro.backstage.lotteryWinners;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.campaign.Campaign;
import com.example.bistro.backstage.campaignPrize.CampaignPrizes;
import com.example.bistro.backstage.lotteryChance.LotteryChance;
import com.example.bistro.backstage.members.Members;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "LotteryWinners")
public class LotteryWinners {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campaignId")
	private Campaign campaign;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "memberId")
	private Members member;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)

	@JoinColumn(name = "prizeId")
	private CampaignPrizes campaignPrizes;
	
	
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
	
	@JsonProperty("memberName")
	public String fetchMemberName() {
	    return member.getMemberName();
	}
	
	@JsonProperty("memberId")
	public Integer fetchMemberId() {
	    return member.getId();
	}
	
	@JsonProperty("prizeName")
	public String fetchPrizeName() {
	    return campaignPrizes.getPrizeName();
	}
	

}
