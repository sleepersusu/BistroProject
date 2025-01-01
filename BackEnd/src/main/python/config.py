# Database configuration
DB_CONFIG = {
    'server': 'localhost',  # 替換成你的數據庫服務器
    'database': 'BistroDB5',   # 替換成你的數據庫名稱
    'username': 'watcher',       # 替換成你的用戶名
    'password': 'P@ssw0rd'  # 替換成你的密碼
}

# 推薦系統配置
RECOMMENDATION_CONFIG = {
    'min_rating': 0,           # 最低評分閾值
    'max_recommendations': 5,  # 每個用戶推薦數量
    'time_decay_factor': 90    # 時間衰減因子（天）
}