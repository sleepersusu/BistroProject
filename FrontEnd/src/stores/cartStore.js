import { defineStore } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'
import axios from 'axios'

axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true

const user = useUserStore()

export const cartStore = defineStore('cart', {
  state: () => ({
    cartItems: [],
    totalAmount:0,
  }),
  getters: {
    // 購物車總金額
    totalAmount(state) {
      return state.cartItems.reduce(
        (sum, item) => sum + item.cartCount * item.menu.productPrice,
        0
      ).toFixed(2);
    },
  },
  actions: {
  //還沒進購物車畫面，在商品畫面

    //加入商品並傳給購物車
      async addToCart(menuItem) {
        if (!user.memberId) {
          //如果不存在用localstorage
        } else {
          try {
            const res = await axios.post(`cart/add/${user.memberId}/${menuItem.id}/${menuItem.count}`)
            console.log(res.data)
          } catch (e) {
            console.error(e)
          }
        }
      },
    //進去購物車畫面
    //找到購物車清單
      async getCart() {
        if (!user.memberId) {
          return null // 確保返回值
        } else {
          try {
            const url = `cart/list`
            const res = await axios.get(url)
            console.log(res.data) // 打印完整的返回數據
            return res // 返回整個響應
          } catch (e) {
            console.error(e)
            return null
          }
        }
      },
    //++購物車加一，沒有產品的話就幫他加一
      async CountCart(menuItem) {
        if (!user.memberId) {
          return null // 確保返回值
        } else {
          try {
            const url = `cart/count/${menuItem.id}`
            const res = await axios.post(url)
            console.log(res.data) // 打印完整的返回數據
            return await this.getCart()
          } catch (e) {
            console.error(e)
            return null
          }
        }
      },
    //--購物車減一，剩餘1再扣1直接刪除
      async MinusCart(menuItem) {
        if (!user.memberId) {
          return null // 確保返回值
        } else {
          try {
            const url = `cart/minusCart/${menuItem.id}`
            const res = await axios.post(url)
            console.log(res.data) // 打印完整的返回數據
            // 重新獲取購物車列表
            return await this.getCart()
          } catch (e) {
            console.error(e)
            return null
          }
        }
      },
    //刪除此商品
      removeItem(itemId) {
        this.cartItems = this.cartItems.filter(item => item.cartId !== itemId);
      },
  },
})
