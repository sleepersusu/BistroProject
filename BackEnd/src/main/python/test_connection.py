from config import DB_CONFIG
import pymssql

def test_connection():
    try:
        conn = pymssql.connect(
            server=DB_CONFIG['server'],
            database=DB_CONFIG['database'],
            user=DB_CONFIG['username'],
            password=DB_CONFIG['password']
        )
        print("數據庫連接成功!")

        # 測試查詢
        cursor = conn.cursor()
        cursor.execute('SELECT TOP 1 * FROM Orders')
        result = cursor.fetchone()
        print("查詢測試成功，第一筆訂單:", result)

        conn.close()

    except Exception as e:
        print(f"連接失敗: {str(e)}")

if __name__ == "__main__":
    test_connection()