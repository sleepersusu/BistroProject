package com.example.bistro.paypal;


import com.example.bistro.backstage.payment.PaymentRepository;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;


@RestController  // 改用 @RestController
@RequiredArgsConstructor
@Slf4j
public class PaypalController {

    private final PaypalService paypalService;
    private final PaymentRepository paymentRepository;


    @PostMapping("/payment/create")
    public ResponseEntity<?> createPayment(
            @RequestParam Double amount,
            @RequestParam String orderNumber
    ) {
        try {
            String cancelUrl = "http://localhost:8085/payment/cancel?orderNumber=" + orderNumber;
            String successUrl = "http://localhost:8085/payment/success?orderNumber=" + orderNumber;

            Payment payment = paypalService.createPayment(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    "Order: " + orderNumber,
                    cancelUrl,
                    successUrl
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    // 返回 PayPal URL 而不是直接重定向
                    return ResponseEntity.ok(Map.of("redirectUrl", links.getHref()));
                }
            }

            return ResponseEntity.badRequest().body("PayPal redirect URL not found");
        } catch (PayPalRESTException e) {
            log.error("PayPal payment creation failed:", e);
            return ResponseEntity.status(500).body("PayPal payment creation failed");
        }
    }

    @GetMapping("/payment/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("orderNumber") String orderNumber
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                // 更新支付狀態
                paypalService.updatePaymentStatus(orderNumber, "成功");

                // 返回一個簡單的關閉頁面
                return """
                        <html>
                        <body>
                        <script>
                            window.close();
                        </script>
                        <p>支付成功！請關閉此視窗返回商店。</p>
                        </body>
                        </html>
                        """;
            }
        } catch (PayPalRESTException e) {
            log.error("PayPal payment execution failed:", e);
        }

        // 支付失敗的情況
        return """
                <html>
                <body>
                <script>
                    window.close();
                </script>
                <p>支付失敗！請關閉此視窗返回商店。</p>
                </body>
                </html>
                """;
    }


    @GetMapping("/payment/cancel")
    public ResponseEntity<?> paymentCancel(@RequestParam("orderNumber") String orderNumber) {
        // 更新支付狀態為取消
        paypalService.updatePaymentStatus(orderNumber, "取消");
        return ResponseEntity.ok(Map.of(
                "status", "cancelled",
                "orderNumber", orderNumber,
                "paymentStatus", "cancelled"

        ));
    }

    @GetMapping("/payment/status")
    public ResponseEntity<?> getPaymentStatus(@RequestParam("orderNumber") String orderNumber) {
        try {
            // 獲取支付記錄
            com.example.bistro.backstage.payment.Payment payment = paypalService.getPaymentByOrderNumber(orderNumber);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "orderNumber", orderNumber,
                    "paymentStatus", payment.getPaymentStatus()
            ));
        } catch (Exception e) {
            log.error("Get payment status failed:", e);
            return ResponseEntity.status(500).body("Get payment status failed");
        }
    }
}