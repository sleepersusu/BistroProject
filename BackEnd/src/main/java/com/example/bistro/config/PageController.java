package com.example.bistro.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
	
	@GetMapping("/")
	public String home() {
		return "login/login";
	}
	
	@GetMapping("/resetPassword")
	public String restPassword() {
		return "login/resetPassword";
	}
	
}
