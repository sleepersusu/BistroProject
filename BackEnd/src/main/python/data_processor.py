from config import DB_CONFIG
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
import pymssql
import sys

class DataProcessor:
    def __init__(self):
        self.conn = pymssql.connect(
            server=DB_CONFIG['server'],
            database=DB_CONFIG['database'],
            user=DB_CONFIG['username'],
            password=DB_CONFIG['password']
        )

    def extract_and_process_data(self):
        """提取和處理數據"""
        query = """
        SELECT 
            o.memberId,
            od.menuId,
            m.productName,
            m.productCategory,
            od.odQuantity,
            c.commentRating,
            o.createdAt
        FROM Orders o
        JOIN OrderDetails od ON o.ID = od.ordersId
        JOIN Menu m ON od.menuId = m.ID
        LEFT JOIN Comment c ON m.ID = c.menuId AND o.memberId = c.memberId
        WHERE o.memberId IS NOT NULL
        """

        try:
            # 讀取數據
            df = pd.read_sql(query, self.conn)
            sys.stderr.write(f"成功讀取數據，總筆數: {len(df)}")

            # 基本數據清理
            df['commentRating'] = df['commentRating'].fillna(0)

            # 添加時間權重（最近3個月的訂單權重更高）
            df['days_since_order'] = (pd.Timestamp.now() - pd.to_datetime(df['createdAt'])).dt.days
            df['time_weight'] = np.exp(-df['days_since_order'] / 90)  # 90天的衰減因子

            # 標準化數量和評分
            scaler = MinMaxScaler()
            df['normalized_quantity'] = scaler.fit_transform(df[['odQuantity']])
            df['normalized_rating'] = scaler.fit_transform(df[['commentRating']])

            # 計算綜合分數
            df['score'] = (df['normalized_quantity'] * 0.7 +
                           df['normalized_rating'] * 0.3) * df['time_weight']

            # 創建用戶-商品矩陣
            user_item_matrix = df.pivot_table(
                index='memberId',
                columns='menuId',
                values='score',
                aggfunc='sum'
            ).fillna(0)

            return df, user_item_matrix

        except Exception as e:
            print(f"處理數據時發生錯誤: {str(e)}")
            return None, None

    def analyze_user_preferences(self, df):
        """分析用戶偏好"""
        try:
            # 計算每個類別的統計
            category_preferences = df.groupby('productCategory').agg({
                'commentRating': 'mean',
                'odQuantity': 'sum',
                'score': 'mean'
            }).round(2)

            # 計算熱門商品
            popular_items = df.groupby(['productName']).agg({
                'odQuantity': 'sum',
                'commentRating': 'mean',
                'score': 'mean'
            }).sort_values('score', ascending=False)

            return category_preferences, popular_items

        except Exception as e:
            print(f"分析偏好時發生錯誤: {str(e)}")
            return None, None

    def close(self):
        """關閉數據庫連接"""
        self.conn.close()

# if __name__ == "__main__":
#     processor = DataProcessor()
#
#     try:
#         # 獲取和處理數據
#         df, user_item_matrix = processor.extract_and_process_data()
#
#         if df is not None:
#             # 分析用戶偏好
#             category_prefs, popular_items = processor.analyze_user_preferences(df)
#
#             if category_prefs is not None:
#                 print("\n類別偏好分析:")
#                 print(category_prefs)
#                 print("\n熱門商品 Top 5:")
#                 print(popular_items.head())
#
#     finally:
#         processor.close()