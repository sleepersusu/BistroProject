# Vue.js 專案設置指南

## API 呼叫

- 使用 `this.axios` 發送api請求
- API 路徑需加上 `${import.meta.env.VITE_API}` 前綴
- @GetMapping("/api/campaign") 假設這是你後端api的路由

範例：

```javascript
try {
  const api = `${import.meta.env.VITE_API}/api/campaign`
  let res = await this.axios.get(api)
  console.log(res.data)
} catch (e) {
  console.log(e)
}
```

## 提示視窗

使用 SweetAlert2 (`window.Swal`):

> 需要使用彈窗alert提示的 我有安裝一個美化的套件 請用window.Swal來使用

```javascript
window.Swal.fire({
  toast: true,
  position: 'top-end',
  icon: 'success',
  title: 'fetch活動資料成功',
  timer: 1500,
  showConfirmButton: false,
  timerProgressBar: true,
})
```

參考：[SweetAlert2 文件](https://sweetalert2.github.io/)

## 主題配色

```scss
$theme-colors: (
        'primary': #494544, //深棕色按鈕
        'secondary': #8f7662,//點綴淺棕色
        'success': #20c997,//成功綠色
        'info': $info,
        'warning': $warning,//提示or輕度警告
        'danger': $danger, //刪除or警告
        'light': #f9f9f9, //背景色or文字
        'dark': #000, //底色or文字
) !default;
```

Bootstrap 類別範例：

- `btn btn-primary`
- `btn btn-outline-primary`
- `bg-primary`
- `text-primary`

## 專案注意事項

### 檔案結構使用規範

- **stores/**: 需要使用 Pinia 的狀態管理檔案
- **router/**: 在 index.js 中新增路由配置
- **views/**: 主要頁面（只要有新增就需要配對路由）
- **components/**: 組件開發注意事項
  - 需要熟悉 props 和 emits 才建議使用組件式開發
  - 不熟悉的建議採用單頁式開發

### 樣式編寫規範

- **禁止修改**: `all.scss` 和 `_variables.scss`
- **新增樣式**:
  - 大量樣式：在 assets/styles 中新增
  - 少量樣式：直接在 Vue 檔案中使用 `<style scoped>`
- 必須使用 `scoped` 避免影響別人的樣式

### 套件使用說明

- 在 main.js 中 import 並使用 `app.use()`
- 遇到問題可以：
  - 查看作者文件
  - 使用 ChatGPT
  - 也可以問我

## 套件安裝

在 `main.js` 中引入並註冊套件：

```javascript
import Package from 'package'
app.use(Package)
```

> 如果有不懂的，可以參考 Campaign.vue 檔案中的基本功能實作範例
