<template>
  <div>
    <div>
      <BannerTop v-bind:title="'Point Shop'"></BannerTop>
    </div>
    <!-- 登入顯示區塊 -->

    <!-- 未登入提示 -->
    <div v-if="!user.memberId" class="d-flex justify-content-center align-items-center flex-column my-5">
      <i class="bi bi-person-exclamation display-1 mb-3"></i>
      <h3 class="mb-4">尚未登入會員</h3>
    </div>


    <!-- 點數顯示區塊 -->
    <div class="container text-center my-4">
      <div class="points-display">
        <span class="points-text">目前總共有</span>
        <span class="points-number">{{ memberPointTotal }}</span>
        <span class="points-text">點</span>
      </div>
    </div>

    <!-- 商品卡片區塊 -->
    <div class="container my-5">
      <ul class="prize-list">
        <li
          v-for="prize in pointPrizes"
          :key="prize.id"
          class="prize-item"
          :class="{ 'insufficient-points': memberPointTotal < prize.pointPrizesPoints }"
        >
          <div v-if="prize.rewardsStatus == '上架中'" class="card">
            <img
              :src="'data:image/jpeg;base64,' + prize.base64Image"
              class="card-img-top"
              alt="Prize Image"
            />
            <div class="card-body">
              <div class="prize-header">
                <h5 class="prize-title">{{ prize.pointPrizesName }}</h5>
                <h5 class="prize-points">{{ prize.pointPrizesPoints }} 點</h5>
              </div>
              <p class="prize-description">{{ prize.pointPrizesDescription }}</p>
              <div class="prize-footer">
                <button
                  class="btn"
                  :class="
                    memberPointTotal >= prize.pointPrizesPoints && prize.pointPrizesCount > 0
                      ? 'btn-primary'
                      : 'btn-secondary'
                  "
                  @click="redeemPrize(prize)"
                  :disabled="
                    memberPointTotal < prize.pointPrizesPoints || prize.pointPrizesCount === 0
                  "
                >
                  {{ memberPointTotal >= prize.pointPrizesPoints ? '兌換商品' : '點數不足' }}
                </button>
                <span class="remaining-count">剩餘 {{ prize.pointPrizesCount }} 份</span>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import BannerTop from '@/components/BannerTop.vue'
