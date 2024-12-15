import { defineStore } from 'pinia'

export const useUserStore = defineStore('userStore', {
    state: () => ({
      apiUrl: import.meta.env.VITE_API,  // 使用 VITE_API 环境变量
      userInfo: null,
    }),
    actions: {
      getApiUrl() {
        return this.apiUrl;
      },
      setUserInfo(user) {
      this.userInfo = user;
    },
    clearUserInfo() {
      this.userInfo = null;
    }
    },getters: {
      isLoggedIn: (state) => state.userInfo !== null,
    }
  });