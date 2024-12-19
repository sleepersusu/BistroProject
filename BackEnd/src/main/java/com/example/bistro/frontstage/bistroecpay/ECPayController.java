package com.example.bistro.frontstage.bistroecpay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.ecpay.payment.integration.AllInOne;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ECPayController {

    @PostMapping("/api/redirectToECPay")
    public void redirectToECPay(HttpServletResponse response) {
        // 初始化 AllInOne 物件
        AllInOne allInOne = new AllInOne("");

        // 設定 ECPay 所需的參數
        Map<String, String> param = new HashMap<>();
        param.put("MerchantID", "3002607"); // 你的商店 ID
        param.put("MerchantTradeNo", "20231219001"); // 交易編號
        param.put("MerchantTradeDate", "2024/12/19 15:00:00"); // 交易時間
        param.put("PaymentType", "AIO"); // 付款方式 (綜合支付)
        param.put("TotalAmount", "1000"); // 付款金額
        param.put("TradeDesc", "Test Order Description"); // 訂單描述
        param.put("ItemName", "Test Item"); // 商品名稱
        param.put("ReturnURL", "http://211.23.128.214:5000"); // 付款完成後跳轉的 URL
//      param.put("OrderResultURL", "http://localhost:5173/cartCheckSuc");
        // 付款結果通知 URL

        try {
            // 呼叫 aioCheckOut 來取得 ECPay 付款頁面的 URL
            String actionUrl = allInOne.aioCheckOut(param, null);     
            // 重定向用戶至 ECPay 的付款頁面
            response.sendRedirect(actionUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping("/api/ecpay")
    public class EcpayController {

        @PostMapping("/callback")
        public String handleCallback(@RequestBody Map<String, String> response) {
            // 檢查交易狀態
            String status = response.get("RtnCode");
            
            if ("1".equals(status)) {
                // 交易成功，回傳 1
                return "1";
            } else {
                // 交易失敗，回傳 0
                return "0";
            }
        }
    }
    
}