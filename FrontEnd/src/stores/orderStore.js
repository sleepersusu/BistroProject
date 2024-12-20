import { defineStore } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'
import axios from 'axios'

axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true

const user = useUserStore()

export const orderStore=defineStore('order',{
  state:()=>({
    orderItems:[],
    orderDetails: {},  // 存儲所有訂單詳情
  }),
  getters:{
    // 可以添加需要的 getters
    hasOrders: (state) => state.orderItems.length > 0,
    getOrderDetailById: (state) => (ordersNumber) => state.orderDetails[ordersNumber],

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
        const response = await axios.get(url)
        const filteredData = {
          ordersName: response.data.ordersName || '',
          ordersTel: response.data.ordersTel || '',
          seatType: response.data.seatType || '',
          ordersSumPrice: response.data.ordersSumPrice || 0,
          pointGetted: response.data.pointGetted || 0,
          ordersRequest: response.data.ordersRequest || '',
          ordersDetails: response.data.ordersDetails || [],
          payment: response.data.payment || []
        }
        this.orderDetails[ordersNumber] = filteredData
        return filteredData
      }
      catch (e) {
        console.error('獲取訂單詳情失敗:', e)
        // 移除任何過期數據
        this.clearOrderDetail(ordersNumber);
        throw e;
      }
    },
    //清理訂單詳情緩存
    clearOrderDetail(ordersNumber) {
      delete this.orderDetails[ordersNumber]
    },
    //清理所有訂單詳情
    clearAllOrderDetails() {
      this.orderDetails = {}
    }

  }

})
