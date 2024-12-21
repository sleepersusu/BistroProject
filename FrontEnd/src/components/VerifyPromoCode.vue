<template>
  <div>
    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title mb-3">Apply Promo Code</h5>
        <div class="input-group mb-3">
          <input
            type="text"
            class="form-control"
            placeholder="Enter promo code"
            v-model="inputPromoCode"
            @keyup.enter="checkAndVerifyPromoCode"
          />
          <button class="btn btn-outline-secondary" type="button" @click="checkAndVerifyPromoCode">
            Apply
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { pointStore } from '@/stores/pointStore'
import { mapState, mapActions } from 'pinia'
import { useUserStore } from '@/stores/userStore'

export default {
  data() {
    return {
      inputPromoCode: '',
    }
  },
  methods: {
    ...mapActions(pointStore, ['verifyPromoCode']),

    async checkAndVerifyPromoCode() {
      try {
        // 檢查是否為空
        if (!this.inputPromoCode.trim()) {
          window.Swal.fire({
            icon: 'warning',
            title: '請輸入優惠碼',
            showConfirmButton: false,
            timer: 1500,
          })
          return
        }

        // 檢查是否已經使用過
        if (this.pointPrizes.some((prize) => prize.promoCode === this.inputPromoCode)) {
          window.Swal.fire({
            icon: 'error',
            title: '優惠碼已使用',
            text: '此優惠碼已在購物車中',
          })
          return
        }

        // 驗證優惠碼
        await this.verifyPromoCode(this.inputPromoCode)

        // 成功後清空輸入框
        this.inputPromoCode = ''

        window.Swal.fire({
          icon: 'success',
          title: '優惠碼套用成功',
          showConfirmButton: false,
          timer: 1500,
        })
      } catch (error) {
        console.error('驗證優惠碼失敗:', error)
        window.Swal.fire({
          icon: 'error',
          title: '優惠碼無效',
          text: '請確認優惠碼是否正確',
        })
      }
    },
  },
  computed: {
    ...mapState(pointStore, ['promoCode', 'pointPrizes']),
  },
}
</script>
