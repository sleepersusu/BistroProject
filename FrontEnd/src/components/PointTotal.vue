<template>
  <div class="container my-4">
    <div class="row justify-content-center">
      <div class="col-12 col-md-8 col-lg-6">

        <div class="card h-100 shadow-sm">
          <div class="card-body d-flex flex-column justify-content-center text-center">
            <h2 class="card-title mb-3">優惠碼資料</h2>

            <!-- 新增顯示兌換商品和優惠碼的區域 -->
            <div v-if="redeemedPrize">
              <p class="card-text">
                已兌換商品：{{ redeemedPrize.name }}
              </p>
              <p class="card-text">
                優惠碼：<strong>{{ redeemedPrize.promoCode }}</strong>
              </p>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
// 使用 ref 來存儲兌換的商品信息
const redeemedPrize = ref(null)
// 提供一個方法來更新兌換商品信息
const updateRedeemedPrize = (prizeName, promoCode) => {
  redeemedPrize.value = {
    name: prizeName,
    promoCode: promoCode
  }
}
// 將方法暴露出去，讓父組件可以調用
defineExpose({
  updateRedeemedPrize
})
</script>

<style scoped>
.card {
  height: 400px;
  background-color: #f8f9fa;
}
</style>
