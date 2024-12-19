<template>
  <div>

    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title mb-3">Apply Promo Code</h5>
          <div class="input-group mb-3">
            <input type="text" class="form-control" placeholder="Enter promo code" v-model="inputPromoCode">
            <button class="btn btn-outline-secondary" type="button" @click="verifyPromoCode">Apply</button>
          </div>
      </div>
    </div>

  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'
const user = useUserStore()

  export default {
  data() {
    return {
      inputPromoCode: "",
      promoCode: [],
    }
  },
  methods: {
    async verifyPromoCode() {
      const api = `${import.meta.env.VITE_API}/api/showPromoCode/${user.memberId}`
      const response = await this.axios.get(api)
      this.promoCode = response.data

      const filterCode = this.promoCode.filter((code)=>code.promoCode===this.inputPromoCode)
      console.log(filterCode)

      // 因為 filter 返回陣列，所以需要取第一個元素
      if (filterCode.length > 0) {
          const pointPrizesName = filterCode[0].pointPrizes.pointPrizesName
          const pointPrizesImg = filterCode[0].pointPrizes.pointPrizesImg
          console.log(pointPrizesName)
          alert("兌換成功")

        // 發送事件到父組件
        this.$emit('promo-code', { name: pointPrizesName, image: pointPrizesImg });
      } else {
          console.log('找不到符合的促銷碼')
      }

    }

  },
  computed: {
  },
  watch: {},
  created() {},
}
</script>

<style scoped>

</style>
