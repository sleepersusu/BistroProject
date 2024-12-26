package com.example.bistro.backstage.ordersDetails;

import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersRepository;
import com.example.bistro.backstage.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class OrderDetailsController {

    @Autowired
    private OrdersDetailsService ordersDetailsService;
    @Autowired
    private OrdersDetailsRepository ordersDetailsRepository;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrdersRepository ordersRepository;

    //findAll ordersDetails
        @GetMapping("/Bistro/ordersDetails/findAll")
        public String findAll(Model model) {
            List<OrdersDetails> allOrdersDetails = ordersDetailsService.findAllOrdersDetails();
            model.addAttribute("allOrdersDetails", allOrdersDetails);
            return "ordersDetails/ordersDetailsView";
        };

    //分頁
    @GetMapping("/Bistro/OrdersDetails/data")
    @ResponseBody
    public Map<String, Object> getOrdersDetailsData(
            @RequestParam(value = "draw", defaultValue = "1") int draw,
            @RequestParam(value = "start", defaultValue = "0") int start,
            @RequestParam(value = "length", defaultValue = "10") int length,
            @RequestParam(value = "search[value]", required = false) String search,
            @RequestParam(value = "order[0][column]", required = false, defaultValue = "0") int sortColumn,
            @RequestParam(value = "order[0][dir]", required = false, defaultValue = "asc") String sortDir) {

        // 設定排序
        String[] columnNames = {"ID", "orders.ID", "orders.ordersNumber", "menu.ID",
                "menu.productName", "odQuantity", "odPrice", "odSumPrice"};
        String sortField = columnNames[sortColumn];
        Sort.Direction direction = sortDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // 建立分頁請求
        PageRequest pageRequest = PageRequest.of(start / length, length, Sort.by(direction, sortField));

        // 獲取分頁數據
        Page<OrdersDetails> ordersDetailsPage = ordersDetailsService.findOrdersWithPagination(search, pageRequest);

        // 準備響應數據
        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", ordersDetailsPage.getTotalElements());
        response.put("recordsFiltered", ordersDetailsPage.getTotalElements());

        // 轉換數據格式
        List<Map<String, Object>> data = ordersDetailsPage.getContent().stream()
                .map(orderDetails -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("ID", orderDetails.getID());
                    item.put("orderId", orderDetails.getOrders().getID());
                    item.put("ordersNumber", orderDetails.getOrders().getOrdersNumber());
                    item.put("menuId", orderDetails.getMenu().getID());
                    item.put("odName", orderDetails.getMenu().getProductName());
                    item.put("odQuantity", orderDetails.getOdQuantity());
                    item.put("odPrice", orderDetails.getOdPrice());
                    item.put("odSumPrice", orderDetails.getOdSumPrice());
                    return item;
                })
                .collect(Collectors.toList());

        response.put("data", data);
        return response;
    }

    //Create ordersDetails when orders insert



}
