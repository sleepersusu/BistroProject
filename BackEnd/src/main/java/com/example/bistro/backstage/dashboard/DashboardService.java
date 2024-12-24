package com.example.bistro.backstage.dashboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    // 取得總會員數
    public Integer getTotalMembers() {
        String sql = "SELECT COUNT(*) FROM Members";
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

    // 取得訂單外帶與內用數據分布
    public List<Map<String, Object>> getOrderTypeDistribution() {
        String sql = """
            SELECT 
                seatType as name,
                COUNT(*) as value,
                CASE seatType
                    WHEN '內用' THEN '#ffd666'
                    WHEN '外帶' THEN '#000'
                END as color
            FROM Orders
            WHERE seatType IN ('內用', '外帶')
            GROUP BY seatType
        """;

        logger.debug("執行訂單外帶與內用分布查詢: {}", sql);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
        logger.debug("查詢到的訂單分布數據: {}", result);
        return result;
    }

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
        List<Integer> values = new ArrayList<>();

        for (Map<String, Object> row : results) {
            try {
                String dateStr = row.get("orderDate").toString();
                String day = dateStr.split("-")[2];
                dates.add(day + "日");
                values.add(((Number) row.get("revenue")).intValue());
            } catch (Exception e) {
                logger.error("處理日期數據時發生錯誤，日期數據: {}", row.get("orderDate"), e);
                continue;
            }
        }

        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("dates", dates);
        revenueData.put("values", values);
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

    // 取得分類營收數據
    public Map<String, Object> getCategoryRevenue() {
        String sql = """
            SELECT 
                m.productCategory as category,
                SUM(od.odSumPrice) as revenue
            FROM OrderDetails od
            JOIN Menu m ON od.menuId = m.ID
            JOIN Orders o ON od.ordersId = o.ID
            WHERE MONTH(o.createdAt) = MONTH(GETDATE())
            AND YEAR(o.createdAt) = YEAR(GETDATE())
            GROUP BY m.productCategory
            ORDER BY revenue DESC
        """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        List<String> categories = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Map<String, Object> row : results) {
            categories.add((String) row.get("category"));
            values.add(((Number) row.get("revenue")).intValue());
        }

        Map<String, Object> categoryRevenue = new HashMap<>();
        categoryRevenue.put("categories", categories);
        categoryRevenue.put("values", values);

        return categoryRevenue;
    }

    // 取得商品營收流向數據
    public Map<String, Object> getProductRevenueFlow() {
        String sql = """
            SELECT 
                m.productCategory as source,
                m.productName as target,
                SUM(od.odSumPrice) as value
            FROM OrderDetails od
            JOIN Menu m ON od.menuId = m.ID
            JOIN Orders o ON od.ordersId = o.ID
            WHERE MONTH(o.createdAt) = MONTH(GETDATE())
            AND YEAR(o.createdAt) = YEAR(GETDATE())
            GROUP BY m.productCategory, m.productName
            ORDER BY m.productCategory, value DESC
        """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> links = new ArrayList<>();
        Set<String> uniqueCategories = new HashSet<>();
        Set<String> uniqueProducts = new HashSet<>();

        // 先收集所有唯一的類別和產品
        for (Map<String, Object> row : results) {
            String category = (String) row.get("source");
            String product = (String) row.get("target");
            uniqueCategories.add(category);
            uniqueProducts.add(product);
        }

        // 建立節點
        int index = 0;
        Map<String, Integer> nodeIndices = new HashMap<>();

        // 添加類別節點
        for (String category : uniqueCategories) {
            Map<String, Object> node = new HashMap<>();
            node.put("name", category);
            nodes.add(node);
            nodeIndices.put(category, index++);
        }

        // 添加產品節點
        for (String product : uniqueProducts) {
            Map<String, Object> node = new HashMap<>();
            node.put("name", product);
            nodes.add(node);
            nodeIndices.put(product, index++);
        }

        // 建立連結
        for (Map<String, Object> row : results) {
            Map<String, Object> link = new HashMap<>();
            link.put("source", nodeIndices.get(row.get("source")));
            link.put("target", nodeIndices.get(row.get("target")));
            link.put("value", ((Number) row.get("value")).intValue());
            links.add(link);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nodes", nodes);
        result.put("links", links);

        return result;
    }

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
            data.put("categoryRevenue", getCategoryRevenue());
            data.put("productRevenueFlow", getProductRevenueFlow());
            logger.info("儀表板數據獲取成功");
        } catch (Exception e) {
            logger.error("獲取儀表板數據時發生錯誤", e);
            return new HashMap<>();
        }
        return data;
    }
}