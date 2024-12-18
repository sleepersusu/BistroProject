import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('userStore', {
  state: () => ({
    apiUrl: import.meta.env.VITE_API, // 使用 VITE_API
    isLoggedIn: false, // isLoggedIn狀態
    memberprofile: {},
  }),
  actions: {
    setLoggedIn() {
      let memberobj = localStorage.getItem('memberobj')
      this.isLoggedIn = true // 設置登入
      this.memberprofile.username = JSON.parse(memberobj).username
      this.memberprofile.userAvatar = JSON.parse(memberobj).userAvatar
    },
    checkLoggedIn() {
      let memberobj = localStorage.getItem('memberobj')
      if (JSON.parse(memberobj)?.memberId) {
        this.memberprofile.username = JSON.parse(memberobj)?.username
        this.memberprofile.userAvatar = JSON.parse(memberobj)?.userAvatar
        this.isLoggedIn = true
      } else {
        this.isLoggedIn = false // 清除登入
      }
    },
    clearLoggedIn() {
      localStorage.removeItem('memberobj')
      // localStorage.clear();
      this.isLoggedIn = false
      this.memberprofile = {}
    },
    async submitLogin(event) {
      try {
        // 後端服務
        let API_URL = `${this.apiUrl}/user/login`
        let form = new FormData(event.target)
        let formData = {}
        form.forEach((value, key) => {
          formData[key] = value
        })
        let formJsonData = JSON.stringify(formData)
        const response = await axios.post(API_URL, formJsonData, {
          headers: {
            'Content-Type': 'application/json',
          },
        })

        // 假設登入成功，後端返回 token
        // 儲存 token（例如使用 localStorage）
        let userAvatar = ''
        let memberId = response.data.memberId
        let imgUrl = `${this.apiUrl}/api/member/photo/${memberId}`
        let imgData = await axios.get(imgUrl, {
          responseType: 'blob',
        })
        if (imgData.data.size === 0) {
          //判斷有無圖片，切換預設頭像
          userAvatar = '/images/avatar.jpg'
        } else {
          userAvatar = URL.createObjectURL(imgData.data)
        }

        const token = response.data.token
        let username = response.data.memberName
        let memberObj = {
          memberId: memberId,
          token: token,
          username: username,
          userAvatar: userAvatar,
        }
        localStorage.setItem('memberobj', JSON.stringify(memberObj))
        this.isLoggedIn = true // 設置登入
        this.memberprofile.username = username
        this.memberprofile.userAvatar = userAvatar
      } catch (error) {
        // 處理錯誤，設登入失敗
        console.error('登入失敗', error)
        this.clearLoggedIn()
      }
    },
    async submitRegister(event){
      try {
        let API_URL = `${this.apiUrl}/api/members/create`
        let form = new FormData(event.target)
        let formData = {}
        form.forEach((value, key) => {
          formData[key] = value
        })
        let formJsonData = JSON.stringify(formData)
        let response= await axios.post(API_URL,formJsonData,{
          headers: {
            'Content-Type': 'application/json',
          }
        })
        console.log(response.data);
        this.submitLogin
      } catch (error) {
        console.error('登入失敗', error)
      }
    },
  },
  getters: {
    memberId: () => Number(localStorage.getItem('memberId')) || null,
  },
})
