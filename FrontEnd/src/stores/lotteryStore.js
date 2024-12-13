import { defineStore } from 'pinia'
import axios from 'axios'
import { statusStore } from './statusStore'

const status = statusStore()

export const lotteryStore = defineStore('lottery', {
  state: () => ({
    winner: {},
    chance: {},
  }),
  actions: {
    async getChancesByCampaign(memberId, campaignId) {
      status.isLoading = true
      const api = `${import.meta.env.VITE_API}/api/lotteryChance/${memberId}/${campaignId}`
      try {
        const res = await axios.get(api)
        this.chance[campaignId] = res.data
      } catch (e) {
        console.log(e)
      } finally {
        status.isLoading = false
      }
    },
    async drawPrize(chanceId) {
      status.isLoading = true
      const api = `${import.meta.env.VITE_API}/api/winner/${chanceId}`
      try {
        const res = await axios.post(api)
        this.winner = res.data
      } catch (e) {
        console.log(e)
      } finally {
        status.isLoading = false
      }
    },
  },
  getters: {
    chanceCount: (state) => (campaignId) => state.chance[campaignId]?.remainingChances || 0,
  },
})
