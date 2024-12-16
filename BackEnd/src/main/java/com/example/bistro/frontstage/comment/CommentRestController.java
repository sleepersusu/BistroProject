package com.example.bistro.frontstage.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.comment.Comment;
import com.example.bistro.backstage.comment.CommentService;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuService;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;
import com.example.bistro.backstage.ordersDetails.OrdersDetailsRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@RestController
public class CommentRestController {
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentFrontService commentFrontService;
	

	@Autowired
	private MenuService menuService;

	@Autowired
	private MembersRepository membersRepo;
	
	@Autowired
	private OrdersDetailsRepository orderDetailsRepo;

	@PostMapping("/api/Bistro/postComment/{orderDetailsId}") // 新增評論
	public ResponseEntity<Map<String, Object>> postComment(HttpSession httpSession,
			@RequestBody Map<String, Object> requestData
			,@PathVariable Integer orderDetailsId) {

		// 從 Session 獲取會員 ID
		Integer membersId = (Integer) httpSession.getAttribute("membersId");
		if (membersId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "用戶未登入"));
		}

		try {
			Optional<OrdersDetails> op = orderDetailsRepo.findById(orderDetailsId);
			if(op.isPresent()) {
				OrdersDetails ordersDetails = op.get();			
				
			}

			Integer menuId = Integer.parseInt(requestData.get("menuId").toString());
			Short commentRating = Short.parseShort(requestData.get("commentRating").toString());
			String commentMessage = requestData.get("commentMessage").toString();

			// 查詢會員與菜單
			Members newMember = membersRepo.findById(membersId).get();
			if (newMember == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "會員不存在"));
			}

			Menu menu = menuService.findMenuById(menuId);
			if (menu == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "菜單不存在"));
			}

			if (commentRating < 1 || commentRating > 5) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Map.of("success", false, "message", "評分必須在 1 到 5 之間"));
			}

			String commentProduct = menu.getProductName();

			Comment newComment = new Comment();
			newComment.setMembers(newMember);
			newComment.setMenu(menu);
			newComment.setCommentProduct(commentProduct);
			newComment.setCommentRating(commentRating);
			newComment.setCommentMessage(commentMessage);

			commentService.createComment(newComment);

			Double avgScore = menuService.countOneMenuAvgScore(commentProduct);
			menu.setAvgScore(avgScore);
			menuService.updateMenu(menu);

			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("comment", newComment);
			return ResponseEntity.ok(response);

		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "請求參數格式錯誤"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "message", "系統錯誤", "error", e.getMessage()));
		}
	}

	@GetMapping("/api/member/comment") // 根據會員取得評論
	public ResponseEntity<?> findAllCommentByMember(HttpSession httpSession) {
		// 從 HttpSession 獲取會員 ID
		Integer memberId = (Integer) httpSession.getAttribute("membersId");

		if (memberId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入以查看您的評論。");
		}

		// 根據會員 ID 獲取評論
		List<Comment> comments = commentService.findCommentByMemberId(memberId);
		if (comments.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("該會員目前無任何評論。");
		}

		return ResponseEntity.ok(comments);
	}

	@GetMapping("/api/{productName}/comment")
	public ResponseEntity<?> findAllCommentByProductName(@PathVariable String productName) {

		// 根據商品找評論
		List<Comment> comments = commentService.findCommentByProductName(productName);

		if (comments.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("該商品目前無任何評論。");
		}

		for (Comment comment : comments) {
			String memberName = comment.getMembers().getMemberName();
			String firstName;

			if (memberName.length() > 3) {
				firstName = memberName.substring(0, 2);
			} else {
				firstName = memberName.substring(0, 1);
			}

			if (comment.getMembers().getMemberSex() == 1) {
				memberName = (firstName + "先生").trim();
			} else {
				memberName = (firstName + "小姐").trim();
			}
			comment.getMembers().setMemberName(memberName);
		}
		return ResponseEntity.ok(comments);
	}

	@GetMapping("/api/member/{memberId}/avatar")
	public ResponseEntity<byte[]> downloadAvaterByMemberId(@PathVariable Integer memberId) {

		Optional<Members> op = membersRepo.findById(memberId);

		if (op.isPresent()) {
			Members member = op.get();
			byte[] memberImg = member.getMemberImg();

			if (memberImg != null) {
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.IMAGE_JPEG);
				httpHeaders.setContentLength(memberImg.length);
				return new ResponseEntity<byte[]>(memberImg, httpHeaders, HttpStatus.OK);

			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		}

		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

	}

	@Transactional
	@DeleteMapping("/api/Bistro/deleteComment/{ID}")
	public ResponseEntity<String> deleteComment(@PathVariable Integer ID, HttpSession httpSession) {

		Integer memberId = (Integer) httpSession.getAttribute("membersId");

		if (memberId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("請先登入以刪除您的評論。");
		}

		if (ID == null) {
			ResponseEntity.badRequest().body("評論ID無效");
		}

		boolean deleteComment = commentService.deleteComment(ID);

		if (!deleteComment) {
			ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到評論，刪除失敗");

		}

		return ResponseEntity.ok("評論已刪除");

	}

	@Transactional
	@PutMapping("/api/put/comment/{ID}") // 修改評論
	public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Integer ID,
			@RequestBody Comment updateComment) {

		// 查詢原始評論
		Comment comment = commentService.findCommentById(ID);
		if (comment == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "評論不存在"));
		}

		try {
			// 查詢相關的菜單與會員
			Menu menu = menuService.findMenuById(updateComment.getMenu().getID());
			if (menu == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "菜單不存在"));
			}

			Optional<Members> op = membersRepo.findById(updateComment.getMembers().getId());

			if (op.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "會員不存在"));

			}

			Members members = op.get();

			// 更新評論資料
			comment.setMenu(menu);
			comment.setMembers(members);
			comment.setCommentProduct(updateComment.getCommentProduct());
			comment.setCommentMessage(updateComment.getCommentMessage());
			comment.setCommentRating(updateComment.getCommentRating());

			// 更新評論
			commentService.updateComment(comment);

			// 更新菜單平均分數
			Double avgScore = menuService.countOneMenuAvgScore(menu.getProductName());
			menu.setAvgScore(avgScore);
			menuService.updateMenu(menu); // 確保平均分數更新到資料庫

			// 返回成功回應
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("updatedComment", comment);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "message", "更新失敗", "error", e.getMessage()));
		}
	}
	
	


	@GetMapping("/api/{productName}/comment/people")
	public ResponseEntity<?> countCommentPeopleByProductName(@PathVariable String productName) {

		// 根據商品找評論
		String commentPeople = commentFrontService.countCommentPeopleByProductName(productName);

		
		return ResponseEntity.ok(commentPeople);
	}

	
	

}
