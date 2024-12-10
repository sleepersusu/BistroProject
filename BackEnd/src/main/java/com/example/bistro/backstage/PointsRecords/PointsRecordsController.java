package com.example.bistro.backstage.PointsRecords;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bistro.backstage.members.MembersService;
import com.example.bistro.backstage.pointPrizes.PointPrizesService;


@Controller
public class PointsRecordsController {

	@Autowired
	private PointsRecordsService PRService;
	@Autowired
	private MembersService MSService;
	@Autowired
	private PointPrizesService PPService;
	
	@GetMapping("/Bistro/PointsRecords/showAll")
	public String showAll(Model model) {
		List<Object[]> results = PRService.findAllPointsRecords();
		model.addAttribute("allPointsRecords", results);
		return "pointPrizes/showPointRecoed";
	}
	
	@PostMapping("/Bistro/PointsRecords/createPointsRecords")
	public String createPointsRecords(@RequestParam Integer memberId, @RequestParam Integer prizeId, 
	                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date recordsDate) {
	    PointsRecordsBean pointsRecordsBean = new PointsRecordsBean();
	    pointsRecordsBean.setMembers(MSService.findMembersById(memberId));
	    pointsRecordsBean.setPointPrizes(PPService.findById(prizeId));
	    pointsRecordsBean.setRecordsDate(recordsDate);
	    PRService.createPointsRecords(pointsRecordsBean);
	    return "redirect:/Bistro/PointsRecords/showAll";
	}
	
	@PostMapping("/Bistro/PointsRecords/delete")
	public String deletePointsRecords(@RequestParam Integer id) throws IOException {
		PRService.deletPointsRecords(id);
	
		return "redirect:/Bistro/PointsRecords/showAll";
	}
	
}
