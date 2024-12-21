import { defineStore } from 'pinia'
import { useUserStore } from './userStore'
import axios from 'axios'

const user = useUserStore()

export const pointStore = defineStore('point', {
  state: () => ({
    pointPrizes: [],
    promoCode: null,
  }),
  actions: {
    async verifyPromoCode(inputPromoCode) {
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
        alert('兌換成功')

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

    // 檢查換卷是否已存在
    isPromoCodeExist(promoCode) {
      return this.pointPrizes.some((prize) => prize.promoCode === promoCode)
    },

    // 刪除購物車獎品
    removePointPrize(item) {
      this.pointPrizes = this.pointPrizes.filter((prize) => prize.name !== item.name)
    },
  },
})
