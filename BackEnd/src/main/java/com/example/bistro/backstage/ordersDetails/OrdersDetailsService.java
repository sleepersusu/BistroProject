package com.example.bistro.backstage.ordersDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrdersDetailsService {
    @Autowired
    OrdersDetailsRepository ordersDetailsRepository;

//訂單詳情邏輯
    //前台：使用者
        //使用者當購物車結完帳時候，新增一筆訂單，同時新增訂單詳情
            public OrdersDetails insertOrdersDetails(OrdersDetails ordersDetails) {
                return ordersDetailsRepository.save(ordersDetails);
            }
        //使用者看到目前自己擁有的訂單詳情
        //使用者不能刪除

    //後台：管理員
        //管理員看到目前所有的訂單
            public List<OrdersDetails> findAllOrdersDetails(){
                return ordersDetailsRepository.findAll();
            };
        //管理員點擊該筆訂單ID時候，會出現訂單詳情
            public OrdersDetails findOrdersDetailsById(Integer id) {
                return ordersDetailsRepository.findById(id).orElse(null);
            };
        //管理員刪除(取消)這筆訂單，不用回傳，沒有時間限制
            //已經在orders中加入cascade

    // Add this method for pagination
    public Page<OrdersDetails> findOrdersWithPagination(String search, Pageable pageable) {
        if (search != null && !search.trim().isEmpty()) {
            return ordersDetailsRepository.findByOdNameContainingOrOrders_OrdersNumberContaining(
                    search, search, pageable);
        }
        return ordersDetailsRepository.findAll(pageable);
    }
            
            
            
            
}
