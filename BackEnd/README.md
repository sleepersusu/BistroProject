# Spring Boot 前台開發指南

> 重要提示：所有前台 API 路徑建議使用 `/api` 前綴，例如 `/api/campaign`、`/api/products`

## 基本規範

- 所有開發都在 `java.com.example.bistro.frontstage` 下建立自己的資料夾
- 禁止修改其他人的檔案！

## 資源使用說明

可以使用的後台資源：

- Bean
- Repository

必須自己重寫的部分：

- Service
- Controller

## API 開發規則

### RESTful API 設計

必須按照以下方式撰寫：

- 查詢資料：GET
- 新增資料：POST
- 更新資料：PUT
- 刪除資料：DELETE

### 回傳格式

- 必須回傳 JSON 格式
- 建議使用 ResponseEntity 回傳
  - 可以傳送狀態碼
  - 方便前端處理資料

### 範例

```java
@GetMapping("/api/campaign")  // 取得活動列表
@PostMapping("/api/campaign")   // 新增活動
@PutMapping("/api/campaign/{id}")  // 更新活動資訊
@DeleteMapping("/api/campaign/{id}")  // 刪除活動資訊
```

### 實用提示

- Service 層處理商業邏輯，Controller 只負責接收請求和回傳結果
- 善用 ResponseEntity 的狀態碼：
  - 200：成功
  - 201：成功新增
  - 400：請求錯誤
  - 404：找不到資源
  - 500：伺服器錯誤

> 有問題可以問我或其他組員！

# 額外補充(這只是建議的命名規則 沒有一定! 請大家自己斟酌)

## RESTful API 命名規則

## ❌ 不好的命名方式（使用動詞）

```
/getProduct
/createOrder
/deleteUser
/updateProfile
```

## ✅ 正確的命名方式（使用名詞）

```
GET    /api/products/{id}     // 取得商品
POST   /api/orders           // 建立訂單
DELETE /api/users/{id}       // 刪除用戶
PUT    /api/profiles/{id}    // 更新檔案
```

## 為什麼要這樣設計？

- HTTP 方法(GET, POST, PUT, DELETE)已經代表了動作
- 使用名詞可以讓 API 更直觀地表示資源
- 符合 RESTful 設計原則

## 實際例子

訂單相關的 API：

```
GET    /api/orders           // 取得訂單列表
GET    /api/orders/{id}      // 取得特定訂單
POST   /api/orders           // 建立新訂單
PUT    /api/orders/{id}      // 更新訂單
DELETE /api/orders/{id}      // 刪除訂單
```

## 巢狀資源的例子

```
GET    /api/users/{id}/orders           // 取得特定用戶的訂單
POST   /api/products/{id}/reviews       // 新增商品評論
GET    /api/orders/{id}/order-items     // 取得訂單項目
```

> 記住：URL 是用來指定資源，HTTP 方法是用來指定動作，所以不要再用傳統 controller 的命名方式了!

> 未來大家找後端工作時，這個很重要 !!

> 注意：以上只是範例，實際開發時請根據自己負責的功能來設計 API
