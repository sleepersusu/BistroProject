<template>
  <Loading :active="isLoading"></Loading>
  <BannerTop :title="'Orders History'"></BannerTop>
  <div class="container">
    <!-- 購物車空的，顯示還沒有訂單紀錄-->
        <div
          class="d-flex justify-content-center align-items-center flex-column my-5"
          v-if="cartItems.length === 0"
        >
          <i class="bi bi-emoji-frown display-1 mb-3"></i>
          <h3 class="mb-5">目前還沒有訂單紀錄!</h3>
          <router-link to="/menu" class="btn btn-primary btn-lg py-3 px-5">去購物吧!</router-link>
        </div>
    <!-- 購物車有東西，顯示資訊-->
        <div >
          <Orders></Orders>
        </div>
  </div>

</template>
<script>
import Orders from '@/components/Orders.vue'
import BannerTop from '@/components/BannerTop.vue'
import { statusStore } from '@/stores/statusStore'
import { useUserStore } from '@/stores/userStore'
import { ref } from 'vue'
import axios from 'axios'
import { storeToRefs } from 'pinia'
import { utils } from '@/mixins/utils.js'
const user = useUserStore()
const status = statusStore()
const { isLoading1 } = storeToRefs(status)

const lotteryResults = ref([])

export default {
  components: { BannerTop, Orders },
  data() {
    return {
      cartItems: [], // 初始为 null，确保加载后赋值为数组
      isLoading: false,
    };
  },
}
</script>
<style></style>
