<template>
  <nav
    class="navbar navbar-expand-lg bg-dark navbar-dark sticky-top"
    :class="{ 'nav-shadow': setShadow, 'navbar-shrink': setShadow }"
  >
    <div class="container">
      <router-link class="navbar-brand text-light" to="/index">Nightly Sips</router-link>
      <div class="d-flex align-items-center justify-content-end">
        <div
          class="circle-avatar d-block d-lg-none"
          v-on:click="triggerOffcanvas"
          :style="{ backgroundImage: `url(${memberprofile?.userAvatar})` }"
          ></div>
        <div>
        </div>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
      </div>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto align-items-center">
          <li class="nav-item">
            <router-link class="nav-link" to="/profile">關於我們</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link position-relative" to="/campaign"
              >限時抽獎<span
                v-if="memberId && allChances > 0"
                class="position-absolute start-100 translate-middle badge rounded-pill bg-light text-primary"
              >
                {{ allChances }}
                <span class="visually-hidden">unread messages</span>
              </span></router-link
            >
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/menu">精選菜單</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/reservations">馬上訂位</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/memberPoints">點數商城</router-link>
          </li>

          <li class="nav-item">
            <router-link class="nav-link position-relative" to="/cart">
              <i class="bi bi-cart fs-5"></i>

              <span
                v-if="totalCartItems > 0"
                class="position-absolute top-5 start-100 translate-middle badge rounded-pill bg-light text-primary"
              >
                {{ totalCartItems > 99 ? '99+' : totalCartItems }}
                <span class="visually-hidden">unread messages</span>
              </span>
            </router-link>
          </li>
          <li class="nav-item ms-lg-5">
            <!-- 如果已登入，顯示頭像；否則顯示登入/註冊按鈕 -->
            <div v-if="!isLoggedIn" class="btn btn-soft-gold-glow" v-on:click="openLoginModal">
              登入 / 註冊
            </div>
            <div v-else class="d-flex align-items-center">
              <!-- 頭像 -->
              <div
                class="circle-avatar d-none d-lg-block"
                v-on:click="triggerOffcanvas"
                :style="{ backgroundImage: `url(${memberprofile?.userAvatar})` }"
              ></div>
              
              <div class="d-flex align-items-center d-none d-lg-block">
                <!-- 會員名稱 -->
                <span class="text-light ms-2">{{ memberprofile.navName }}</span>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </nav>
  <login ref="loginModal"></login>
  <AvatarProfile ref="avatarProfile"></AvatarProfile>
</template>

<script>
import Login from './login.vue'
import { useUserStore } from '@/stores/userStore'
import { lotteryStore } from '@/stores/lotteryStore'
import { mapState, mapActions } from 'pinia'

import { cartStore } from '@/stores/cartStore.js'

import AvatarProfile from './AvatarProfile.vue'
import { pointStore } from '@/stores/pointStore'

export default {
  data() {
    return {
      setShadow: false,
    }
  },
  components: {
    Login,
    AvatarProfile,
  },
  computed: {
    ...mapState(useUserStore, ['isLoggedIn', 'memberprofile', 'memberId']),
    ...mapState(lotteryStore, ['allChances']),
    ...mapState(cartStore, ['totalCartItems']),
  },
  methods: {
    ...mapActions(useUserStore, ['setLoggedIn', 'checkLoggedIn','loadMemberData']),
    ...mapActions(lotteryStore, ['getAllChanceByMember']),
    ...mapActions(cartStore, ['getCart']),
    ...mapActions(pointStore, ['getMemberPoint']),
    navShadow() {
      requestAnimationFrame(() => {
        this.setShadow = window.scrollY > 100
      })
    },
    openLoginModal() {
      this.$refs.loginModal.openLoginModal()
    },

    async created() {
      this.checkLoggedIn()
      if (this.isLoggedIn) {
        await this.getCart() // 登入後獲取購物車數據
      }
    },

    triggerOffcanvas() {
      //觸發會員右側欄
      const avatarProfileComponent = this.$refs.avatarProfile
      this.loadMemberData()
      avatarProfileComponent.openOffcanvas()
    },
  },
  mounted() {
    window.addEventListener('scroll', this.navShadow)
    this.navShadow()
    this.getAllChanceByMember()
    this.memberprofile.navName = JSON.parse(localStorage.getItem('memberobj'))?.userName
  },
  unmounted() {
    window.removeEventListener('scroll', this.navShadow)
  },
  watch: {
    // 監聽登入狀態變化
    async isLoggedIn(newValue) {
      if (newValue) {
        await this.getCart()
        await this.getMemberPoint()
      }
    },
  },
  created() {
    this.checkLoggedIn()
  },
}
</script>

<style scoped>
.nav-shadow {
  box-shadow: 0 0 12px var(--bs-light) !important;
}

.nav-item {
  font-size: 1rem !important;
}

.navbar {
  padding: 1rem 0;
  min-height: 80px;
  transition: all 0.3s ease;
}

.navbar-shrink {
  padding: 0.5rem 0;
  min-height: 60px;
}

.navbar-brand {
  font-family: 'Chakra Petch', sans-serif !important;
  font-size: 1.75rem;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.navbar-shrink .navbar-brand {
  font-size: 1.25rem;
}

.nav-link {
  font-weight: 500;
  margin: 0 0.5rem;
  padding: 0.5rem 1rem !important;
  position: relative;
  transition: all 0.3s ease;
}

.nav-link::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  background: var(--bs-light);
  left: 50%;
  bottom: 0;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-link:hover::after,
.nav-link.active::after {
  width: 100%;
}

.nav-link:hover,
.nav-link.active {
  color: var(--bs-light) !important;
}

.navbar-toggler {
  border: none;
  padding: 0.5rem 0.75rem;
}

.navbar-toggler:focus {
  box-shadow: none;
  outline: none;
}

.circle-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  cursor: pointer;
}

.circle-avatar:hover {
  opacity: 0.8;
}

.btn-soft-gold-glow {
  background: linear-gradient(45deg, #ffd700, #ffcc00); /* 金色漸變背景 */
  border: none;
  color: white;
  padding: 10px 20px;
  font-size: 1rem;
  font-weight: bold;
  border-radius: 30px; /* 圓角 */
  box-shadow: 0 0 5px rgba(255, 215, 0, 0.4), 0 0 10px rgba(255, 215, 0, 0.3); /* 柔和的發光效果 */
  transition: background 0.3s, transform 0.2s, box-shadow 0.3s;
}

.btn-soft-gold-glow:hover {
  background: linear-gradient(45deg, #e6b800, #ffbb33); /* Hover時的金色漸變 */
  transform: scale(1.05); /* 微微放大效果 */
  box-shadow: 0 0 10px rgba(255, 215, 0, 0.5), 0 0 15px rgba(255, 215, 0, 0.4); /* 更柔和的發光效果 */
}

.btn-soft-gold-glow:focus {
  outline: none; /* 去掉聚焦框 */
}


@media (max-width: 992px) {
  .nav-link {
    margin: 0.5rem 0;
  }

  .nav-link::after {
    left: 0;
    transform: none;
  }

  .nav-link:hover::after {
    width: 100%;
  }

  .btn-reservation {
    margin: 0.5rem 0;
    display: inline-block;
  }
}

</style>
