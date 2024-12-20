import { defineStore } from 'pinia';
import { notify } from '@kyvg/vue3-notification';

export const useNotificationStore = defineStore('notificationStore', {
  state: () => ({
    notifications: [],
  }),
  actions: {
    showNotification({ title, text, type = 'info', duration = 3000 }) {
      notify({
        title,
        text,
        type,
        duration,
        speed: 300, // 動畫速度
        ignoreDuplicates: true, // 忽略重複的通知
        closeOnClick: true, // 點擊關閉
        group: 'cart',        // 分組
        classes: 'custom-notification'  // 添加這行

      })
    },

    //成功
    success(message) {
      this.showNotification({
        title: '成功',
        text: message,
        type: 'success',
      })
    },

    //錯誤
    error(message) {
      this.showNotification({
        title: '错误',
        text: message,
        type: 'error',
      })
    },

    //警告
    warn(message) {
      this.showNotification({
        title: '警告',
        text: message,
        type: 'warn',
      })
    },

    //通知
    info(message) {
      this.showNotification({
        title: '信息',
        text: message,
        type: 'info',
      })
    },
  },
})
