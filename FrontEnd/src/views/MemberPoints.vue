<template>
  <div>
    <BannerTop :title="'PointShop'"></BannerTop>

    <div class="container d-flex my-5">
      <ul class="d-flex flex-wrap">
        <li
          v-for="prize in pointPrizes"
          :key="prize.id"
          class="d-flex flex-column"
          style="width: 18rem; margin: 10px"
        >
          <div
            v-if="prize.rewardsStatus == '上架中'"
            class="card"
            style="height: 490px; position: relative"
          >
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
              <div
                class="d-flex justify-content-between align-items-center"
                style="position: absolute; bottom: 16px; width: 100%"
              >
                <a href="#" class="btn btn-primary" @click="redeemPrize(prize)"> 兌換商品 </a>
                <h5 class="card-text mb-0 text-danger">剩餘{{ prize.pointPrizesCount }} 份</h5>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>

    <!-- <PointTotal :redeemed-prize="redeemedPrize" :memberId="memberId" ref="pointTotal" /> -->
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
    //登入畫面的
    openLoginModal() {
      this.$refs.loginModal.openLoginModal()
    },

    async getPointPrizes() {
      const api = `${import.meta.env.VITE_API}/api/pointPrizes`
      const response = await this.axios.get(api)
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
        //用promoCode去接產生的兌換碼
        const promoCode = this.generateRandomCode()

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
            if (response.data.兌換狀態) {
                window.Swal.fire({
                  toast: true,
                  // position: 'top-end',
                  icon: 'success',
                  title: `兌換成功！您已成功兌換 ${prize.pointPrizesName}`,
                  html: `兌換碼是 : ${promoCode}<br>可前往會員中心查看`,
                  timer: 5000,
                  showConfirmButton: false,
                  timerProgressBar: true,
                })
              }
          } catch (error) {
            console.error('產生兌換紀錄失敗:', error);
          }
            //扣除獎品庫存
          try {
            const MinusPrizesApi = `${import.meta.env.VITE_API}/api/MinusOnePrizesCount`
            const MinusPrizeresponse = await this.axios.post(MinusPrizesApi, requestData)
          } catch (error) {
            console.error('扣除獎品庫存失敗:', error);
          }
            //將會員各自的兌換碼存進SQL
          try{
            const promoApi = `${import.meta.env.VITE_API}/api/promoCode`
            const promoResponse = await this.axios.post(promoApi,promoData)
          } catch (error) {
            console.error('兌換碼存進SQL失敗:', error);
          }

            //兌換商品後扣除相對應積分
          try{
            const minusPointApi = `${import.meta.env.VITE_API}/api/minusMemberPoint`
            const minusPointResponse = await this.axios.post(minusPointApi, requestData)
          } catch (error) {
            console.error('兌換商品後扣除積分失敗:', error);
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
