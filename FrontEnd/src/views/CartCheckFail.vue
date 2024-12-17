<template>
  <div>
    <PageTop v-bind:page1="'Home'" v-bind:page2="'Cart'"></PageTop>
  </div>
  <div class="text-center">
    <i class="bi bi-x-circle-fill" style="font-size: 5rem"></i>
  </div>
  <!-- Step Indicator -->
  <div class="p-5">
    <!-- Step Indicator -->
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
      <h2 class="cta-heading">Fail Shopping !</h2>
      <div class="d-flex justify-content-center">
        <p style="text-align: center">
          訂購失敗
          <br />很抱歉，我們未能處理您的訂單。
          <br />請重新嘗試訂購或聯繫我們的客戶服務以獲得進一步幫助。
        </p>
      </div>
      <br />
      <a href="#" class="btn btn-primary cta-btn mt-5 mb-5">Back to Cart</a>
    </div>
    <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
  </section>
</template>
<script>
import { defineComponent } from 'vue'
import BannerTop from '@/components/BannerTop.vue'
import PageTop from '@/components/PageTop.vue'

export default defineComponent({
  components: { PageTop, BannerTop },
})
</script>
<style scoped>
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
  margin-bottom: 25px;
}

.cta-btn {
  font-size: 1.25rem;
  padding: 12px 30px;
}
</style>
