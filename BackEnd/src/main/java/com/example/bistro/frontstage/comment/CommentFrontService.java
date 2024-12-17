package com.example.bistro.frontstage.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.comment.CommentRepository;

@Service
public class CommentFrontService {

	@Autowired
	private CommentRepository  commentRepo;
	
	
	public String  countCommentPeopleByProductName(String productName){
		 Integer commentPeopleNumber = commentRepo.countCommentPeopleByProductName(productName);
	
		if(commentPeopleNumber>9999) {
			return "9999+";			
		}		
		return commentPeopleNumber.toString();

	}
}
