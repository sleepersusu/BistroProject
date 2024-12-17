package com.example.bistro.backstage.menu;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.bistro.backstage.comment.Comment;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Menu")
public class Menu {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer ID;
	private String productCategory;
	private String productName;

	@Lob
	private byte[] productImg;
	@Setter
    private Integer productPrice;
	@Setter
    @Getter
    private String productDescribe;
	@Setter
    @Getter
    private String productImgUrl;

	private Integer productCount;

	private Integer minproductCount;

	private Double avgScore;

	@Setter
    private String menuStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd") // 前端輸入輸出時的格式對應，若須強制轉換格式，el 須使用雙層大括號
	@Temporal(TemporalType.DATE)
	@Column(name = "createdAt")
	private Date createdAt;

	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<OrdersDetails> ordersDetails; // 用於映射 OrderDetails 實體中的 menu


	@JsonIgnore
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	private List<Comment> comments = new ArrayList<Comment>(); // 用於映射 Comment 實體中的 menu
	

	

	@PrePersist // 當物件要轉換成 Persistent 狀態以前，執行這個方法
	public void createdAt() {
		if (createdAt == null) {
			createdAt = new Date();
		}
	}



	public double getAvgScore() {
		if (avgScore == null) {
			return 0.0; // 默認值，避免返回 null
		}
		return avgScore;
	}


}
