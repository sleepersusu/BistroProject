package com.example.bistro.frontstage.comment;



import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.PrePersist;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDTO {

	
	private Integer id;
	
	private Integer memberId;
	
	private Integer menuId;
	
	
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
	
}
