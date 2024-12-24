// src/services/paypalService.js
import axios from 'axios';

export const paypalService = {
  // 創建PayPal支付
  async createPayment(orderData) {
    try {
      const response = await axios.post(`${import.meta.env.VITE_API}/payment/create`, {
        amount: orderData.ordersSumPrice,
        currency: 'TWD',
        description: `訂單編號: ${orderData.ordersNumber}`
      });

      if (response.data.approvalUrl) {
        return response.data;
      } else {
        throw new Error('無法獲取PayPal付款連結');
      }
    } catch (error) {
      console.error('建立PayPal支付失敗:', error);
      throw error;
    }
  },

  // 驗證PayPal支付狀態
  async verifyPayment(paymentId, PayerID) {
    try {
      const response = await axios.get(`${import.meta.env.VITE_API}/payment/success`, {
        params: { paymentId, PayerID }
      });
      return response.data;
    } catch (error) {
      console.error('驗證PayPal支付失敗:', error);
      throw error;
    }
  }
};
