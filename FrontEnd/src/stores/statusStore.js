import { defineStore } from 'pinia'

export const statusStore = defineStore('status', {
  state: () => ({
    loadingCount: 0,
    isLoading: false,
  }),
  actions: {
    start() {
      this.loadingCount++
      this.isLoading = true
    },
    finish() {
      this.loadingCount--
      if (this.loadingCount <= 0) {
        this.loadingCount = 0
        this.isLoading = false
      }
    },
  },
})
