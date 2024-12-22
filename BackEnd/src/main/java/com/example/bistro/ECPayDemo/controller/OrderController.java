package com.example.bistro.ECPayDemo.controller;

import java.io.IOException;
import java.util.Map;

import com.example.bistro.ECPayDemo.request.CheckoutResult;
import com.example.bistro.ECPayDemo.request.CheckoutResult1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    OrderService orderService;

    private static String paymentStatus;

    @GetMapping("/ecpayCheckout")
    public String ecpayCheckout(@RequestParam CheckoutResult checkoutResult) {
        try {
            log.info("有進到後端，收到的參數{}",checkoutResult);
            String aioCheckOutALLForm = orderService.ecpayCheckout(checkoutResult);
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
       
       /* 正式邏輯：
       if (paymentStatus == null) {
           System.out.println("paymentStatus 為 null，設為 0");
           paymentStatus = "0";
       }

       if ("1".equals(paymentStatus)) {
           System.out.println("導向成功頁面");
           response.sendRedirect("http://localhost:5173/cartCheckSuc");
       } else {
           System.out.println("導向失敗頁面");
           response.sendRedirect("http://localhost:5173/cartCheckFail");
       }
       */
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

// 新增：處理綠界的回調
//@PostMapping("/ecpay/callback")
//public String handleCallback(@RequestBody Map<String, String> response) {
//	System.out.println("有進到callback");
//  String status = response.get("RtnCode");
//  // 這裡可以加入交易記錄的處理邏輯
//  return status;
//}

//  // 處理付款結果的重定向
//  @GetMapping("/payment-result")
//  public void handlePaymentResult(HttpServletResponse response) throws IOException {
//  	System.out.println("有進到payment-result");
/// /      if ("1".equals(RtnCode)) {
/// /          response.sendRedirect("http://localhost:5173/cartCheckSuc");
/// /      } else {
/// /          response.sendRedirect("http://localhost:5173/cartCheckFail");
/// /      }
//  	response.sendRedirect("http://localhost:5173/cartCheckSuc");
//  }
//} 
