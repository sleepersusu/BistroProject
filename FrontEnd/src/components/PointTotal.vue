<template>
  <div class="container my-4">
    <div class="row justify-content-center">
      <div class="col-12 col-md-8 lg-6">
        <div class="promo-card">
          <div class="promo-content">
            <h2 class="promo-title">優惠券列表</h2>
            <div class="divider">
            </div>

            <div class="coupon-list">
              <ul class="coupon-items">
                <li v-for="item in memberPromoCode" :key="item.promoCode" class="coupon-item">
                  <div class="coupon-content">
                    <div class="coupon-main">
                      <div class="coupon-details">
                        <span class="coupon-name">{{ item.pointPrizes.pointPrizesName }}</span>
                        <span class="coupon-code">{{ item.promoCode }}</span>
                      </div>
                    </div>
                    <button
                      class="use-btn"
                      @click="usePromoCode(item)"
                      :disabled="!hasCartItems"
                      :title="!hasCartItems ? '購物車需要至少一份餐點' : '使用優惠券'"
                    >
                      使用優惠券
                    </button>
                  </div>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'
import { mapState, mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore.js'
import { pointStore } from '@/stores/pointStore'

export default {
  data() {
    return {
      memberPromoCode: [],
    }
  },

  methods: {
    ...mapActions(pointStore, ['addToCart']),

    async getPromoCode() {
      const api = `${import.meta.env.VITE_API}/api/showPromoCode/${this.memberId}`
      const response = await this.axios.get(api)
      this.memberPromoCode = response.data
    },

    async usePromoCode(item) {
      if (!this.hasCartItems) {
        window.Swal.fire({
          icon: 'warning',
          title: '無法使用優惠券',
          text: '購物車需要至少一份餐點才能使用優惠券',
        })
        return
      }

      try {
        await this.addToCart({
          name: item.pointPrizes.pointPrizesName,
          promoCode: item.promoCode,
          img: item.pointPrizes.pointPrizesImg,
        })

        window.Swal.fire({
          icon: 'success',
          title: '已加入購物車',
          showConfirmButton: false,
          timer: 1500,
        })
      } catch (error) {
        console.error('添加優惠券失敗:', error)
        window.Swal.fire({
          icon: 'error',
          title: '優惠券添加失敗',
          text: '請稍後再試',
        })
      }
    },
  },

  computed: {
    ...mapState(useUserStore, ['memberId']),
    ...mapState(cartStore, ['cartItems']),

    hasCartItems() {
      return this.cartItems && this.cartItems.length > 0
    },
  },

  created() {
    this.getPromoCode()
  },
}
</script>

<style scoped>
/* 卡片基本樣式 */
.promo-card {
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  padding: 2.5rem;
  min-height: 500px;
  position: relative;
  border: 1px solid #f5f5f5;
}

/* 標題樣式 */
.promo-title {
  color: #2c3e50;
  font-family: 'Noto Serif TC', serif;
  font-size: 2.2rem;
  text-align: center;
  margin-bottom: 1.5rem;
  font-weight: 600;
}

/* 分隔線樣式 */
.divider {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 2rem 0;
}

/* 優惠券列表樣式 */
.coupon-items {
  list-style: none;
  padding: 0;
}

.coupon-item {
  background: #faf9f7;
  border: 1px solid #f0ebe5;
  border-radius: 12px;
  margin-bottom: 0.5rem;
  padding: 0.5rem 1rem;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.coupon-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  background: linear-gradient(to right, #faf9f7, #ffffff);
}

/* 優惠券內容布局 */
.coupon-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  gap: 1rem;
  padding: 0.5rem 0;
}

.coupon-main {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  flex: 1;
}

.coupon-icon {
  color: #9b8579;
  font-size: 1.2rem;
  flex-shrink: 0;
}

.coupon-details {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  flex: 1;
}

.coupon-name {
  color: #2c3e50;
  font-size: 1rem;
  font-weight: 500;
  white-space: nowrap;
  margin: 0;
}

.coupon-code {
  color: #9b8579;
  font-family: 'Roboto Mono', monospace;
  font-size: 0.9rem;
  letter-spacing: 1px;
  background: rgba(155, 133, 121, 0.1);
  padding: 0.2rem 0.6rem;
  border-radius: 4px;
  white-space: nowrap;
}

/* 按鈕樣式 */
.use-btn {
  background: #9b8579;
  color: white;
  border: none;
  padding: 0.3rem 0.8rem;
  border-radius: 6px;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  white-space: nowrap;
  flex-shrink: 0;
}

.use-btn:hover:not(:disabled) {
  background: #8a7468;
  transform: translateY(-1px);
}

.use-btn:disabled {
  background: #e0e0e0;
  cursor: not-allowed;
}

/* 工具提示樣式 */
.use-btn[title] {
  position: relative;
}

.use-btn[title]:hover:disabled::after {
  content: attr(title);
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 0.5rem;
  border-radius: 4px;
  font-size: 0.8rem;
  white-space: nowrap;
  margin-bottom: 5px;
}

/* 動畫效果 */
.coupon-item {
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 響應式設計 */
@media (max-width: 768px) {
  .promo-card {
    padding: 1.5rem;
  }

  .promo-title {
    font-size: 1.8rem;
  }

  .coupon-details {
    gap: 1rem;
  }

  .coupon-name {
    font-size: 0.9rem;
  }

  .coupon-code {
    font-size: 0.8rem;
  }

  .use-btn {
    padding: 0.2rem 0.6rem;
    font-size: 0.8rem;
  }
}
</style>
