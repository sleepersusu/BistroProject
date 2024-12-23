import { defineStore } from 'pinia'
import { useUserStore } from './userStore'
import axios from 'axios'

export const pointStore = defineStore('point', {
  state: () => ({
    pointPrizes: [],
    promoCode: null,
    memberPointTotal: 0
  }),
  actions: {
    //判斷是否有此兌換碼
    async verifyPromoCode(inputPromoCode) {
      const user = useUserStore()
      const api = `${import.meta.env.VITE_API}/api/showPromoCode/${user.memberId}`
      const response = await axios.get(api)
      this.promoCode = response.data

      const filterCode = this.promoCode.filter((code) => code.promoCode === inputPromoCode)
      console.log(filterCode)

      // 因為 filter 返回陣列，所以需要取第一個元素
      if (filterCode.length > 0) {
        const pointPrizesName = filterCode[0].pointPrizes.pointPrizesName
        const pointPrizesImg = filterCode[0].pointPrizes.pointPrizesImg
        const promoCode = filterCode[0].promoCode
        console.log(promoCode)

        this.pointPrizes.push({
          name: pointPrizesName,
          img: pointPrizesImg,
          promoCode: promoCode,
        })
      } else {
        console.log('找不到符合的促銷碼')
      }
    },

    clearPointPrizes() {
      this.pointPrizes = []
    },

    addToCart(prizeData) {
      if (!this.pointPrizes.some((prize) => prize.promoCode === prizeData.promoCode)) {
        this.pointPrizes.push(prizeData)
      }
    },

    // 刪除購物車獎品
    removePointPrize(item) {
      this.pointPrizes = this.pointPrizes.filter((prize) => prize.name !== item.name)
    },

    async getMemberPoint() {
      const user = useUserStore()
      try {
        if (!user.memberId) {
          this.memberPointTotal = 0
          return
        }

        const api = `${import.meta.env.VITE_API}/api/getMemberPoint`
        const response = await axios.get(api)
        const currentMemberData = response.data.find((data) => data.members.id === user.memberId)

        this.memberPointTotal = currentMemberData ? currentMemberData.pointsTotal : 0
      } catch (error) {
        console.error('獲取會員點數失敗:', error)
        this.memberPointTotal = 0
      }
    },
  },
})
