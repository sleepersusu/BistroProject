package com.example.bistro.frontstage.orders;

import com.example.bistro.backstage.orders.Orders;
import com.example.bistro.backstage.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrdersRestController {

    @Autowired
    private OrdersFrontService ordersFrontService;

    @Autowired
    private OrdersService ordersService;

    //新增訂單，來自DTO的資料轉換存到資料庫
        @PostMapping("/create")
        public ResponseEntity<Orders> createOrder(@RequestBody OrdersDTO ordersRequestDTO) {
            Orders newOrder = ordersFrontService.createOrder(ordersRequestDTO);
            return ResponseEntity.ok(newOrder);
        }

    // 查詢所有訂單
        @GetMapping("/list")
        public ResponseEntity<List<Orders>> findAllOrders() {
            List<Orders> orders = ordersService.findAllOrders();
            return ResponseEntity.ok(orders);
        }
    //查看單筆訂單
        @GetMapping("/{id}")
        public ResponseEntity<Orders> findOrderById(@PathVariable Integer id) {
            Optional<Orders> order = Optional.ofNullable(ordersService.findOrderById(id));
            return order.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }
    //刪除訂單
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
            if (!ordersService.deleteOrdersById(id)) {          // 檢查訂單是否存在，如果不存在，返回 404
                return ResponseEntity.notFound().build();
            }
            ordersService.deleteOrdersById(id);                 // 執行刪除操作
            return ResponseEntity.noContent().build();          // 返回 204 No Content，表示成功刪除
        }


}
