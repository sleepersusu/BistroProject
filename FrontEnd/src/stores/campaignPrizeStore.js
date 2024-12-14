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
        const res = await axios.get(
          `${import.meta.env.VITE_API}/api/campaignPrize/prizeByCampaign/${campaignId}`,
        )
        const prizeData = res.data

        const prizesWithImages = await Promise.all(
          prizeData.map(async (prize, index) => {
            let imgUrl = null

            if (prize.prizeImg) {
              const imageRes = await axios.get(
                `${import.meta.env.VITE_API}/api/campaignPrize/image/${prize.id}`,
                {
                  responseType: 'blob',
                },
              )
              imgUrl = URL.createObjectURL(imageRes.data)
            }

            return {
              x: Math.floor(index / 3),
              y: index % 3,
              background: index === 0 ? 'red' : '#1C1C1C',
              fonts: [
                {
                  text: prize.prizeName,
                  fontSize: '12px',
                  fontColor: '#fff',
                  top: '70%',
                },
              ],
              id: prize.id,
              ...(imgUrl && {
                imgs: [
                  {
                    src: imgUrl,
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