import PointTotal from '@/components/PointTotal.vue'
import { useUserStore } from '@/stores/userStore'
import login from '@/components/login.vue'
import { pointStore } from '@/stores/pointStore'
import { mapActions,mapState } from 'pinia'

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
    ...mapActions(pointStore,['getMemberPoint']),
    generateRandomCode() {
      const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789'
      let result = ''
      for (let i = 0; i < 6; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length)
        result += characters[randomIndex]
      }
      return result
    },

    async getPointPrizes() {
      const api = `${import.meta.env.VITE_API}/api/pointPrizes`
      const response = await this.axios.get(api)
      this.pointPrizes = response.data
    },

    async redeemPrize(prize) {
      if (prize.pointPrizesCount === 0) {
        Swal.fire({
          icon: 'error',
          title: '商品已售完',
          text: '很抱歉，此商品已經售完',
          confirmButtonText: '確定',
        })
        return
      }

      const result = await Swal.fire({
        icon: 'question',
        title: `確定要兌換${prize.pointPrizesName}嗎?`,
        html: `
          <p>您目前有 <span style="color: #007bff; font-size: 20px; font-weight: bold;">${this.memberPointTotal}</span> 點</p>
          <p>將會扣除 <span style="color: #dc3545; font-size: 20px; font-weight: bold;">${prize.pointPrizesPoints}</span> 點</p>
          <p>兌換後剩餘 <span style="color: #28a745; font-size: 20px; font-weight: bold;">${this.memberPointTotal - prize.pointPrizesPoints}</span> 點</p>
          <p style="color: #6c757d; font-size: 14px;">兌換後無法返還</p>
        `,
        showCancelButton: true,
        confirmButtonText: '兌換',
        cancelButtonText: '在想一下',
        reverseButtons: true,
      })

      if (!result.isConfirmed) {
        return
      }

      const promoCode = this.generateRandomCode()
      const requestData = {
        memberId: user.memberId,
        pointPrizesId: prize.id,
        recordsDate: new Date().toISOString(),
      }

      const promoData = {
        memberId: user.memberId,
        pointPrizesId: prize.id,
        promoCode,
      }

      try {
        // 產生兌換紀錄
        const api = `${import.meta.env.VITE_API}/api/pointRecord`
        const response = await this.axios.post(api, requestData)

        if (response.data.兌換狀態) {
          // 執行所有後續操作
          await Promise.all([
            // 扣除獎品庫存
            this.axios.post(`${import.meta.env.VITE_API}/api/MinusOnePrizesCount`, requestData),
            // 儲存兌換碼
            this.axios.post(`${import.meta.env.VITE_API}/api/promoCode`, promoData),
            // 扣除會員點數
            this.axios.post(`${import.meta.env.VITE_API}/api/minusMemberPoint`, requestData),
          ])

          window.Swal.fire({
            toast: true,
            icon: 'success',
            title: `兌換成功！您已成功兌換 ${prize.pointPrizesName}`,
            html: `兌換碼是 : ${promoCode}<br>可前往會員中心查看`,
            timer: 5000,
            showConfirmButton: false,
            timerProgressBar: true,
          })
          this.getPointPrizes()
          this.getMemberPoint()
        }
      } catch (error) {
        console.error('兌換處理失敗:', error)
        Swal.fire({
          icon: 'error',
          title: '兌換失敗',
          text: '請稍後再試',
          confirmButtonText: '確定',
        })
      }
    },
  },
  watch: {

  },
  computed: {
    ...mapState(pointStore,['memberPointTotal']),
    user() {
      return user
    }
  },
  created() {
    this.getPointPrizes()
    this.getMemberPoint()
  },
}
</script>

<style scoped>
.bi {
  color: #6c757d;
}

h3 {
  color: #6c757d;
  font-weight: normal;
}

.points-display {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 20px;
  border-radius: 15px;
  display: inline-block;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin: 20px 0;
}

.points-number {
  color: #007bff;
  font-size: 24px;
  font-weight: bold;
  margin: 0 10px;
}

.points-text {
  color: #495057;
  font-size: 18px;
}

.prize-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  padding: 0;
  list-style: none;
  width: 100%;
  max-width: 1320px; /* 可以根據需要調整 */
  margin: 0 auto;
}

.prize-item {
  width: 100%;
  transition: transform 0.2s;
}

.prize-item:hover {
  transform: translateY(-5px);
}

.card {
  height: 490px;
  border: none;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  overflow: hidden;
}

.card-img-top {
  height: 200px;
  object-fit: contain;
  background-color: #f8f9fa; /* 可選：設置背景色，讓留白部分不會太突兀 */
  padding: 10px; /* 可選：加一點內邊距讓圖片不會貼邊 */
}

.card-body {
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.prize-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.prize-title {
  font-size: 1.1rem;
  margin: 0;
}

.prize-points {
  color: #dc3545;
  margin: 0;
}

.prize-description {
  flex-grow: 1;
  overflow-y: auto;
  font-size: 0.9rem;
  color: #6c757d;
  margin-bottom: 15px;
}

.prize-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: 10px;
}

.remaining-count {
  font-size: 0.9rem;
  color: #6c757d;
  margin-right: 15px;
}

.insufficient-points .card {
  opacity: 0.8;
}

.btn {
  padding: 8px 16px;
  border-radius: 8px;
}

.btn-secondary {
  background-color: #6c757d;
  color: white;
  cursor: not-allowed;
}

/* 隱藏滾動條但保持功能 */
.prize-description::-webkit-scrollbar {
  width: 0px;
}
</style>
