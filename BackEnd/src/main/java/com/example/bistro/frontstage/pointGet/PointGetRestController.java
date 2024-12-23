package com.example.bistro.frontstage.pointGet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.bistro.backstage.members.Members;
import com.example.bistro.backstage.members.MembersRepository;
import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersRepository;
import com.example.bistro.backstage.pointsGet.PointsGetBean;
import com.example.bistro.backstage.pointsGet.PointsGetRepository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class PointGetRestController {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MembersRepository membersRepository;

    @Autowired
    private PointsGetRepository pointsGetRepository;

    @PostMapping("/api/createPointGet")
    public ResponseEntity<?> createPoints(@RequestBody PointGetDTO request) {

        Orders order = ordersRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));

        Members member = membersRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + request.getMemberId()));

        PointsGetBean pointsGet = new PointsGetBean();
        pointsGet.setOrders(order);           
        pointsGet.setMembers(member);         
        pointsGet.setPointGetted(request.getPointGetted());

        pointsGetRepository.save(pointsGet);

        return ResponseEntity.ok("Points record created successfully! Points: " + request.getPointGetted());
    }

    @PostMapping("/api/CreatePointGet")
    public String PointGet(int memberId, int pointGetted) {
    	
    	
    	return null;
    }
}