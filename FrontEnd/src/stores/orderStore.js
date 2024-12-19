import { defineStore } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'
import axios from 'axios'

axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true

const user = useUserStore()

export const orderStore=defineStore('order',{
  state:()=>({
    orderItems:[],
  }),
  getters:{

  },
  actions:{

  }

})
