package com.example.bistro.frontstage.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bistro.backstage.menu.MenuRepository;

@Service
public class MenuRestService {

	
	@Autowired
	private MenuRepository menuRepo;
	
}
