<template>
  <div class="container my-4">
  <div class="row justify-content-center">
    <div class="col-12 col-md-8 col-lg-6">
      <div class="card h-100 shadow-sm">
        <div class="card-body d-flex flex-column justify-content-center text-center">
          <h2 class="card-title mb-3">優惠碼資料</h2>

          <div v-if="redeemedPrize">
            <hr />
            <p class="card-text">目前會員ID是: {{ memberId }}</p>
            <p class="card-text">目前您持有的優惠卷:</p>
            <br />
            <ul class="list-unstyled">
              <li v-for="item in memberPromoCode" :key="item.promoCode">
                <font-awesome-icon :icon="['fas', 'martini-glass']" />{{ item.pointPrizes.pointPrizesName }} : {{ item.promoCode }}
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
      const api = `${import.meta.env.VITE_API}/api/showPromoCode/${this.memberId}`

      const response = await this.axios.get(api)
      this.memberPromoCode = response.data

      console.log('API Response:', response) // 打印 API 回应以便检查
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
  },

  created() {
    console.log('子组件 created', this.memberId)
    this.getPromoCode()
  },
}
</script>

<style scoped>
.card {
  height: 400px;
  background-color: #f8f9fa;
}
</style>
