<template>
  <div class="text-center mt-5">
    <i class="bi bi-check-circle-fill" style="font-size: 5rem"></i>
  </div>
  <!-- Step Indicator -->
  <div class="p-5">
    <div class="step-indicator mb-1 mt-3">
      <div class="step">1</div>
      <div class="step-connector"></div>
      <div class="step">2</div>
      <div class="step-connector"></div>
      <div class="step active">3</div>
    </div>
  </div>
  <!-- CTA Section -->
  <section class="cta-section">
    <div class="container">
      <h2 class="cta-heading">THANK YOU!</h2>
      <div class="d-flex justify-content-center">
        <p>
          <span class="order-number-highlight text-dark">
            訂單編號：<strong>{{ orderNumber }}</strong>
          </span>
          您的訂單已成功完成！<br />
          我們將盡快為您準備並安排配送，<br />
          期待能為您提供一份令人滿意的體驗。
          <br />
        </p>
      </div>
      <div class="button-container">
        <router-link to="/membercenter/orders" class="btn btn-primary cta-btn">
          My Orders
        </router-link>
      </div>
    </div>
    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
  </section>
</template>

<script>
import { defineComponent } from 'vue'
import BannerTop from '@/components/BannerTop.vue'
import Orders from '@/components/Orders.vue'
import PageTop from '@/components/PageTop.vue'
import { campaignStore } from '@/stores/campaignStore.js'
import { lotteryStore } from '@/stores/lotteryStore.js'
import { statusStore } from '@/stores/statusStore.js'
import { useUserStore } from '@/stores/userStore.js'
import { orderStore } from '@/stores/orderStore.js'
import Swal from 'sweetalert2'

const campaign = campaignStore()
const lottery = lotteryStore()
const status = statusStore()
const user = useUserStore()
const order = orderStore()

export default defineComponent({
  name: 'CartCheckSuc',
  components: { PageTop, Orders, BannerTop },
  data() {
    return {
      // 從路由參數中get訂單編號
      orderNumber: this.$route.query.orderNumber || '未知',
    }
  },
  methods: {
    async addMemberChance() {
      status.start()
      const orderData = await order.getOrderDetail(this.orderNumber)
      console.log(orderData)
      const sumPirce = orderData?.ordersSumPrice
      const activeCampaign = await campaign.getActiveCampaign()
      const res = await lottery.addChance(activeCampaign[0].id, user.memberId, sumPirce)
      if (res?.status === 200) {
        this.showAlert(res.data.newChances)
      }
      status.finish()
    },
    showAlert(chances) {
      Swal.fire({
        toast: true,
        position: 'bottom-end',
        icon: 'success',
        iconColor: 'black',
        title: `恭喜！您已獲得${chances}次抽獎機會`,
        text: '點擊立即前往抽獎',
        showConfirmButton: false,
        timer: 8000,
        timerProgressBar: true,
        didOpen: (toast) => {
          toast.addEventListener('mouseenter', Swal.stopTimer)
          toast.addEventListener('mouseleave', Swal.resumeTimer)
          toast.addEventListener('click', () => {
            this.$router.push('/campaign')
            Swal.close()
          })
          toast.style.cursor = 'pointer'
        },
      })
    },
  },
  async created() {
    this.addMemberChance()
  },
})
</script>

<style scoped>
.order-number-highlight {
  font-size: 1.5rem; /* 字号增大 */
  font-weight: bold; /* 加粗 */
  margin: 20px 0; /* 上下增加间距 */
  display: block; /* 设置为块级元素，便于控制间距 */
}

.step-indicator {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.step {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #6c757d;
  position: relative;
  z-index: 1;
}

.step.active {
  background-color: #000;
  color: white;
}

.step.completed {
  background-color: #198754;
  color: white;
}

.step-connector {
  width: 100px;
  height: 2px;
  background-color: #e9ecef;
  margin: 17px 10px;
}

.step-connector.active {
  background-color: #0d6efd;
}

/* Custom CSS for styling the CTA section */
.cta-section {
  background-color: #f8f9fa;
  padding: 15px 0;
  text-align: center;
}

.cta-heading {
  font-size: 3rem;
  margin-bottom: 35px;
  font-weight: bold;
  text-align: center;
}

.cta-text {
  text-align: center;
  font-size: 1.1rem;
  line-height: 1.8;
}

.table {
  width: 85%;
  text-align: center;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.cta-btn {
  font-size: 1.25rem;
  padding: 12px 30px;
}

.button-container {
  display: flex;
  justify-content: center;
  gap: 20px; /* 自定义间距 */
  margin-top: 2rem;
  margin-bottom: 5rem;
}
</style>
