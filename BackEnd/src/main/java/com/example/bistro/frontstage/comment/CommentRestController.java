package com.example.bistro.frontstage.comment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.example.bistro.backstage.comment.CommentRepository;
import com.example.bistro.backstage.comment.CommentService;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.members.MembersService;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuService;
import com.example.bistro.backstage.orders.Orders;
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
	private CommentRepository commentRepo;

	@PostMapping("/api/comment/postComment")
	public ResponseEntity<?> createComment(@RequestBody CommentDTO dto, HttpSession httpSession) {
	    try {
	        // 從 session 中取得會員 ID
	        Integer memberId = (Integer) httpSession.getAttribute("membersId");
	        if (memberId == null) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or missing Member ID");
	        }

	        // 驗證會員是否存在
	        Optional<Members> memberOp = membersRepo.findById(memberId);
	        if (!memberOp.isPresent()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
	        }
	        Members member = memberOp.get();

	        // 驗證菜單是否存在
	        Menu menu = menuService.findMenuByProductName(dto.getCommentProduct());
	        if (menu == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu not found for the given product name");
	        }

	        // 創建評論物件並填充屬性
	        Comment newComment = new Comment();
	        newComment.setMenu(menu);
	        newComment.setMembers(member);
	        newComment.setCommentMessage(dto.getCommentMessage());
	        newComment.setCommentProduct(dto.getCommentProduct());
	        newComment.setCommentRating(dto.getCommentRating());
	        newComment.setCommentTime(new Date()); // 設定當前時間

	        // 保存評論到資料庫
	        Comment savedComment = commentRepo.save(newComment);

	        // 驗證保存結果
	        if (savedComment == null || savedComment.getID() == null) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create comment");
	        }

	        // 構建回應資料
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "Comment created successfully");
	        response.put("commentId", savedComment.getID());
	        response.put("commentTime", savedComment.getCommentTime());
	        
	        System.out.println(response);
	        return ResponseEntity.ok(response);

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("An unexpected error occurred: " + e.getMessage());
	    }
	}


	

	@GetMapping("/api/member/comment") // 根據會員取得評論
	public ResponseEntity<?> findAllCommentByMember(HttpSession httpSession) {

//		 從 HttpSession 獲取會員 ID
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
	        Members member = comment.getMembers();
	        if (member == null) {
	            continue; // 若無成員資訊，跳過該評論
	        }

	        String memberName = member.getMemberName();
	        if (memberName == null || memberName.isEmpty()) {
	            continue; // 若名字為空，跳過該評論
	        }

	        // 要先去除 "Mr." 或 "Ms."否則之後會變成Mr.Mr
	        if (memberName.startsWith("Mr.") || memberName.startsWith("Ms.")) {
	            memberName = memberName.substring(3).trim();
	        }

	        // 取得名字的前綴
	        String firstName = memberName.length() > 3 ? memberName.substring(0, 2) : memberName.substring(0, 1);

	        // 根據性別設定稱謂
	        Short memberSex = member.getMemberSex();
	        if (memberSex != null) {
	            if (memberSex == 1) {
	                memberName = ("Mr." + firstName).trim();
	            } else if (memberSex == 0) {
	                memberName = ("Ms." + firstName).trim();
	            }
	        }

	        // 更新名字
	        member.setMemberName(memberName);
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
	@PutMapping("/api/put/comment/{ID}")
	public ResponseEntity<Map<String, Object>> updateComment(@PathVariable Integer ID, @RequestBody CommentDTO dto,
			HttpSession httpSession) {
		try {

			Integer membersId = (Integer) httpSession.getAttribute("membersId");

			// 查詢原始評論
			Comment comment = commentService.findCommentById(ID);
			if (comment == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "評論不存在"));
			}

			// 查詢相關的菜單與會員
			Menu menu = menuService.findMenuById(dto.getMenuId());
			if (menu == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "菜單不存在"));
			}

			// 驗證會員
			Optional<Members> op = membersRepo.findById(dto.getMemberId());
			if (op.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "會員不存在"));
			}

			// 權限檢查
			if (!membersId.equals(dto.getMemberId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body(Map.of("success", false, "message", "您無權修改此評論"));
			}

			Members members = op.get();

			// 5. 更新評論資料
			comment.setMenu(menu);
			comment.setMembers(members);
			comment.setCommentProduct(dto.getCommentProduct());
			comment.setCommentMessage(dto.getCommentMessage());
			comment.setCommentRating(dto.getCommentRating());
			comment.setCommentTime(dto.getCommentTime());

			// 6. 在同一個事務中完成所有更新
			Comment updatedComment = commentService.updateCommentAndMenuScore(comment, menu);

			// 7. 準備回應
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("updatedComment", updatedComment);

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			// 記錄詳細錯誤信息
			e.printStackTrace();
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
