package com.example.bistro.backstage.PointsRecords;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PointsRecordsService {

	@Autowired
	private PointsRecordsRepository PRRepo;
	
	public List<Object[]> findAllPointsRecords(){
		return PRRepo.findMembersAllPointRecord();
	}
	
	public PointsRecordsBean createPointsRecords(PointsRecordsBean PointsRecords) {
		return PRRepo.save(PointsRecords);
	}
	
	public void deletPointsRecords(Integer id) {
		PRRepo.deleteById(id);
	}
	
}
