package com.example.bistro.members;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MembersController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/Bistro/Member/getMembersInfo")
    public ResponseEntity<Map<String, Object>> getMembersInfo(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {
        // 確保至少提供一個參數
        if ((name == null || name.isEmpty()) && (phone == null || phone.isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of("error", "請提供姓名或電話號碼"));
        }

        // 使用 Service 查找會員
        Members members = memberService.findMembers(name, phone);

        // 返回會員信息或 null
        if (members != null) {
            return ResponseEntity.ok(Map.of(
                    "memberId", members.getId(),
                    "name", members.getMemberName(),
                    "phone", members.getMemberPhone()
            ));
        } else {
            return ResponseEntity.ok(Map.of(
                    "message", "非會員"
            ));
        }
    }
    
    @GetMapping("/Bistro/Member/findAllMembers")
    public String findAll(Model model) {
    	List<Members> allMembers = memberService.findAllMembers();
    	model.addAttribute("allMembers",allMembers);
        return "member/membersView";
    }
    
}
