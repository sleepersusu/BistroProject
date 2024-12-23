package com.example.bistro.backstage.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardPageController {

    @GetMapping("/index")
    public String showDashboardPage() {
        return "/HomePage/BackEndIndex";  // 对应 `src/main/resources/templates/dashboard.html`
    }
}
