<template>
  <div>
    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title mb-3">輸入獎品兌換碼</h5>
        <div class="input-group mb-3">
          <input
            type="text"
            class="form-control"
            placeholder="獎品兌換碼"
            v-model="inputPromoCode"
            @keyup.enter="checkAndVerifyPromoCode"
          />
          <button class="btn btn-outline-secondary" type="button" @click="checkAndVerifyPromoCode">
            傳送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { pointStore } from '@/stores/pointStore'
import { mapState, mapActions } from 'pinia'

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
        // 直接檢查是否已使用過
        if (this.pointPrizes.some((prize) => prize.promoCode === this.inputPromoCode)) {
          window.Swal.fire({
            icon: 'error',
            title: '優惠碼已使用',
            text: '此優惠碼已在購物車中',
          })
          return
        }

        // 如果有輸入才進行驗證
        // 呼叫pinia處存的verifyPromoCode方法
        if (this.inputPromoCode) {
          await this.verifyPromoCode(this.inputPromoCode)
          // 成功後清空輸入框
          this.inputPromoCode = ''

          window.Swal.fire({
            icon: 'success',
            iconColor: 'black',
            title: '優惠碼套用成功',
            showConfirmButton: false,
            timer: 1500,
          })
        }
      } catch (error) {
        console.error('驗證優惠碼失敗:', error)
        window.Swal.fire({
          icon: 'error',
          iconColor: 'black',
          title: '優惠碼無效',
          text: '請確認優惠碼是否正確',
          confirmButtonColor: '#000000'
        })
      }
    },
  },
  computed: {
    ...mapState(pointStore, ['promoCode', 'pointPrizes']),
  },
}
</script>
