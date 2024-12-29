<template>
  <div class="d-flex flex-column p-3 bg-dark membernav">
    <ul class="nav flex-column">
      <li class="nav-item">
        <router-link to="/membercenter/index" class="nav-link">
          <span class="icon-circle"><i class="bi bi-house-heart-fill"></i></span> 會員中心
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/membercenter/profile" class="nav-link">
          <span class="icon-circle"><i class="bi bi-person-vcard-fill"></i></span> 會員資料
        </router-link>
      </li>
      <li class="nav-item">
        <router-link to="/membercenter/orders" class="nav-link">
          <span class="icon-circle"><i class="bi bi-briefcase-fill"></i></span> 我的訂單
        </router-link>
      </li>

      <li class="nav-item">
        <router-link to="/membercenter/comment" class="nav-link">
          <span class="icon-circle"><i class="bi bi-chat-square-text-fill"></i></span> 我的評論
        </router-link>
      </li>

      <li class="nav-item">
        <router-link to="/membercenter/lotteryresult" class="nav-link">
          <span class="icon-circle"><i class="bi bi-dice-6-fill"></i></span> 抽獎結果
        </router-link>
      </li>

      <li class="nav-item">
        <router-link to="/membercenter/MerberOwnPrize" class="nav-link">
          <span class="icon-circle"><i class="bi bi-ticket text-white"></i></span> 會員優惠卷
        </router-link>
      </li>

      <li class="nav-item">
        <router-link to="/membercenter/PromoRecord" class="nav-link">
          <span class="icon-circle"><i class="bi bi-card-checklist"></i></span> 兌換紀錄
        </router-link>
      </li>

      <li class="nav-item">
        <router-link to="/index" v-on:click="clearLoggedIn" class="nav-link">
          <span class="icon-circle"><i class="bi bi-door-closed-fill"></i></span> 登出
        </router-link>
      </li>
    </ul>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'
import { mapActions } from 'pinia'
export default {
  methods: {
    ...mapActions(useUserStore, ['clearLoggedIn']),
  },
}
</script>

<style scoped>
.nav-link {
  font-size: 1.1rem;
  color: #adb5bd;
  display: flex;
  align-items: center;
  padding: 12px 15px;
  position: relative;
  transition: all 0.3s ease;
}

.icon-circle {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #6c757d;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  transition: background-color 0.3s ease;
}

.icon-circle i {
  color: #ffffff;
}

.nav-link:hover {
  background-color: #495057;
  color: #ffffff;
}

.nav-link:hover .icon-circle {
  background-color: #ffc107;
}

.membernav {
  min-height: 85vh;

}
</style>
