package com.example.bistro.frontstage.comment;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.comment.Comment;
import com.example.bistro.backstage.comment.CommentRepository;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CommentFrontService {

	@Autowired
	private CommentRepository commentRepo;

	@Autowired
	private MembersRepository membersRepo;

	@Autowired
	private MenuRepository menuRepo;

	@Autowired
	private MenuRepository menuService;

	public String countCommentPeopleByProductName(String productName) {
		Integer commentPeopleNumber = commentRepo.countCommentPeopleByProductName(productName);

		if (commentPeopleNumber > 9999) {
			return "9999+";
		}
		return commentPeopleNumber.toString();

	}

	public Comment createComment(CommentDTO dto) {
	    Comment comment = new Comment();
	    
	    if(dto.getCommentMessage().length()<200) {
	    	comment.setCommentMessage(dto.getCommentMessage());	    	
	    }
	    
	    comment.setCommentRating(dto.getCommentRating());
	    comment.setMenu(menuRepo.findById(dto.getMenuId()).orElse(null));
	    comment.setMembers(membersRepo.findById(dto.getMemberId()).orElse(null));
	    return commentRepo.save(comment);
	}

}
