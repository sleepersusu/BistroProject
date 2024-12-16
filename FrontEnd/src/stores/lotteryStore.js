import { defineStore } from 'pinia'
import axios from 'axios'

export const lotteryStore = defineStore('lottery', {
  state: () => ({
    winner: {},
    chance: {},
  }),
  actions: {
    async getChancesByCampaign(memberId, campaignId) {
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
          console.error('獲取抽獎機會失敗:', error)
        }
      }
    },
    async drawPrize(chanceId) {
      const api = `${import.meta.env.VITE_API}/api/winner/${chanceId}`
      try {
        const res = await axios.post(api)
        this.winner = res.data
      } catch (e) {
        console.error(e)
        throw e
      }
    },
  },
  getters: {
    chanceCount: (state) => (campaignId) => state.chance[campaignId]?.remainingChances || 0,
  },
})
