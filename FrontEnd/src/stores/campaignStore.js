import { defineStore } from 'pinia'
import { utils } from '@/mixins/utils'
import axios from 'axios'

export const campaignStore = defineStore('campaign', {
  state: () => ({
    campaigns: [],
  }),
  actions: {
    async getCampaigns() {
      const api = `${import.meta.env.VITE_API}/api/campaign`
      try {
        const res = await axios.get(api)
        console.log(res.data)

        const statusPriority = {
          IN_PROGRESS: 0,
          NOT_STARTED: 1,
          EXPIRED: 2,
        }

        this.campaigns = res.data
          .sort((a, b) => b.id - a.id)
          .slice(0, 6)
          .sort((a, b) => {
            const statusDiff = statusPriority[a.campaignStatus] - statusPriority[b.campaignStatus]
            if (statusDiff !== 0) return statusDiff

            const { formatDate } = utils()
            const dateA = new Date(formatDate(a.startDate))
            const dateB = new Date(formatDate(b.startDate))

            return dateB - dateA
          })

        this.campaigns = await Promise.all(
          this.campaigns.map(async (campaign) => {
            const imageUrl = await this.getCampaignsIamge(campaign.id)
            return {
              ...campaign,
              imageUrl,
            }
          }),
        )
      } catch (e) {
        console.error(e)
      } finally {
      }
    },

    async getCampaignsIamge(id) {
      const api = `${import.meta.env.VITE_API}/api/campaign/image/${id}`
      try {
        const res = await axios.get(api, {
          responseType: 'blob',
        })
        return URL.createObjectURL(res.data)
      } catch (e) {
        console.error(e)
        return ''
      }
    },
    async getActiveCampaign() {
      const api = `${import.meta.env.VITE_API}/api/campaign/active`
      try {
        const res = await axios.get(api)
        return res.data
      } catch (e) {
        console.error(e)
        return []
      }
    },
    clearCampaignImages() {
      this.campaigns.forEach((campaign) => {
        if (campaign.imageUrl) {
          URL.revokeObjectURL(campaign.imageUrl)
        }
      })
      this.campaigns = []
    },
  },
})
