import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('userStore', {
  state: () => ({
    apiUrl: import.meta.env.VITE_API, // 使用 VITE_API
    isLoggedIn: false, // isLoggedIn狀態
    memberprofile: {
      userName: '',
      userEmail: '',
      userPhone: '',
      userAvatar: '', // 默认值为空字符串，或者给一个默认头像
      userGender: null,
      userAddress: '', // 默认值为空字符串
      userBirthdate: '',
    },
  }),
  getters: {
    // 取得會員資料
    getProfile: (state) => state.memberprofile,

    // 檢查是否登入
    checkLogin: (state) => state.isLoggedIn,

    memberId: () => Number(JSON.parse(localStorage.getItem('memberobj'))?.memberId) || null,
  },
  actions: {
    setLoggedIn() {
      let memberobj = localStorage.getItem('memberobj')
      this.isLoggedIn = true // 設置登入
      this.memberprofile.userName = JSON.parse(memberobj).userName
      this.memberprofile.userAvatar = JSON.parse(memberobj).userAvatar
    },
    checkLoggedIn() {
      let memberobj = localStorage.getItem('memberobj')
      if (JSON.parse(memberobj)?.memberId) {
        this.memberprofile.userName = JSON.parse(memberobj)?.userName
        this.memberprofile.userAvatar = JSON.parse(memberobj)?.userAvatar
        this.isLoggedIn = true
      } else {
        this.isLoggedIn = false // 清除登入
      }
    },
    loadMemberData(memberId) {//會員資料頁面撈資訊
      try {
        let response = axios.get(`${this.apiUrl}/api/members/${memberId}`)
        .then((response) => {
          this.userEmail=response.data.memberEmail
          this.userPhone=response.data.memberPhone
          this.userGender=response.data.memberSex
          this.userAddress=response.data.memberAddress
          this.userBirthdate=response.data.memberBirthday
          console.log(this.userBirthdate)
          console.log(response.data.memberAccount)
          console.log(response.data.memberFavor)
          console.log(response.data.memberImg)
          console.log(response.data.memberPassword)
          console.log(response.data.memberPoint)
          console.log(response.data.memberShip)
          console.log(response.data.memberStatus)
        })

        return response.data
      } catch (error) {}
    },
    clearLoggedIn() {
      localStorage.removeItem('memberobj')
      this.isLoggedIn = false
      this.memberprofile = {}
    },
    async handleGoogleLogin(idToken) {
      try {
        let response = await axios.get(`${this.apiUrl}/google`, {
          params: {
            idToken: idToken, // 將 code 作為請求參數發送
          },
        })
        const token = response.data.token
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
        let userName = response.data.memberName
        let userPoint = response.data.memberPoint
        let memberObj = {
          memberId,
          token: token,
          userName,
          userAvatar,
          userPoint,
        }
        localStorage.setItem('memberobj', JSON.stringify(memberObj))
        this.isLoggedIn = true // 設置登入
        this.memberprofile.userName = userName
        this.memberprofile.userAvatar = userAvatar
        this.memberprofile.userPoint = userPoint
      } catch (error) {
        console.error('Google發送後端失敗', error)
      }
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
        let userName = response.data.memberName
        let userPoint = response.data.memberPoint
        let memberObj = {
          memberId: memberId,
          token: token,
          userName,
          userAvatar,
          userPoint,
        }
        localStorage.setItem('memberobj', JSON.stringify(memberObj))
        this.isLoggedIn = true // 設置登入
        this.memberprofile.userName = userName
        this.memberprofile.userAvatar = userAvatar
        this.memberprofile.userPoint = userPoint
      } catch (error) {
        // 處理錯誤，設登入失敗
        console.error('登入失敗', error)
        this.clearLoggedIn()
      }
    },
    async submitRegister(event) {
      try {
        let API_URL = `${this.apiUrl}/api/members/create`
        let form = new FormData(event.target)
        let formData = {}
        form.forEach((value, key) => {
          formData[key] = value
        })
        let formJsonData = JSON.stringify(formData)
        let response = await axios.post(API_URL, formJsonData, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        console.log(response.data)
        this.submitLogin
      } catch (error) {
        console.error('登入失敗', error)
      }
    },
    async userProfileAccess(event) {
      let Profile_URL = `${this.apiUrl}/google/login`
      let form = new FormData(event.target)
      let formData = {}
      form.forEach((value, key) => {
        formData[key] = value
      })
      let response = await axios.post(Profile_URL, formData)
      console.log(response.data)
    },
  },
})
