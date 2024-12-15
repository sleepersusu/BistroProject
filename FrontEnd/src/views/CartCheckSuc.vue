<template>
  <div>
    <PageTop v-bind:page1="'Home'" v-bind:page2="'Cart'"></PageTop>
  </div>
  <div class="text-center">
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
  <section class="cta-section mt-3">
    <div class="container">

      <div class="row">
        <!-- 左側文字 -->
        <div class="col-lg-4 mb-4">
          <div class="cta-text">
            <h3 class="cta-heading ">
              THANK YOU!
            </h3>
            <p>
              感謝您的訂購！<br />
              親愛的顧客，感謝您選擇酌夜語。<br />
              您的訂單已成功完成！<br />
              我們將盡快為您準備並安排配送，<br />
              期待能為您提供一份令人滿意的體驗。
            </p>
            <a href="#" class="btn btn-primary cta-btn mt-3">Back to Home</a>
          </div>
        </div>
        <!-- 右側表格 -->
        <div class="col-lg-8">
          <div class="table-responsive">
            <table class="table table-striped table-hover align-middle">
              <thead class="table-dark">
              <tr>
                <th scope="col">編號</th>
                <th scope="col">產品名稱</th>
                <th scope="col">數量</th>
                <th scope="col">價格</th>
                <th scope="col">總計</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                <th scope="row">1</th>
                <td>紅酒 A</td>
                <td>2</td>
                <td>$100</td>
                <td>$200</td>
              </tr>
              <tr>
                <th scope="row">2</th>
                <td>白酒 B</td>
                <td>1</td>
                <td>$150</td>
                <td>$150</td>
              </tr>
              <tr>
                <th scope="row">3</th>
                <td>香檳 C</td>
                <td>3</td>
                <td>$200</td>
                <td>$600</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </section>
  <div>
    <Orders></Orders>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import BannerTop from '@/components/BannerTop.vue'
import Orders from '@/components/Orders.vue'
import PageTop from '@/components/PageTop.vue'

export default defineComponent({
  components: { PageTop, Orders, BannerTop },
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
  margin-bottom: 35px;
  font-weight: bold;
  text-align: left;
}

.cta-text {
  text-align: left;
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
</style>
