import pandas as pd
import numpy as np
import sys
from sklearn.metrics.pairwise import cosine_similarity
from data_processor import DataProcessor

class MenuRecommender:
    def __init__(self, user_item_matrix, item_data):
        self.user_item_matrix = user_item_matrix
        self.item_data = item_data
        self.user_similarity = None
        self.calculate_user_similarity()

    def calculate_user_similarity(self):
        """計算用戶相似度矩陣"""
        try:
            self.user_similarity = pd.DataFrame(
                cosine_similarity(self.user_item_matrix),
                index=self.user_item_matrix.index,
                columns=self.user_item_matrix.index
            )
            sys.stderr.write("成功計算用戶相似度矩陣")
        except Exception as e:
            sys.stderr.write(f"計算用戶相似度時發生錯誤: {str(e)}")

    def get_similar_users(self, user_id, n=5):
        """獲取最相似的用戶"""
        if user_id not in self.user_similarity.index:
            print(f"找不到用戶 {user_id}")
            return None

        similar_users = self.user_similarity[user_id].sort_values(ascending=False)[1:n+1]
        return similar_users

    def get_recommendations(self, user_id, n_recommendations=5):
        """獲取推薦商品"""
        try:
            # 檢查是否為新用戶
            if user_id not in self.user_item_matrix.index:
                print(f"用戶 {user_id} 是新用戶，返回熱門推薦")
                return self.get_popular_recommendations(n_recommendations)

            # 獲取相似用戶
            similar_users = self.get_similar_users(user_id)
            if similar_users is None:
                return self.get_popular_recommendations(n_recommendations)

            # 獲取用戶已購買的商品
            user_items = self.user_item_matrix.columns[self.user_item_matrix.loc[user_id] > 0]

            # 獲取相似用戶喜歡的商品
            similar_user_items = self.user_item_matrix.loc[similar_users.index]

            # 計算推薦分數
            recommendations = similar_user_items.mean()

            # 排除用戶已購買的商品
            recommendations = recommendations[~recommendations.index.isin(user_items)]

            # 獲取推薦商品的詳細信息
            top_recommendations = recommendations.sort_values(ascending=False).head(n_recommendations)

            recommended_items = self.item_data[
                self.item_data['menuId'].isin(top_recommendations.index)
            ].drop_duplicates('menuId')

            return recommended_items[['productName', 'productCategory', 'score']]

        except Exception as e:
            print(f"生成推薦時發生錯誤: {str(e)}")
            return None

    def get_popular_recommendations(self, n=5):
        """獲取熱門推薦（用於新用戶）"""
        try:
            popular_items = self.item_data.groupby('menuId').agg({
                'score': 'mean',
                'productName': 'first',
                'productCategory': 'first'
            }).sort_values('score', ascending=False)

            return popular_items[['productName', 'productCategory', 'score']].head(n)

        except Exception as e:
            print(f"獲取熱門推薦時發生錯誤: {str(e)}")
            return None

def get_recommendations_for_user(user_id):
    """主要推薦函數"""
    processor = DataProcessor()

    try:
        # 獲取數據
        df, user_item_matrix = processor.extract_and_process_data()

        if df is not None and user_item_matrix is not None and len(df) > 0:
            # 檢查用戶是否有歷史記錄
            user_history = df[df['memberId'] == user_id]

            if len(user_history) > 0:
                # 創建推薦器實例
                recommender = MenuRecommender(user_item_matrix, df)
                recommendations = recommender.get_recommendations(user_id)
                sys.stderr.write(f"為用戶 {user_id} 生成個人化推薦")
                return recommendations
            else:
                # 如果用戶沒有歷史記錄，返回熱門商品
                sys.stderr.write(f"用戶 {user_id} 沒有歷史記錄，返回熱門推薦")
                recommender = MenuRecommender(user_item_matrix, df)
                return recommender.get_popular_recommendations()
        else:
            sys.stderr.write("沒有足夠的數據生成推薦")
            return None

    finally:
        processor.close()
# if __name__ == "__main__":
#     # 測試代碼
#     test_user_id = 5  # 替換為實際的用戶ID
#     recommendations = get_recommendations_for_user(test_user_id)
#
#     if recommendations is not None:
#         print(f"\n用戶 {test_user_id} 的推薦商品:")
#         print(recommendations)
#     else:
#         print("無法生成推薦")