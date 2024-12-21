<template>
  <div>
    <!-- 點數顯示區塊 - 加入新的樣式 -->
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
                    memberPointTotal >= prize.pointPrizesPoints ? 'btn-primary' : 'btn-secondary'
                  "
                  @click="redeemPrize(prize)"
                  :disabled="memberPointTotal < prize.pointPrizesPoints"
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
      memberPointTotal: 0,
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

    async getMemberPoint() {
      try {
        const api = `${import.meta.env.VITE_API}/api/getMemberPoint`
        const response = await this.axios.get(api)

        // 找到當前登入會員的點數資料
        const currentMemberData = response.data.find((data) => data.members.id === user.memberId)

        // 如果找到會員資料，設置其點數總額
        if (currentMemberData) {
          this.memberPointTotal = currentMemberData.pointsTotal
          console.log('會員點數:', this.memberPointTotal)
        } else {
          this.memberPointTotal = 0
          console.log('未找到會員點數資料')
        }
      } catch (error) {
        console.error('獲取會員點數失敗:', error)
        this.memberPointTotal = 0
      }
    },

    async redeemPrize(prize) {
      // 先判斷點數是否足夠
      if (this.memberPointTotal < prize.pointPrizesPoints) {
        Swal.fire({
          icon: 'error',
          title: '點數不足',
          text: `您目前有 ${this.memberPointTotal} 點，還差 ${prize.pointPrizesPoints - this.memberPointTotal} 點才能兌換此商品`,
          confirmButtonText: '確定',
        })
        return // 如果點數不足，直接返回不執行後續兌換流程
      }

      //確定兌換跳出視窗
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
            console.error('產生兌換紀錄失敗:', error)
          }
          //扣除獎品庫存
          try {
            const MinusPrizesApi = `${import.meta.env.VITE_API}/api/MinusOnePrizesCount`
            const MinusPrizeresponse = await this.axios.post(MinusPrizesApi, requestData)
          } catch (error) {
            console.error('扣除獎品庫存失敗:', error)
          }
          //將會員各自的兌換碼存進SQL
          try {
            const promoApi = `${import.meta.env.VITE_API}/api/promoCode`
            const promoResponse = await this.axios.post(promoApi, promoData)
          } catch (error) {
            console.error('兌換碼存進SQL失敗:', error)
          }

          //兌換商品後扣除相對應積分
          try {
            const minusPointApi = `${import.meta.env.VITE_API}/api/minusMemberPoint`
            const minusPointResponse = await this.axios.post(minusPointApi, requestData)
          } catch (error) {
            console.error('兌換商品後扣除積分失敗:', error)
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
      this.getMemberPoint()
    },
  },
  computed: {},
  watch: {},
  created() {
    this.getPointPrizes(), this.getMemberPoint()
  },
}
</script>

<style scoped>
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
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 0;
  list-style: none;
  justify-content: center;
}

.prize-item {
  width: 300px;
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
  object-fit: cover;
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
