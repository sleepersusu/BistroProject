package com.example.bistro.backstage.orders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.bistro.backstage.employee.EmployeeRepository;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.members.MembersService;
import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.menu.MenuRepository;
import com.example.bistro.backstage.payment.Payment;
import com.example.bistro.backstage.seats.Seats;
import com.example.bistro.backstage.seats.SeatsRepository;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OrdersController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private MembersRepository membersRepositoryDao;
    @Autowired
    private SeatsRepository seatsRepository;
    @Autowired
    private EmployeeRepository employeeRepositoryDao;
    @Autowired
    private MenuRepository menuRepositoryDao;
    
    @Autowired
    private MembersService membersService;

    //findAll orders
        @GetMapping("/Bistro/Orders/findAll")
        public String findAll(Model model) {
            List<Orders> allOrders = ordersService.findAllOrders();
            List<Menu> allMenus = menuRepositoryDao.findAll();

            model.addAttribute("allOrders", allOrders);
            model.addAttribute("allMenus", allMenus);

            return "orders/ordersView";
        };

    //delete orders by id OK
        @PostMapping("/Bistro/Orders/deleteOrders")
        public String deleteOrders(@RequestParam Integer id) {
            ordersService.deleteOrdersById(id);
            return "redirect:/Bistro/Orders/findAll";
        };

    // 生成訂單編號
    private String generateOrdersNumber() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return "O" + uuid.substring(0, 10);
    }

    @GetMapping("/Bistro/Orders/findMemberByPhone")
    @ResponseBody
    public Optional<Members> findMemberByPhone(@RequestParam String memberPhone) {
        return membersRepositoryDao.findByMemberPhone(memberPhone);
    }



    //create 新增一筆訂單 OK 包含memberNull問題和Payment問題
    @PostMapping("/Bistro/Orders/createOrders")
    public String createOrders(
            @RequestParam String ordersName,
            @RequestParam String ordersTel,
            @RequestParam String seatType,
            @RequestParam Integer ordersSumPrice,
            @RequestParam String ordersRequest,
            @RequestParam String paymentStatus,
            @RequestParam String paymentWay,
            @RequestParam Integer paymentPrice,
            @RequestParam(required = false) Integer memberId,
            @RequestParam List<Integer> menuId,
            @RequestParam List<Integer> quantities,
            @RequestParam List<Integer> prices,
            @RequestParam Integer seatsId) {

        Orders orders = new Orders();
        orders.setOrdersName(ordersName);
        orders.setOrdersNumber(generateOrdersNumber());
        orders.setOrdersTel(ordersTel);
        orders.setSeatType(seatType);
        orders.setOrdersSumPrice(ordersSumPrice);
        orders.setPointGetted(ordersSumPrice / 100);
        if(memberId !=null) {
        	Members memberData = membersService.findMembersById(memberId);
        	Integer PointDate = memberData.getMemberPoint();
        	memberData.setMemberPoint(PointDate+(ordersSumPrice / 100));       	
        }
        orders.setOrdersRequest(ordersRequest);

        // 處理會員資訊
        if (memberId != null && memberId != 0) {
            Members member = membersRepositoryDao.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + memberId));
            orders.setMembers(member);
        } else {
            orders.setMembers(null);
        }

        // 處理座位
        Seats seat = seatsRepository.findById(seatsId).orElse(null);
        orders.setSeats(seat);

        // 處理付款資訊
        Payment payment = new Payment();
        payment.setOrders(orders);
        payment.setPaymentStatus(paymentStatus);
        payment.setPaymentWay(paymentWay);
        payment.setPaymentPrice(paymentPrice);
        orders.getPayment().add(payment);
        orders.setLatestPaymentStatus(paymentStatus);

        // 處理訂單詳情
        for (int i = 0; i < menuId.size(); i++) {
            int finalI = i;
            Menu menu = menuRepositoryDao.findById(menuId.get(i))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu ID: " + menuId.get(finalI)));

            OrdersDetails orderDetails = new OrdersDetails();
            orderDetails.setOrders(orders);
            orderDetails.setMenu(menu);
            orderDetails.setOdName(menu.getProductName());
            orderDetails.setOdQuantity(quantities.get(i)); // 使用對應索引的數量
            orderDetails.setOdPrice(prices.get(i));
            orderDetails.setOdSumPrice(quantities.get(i) * prices.get(i));
            orders.getOrdersDetails().add(orderDetails);
        }

        ordersService.insertOrders(orders);
        return "redirect:/Bistro/Orders/findAll";
    }
}


    //update



