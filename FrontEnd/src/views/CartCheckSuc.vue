<template>
  <div class="text-center ">
      <i class="bi bi-check-circle-fill " style="font-size: 5rem" ></i>
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
      <h2 class="cta-heading">THANK YOU ! </h2>
      <div class=" d-flex justify-content-center">
      <p style="text-align: center">感謝您的訂購！
        <br>親愛的顧客，感謝您選擇酌夜語，您的訂單已成功完成！
        <br>我們將盡快為您準備並安排配送，期待能為您提供一份令人滿意的體驗。
      </p>
      </div>
      <br>
      <a href="#" class="btn btn-primary cta-btn mt-5 mb-5">Back to Home</a>
    </div>
    <br><br><br><br><br><br><br><br><br><br><br><br>




  </section>
</template>
<script>
import { defineComponent } from 'vue'
import BannerTop from '@/components/BannerTop.vue'

export default defineComponent({
  components: { BannerTop },
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
