package com.example.bistro.frontstage.comment;



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
	

	
	
}
