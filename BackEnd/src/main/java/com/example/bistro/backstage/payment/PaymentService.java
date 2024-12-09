package com.example.bistro.backstage.payment;


import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    private OrdersRepository ordersRepository;

    @Transactional
    public Orders processCheckout(Integer orderId, String paymentMethod) {

        // 檢查訂單是否有效
        Orders orders = ordersRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("無效的訂單 ID"));

        // 模擬支付成功
        Payment payment = new Payment();
        payment.setOrders(orders);
        payment.setPaymentPrice(payment.getPaymentPrice());
        payment.setPaymentStatus(payment.getPaymentStatus());
        payment.setPaymentWay(payment.getPaymentWay());
        payment.setCreatedAt(new Date());
        paymentRepository.save(payment);

        return payment.getOrders();

    }


}
