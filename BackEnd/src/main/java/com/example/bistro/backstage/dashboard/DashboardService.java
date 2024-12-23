package com.example.bistro.backstage.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);


    // 取得總會員數
    // 取得總會員數，不加入 memberStatus 的條件
    public Integer getTotalMembers() {
        String sql = "SELECT COUNT(*) FROM Members";  // 移除 WHERE 條件
        logger.debug("執行會員總數查詢: {}", sql);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        logger.debug("會員總數: {}", count);
        return count;
    }

    // 取得本月營收
    public Integer getMonthlyRevenue() {
        String sql = """
            SELECT COALESCE(SUM(ordersSumPrice), 0)
            FROM Orders
            WHERE MONTH(createdAt) = MONTH(GETDATE())
            AND YEAR(createdAt) = YEAR(GETDATE())
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 取得本月訂單數
    public Integer getMonthlyOrders() {
        String sql = """
            SELECT COUNT(*)
            FROM Orders
            WHERE MONTH(createdAt) = MONTH(GETDATE())
            AND YEAR(createdAt) = YEAR(GETDATE())
        """;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // 取得會員性別分布
//    public List<Map<String, Object>> getGenderDistribution() {
//        String sql = """
//            SELECT  CASE memberSex
//                    WHEN 0 THEN '男性'
//                    WHEN 1 THEN '女性'
//                    ELSE '其他'
//                END as name,
//                COUNT(*) as value,
//                CASE memberSex
//                    WHEN 0 THEN '#66b3ff'
//                    WHEN 1 THEN '#ff9999'
//                    ELSE '#999999'
//                END as color
//            FROM Members
//            WHERE memberSex IS NOT NULL
//            GROUP BY memberSex
//       """;
//        logger.debug("執行會員性別分布查詢: {}", sql);
//        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
//        logger.debug("查詢到的性別分布數據: {}", result);
//        return result;
//    }

    // 取得每日營收趨勢
    public Map<String, Object> getRevenueData() {
        String sql = """
            SELECT CONVERT(varchar, createdAt, 23) as orderDate,
                    SUM(ordersSumPrice) as revenue
            FROM Orders
            WHERE MONTH(createdAt) = MONTH(GETDATE())
            AND YEAR(createdAt) = YEAR(GETDATE())
            GROUP BY CONVERT(varchar, createdAt, 23)
            ORDER BY orderDate
        """;
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        logger.debug("執行 SQL 查詢: {}", sql);
        List<String> dates = new ArrayList<>();
        logger.debug("查詢到 {} 條記錄", results.size());
        List<Integer> values = new ArrayList<>();

        for (Map<String, Object> row : results) {
            try {
                String dateStr = row.get("orderDate").toString();
                // 假設格式是 YYYY-MM-DD，取最後一部分作為日期
                String day = dateStr.split("-")[2];
                dates.add(day + "日");
                values.add(((Number) row.get("revenue")).intValue());
            } catch (Exception e) {
                // 如果處理特定行時出錯，記錄錯誤但繼續處理其他行
                logger.error("處理日期數據時發生錯誤，日期數據: {}", row.get("orderDate"), e);
                continue;
            }
        }

        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("dates", dates);
        revenueData.put("values", values);
        logger.debug("已生成營收數據，日期數: {}, 金額數: {}",
                dates.size(), values.size());

        return revenueData;
    }

    // 取得熱門餐點排行
    public Map<String, Object> getTopDishes() {
        String sql = """
            SELECT TOP 5
                od.odName,
                SUM(od.odQuantity) as totalQuantity
            FROM OrderDetails od
            JOIN Orders o ON od.ordersId = o.ID
            WHERE MONTH(o.createdAt) = MONTH(GETDATE())
            AND YEAR(o.createdAt) = YEAR(GETDATE())
            GROUP BY od.odName
            ORDER BY totalQuantity DESC
        """;
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Map<String, Object> row : results) {
            names.add((String) row.get("odName"));
            values.add(((Number) row.get("totalQuantity")).intValue());
        }

        Map<String, Object> topDishes = new HashMap<>();
        topDishes.put("names", names);
        topDishes.put("values", values);

        return topDishes;
    }

