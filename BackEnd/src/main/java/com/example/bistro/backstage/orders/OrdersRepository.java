package com.example.bistro.backstage.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

    Optional<Orders> findByOrdersTel(String ordersTel);  // 根據電話查詢訂單

    Optional<Orders> findByOrdersNumber(String ordersNumber);

    List<Orders> findByMembersId(Integer memberId);

    // 指定時間範圍的訂單數
        Integer countByCreatedAtBetween(Date startDate, Date endDate);

    // 指定時間範圍的總營收
        @Query("SELECT SUM(o.ordersSumPrice) FROM Orders o WHERE o.createdAt BETWEEN :startDate AND :endDate")
        Integer sumOrdersSumPriceByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    // 每日營收趨勢

    // 每日營收趨勢 - 使用 MSSQL 原生 SQL
    @Query(value = """
        SELECT CONVERT(date, created_at) as order_date,
                COUNT(*) as order_count,
                SUM(orders_sum_price) as daily_revenue
        FROM Orders
        WHERE created_at BETWEEN :startDate AND :endDate
        GROUP BY CONVERT(date, created_at)
        ORDER BY order_date
        """, nativeQuery = true)
    List<Object[]> findDailyRevenue(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // 內用外帶分析
    @Query("SELECT o.seatType, COUNT(o), SUM(o.ordersSumPrice), AVG(o.ordersSumPrice) " +
            "FROM Orders o " +
            "WHERE o.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY o.seatType")
    List<Object[]> analyzeSeatTypes(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // 各時段銷售分析 - 使用 MSSQL 原生 SQL
    @Query(value = """
        SELECT 
            DATEPART(HOUR, created_at) as hour_of_day,
            COUNT(*) as order_count,
            SUM(orders_sum_price) as total_revenue
        FROM Orders
        WHERE created_at BETWEEN :startDate AND :endDate
        GROUP BY DATEPART(HOUR, created_at)
        ORDER BY hour_of_day
        """, nativeQuery = true)
    List<Object[]> findSalesByHour(@Param("startDate") Date startDate, @Param("endDate") Date endDate);


    // 使用 Orders 物件查詢特定時間範圍的訂單
    @Query("SELECT o FROM Orders o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt")
    List<Orders> findOrdersByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    // 依座位類型查詢訂單
    List<Orders> findBySeatTypeAndCreatedAtBetween(String seatType, Date startDate, Date endDate);

    // 查詢特定時間範圍內的所有訂單，按照創建時間排序
    List<Orders> findByCreatedAtBetweenOrderByCreatedAtDesc(Date startDate, Date endDate);

}
