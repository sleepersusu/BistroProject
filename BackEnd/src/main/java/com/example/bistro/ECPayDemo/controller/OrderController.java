package com.example.bistro.ECPayDemo.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.ECPayDemo.service.OrderService;
import com.example.bistro.ecpay.payment.integration.domain.AioCheckOutALL;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrderController {

    @Autowired
    OrderService orderService;

    private static String paymentStatus;

    @GetMapping("/ecpayCheckout")
    public String ecpayCheckout(
            @RequestParam(required = false, defaultValue = "0") String amount,
            @RequestParam(required = false, defaultValue = "") String ordersName,
            @RequestParam(required = false, defaultValue = "") String ordersTel) {
        try {
            System.out.println("有進到後端");
            String aioCheckOutALLForm = orderService.ecpayCheckout(amount, ordersName, ordersTel);
            return aioCheckOutALLForm;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    @RequestMapping(value = "/ecpay/callback", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<String> handleCallback(
            @RequestBody(required = false) Map<String, String> postData,
            @RequestParam Map<String, String> getData,
            HttpServletRequest request
    ) {
        //進不去的
        System.out.println("=== 進入 callback ===");

        Map<String, String> responseData;
        if (request.getMethod().equals("POST")) {
            responseData = postData != null ? postData : getData;
        } else {
            responseData = getData;
        }

        System.out.println("收到的 response: " + responseData);

        String rtnCode = responseData.get("RtnCode");
        paymentStatus = rtnCode;

        System.out.println("Callback 設置 paymentStatus: " + paymentStatus);

        // 必須回應 "1|OK"
        return ResponseEntity.ok("1|OK");
    }


    @RequestMapping(value = "/payment-result", method = {RequestMethod.POST, RequestMethod.GET})
    public void handlePaymentResult(HttpServletResponse response) throws IOException {
        //進的去得
        System.out.println("=== 進入 payment-result ===");
        System.out.println("當前 paymentStatus: " + paymentStatus);

        AioCheckOutALL obj = new AioCheckOutALL();
        System.out.println(obj.getTotalAmount());
        System.out.println("666666666666666666666");

        // 測試用：直接導向成功頁面
        response.sendRedirect("http://localhost:5173/cartCheckSuc");

    }

    @RequestMapping(value = {"/ecpay/callback", "/payment-result"}, method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }
}

