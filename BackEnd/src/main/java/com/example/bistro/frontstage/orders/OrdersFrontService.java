package com.example.bistro.frontstage.orders;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.menu.MenuRepository;
import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersRepository;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;
import com.example.bistro.backstage.ordersDetails.OrdersDetailsRepository;
import com.example.bistro.backstage.payment.Payment;
import com.example.bistro.backstage.payment.PaymentRepository;
import com.example.bistro.backstage.seats.SeatsRepository;
import com.example.bistro.frontstage.cart.CartRepository;
import com.example.bistro.frontstage.cart.CartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class OrdersFrontService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private SeatsRepository seatsRepository;

    @Autowired
    private OrdersDetailsRepository ordersDetailsRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartService cartService;


    // 生成訂單編號

        private String generateOrdersNumber() {
            String uuid = UUID.randomUUID().toString().replace("-", "");
            return "O" + uuid.substring(0, 10);
        }

    // 新增訂單
    public Orders createOrder(OrdersDTO ordersDTO) {
        Orders newOrder = new Orders();

        // 訂單基本資料
            newOrder.setOrdersNumber(generateOrdersNumber());
            newOrder.setOrdersName(ordersDTO.getOrdersName());
            newOrder.setOrdersTel(ordersDTO.getOrdersTel());
            newOrder.setSeatType(ordersDTO.getSeatType());
            newOrder.setOrdersSumPrice(ordersDTO.getOrdersSumPrice());
            newOrder.setOrdersRequest(ordersDTO.getOrdersRequest());
            newOrder.setLatestPaymentStatus(ordersDTO.getLatestPaymentStatus());
        // 計算 pointGetted（根據總金額 / 100）
            if (ordersDTO.getOrdersSumPrice() != null) {
                Integer pointGetted = ordersDTO.getOrdersSumPrice() / 100; // 金額除以 100

                newOrder.setPointGetted(pointGetted);
            } else {
                newOrder.setPointGetted(0); // 若金額為空，點數設為 0
            }

        // 設置會員 (若是非會員，memberId 可以為 null)
            if (ordersDTO.getMemberId() != null) {
                Members member = membersRepository.findById(ordersDTO.getMemberId())
                        .orElseThrow(() -> new RuntimeException("Member not found"));
                newOrder.setMembers(member);
            }

        // 儲存訂單到資料庫
            Orders savedOrder = ordersRepository.save(newOrder);

        // 儲存訂單詳情 (購物車資料)
            List<OrdersDetails> orderDetails = new ArrayList<>();
            for (OrdersDetailsDTO detailsDTO : ordersDTO.getOrdersDetails()) {
                OrdersDetails orderDetail = new OrdersDetails();
                orderDetail.setOdName(detailsDTO.getOdName());
                orderDetail.setOdQuantity(detailsDTO.getOdQuantity());
                orderDetail.setOdPrice(detailsDTO.getOdPrice());
                orderDetail.setOdSumPrice(detailsDTO.getOdSumPrice());

                // 判斷菜單
                if (detailsDTO.getMenuId() != null) {
                    Optional<Menu> menu = menuRepository.findById(detailsDTO.getMenuId());
                    if (menu.isPresent()) {
                        orderDetail.setMenu(menu.get());
                    } else {
                        throw new RuntimeException("Menu item not found for ID: " + detailsDTO.getMenuId());
                    }
                }
                    orderDetail.setOrders(savedOrder);  // 設定對應的訂單
                    ordersDetailsRepository.save(orderDetail);  // 儲存訂單詳情
                    orderDetails.add(orderDetail);
            }
            savedOrder.setOrdersDetails(orderDetails);

        // 儲存付款資訊
            List<Payment> payments = new ArrayList<>();
            boolean allPaymentSuc = false;
            for (PaymentDTO paymentDTO : ordersDTO.getPayments()) {
                Payment payment = new Payment();
                payment.setPaymentPrice(paymentDTO.getPaymentPrice());
                payment.setPaymentWay(paymentDTO.getPaymentWay());
                payment.setPaymentStatus(paymentDTO.getPaymentStatus());
                payment.setOrders(savedOrder);  // 設定對應的訂單
                paymentRepository.save(payment);  // 儲存付款資訊
                payments.add(payment);

                if("成功".equals(paymentDTO.getPaymentStatus())) {
                    allPaymentSuc =true;
                }
            }
            savedOrder.setPayment(payments);
            //判斷成功或失敗才刪除
                if ( allPaymentSuc && ordersDTO.getMemberId() != null) {
                    cartService.clearCartByMemberId(ordersDTO.getMemberId());
                }

        // 返回儲存的訂單
            return ordersRepository.save(newOrder);
    }

    // 根據 memberId 查找所有訂單
        public List<Orders> findAllOrdersByMemberId(Integer memberId) {
            return ordersRepository.findByMembersId(memberId);
        }


}
