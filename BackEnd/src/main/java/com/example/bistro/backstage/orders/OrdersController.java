package com.example.bistro.backstage.orders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.example.bistro.backstage.menu.Menu;
import com.example.bistro.backstage.ordersDetails.OrdersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
//            List<Orders> allOrders = ordersService.findAllOrders();
            List<Menu> allMenus = menuRepositoryDao.findAll();

//            model.addAttribute("allOrders", allOrders);
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

    @GetMapping("/Bistro/Orders/data")
    @ResponseBody
    public Map<String, Object> getOrdersData(
            @RequestParam(value = "draw", defaultValue = "1") int draw,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 計算頁碼
        int page = start / length;

        // 獲取分頁數據
        Page<Orders> ordersPage = ordersService.findOrdersWithPagination(PageRequest.of(page, length));

        // 準備 DataTables 所需的響應格式
        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", ordersPage.getTotalElements());
        response.put("recordsFiltered", ordersPage.getTotalElements());

        // 轉換訂單數據為適合的格式
        List<Map<String, Object>> data = ordersPage.getContent().stream()
                .map(order -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("ID", order.getID());
                    item.put("ordersNumber", order.getOrdersNumber());
                    item.put("memberId", order.getMembers() != null ? order.getMembers().getId() : "非會員");
                    item.put("ordersName", order.getOrdersName());
                    item.put("ordersTel", order.getOrdersTel());
                    item.put("seatType", order.getSeatType());
                    item.put("ordersSumPrice", order.getOrdersSumPrice());
                    item.put("pointGetted", order.getPointGetted());
                    item.put("ordersRequest", order.getOrdersRequest());
                    item.put("latestPaymentStatus", order.getLatestPaymentStatus());
                    // 格式化 createdAt 字段，去掉毫秒部分 2024-11-15 12:30:00
                        Date createdAt = order.getCreatedAt();
                        LocalDateTime localDateTime = Instant.ofEpochMilli(createdAt.getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
                        String formattedDate = localDateTime.format(formatter);
                        item.put("createdAt", formattedDate);
                    return item;
                })
                .collect(Collectors.toList());

        response.put("data", data);

        return response;
    }
}


    //update



