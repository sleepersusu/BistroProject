package com.example.bistro.backstage.orders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdersRepository ordersRepository;

//訂單邏輯
    //前台：使用者
        //使用者當購物車結完帳時候，新增一筆訂單
            public Orders insertOrders(Orders orders) {
                return ordersRepository.save(orders);
            }

        // 根據電話查詢訂單
            public Optional<Orders> findOrderByPhone(String ordersTel) {
                return ordersRepository.findByOrdersTel(ordersTel);
            }
        // 根據ordersNumber查詢訂單
            public Optional<Orders> findOrderByOrdersNumber(String ordersNumber) {
                return ordersRepository.findByOrdersNumber(ordersNumber);
            }

    //使用者看到目前自己擁有的訂單
        //使用者點擊該筆訂單ID時候，會出現訂單詳情
        //使用者刪除(取消)這筆訂單，有時間限制

    //後台：管理員
        //管理員看到目前所有的訂單
            public List<Orders> findAllOrders(){
                return ordersRepository.findAll();
            };
        //管理員點擊該筆訂單ID時候，會出現訂單詳情
            public Orders findOrderById(Integer id) {
                return ordersRepository.findById(id).orElse(null);
            };
        //管理員刪除(取消)這筆訂單，不用回傳，沒有時間限制
            public boolean deleteOrdersById(Integer id) {
                Optional<Orders> order = ordersRepository.findById(id);
                if (order.isPresent()) {
                    ordersRepository.deleteById(id);  // 刪除訂單
                    return true;  // 表示訂單刪除成功
                }
                return false;  // 如果訂單不存在，返回 false
            }




}
