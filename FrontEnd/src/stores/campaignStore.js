import { defineStore } from 'pinia'
import { statusStore } from './statusStore'
import axios from 'axios'

const status = statusStore()

export const campaignStore = defineStore('campaign', {
  state: () => ({
    campaigns: [],
  }),
  actions: {
    async getCampaigns() {
      status.isLoading = true
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
        console.log(e)
      } finally {
        status.isLoading = false
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
        console.log(e)
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
