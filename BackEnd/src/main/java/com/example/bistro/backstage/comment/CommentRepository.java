package com.example.bistro.backstage.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	@Query("FROM Comment c WHERE c.members.ID= :memberId")
	List<Comment> findCommentByMembers(Integer memberId);

	
	@Query("FROM Comment c WHERE c.menu.productName= :productName")
	List<Comment> findCommentByProductName(String productName);


	@Query("SELECT COUNT(c) FROM Comment c WHERE c.menu.productName= :productName")
	Integer countCommentPeopleByProductName(String productName);

}
