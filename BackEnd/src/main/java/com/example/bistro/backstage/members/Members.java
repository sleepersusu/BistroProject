package com.example.bistro.backstage.members;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.cart.Cart;
import com.example.bistro.backstage.comment.Comment;
import com.example.bistro.backstage.orders.Orders;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity @Table(name = "Members")
public class Members {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(unique=true,nullable = false)
	private String memberAccount;
	@Column(nullable = false)
	private String memberPassword;
	private String memberName;
	private Short memberAge;
	private Short memberSex;
	private String membership;
	
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date memberBirthday;
    
	private Short memberFavor;
	private String memberAddress;
	private String memberPhone;
	private String memberEmail;
	@Lob
	private byte[] memberImg;
	private Integer memberPoint;
	private String memberStatus;
	
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
	
    // 一對一：每個會員只有一個購物車
    @OneToOne(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;  // 每個會員只有一個購物車

	// 一對多：一個會員可以有很多訂單
    @JsonIgnore
    @OneToMany(mappedBy = "members", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>(); // 會員可以有多個訂單
    
    @JsonIgnore
	@OneToMany(mappedBy = "members",fetch = FetchType.LAZY)
	private List<Comment> comments =new ArrayList<Comment>();
     // 用於映射 Comment 實體中的 members 

	
}
