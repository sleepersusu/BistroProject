package com.example.bistro.backstage.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {


    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        try {
            Map<String, Object> data = dashboardService.getDashboardData();
            logger.info("Dashboard data retrieved successfully: {}", data);
            System.out.println("返回的數據: " + data);  // 加入這行查看數據
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            logger.error("Error retrieving dashboard data", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<>());  // 返回空的 Map 而不是 null
        }
    }

}