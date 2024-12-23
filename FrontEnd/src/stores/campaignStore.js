import { defineStore } from 'pinia'
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

        this.campaigns = res.data.sort((a, b) => b.id - a.id)
        this.campaigns.sort((a, b) => {
          return statusPriority[a.campaignStatus] - statusPriority[b.campaignStatus]
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
