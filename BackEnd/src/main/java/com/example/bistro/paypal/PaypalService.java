package com.example.bistro.paypal;


import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersRepository;
import com.example.bistro.backstage.payment.PaymentRepository;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j  // 加入這個注解
@Service
@RequiredArgsConstructor
public class PaypalService {

    private final APIContext apiContext;
    private final PaymentRepository paymentRepository;  // 注入 PaymentRepository
    private final OrdersRepository ordersRepository;  // 新增這個



    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl
    ) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format(Locale.forLanguageTag(currency),"%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);

        payment.setRedirectUrls(redirectUrls);
        return payment.create(apiContext);
    }

    public Payment executePayment(
            String paymentId,
            String payerId
    ) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);


    PaymentExecution paymentExecution = new PaymentExecution();
    paymentExecution.setPayerId(payerId);

    return payment.execute(apiContext,paymentExecution);

    }

    // 新增 updatePaymentStatus 方法
    @Transactional
    public void updatePaymentStatus(String ordersNumber, String status) {
        try {
            // 更新 Payment 表
            com.example.bistro.backstage.payment.Payment dbPayment = paymentRepository.findByOrders_OrdersNumber(ordersNumber)
                    .orElseThrow(() -> new RuntimeException("Payment not found: " + ordersNumber));
            dbPayment.setPaymentStatus(status);
            paymentRepository.save(dbPayment);

            // 更新 Orders 表
            Orders order = ordersRepository.findByOrdersNumber(ordersNumber)
                    .orElseThrow(() -> new RuntimeException("Order not found: " + ordersNumber));
            order.setLatestPaymentStatus("已付款");  // 當 PayPal 支付成功時設為已付款
            ordersRepository.save(order);

            log.info("Successfully updated payment status for order: {}", ordersNumber);
        } catch (Exception e) {
            log.error("Failed to update payment status for order: " + ordersNumber, e);
            throw new RuntimeException("Payment status update failed", e);
        }
    }

    // 新增這個方法來獲取支付狀態
    public com.example.bistro.backstage.payment.Payment getPaymentByOrderNumber(String orderNumber) {
        return paymentRepository.findByOrders_OrdersNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderNumber));
    }
}

