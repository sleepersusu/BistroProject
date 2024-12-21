package com.example.bistro.backstage.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuService;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepo;
	
	@Autowired
	private MenuService menuService;

	public Comment createComment(Comment comment) {
		return commentRepo.save(comment);
	}

	public List<Comment> findAllComment() {
		return commentRepo.findAll();
	}

	public List<Comment> findCommentByMemberId(Integer memberId) {
		return commentRepo.findCommentByMembers(memberId);

	}

	public Comment findCommentById(Integer id) {
		Optional<Comment> op = commentRepo.findById(id);

		if (op.isPresent()) {
			return op.get();
		}
		return null;
	}

	public boolean deleteComment(Integer id) {
		Optional<Comment> op = commentRepo.findById(id);
		if (op.isPresent()) {
			Comment comment = op.get();
			commentRepo.delete(comment);
			return true;
		}
		return false;
		
	}

	public Comment updateComment(Comment comment) {
		return commentRepo.save(comment);
	}

	@Transactional
	public List<Comment> findCommentByMembers(Integer memberId) {

		return commentRepo.findCommentByMembers(memberId);

	}
	@Transactional
	public List<Comment> findCommentByProductName(String productName) {
		
		List<Comment> commentByProductName = commentRepo.findCommentByProductName(productName);
		
		return commentByProductName;
		
		

	}

	@Transactional
    public Comment updateCommentAndMenuScore(Comment comment, Menu menu) {
        // 1. 更新評論
        Comment savedComment = commentRepo.save(comment);
        
        // 2. 更新菜單平均分數
        Double avgScore = menuService.countOneMenuAvgScore(menu.getProductName());
        menu.setAvgScore(avgScore);
        menuService.updateMenu(menu);
        
        return savedComment;
    }
}
