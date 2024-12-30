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
      // userPoint: '',
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
        let memberId = JSON.parse(memberobj)?.memberId
        this.memberprofile.userName = JSON.parse(memberobj)?.userName
        // this.memberprofile.userPoint = JSON.parse(memberobj)?.userPoint
        let imgUrl = `${this.apiUrl}/api/members/photo/${memberId}`
        let userAvatar = ''
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
    async updateUserImage(file) {
      //圖片上傳
      console.log('觸發上傳')
      const formData = new FormData()
      formData.append('file', file)
      let response = await axios.post(
        `${this.apiUrl}/api/members/photo/${this.memberId}`,
        formData,
        {
          headers: {},
          responseType: 'blob',
        },
      )
      let userAvatar = URL.createObjectURL(response.data)
      this.memberprofile.userAvatar = userAvatar
    },
    async loadMemberData() {
      //會員資料頁面撈資訊
      try {
        const pointStoreInstance = pointStore()
        await axios.get(`${this.apiUrl}/api/frontend/members/${this.memberId}`).then((response) => {
          this.memberprofile.userName = response.data.memberName
          this.memberprofile.userEmail = response.data.memberEmail
          this.memberprofile.userPhone = response.data.memberPhone
          this.memberprofile.userGender = response.data.memberSex
          this.memberprofile.userFavor = response.data.memberFavor
          this.memberprofile.userAddress = response.data.memberAddress
          // console.log(this.memberprofile.userPoint);
          this.memberprofile.userBirthdate = response.data.memberBirthday
          // console.log(response.data.memberAccount)
          // console.log(response.data.memberPassword)
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
      let response = await axios.get(`${this.apiUrl}/user/logout`)
      console.log('會員登出' + response.data.status)
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
        // let userPoint = response.data.memberPoint
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
        // this.memberprofile.userPoint = userPoint
        const cart = cartStore()
        cart.getCart()
      } catch (error) {
        console.error('Google發送後端失敗', error)
      }
    },
    async submitLogin(loginForm) {
      try {
        // 後端服務
        let API_URL = `${this.apiUrl}/user/login`
        // let form = new FormData(event.target)
        // let formData = {}
        // form.forEach((value, key) => {
        //   formData[key] = value
        // })
        // let formJsonData = JSON.stringify(formData)
        const response = await axios.post(API_URL, loginForm, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        console.log(response)
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
        // let userPoint = response.data.memberPoint
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
        // this.memberprofile.userPoint = userPoint
        const cart = cartStore()
        cart.getCart()
        const point = pointStore()
        point.getMemberPoint()
        const lottery = lotteryStore()
        lottery.getAllChanceByMember()
      } catch (error) {
        // 處理錯誤，設登入失敗
        console.log(error.response)
        if (error.response) {
          // 當伺服器返回錯誤響應時
          console.error('伺服器錯誤:', error.response)
          console.log('HTTP 狀態碼:', error.response.status) // 例如 403
          // 根據返回的錯誤訊息顯示對應提示
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: error.response.data.message || '登入失敗',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else if (error.request) {
          // 請求發送成功，但沒有得到回應
          console.error('請求未獲得回應:', error.request)
        } else {
          console.error('發送請求時出錯:', error.message)
        }
        this.clearLoggedIn()
      }
    },
    async submitRegister(registForm) {
      try {
        let API_URL = `${this.apiUrl}/api/members/create`
        // let form = new FormData(event.target)
        // let formData = {}
        // form.forEach((value, key) => {
        //   formData[key] = value
        // })
        // let formJsonData = JSON.stringify(formData)
        let response = await axios.post(API_URL, registForm, {
          headers: {
            'Content-Type': 'application/json',
          },
        })
        if (response.data.status === 'success')
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'success',
            iconColor: 'black',
            title: `資料已更新！`,
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
          let logindata= {
            account:registForm.userEmail,
            password:registForm.userPassword
          }
          console.log(logindata)
        await this.submitLogin(logindata)
      } catch (error) {
        if (error.response.status === 409) {
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'error',
            iconColor: 'red',
            title: `帳號或電話資料重複申請！`,
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else {
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'error',
            iconColor: 'red',
            title: `註冊失敗！`,
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        }
        console.error('登入失敗', error)
      }
    },
    async submitRestPasswordEmail(email) {
      try {
        const response = axios.post(`${this.apiUrl}/api/forgot-password/${email}`)
        Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          iconColor: 'black',
          title: `重設密碼連結已發送到您的電子信箱`,
          timer: 2500,
          showConfirmButton: false,
          timerProgressBar: true,
        })
        console.log('信箱response' + response.data)
      } catch (e) {
        console.log(e)
        Swal.showValidationMessage(e.response?.data.message || '發送失敗')
      }
    },
    async cancelAccount(){
      console.log('準備註銷帳號');     
      try {
        let response=await axios.delete(`${this.apiUrl}/api/frontend/members/${this.memberId}`)
        console.log(response.data)
      } catch (error) {
        console.log(error.response)
        if (error.response) {
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: error.response.data.message || '註銷失敗',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else if (error.request) {
          // 請求發送成功，但沒有得到回應
          console.error('請求未獲得回應:', error.request)
        } else {
          console.error('發送請求時出錯:', error.message)
        }
      }
    },
    async sendSMS(data){
      try {
        let url=`${this.apiUrl}/api/frontend/members/${this.memberId}/sms`
        let response=await axios.put(url,data)
        if(response.data.status=== 'success'){
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'success',
            iconColor: 'black',
            title: '簡訊已發送成功',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        }
        let result=response.data.status
      } catch (error) {
        console.log(error.response)
        if (error.response) {
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: error.response.data.message || '失敗',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else if (error.request) {
          // 請求發送成功，但沒有得到回應
          console.error('請求未獲得回應:', error.request)
        } else {
          console.error('發送請求時出錯:', error.message)
        }
      }
    },

    async checkVerifyCode(data, callback){
      try {
        let url=`${this.apiUrl}/api/frontend/members/${this.memberId}/verify`
        let response=await axios.put(url,data)
        if (response.data.status === 'success') {
          if (callback && typeof callback === 'function') {
            callback(response, null); 
          }
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'success',
            iconColor: 'black',
            title: '驗證成功',
            timer: 1000,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        }
      } catch (error) {
        if (callback && typeof callback === 'function') {
          callback(null, error);  // 這裡傳遞null表示沒有正確的response
        }
        console.log(error.response)
        if (error.response) {
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: error.response.data.message || '失敗',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else if (error.request) {
          // 請求發送成功，但沒有得到回應
          console.error('請求未獲得回應:', error.request)
        } else {
          console.error('發送請求時出錯:', error.message)
        }
      }
    },

    async changePassword(data, callback){
      try {
        let url=`${this.apiUrl}/api/frontend/members/${this.memberId}/password`
        let response=await axios.put(url,data)
        if (response.data.status === 'success') {
          if (callback && typeof callback === 'function') {
            callback(response, null); 
          }
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'success',
            iconColor: 'black',
            title: '更改密碼成功',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        }
      } catch (error) {
        if (callback && typeof callback === 'function') {
          callback(null, error);  // 這裡傳遞null表示沒有正確的response
        }
        console.log(error.response)
        if (error.response) {
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: error.response.data.message || '失敗',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else if (error.request) {
          // 請求發送成功，但沒有得到回應
          console.error('請求未獲得回應:', error.request)
        } else {
          console.error('發送請求時出錯:', error.message)
        }
      }
    },



  },
})
