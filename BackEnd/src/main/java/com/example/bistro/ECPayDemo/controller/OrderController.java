package com.example.bistro.ECPayDemo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.ECPayDemo.service.OrderService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class OrderController {

	@Autowired
	OrderService orderService;

	@GetMapping("/ecpayCheckout")
	public String ecpayCheckout() {
		System.out.println("有進到後端");
		String aioCheckOutALLForm = orderService.ecpayCheckout();

		return aioCheckOutALLForm;
	}
	

    // 新增：處理綠界的回調
    @PostMapping("/ecpay/callback")
    public String handleCallback(@RequestBody Map<String, String> response) {
    	System.out.println("有進到callback");
        String status = response.get("RtnCode");
        // 這裡可以加入交易記錄的處理邏輯
        return status;
    }

    // 處理付款結果的重定向
    @GetMapping("/payment-result")
    public void handlePaymentResult(HttpServletResponse response) throws IOException {
    	System.out.println("有進到payment-result");
//        if ("1".equals(RtnCode)) {
//            response.sendRedirect("http://localhost:5173/cartCheckSuc");
//        } else {
//            response.sendRedirect("http://localhost:5173/cartCheckFail");
//        }
    	response.sendRedirect("http://localhost:5173/cartCheckSuc");
    }
} 
//	
//    // 這個是給使用者瀏覽器導向的端點
//    @GetMapping("/payment-result")
//    public void handlePaymentResult(@RequestParam String RtnCode, HttpServletResponse response) throws IOException {
//        // 根據交易結果重新導向到前端的成功或失敗頁面
//        if ("1".equals(RtnCode)) {
//            response.sendRedirect("http://localhost:8080/cartCheckSuc");
//        } else {
//            response.sendRedirect("http://localhost:8080/cartCheckFail");
//        }
//    }