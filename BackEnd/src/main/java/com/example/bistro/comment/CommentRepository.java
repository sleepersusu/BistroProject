package com.example.bistro.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	@Query("FROM Comment c WHERE c.members.ID= :memberId")
	List<Comment> findCommentByMembers(Integer memberId);

	
	@Query("FROM Comment c WHERE c.menu.ID= :menuId")
	List<Comment> findCommentByMenuId(Integer menuId);
}
