package com.example.bistro.backstage.comment;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

@Entity
@Table(name = "Comment")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="memberId",referencedColumnName = "ID")
	private Members members;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="menuId",referencedColumnName = "ID")
	private Menu menu;
	
	
	
	
	
	
	private String  commentProduct;
	

	private Short commentRating;
	
	
	private String  commentMessage;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")//for restful api 的時間格式
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 前端輸入輸出時的格式對應，若須強制轉換格式，el 須使用雙層大括號
	@Temporal(TemporalType.TIMESTAMP)
	private Date commentTime;
	
	
	@PrePersist //當物件轉換成persist 狀態以前,執行這個方法
	public void onCreate() {
		
		if(commentTime==null) {
			commentTime=new Date();
		}
		
	}
	

	
	
	@JsonProperty("memberName")
	public String fetchMemberName() {
		return members.getMemberName();
	}
	
	@JsonProperty("memberSex")
	public Short fetchMemberGender() {
		return members.getMemberSex();
	}
	
	@JsonProperty("memberid")
	public Integer fetchMemberId() {
		return members.getId();
	}
	
	@JsonProperty("menuid")
	public Integer fetchmenuId() {
		return menu.getID();
	}
	
	
	
	public Integer getID() {
		return ID;
	}


	public void setID(Integer iD) {
		ID = iD;
	}


	

	public Members getMembers() {
		return members;
	}


	public void setMembers(Members members) {
		this.members = members;
	}


	public Menu getMenu() {
		return menu;
	}


	public void setMenu(Menu menu) {
		this.menu = menu;
	}


	public String getCommentProduct() {
		return commentProduct;
	}


	public void setCommentProduct(String commentProduct) {
		this.commentProduct = commentProduct;
	}


	public Short getCommentRating() {
		return commentRating;
	}


	public void setCommentRating(Short commentRating) {
		this.commentRating = commentRating;
	}


	public String getCommentMessage() {
		return commentMessage;
	}


	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}


	public Date getCommentTime() {
		return commentTime;
	}


	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}


	
	
	
}