//    //點餐組合分析
//    public List<Map<String, Object>> getDishCombinationAnalysis() {
//        String sql = """
//        SELECT
//            od1.od_name as dish1,
//            od2.od_name as dish2,
//            COUNT(*) as combination_count
//        FROM Order_details od1
//        JOIN Order_details od2 ON od1.orders_id = od2.orders_id
//        JOIN Orders o ON od1.orders_id = o.id
//        WHERE od1.od_name < od2.od_name
//            AND o.created_at BETWEEN
//                DATE_FORMAT(NOW(), '%Y-%m-01 00:00:00')
//                AND DATE_FORMAT(LAST_DAY(NOW()), '%Y-%m-%d 23:59:59')
//        GROUP BY od1.od_name, od2.od_name
//        HAVING combination_count > 5
//        ORDER BY combination_count DESC;
//    """;
//
//        return jdbcTemplate.queryForList(sql);
//    }
// 取得訂單外帶與內用數據分布
public List<Map<String, Object>> getOrderTypeDistribution() {
    String sql = """
        SELECT 
            seatType as name,
            COUNT(*) as value,
            CASE seatType
                WHEN '內用' THEN '#ffd666'  -- 內用顏色
                WHEN '外帶' THEN '#000' -- 外帶顏色
            END as color
        FROM Orders
        WHERE seatType IN ('內用', '外帶')  -- 只保留內用和外帶
        GROUP BY seatType
    """;

    logger.debug("執行訂單外帶與內用分布查詢: {}", sql);
    List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
    logger.debug("查詢到的訂單分布數據: {}", result);
    return result;
}


//
//    //外帶內用分析
//        public List<Map<String, Object>> getOrderTypeAnalysis() {
//        String sql = """
//            SELECT
//                seatType,
//                COUNT(*) as order_count
//            FROM Orders
//            WHERE createdAt BETWEEN DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()), 0)  -- 本月第一天
//                AND DATEADD(SECOND, -1, DATEADD(MONTH, DATEDIFF(MONTH, 0, GETDATE()) + 1, 0))  -- 本月最后一天
//            GROUP BY seatType;
//        """;
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    //會員消費分析
//    public List<Map<String, Object>> getMemberConsumptionAnalysis() {
//        String sql = """
//        SELECT
//            m.member_name,
//            COUNT(o.id) as order_count,
//            SUM(o.orders_sum_price) as total_spent,
//            AVG(o.orders_sum_price) as avg_order_value,
//            MAX(o.created_at) as last_order_date
//        FROM Members m
//        JOIN Orders o ON m.id = o.member_id
//        WHERE o.created_at BETWEEN
//            DATE_FORMAT(NOW(), '%Y-%m-01 00:00:00')
//            AND DATE_FORMAT(LAST_DAY(NOW()), '%Y-%m-%d 23:59:59')
//        GROUP BY m.id, m.member_name
//        ORDER BY total_spent DESC;
//    """;
//
//        return jdbcTemplate.queryForList(sql);
//    }
//
//    //各類別銷售分析
//    public List<Map<String, Object>> getCategorySalesAnalysis() {
//        String sql = """
//        SELECT
//            m.product_category,
//            COUNT(DISTINCT o.id) as order_count,
//            SUM(od.od_quantity) as total_quantity,
//            SUM(od.od_sum_price) as total_revenue
//        FROM Orders o
//        JOIN Order_details od ON o.id = od.orders_id
//        JOIN Menu m ON od.menu_id = m.id
//        WHERE o.created_at BETWEEN
//            DATE_FORMAT(NOW(), '%Y-%m-01 00:00:00')
//            AND DATE_FORMAT(LAST_DAY(NOW()), '%Y-%m-%d 23:59:59')
//        GROUP BY m.product_category
//        ORDER BY total_revenue DESC;
//    """;
//
//        return jdbcTemplate.queryForList(sql);
//    }








    // 取得所有儀表板數據
    public Map<String, Object> getDashboardData() {
        logger.info("開始獲取儀表板數據");
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("totalMembers", getTotalMembers());
            data.put("monthlyRevenue", getMonthlyRevenue());
            data.put("monthlyOrders", getMonthlyOrders());
            data.put("orderTypeDistribution", getOrderTypeDistribution());
            data.put("revenueData", getRevenueData());
            data.put("topDishes", getTopDishes());
//            data.put("memberConsumption", getMemberConsumptionAnalysis());
//            data.put("orderTypeAnalysis", getOrderTypeAnalysis());
//            data.put("dishCombination", getDishCombinationAnalysis());
//            data.put("categorySales", getCategorySalesAnalysis());
//            data.put("dailyRevenueTrend", getDailyRevenueTrend());
            logger.info("儀表板數據獲取成功");
        } catch (Exception e) {
            logger.error("獲取儀表板數據時發生錯誤", e);
            // 返回空的 Map 而不是 null
            return new HashMap<>();
        }
        return data;
    }
}