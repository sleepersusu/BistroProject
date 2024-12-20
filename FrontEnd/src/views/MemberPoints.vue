<template>
  <div>
    <BannerTop :title="'PointShop'"></BannerTop>

    <div class="container d-flex my-5">
      <ul class="d-flex flex-wrap">
        <li v-for="prize in pointPrizes" :key="prize.id" class="d-flex flex-column" style="width: 18rem; margin: 10px">
          <div
            v-if="prize.rewardsStatus == '上架中'"
            class="card" style="height: 490px; position: relative" >
            <!-- 動態綁定圖片的 Base64 編碼 -->
            <img
              :src="'data:image/jpeg;base64,' + prize.base64Image"
              class="card-img-top w-100"
              alt="Prize Image"
              style="height: 300px; object-fit: cover"
            />
            <div class="card-body" style="overflow-y: auto; height: calc(100% - 300px)">
              <div class="d-flex justify-content-between">
                <h5 class="card-title">{{ prize.pointPrizesName }}</h5>
                <h5 class="card-text" style="color: red">{{ prize.pointPrizesPoints }} 點</h5>
              </div>
              <p class="card-text">{{ prize.pointPrizesDescription }}</p>
              <a
                href="#"
                class="btn btn-primary mt-auto"
                style="position: absolute; bottom: 16px"
                @click="redeemPrize(prize)">兌換商品
              </a>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <PointTotal :redeemed-prize="redeemedPrize" :memberId="memberId" ref="pointTotal" />
  </div>

  <login ref="loginModal"></login>
</template>

<script>
import BannerTop from '@/components/BannerTop.vue'
import PointTotal from '@/components/PointTotal.vue'
import { useUserStore } from '@/stores/userStore'
import login from '@/components/login.vue'

const user = useUserStore()

export default {
  components: {
    PointTotal,
    BannerTop,
    login,
  },
  data() {
    return {
      pointPrizes: [],
      redeemedPrize: {},
      memberId: user.memberId,
    }
  },
  methods: {
    generateRandomCode() {
      const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
      let result = ''
      for (let i = 0; i < 6; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length)
        result += characters[randomIndex]
      }
      return result
    },
    openLoginModal() {
      this.$refs.loginModal.openLoginModal()
    },

    async getPointPrizes() {
      const api = `${import.meta.env.VITE_API}/api/pointPrizes`

      const response = await this.axios.get(api)
      // console.log("66666", response)
      // console.log(localStorage)
      // console.log(localStorage.memberId)

      const memberId = user.memberId
      this.pointPrizes = response.data
    },

    async redeemPrize(prize) {
      //確定兌換跳出視窗
      const result = await Swal.fire({
        icon: 'question',
        title: `確定要兌換${prize.pointPrizesName}嗎?`,
        html: `<p>將會扣除 <span style="color: lightblue; font-size: 20px; font-weight: bold;">${prize.pointPrizesPoints}</span> 點，兌換後無法返還</p>`,
        showCancelButton: true,
        confirmButtonText: '兌換',
        cancelButtonText: '在想一下',
        reverseButtons: true,
      })

      //if(兌換成功)產生兌換碼
      if (result.isConfirmed) {
        const promoCode = this.generateRandomCode()

        // 調用子組件的方法更新優惠碼和商品信息
        this.redeemedPrize = { name: prize.pointPrizesName, promoCode: promoCode }

        const memberId = user.memberId
        if (memberId) {
          const requestData = {
            memberId,
            pointPrizesId: prize.id, // 商品 ID 來自按下的商品
            recordsDate: new Date().toISOString(), // 使用當前時間作為兌換日期
          }

          const promoData = {
            memberId,
            pointPrizesId: prize.id,
            promoCode,
          }

          try {
            //產生兌換紀錄
            const api = `${import.meta.env.VITE_API}/api/pointRecord`
            const response = await this.axios.post(api, requestData)
            // console.log(response)

            //將會員各自的兌換碼存進SQL
            const promoApi = `${import.meta.env.VITE_API}/api/promoCode`
            const promoResponse = await fetch(promoApi, {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(promoData),
            })

            //兌換商品後扣除相對應積分
            const minusPointApi = `${import.meta.env.VITE_API}/api/minusMemberPoint`
            const minusPointResponse = await this.axios.post(minusPointApi, requestData)
            console.log(minusPointResponse)

            if (response.data.兌換狀態) {
              window.Swal.fire({
                toast: true,
                // position: 'top-end',
                icon: 'success',
                title: `兌換成功！您已成功兌換 ${prize.pointPrizesName}`,
                html: `兌換碼是 : ${promoCode}<br>可前往會員資訊查看`,
                timer: 5000,
                showConfirmButton: false,
                timerProgressBar: true,
              })

              // 觸發子組件重新獲取資料的方法
              this.$refs.pointTotal.getPromoCode()

              const MinusPrizesApi = `${import.meta.env.VITE_API}/api/MinusOnePrizesCount`
              const MinusPrizesResponse = await fetch(MinusPrizesApi, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(requestData), // 將資料轉換為 JSON 格式發送
              })

              if (MinusPrizesResponse.ok) {
                console.log('庫存已減少')
              } else {
                console.error(
                  '庫存減少失敗:',
                  MinusPrizesResponse.status,
                  await MinusPrizesResponse.text(),
                )
              }
            } else {
              console.error('Failed to record point:', response.status)
            }
          } catch (error) {
            console.error('Error sending request:', error)
          }
        } else {
          //if(不是會員)跳轉到登入畫面
          window.Swal.fire({
            title: '兌換商品需要點數',
            text: '請先登入會員，才能進行兌換。',
            icon: 'warning',
            showCancelButton: true, // 顯示取消按鈕
            confirmButtonText: '跳轉至登入',
            cancelButtonText: '取消',
            reverseButtons: true, // 反轉按鈕的顯示順序
          }).then((result) => {
            if (result.isConfirmed) {
              // this.$router.push({ path: 'Login' })
              this.openLoginModal()
            } else {
              console.log('User canceled the action')
            }
          })
        }
      } else {
        console.log('User canceled redemption')
      }
    },
  },
  computed: {},
  watch: {},
  created() {
    this.getPointPrizes()
  },
}
</script>

<style scoped>
.card-body::-webkit-scrollbar {
  display: none;
}
</style>
