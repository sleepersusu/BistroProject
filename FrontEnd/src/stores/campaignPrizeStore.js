import { defineStore } from 'pinia'
import axios from 'axios'

export const campaignPrizeStore = defineStore('prize', {
  state: () => ({
    prizes: [],
  }),
  actions: {
    async getPrizesByCampaign(campaignId) {
      try {
        this.prizes = []
        const api = `${import.meta.env.VITE_API}/api/campaignPrize/prizeByCampaign/${campaignId}`
        const res = await axios.get(api)
        const prizeData = res.data

        const prizesWithImages = await Promise.all(
          prizeData.map(async (prize, index) => {
            const imageUrl = await this.getPrizeImage(prize.id)
            return {
              x: Math.floor(index / 3),
              y: index % 3,
              background: index === 0 ? 'red' : '#1C1C1C',
              fonts: [
                {
                  text: prize.prizeName,
                  fontSize: '12px',
                  fontColor: '#fff',
                  top: '65%',
                },
                {
                  text: `剩餘: ${prize.prizeQuantity}`,
                  fontSize: '12px',
                  fontColor: prize.prizeQuantity === 0 ? '#666' : '#fff',
                  top: '80%',
                },
              ],
              id: prize.id,
              ...(imageUrl && {
                imgs: [
                  {
                    src: imageUrl,
                    width: '60px',
                    height: '60px',
                    top: '10px',
                  },
                ],
              }),
            }
          }),
        )

        this.prizes = prizesWithImages
      } catch (e) {
        console.error('獲取獎品失敗:', e)
      }
    },

    async getPrizeImage(id) {
      try {
        const api = `${import.meta.env.VITE_API}/api/campaignPrize/image/${id}`
        const res = await axios.get(api, { responseType: 'blob' })
        return URL.createObjectURL(res.data)
      } catch (e) {
        console.error(e)
      }
    },

    clearPrizeImages() {
      this.prizes.forEach((prize) => {
        if (prize.imgs?.[0]?.src) {
          URL.revokeObjectURL(prize.imgs[0].src)
        }
      })
      this.prizes = []
    },
  },
})
