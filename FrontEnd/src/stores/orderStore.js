import { defineStore } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'
import axios from 'axios'

axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true

const user = useUserStore()

export const orderStore=defineStore('order',{
  state:()=>({
    orderItems:[],
    currentOrderDetail: null
  }),
  getters:{

  },
  actions:{
    async getOrder() {
      if (!user.memberId) {
        return null // 確保返回值
      } else {
        try {
          const url = `api/orders/list/member`
          const res = await axios.get(url)
          this.orderItems = res.data;
          console.log(res.data) // 打印完整的返回數據
          return res // 返回整個響應
        }
        catch (e) {
          console.error(e)
          return null
        }
      }
    },
    async getOrderDetail(ordersNumber) {
      try {
        const url = `api/orders/detailList/${ordersNumber}`
        const res = await axios.get(url)
        this.currentOrderDetail = res.data
        return res.data
      }
      catch (e) {
        console.error('獲取訂單詳情失敗:', e)
        throw e
      }
    },

  }

})
