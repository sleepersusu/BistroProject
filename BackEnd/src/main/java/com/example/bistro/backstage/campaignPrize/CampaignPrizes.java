package com.example.bistro.backstage.campaignPrize;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.campaign.Campaign;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Table(name = "CampaignPrizes")
public class CampaignPrizes {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "prizeName")
	private String prizeName;
	
	@Lob
	@Column(name = "prizeImg")
	private byte[] prizeImg;
	
	@Column(name = "prizeQuantity")
	private Integer prizeQuantity;
	
	@Column(name = "prizeDescription")
	private String prizeDescription;
	
	@Column(name = "probability", columnDefinition = "DECIMAL(5,2)")
    private BigDecimal probability; 
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name = "campaignId")
	private Campaign campaign;
	
	@PrePersist 
	public void onCreate() {
		if(createdAt == null) {
			createdAt = new Date();
		}		
	}
	
	public CampaignPrizes() {
	}

	

}
