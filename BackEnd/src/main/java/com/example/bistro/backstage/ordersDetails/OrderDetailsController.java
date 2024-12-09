package com.example.bistro.backstage.ordersDetails;

import com.example.bistro.backstage.orders.OrdersRepository;
import com.example.bistro.backstage.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

    //Create ordersDetails when orders insert



}
