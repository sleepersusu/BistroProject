package com.example.bistro.backstage.employee;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobTitleService {
	
	@Autowired
	private JobTitleRepository jobTitleRepo;
	
	public List<JobTitle> findAllJobTitle() {
		List<JobTitle> allJobTitle = jobTitleRepo.findAll();
		return allJobTitle;
	}
}
