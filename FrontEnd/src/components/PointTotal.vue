<template>
  <div class="container my-4">
    <div class="row justify-content-center">
      <div class="col-12 col-md-8 col-lg-6">
        <div class="card h-100 shadow-sm">
          <div class="card-body d-flex flex-column justify-content-center text-center">
            <h2 class="card-title mb-3">優惠碼資料</h2>

            <!-- 新增顯示兌換商品和優惠碼的區域 -->
            <div v-if="redeemedPrize">
              <p class="card-text">已兌換商品：{{ redeemedPrize.name }}</p>
              <p class="card-text">
                優惠碼：<strong>{{ redeemedPrize.promoCode }}</strong>
              </p>
              <hr />
              <p class="card-text">目前會員ID是:{{ memberId }}</p>
              <br />
              <p class="card-text">您持有的優惠卷有:</p>
              <br />
              <ul>
                <li
                  v-for="item in memberPromoCode"
                  :key="item.promoCode"
                  style="list-style-type: none"
                >
                  {{ item.pointPrizes.pointPrizesName }} : {{ item.promoCode }}
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
export default {
  props: ['redeemedPrize', 'memberId'],

  data() {
    return {
      memberPromoCode: [],
    }
  },

  methods: {
    async getPromoCode() {
      const api = `${import.meta.env.VITE_API}/api/showPromoCode?memberId=${this.memberId}`

      const response = await this.axios.get(api)
      this.memberPromoCode = response.data
    },
  },

  watch: {
    redeemedPrize: {
      handler(newValue) {
        if (newValue && Object.keys(newValue).length > 0) {
          this.getPromoCode()
        }
      },
      deep: true,
    },

    created() {
      this.getPromoCode()
    },
  },
}
</script>

<style scoped>
.card {
  height: 400px;
  background-color: #f8f9fa;
}
</style>
