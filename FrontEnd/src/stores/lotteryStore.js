import { defineStore } from 'pinia'
import { useUserStore } from './userStore'
import axios from 'axios'

export const lotteryStore = defineStore('lottery', {
  state: () => ({
    winner: {},
    chance: {},
    allChances: 0,
  }),
  actions: {
    async getChancesByCampaign(memberId, campaignId) {
      const user = useUserStore()
      if (!user.memberId) return
      const api = `${import.meta.env.VITE_API}/api/lotteryChance/${memberId}/${campaignId}`
      try {
        const res = await axios.get(api)
        this.chance[campaignId] = res.data
      } catch (e) {
        if (e.response?.status === 404) {
          this.chance[campaignId] = {
            id: null,
            remainingChances: 0,
          }
        } else {
          console.error('獲取抽獎機會失敗:', e)
        }
      }
    },

    async getAllChanceByMember() {
      const user = useUserStore()
      if (!user.memberId) {
        console.log('找不到會員id')
        return
      }
      const api = `${import.meta.env.VITE_API}/api/lotteryChance/member/${user.memberId}`
      try {
        const res = await axios.get(api)
        this.allChances = res.data.reduce((total, chance) => {
          return total + chance.remainingChances
        }, 0)
      } catch (e) {
        if (e.response?.status === 404) {
          this.allChances = 0
        } else {
          console.error('獲取抽獎機會失敗:', e)
        }
      }
    },
    async drawPrize(chanceId) {
      const api = `${import.meta.env.VITE_API}/api/frontend/winner/${chanceId}`
      try {
        const res = await axios.post(api)
        this.winner = res.data
        await this.getAllChanceByMember()
      } catch (e) {
        console.error(e)
        throw e
      }
    },

    async addChance(campaignId, memberId, orderAmount) {
      const api = `${import.meta.env.VITE_API}/api/lotteryChance`
      try {
        const res = await axios.post(api, {
          memberId,
          campaignId,
          orderAmount,
        })
        await this.getAllChanceByMember()
        return res
      } catch (e) {
        console.log(e.response.data.message)
      }
    },
  },
  getters: {
    chanceCount: (state) => (campaignId) => state.chance[campaignId]?.remainingChances || 0,
  },
})
