import { defineStore } from 'pinia'

export const useUserStore = defineStore('userStore', {
  state: () => ({
    apiUrl: import.meta.env.VITE_API, // 使用 VITE_API
    isLoggedIn: false,  // isLoggedIn狀態
  }),
  actions: {
    getApiUrl() {
      return this.apiUrl;
    },
    setLoggedIn() {
      this.isLoggedIn = true; // 設置登入
    },
    clearLoggedIn() {
      this.isLoggedIn = false; // 清除登入
    },
  },
})
