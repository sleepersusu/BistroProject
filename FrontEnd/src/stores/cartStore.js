import { defineStore } from 'pinia'
import { useUserStore } from '@/stores/userStore.js'
import axios from 'axios'
axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true

const user = useUserStore();


export const cartStore = defineStore("cart",{
  state:()=>({

  }),
  actions:{
//還沒進購物車畫面，在商品畫面
  //加入商品並傳給購物車
    async addToCart(menuItem){
      if (!user.memberId){
        //如果不存在用localstorage
      }else{
        try {
          const res = await axios.post(`cart/add/${user.memberId}/${menuItem.id}/${menuItem.count}`)
          console.log(res.data)
        }catch (e){
          console.error(e)
        }
      }
    },
//進去購物車畫面
    //找到購物車清單
    async getCart(){
      if (!user.memberId) {
        return null; // 確保返回值
      } else {
        try {
          const url = `cart/list`;
          const res = await axios.get(url);
          console.log(res.data); // 打印完整的返回數據
          return res; // 返回整個響應
        } catch (e) {
          console.error(e);
          return null;
        }
      }
    }
  }
})
