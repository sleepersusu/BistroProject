import json
import sys
from recommender import get_recommendations_for_user

def print_json(data):
    print(json.dumps(data, ensure_ascii=False))

def main():
    if len(sys.argv) < 2:
        print_json({"error": "Missing user ID"})
        sys.exit(1)

    user_id = int(sys.argv[1])

    try:
        sys.stderr.write(f"正在生成用戶 {user_id} 的推薦...\n")

        recommendations = get_recommendations_for_user(user_id)

        if recommendations is not None and not recommendations.empty:
            result = recommendations.to_dict('records')
            print_json({"recommendations": result})
            sys.stderr.write("生成推薦成功。\n")
        else:
            print_json({"recommendations": []})
            sys.stderr.write("沒有找到推薦。\n")

    except ValueError as e:
        sys.stderr.write(f"無效的用戶 ID: {user_id}\n")
        print_json({"error": "Invalid user ID"})
        sys.exit(1)

    except Exception as e:
        sys.stderr.write(f"生成推薦時發生錯誤: {str(e)}\n")
        print_json({"error": "Failed to generate recommendations"})
        sys.exit(1)

if __name__ == "__main__":
    main()