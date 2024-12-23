package com.example.bistro.backstage.campaign;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity 
@Table(name = "Campaign")
public class Campaign {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	
	@Column(name = "campaignTitle")
	private String campaignTitle;
	
	@JsonIgnore
	@Lob
	@Column(name = "campaignImg")
	private byte[] campaignImg;
	
	@Column(name = "campaignDescription", length = 1000)
	private String campaignDescription;
	
	@Column(name = "campaignType")
	private String campaignType;
	
	@Column(name = "minOrderAmount")
	private Integer minOrderAmount;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startDate")
	private Date startDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endDate")
	private Date endDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdAt")
	private Date createdAt;
	
	@Column(name = "note")
	private String note;
	
	@PrePersist 
	public void onCreate() {
		if(createdAt == null) {
			createdAt = new Date();
		}		
	}
	
	public enum CampaignStatus {
        NOT_STARTED("未開始"),
        IN_PROGRESS("進行中"),
        EXPIRED("已結束");

        private String description;

        CampaignStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    
    @Transient  
    public CampaignStatus getCampaignStatus() {
        Date now = new Date();
        
        if (now.before(startDate)) {
            return CampaignStatus.NOT_STARTED;
        } else if (now.after(endDate)) {
            return CampaignStatus.EXPIRED;
        } else {
            return CampaignStatus.IN_PROGRESS;
        }
    }

    @Transient
    public boolean isActive() {
        return getCampaignStatus() == CampaignStatus.IN_PROGRESS;
    }

	
	
	

}
