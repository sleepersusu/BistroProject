package com.example.bistro.frontstage.pointGet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.pointsGet.PointsGetRepository;

@Service
public class PointGetFrontService {
	
	@Autowired
	private PointsGetRepository pointsGetRepo;
	
	public List<Object[]> findAllPointsGet(){
		return pointsGetRepo.findMembersAllPointGet();
	}
	
	public String createPointGet(int memberId, int pointGetted) {
		
		return null;
	}
}
