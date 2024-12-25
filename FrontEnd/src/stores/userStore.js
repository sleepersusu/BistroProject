import { defineStore } from 'pinia'
import axios from 'axios'
import { pointStore } from './pointStore'
import { lotteryStore } from './lotteryStore'
import { cartStore } from '@/stores/cartStore.js'

export const useUserStore = defineStore('userStore', {
  state: () => ({
    apiUrl: import.meta.env.VITE_API, // 使用 VITE_API
    isLoggedIn: false, // isLoggedIn狀態
    memberprofile: {
      navName: '',
      userName: '',
      userEmail: '',
      userPhone: '',
      userPoint: '',
      userShip: '',
      userAvatar: '/images/avatar.jpg', 
      userGender: null,
      userAddress: '', 
      userBirthdate: '',
      city: '',
      district: '',
    },
  }),
  getters: {
    // 取得會員資料
    getProfile: (state) => state.memberprofile,

    // 檢查是否登入
    checkLogin: (state) => state.isLoggedIn,

    memberId: () => Number(JSON.parse(localStorage.getItem('memberobj'))?.memberId) || null,

    memberId: (state) => {
      if (state.isLoggedIn === true) {
        return Number(JSON.parse(localStorage.getItem('memberobj'))?.memberId)
      } else {
        return null
      }
    },
  },
  actions: {
    async setLoggedIn() {
      let memberobj = localStorage.getItem('memberobj')
      this.isLoggedIn = true // 設置登入
      this.memberprofile.userName = JSON.parse(memberobj).userName
      this.memberprofile.userAvatar = JSON.parse(memberobj).userAvatar
    },
    async checkLoggedIn() {
      let memberobj = localStorage.getItem('memberobj')
      if (JSON.parse(memberobj)?.memberId) {
        this.isLoggedIn = true
        let memberId=JSON.parse(memberobj)?.memberId
        this.memberprofile.userName = JSON.parse(memberobj)?.userName
        let imgUrl = `${this.apiUrl}/api/members/photo/${memberId}`
        let userAvatar=''
        let imgData = await axios.get(imgUrl, {
          responseType: 'blob',
        })
        if (imgData.data.size === 0) {
          //判斷有無圖片，切換預設頭像
          userAvatar = '/images/avatar.jpg'
        } else {
          userAvatar = URL.createObjectURL(imgData.data)
        }
        this.memberprofile.userAvatar = userAvatar
      } else {
        this.isLoggedIn = false // 清除登入
      }
    },
    async updateUserImage(file) { //圖片上傳
      console.log('觸發上傳')
      const formData = new FormData()
      formData.append('file', file)
      console.log('圖片'+formData)
      let response = await axios.post(`${this.apiUrl}/api/members/photo/${this.memberId}`,formData,{
        headers: {
        },
        responseType: 'blob',
    })
      let userAvatar = URL.createObjectURL(response.data)
      this.memberprofile.userAvatar=userAvatar
    },
    async loadMemberData(memberId) {
      //會員資料頁面撈資訊
      try {
        await axios.get(`${this.apiUrl}/api/frontend/members/${memberId}`).then((response) => {
          this.memberprofile.userName = response.data.memberName
          this.memberprofile.userEmail = response.data.memberEmail
          this.memberprofile.userPhone = response.data.memberPhone
          this.memberprofile.userGender = response.data.memberSex
          this.memberprofile.userFavor = response.data.memberFavor
          this.memberprofile.userAddress = response.data.memberAddress
          this.memberprofile.userPoint = response.data.memberPoint
          this.memberprofile.userBirthdate = response.data.memberBirthday
          // console.log(response.data.memberAccount)
          // console.log(response.data.memberEmail)
          // console.log(response.data.memberImg)
          // console.log(response.data.memberPassword)
          // console.log(response.data.memberShip)
          // console.log(response.data.memberStatus)
          this.parseAddress(this.memberprofile.userAddress)
        })
      } catch (error) {
        throw error
      }
    },
    async submitProfile() {
      console.log('準備提交')
      let memberobj = JSON.parse(localStorage.getItem('memberobj'))
      let memberId = memberobj.memberId
      try {
        this.isSubmitting = true
        const formData = {
          userName: this.memberprofile.userName,
          userEmail: this.memberprofile.userEmail,
          userPhone: this.memberprofile.userPhone,
          userFavor: this.memberprofile.userFavor,
          userGender: this.memberprofile.userGender,
          userAddress: this.memberprofile.userAddress,
          userBirthdate: this.memberprofile.userBirthdate,
        }
        memberobj.userName = this.memberprofile.userName
        localStorage.setItem('memberobj', JSON.stringify(memberobj))
        this.memberprofile.navName = this.memberprofile.userName

        let Profile_Url = `${this.apiUrl}/api/frontend/members/${memberId}`
        let response = await axios.put(Profile_Url, JSON.stringify(formData), {
          headers: {
            'Content-Type': 'application/json',
          },
        })

        return response
      } catch (error) {
        console.log('axios出問題')
      }
    },
    parseAddress(Address) {
      // 匹配市，匹配到市後，後面可以跟鄉、鎮或區
      const cityRegex = /([^\d]+市)/ // 正則表達式用來匹配市
      // 匹配區、鄉或鎮，區的後面可能還會有更多的地址信息，如街道等
      const districtRegex = /([^\d]+(區|鄉|鎮))(?![^\d])/ // 正則表達式用來匹配區、鄉、鎮
      if (Address) {
        const cityMatch = Address.match(cityRegex) // 查找市
        const districtMatch = Address.replace(cityMatch ? cityMatch[0] : '', '').match(
          districtRegex,
        )
        this.memberprofile.city = cityMatch[0]
        this.memberprofile.district = districtMatch[0]
      }
    },
    async clearLoggedIn() {
      localStorage.removeItem('memberobj')
      let response=await axios.get(`${this.apiUrl}/user/logout`)
      console.log('會員登出'+response.data.status)
      this.isLoggedIn = false
      this.memberprofile = {}
      const point = pointStore()
      point.getMemberPoint()
      //get cart again
      const cart = cartStore()
      cart.getCart()
    },
    async handleGoogleLogin(idToken) {
      try {
        let response = await axios.get(`${this.apiUrl}/google`, {
          params: {
            idToken: idToken, // 將 idToken 作為請求參數發送
          },
        })
        const token = response.data.token
        let userAvatar = ''
        let memberId = response.data.memberId
        let imgUrl = `${this.apiUrl}/api/members/photo/${memberId}`
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
        }
        localStorage.setItem('memberobj', JSON.stringify(memberObj))
        this.isLoggedIn = true // 設置登入
        this.memberprofile.userName = userName
        this.memberprofile.navName = userName
        this.memberprofile.userAvatar = userAvatar
        this.memberprofile.userPoint = userPoint
        const cart = cartStore()
        cart.getCart()
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
        let imgUrl = `${this.apiUrl}/api/members/photo/${memberId}`
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
          memberId,
          token: token,
          userName,
          userAvatar,
        }
        localStorage.setItem('memberobj', JSON.stringify(memberObj))
        this.isLoggedIn = true // 設置登入
        this.memberprofile.userName = userName
        this.memberprofile.navName = userName
        this.memberprofile.userAvatar = userAvatar
        this.memberprofile.userPoint = userPoint
        const cart = cartStore()
        cart.getCart()
        const point = pointStore()
        point.getMemberPoint()
        const lottery = lotteryStore()
        lottery.getAllChanceByMember()
      } catch (error) {
        // 處理錯誤，設登入失敗
        console.error('登入失敗', error)
        Swal.fire({
          toast: false,
          position: 'top',
          icon: 'warning',
          iconColor: 'red',
          title: `查無此帳號`,
          timer: 1500,
          showConfirmButton: false,
          timerProgressBar: true,
        })
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
  },
})
