package com.example.bistro.backstage.members;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.bistro.backstage.config.ImageService;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;

@Controller
public class MembersController {

	@Autowired
	private MembersService membersService;
	
	@Autowired
	private ImageService imageService;


    @GetMapping("/Bistro/Member/getMembersInfo")
    public ResponseEntity<Map<String, Object>> getMembersInfo(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone) {
        // 確保至少提供一個參數
        if ((name == null || name.isEmpty()) && (phone == null || phone.isEmpty())) {
            return ResponseEntity.badRequest().body(Map.of("error", "請提供姓名或電話號碼"));
        }

        // 使用 Service 查找會員
        Members members = membersService.findMembers(name, phone);

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
    	List<Members> allMembers = membersService.findAllMembers();
    	model.addAttribute("allMembers",allMembers);
        return "member/membersView";
    }
    
    
    //分頁
    @GetMapping("/Bistro/Member/data")
    @ResponseBody
    public Map<String, Object> getOrdersDetailsData(
            @RequestParam(value = "draw", defaultValue = "1") int draw,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length,
            @RequestParam(value = "search[value]", required = false) String search,
            @RequestParam(value = "order[0][column]", required = false, defaultValue = "0") int sortColumn,
            @RequestParam(value = "order[0][dir]", required = false, defaultValue = "asc") String sortDir) {

        // 設定排序
        String[] columnNames = {"id", "Members.memberAccount", "Members.memberPassword",
                "Members.memberName", "Members.memberSex", "Members.memberBirthday", "Members.memberFavor",
                "Members.memberAddress","Members.memberPhone","Members.memberEmail","Members.memberImg","Members.memberStatus"};
        String sortField = columnNames[sortColumn];
        Sort.Direction direction = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 建立分頁請求
        PageRequest pageRequest = PageRequest.of(start / length, length, Sort.by(direction, sortField));

        // 獲取分頁數據
        Page<Members> memberDetailsPage = membersService.findWithPagination(search, pageRequest);

        // 準備響應數據
        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", memberDetailsPage.getTotalElements());
        response.put("recordsFiltered", memberDetailsPage.getTotalElements());

        // 轉換數據格式
        List<Map<String, Object>> data = memberDetailsPage.getContent().stream()
                .map(Members -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", Members.getId());
                    item.put("memberAccount", Members.getMemberAccount());
                    item.put("memberPassword", Members.getMemberPassword());
                    item.put("memberName", Members.getMemberName());
                    item.put("memberSex", Members.getMemberSex());
                    item.put("memberBirthday", Members.getMemberBirthday());
                    item.put("memberFavor", Members.getMemberFavor());
                    item.put("memberAddress", Members.getMemberAddress());
                    item.put("memberPhone", Members.getMemberPhone());
                    item.put("memberEmail", Members.getMemberEmail());
                    item.put("memberImg", Members.getMemberImg());
                    item.put("memberStatus", Members.getMemberStatus());
                    return item;
                })
                .collect(Collectors.toList());

        response.put("data", data);
        return response;
    }

    //Create ordersDetails when orders insert
    
    
    @Transactional
    @PostMapping("/Bistro/Member/UpdateMember")
    public String updateMember(@ModelAttribute Members memberBean,@RequestParam("memberPhoto") MultipartFile file) {
    	String type = "member";
    	Members dbMember = membersService.findMembersById(memberBean.getId());
        memberBean.setMemberShip(dbMember.getMemberShip());
        memberBean.setMemberStatus(dbMember.getMemberStatus());
        memberBean.setCreatedAt(dbMember.getCreatedAt());
            try {
            	if (file != null && !file.isEmpty()) {
                    byte[] fileBytes = file.getBytes(); // 轉成 byte[]
                    imageService.imageUpload(type, memberBean.getId(), fileBytes);
                    memberBean.setMemberImg(fileBytes);
            	}else {
            		memberBean.setMemberImg(dbMember.getMemberImg());
            	}
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = membersService.updateMember(memberBean);
            System.out.println(result);
        return "redirect:/Bistro/Member/findAllMembers";
    }
    
    @Transactional
    @PostMapping("/Bistro/Member/postMember")
    public String createNewMember(@ModelAttribute Members memberBean,@RequestParam("memberPhoto") MultipartFile file) {
    	String name = memberBean.getMemberName();
    	String phone = memberBean.getMemberPhone();
    	Members dbMember = membersService.findMembers(name, phone);
    	if(dbMember==null) {
        	try {
        		if (file != null && !file.isEmpty()) {
        			byte[] fileBytes = file.getBytes(); // 轉成 byte[]
        			memberBean.setMemberImg(fileBytes); 
        		}
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        	membersService.insertMember(memberBean);
    		return "redirect:/Bistro/Member/findAllMembers";
    	}else {
    		System.out.println("新增失敗，資料已存在");
    	}
    	return "redirect:/Bistro/Member/findAllMembers";
    }
    
    @Transactional
    @PostMapping("/Bistro/Member/cancel")
    public String cancelMember(@RequestParam Integer id) {
    	Members memberBean = membersService.findMembersById(id);
    	String memberShip="註銷";
    	memberBean.setMemberStatus(memberShip);
    	String result = membersService.updateMember(memberBean);
    	System.out.println(result);
    	return "redirect:/Bistro/Member/findAllMembers";
    }
    @Transactional
    @PostMapping("/Bistro/Member/active")
    public String activeMember(@RequestParam Integer id) {
    	Members memberBean = membersService.findMembersById(id);
    	String memberShip="啟用";
    	memberBean.setMemberStatus(memberShip);
    	String result = membersService.updateMember(memberBean);
    	System.out.println(result);
    	return "redirect:/Bistro/Member/findAllMembers";
    }
}
