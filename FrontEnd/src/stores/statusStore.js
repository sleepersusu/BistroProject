import { defineStore } from 'pinia'

export const statusStore = defineStore('status', {
  state: () => ({
    isLoading: false,
  }),
})
