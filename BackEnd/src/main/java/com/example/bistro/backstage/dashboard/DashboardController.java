package com.example.bistro.backstage.dashboard;

import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.orders.OrdersRepository;
import com.example.bistro.backstage.ordersDetails.OrdersDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrdersDetailsRepository ordersDetailsRepository;


}
