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
        this.campaigns = res.data

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
