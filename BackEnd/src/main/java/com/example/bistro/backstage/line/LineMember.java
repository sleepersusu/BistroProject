package com.example.bistro.backstage.line;

import java.util.Date;

import com.example.bistro.backstage.members.Members;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MemberLine")
@Getter
@Setter
@NoArgsConstructor
public class LineMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Members member;
    
    @Column(name = "lineUserId", nullable = false)
    private String lineUserId;
    
    @Column(name = "createdAt")
    private Date createdAt;
    
    @PrePersist 
	public void onCreate() {
		if(createdAt == null) {
			createdAt = new Date();
		}		
	}
}
