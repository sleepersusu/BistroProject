<template>
  <nav
    class="navbar navbar-expand-lg bg-dark navbar-dark sticky-top"
    :class="{ 'nav-shadow': setShadow, 'navbar-shrink': setShadow }"
  >
    <div class="container">
      <router-link class="navbar-brand text-light" to="/index">Nightly Sips</router-link>
      <div class="d-flex align-items-center justify-content-end">
        <div v-if="!isLoggedIn" class="btn btn-soft-gold-glow d-block d-lg-none" v-on:click="openLoginModal">
              登入 / 註冊
            </div>
        <div v-else
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
          <li class="nav-item ms-lg-5 d-none d-lg-block">
            <!-- 如果已登入，顯示頭像；否則顯示登入/註冊按鈕 -->
            <div v-if="!isLoggedIn" class="btn btn-soft-gold-glow" v-on:click="openLoginModal">
              登入 / 註冊
            </div>
            <div v-else class="d-flex align-items-center">
              <!-- 頭像 -->
              <div
                class="circle-avatar "
                v-on:click="triggerOffcanvas"
                :style="{ backgroundImage: `url(${memberprofile?.userAvatar})` }"
              ></div>
              
              <div v-on:click="triggerOffcanvas" class="d-flex align-items-center ms-2">
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

/* 頭像設計 */
.circle-avatar {
  width: 40px;              /* 頭像的大小 */
  height: 40px;
  border-radius: 50%;       /* 使其為圓形 */
  background-size: cover;  /* 背景圖片覆蓋 */
  background-position: center;
  cursor: pointer;         /* 鼠標懸停時顯示為可點擊 */
  transition: transform 0.3s ease, box-shadow 0.3s ease, opacity 0.3s ease; /* 添加過渡效果 */
  box-shadow: 0 6px 20px rgba(255, 255, 255, 0.2); /* 頭像的陰影效果，亮色陰影 */
}

/* 懸停效果：當用戶將鼠標懸停在頭像上時，加入陰影並放大 */
.circle-avatar:hover {
  box-shadow: 0 10px 30px rgba(255, 255, 255, 0.3); /* 增強亮色陰影 */
  transform: scale(1.1); /* 微微放大效果 */
  opacity: 0.9; /* 當懸停時稍微變透明 */
}

/* 會員名稱樣式 */
.d-flex.align-items-center {
  display: flex;
  align-items: center;
}

/* 會員名稱文字樣式 */
.d-flex.align-items-center span {
  font-size: 1rem;         /* 字體大小 */
  font-weight: 600;        /* 字重 */
  color: #ffffff;          /* 文字顏色（白色） */
  text-shadow: 0 2px 5px rgba(255, 255, 255, 0.2); /* 文字陰影，亮色陰影 */
  transition: transform 0.3s ease, text-shadow 0.3s ease; /* 文字放大和陰影過渡效果 */
}

/* 會員名稱的懸停效果：當鼠標懸停時，文字與頭像一起放大並增強陰影 */
.d-flex.align-items-center:hover span {
  transform: scale(1.1); /* 文字和頭像一起微微放大 */
  text-shadow: 0 3px 10px rgba(255, 255, 255, 0.3); /* 增強亮色陰影 */
}

/* 響應式設計：當屏幕寬度小於992px時，頭像和名字的排版調整 */
@media (max-width: 992px) {
  .circle-avatar {
    width: 35px; /* 在小螢幕上略微縮小頭像 */
    height: 35px;
  }

  .d-flex.align-items-center span {
    font-size: 0.875rem; /* 在小螢幕上縮小字體大小 */
  }
}

.btn-soft-gold-glow {
  background: linear-gradient(45deg, #ffd700, #ffcc00);
  border: none;
  color: #333333;
  padding: 10px 20px;
  font-size: 1rem;
  font-weight: bold;
  border-radius: 30px;
  box-shadow: 0 0 5px rgba(255, 215, 0, 0.4), 0 0 10px rgba(255, 215, 0, 0.3);
  transition: background 0.3s, transform 0.2s, box-shadow 0.3s;
}

/* 在螢幕寬度小於 992px 時縮小按鈕 */
@media (max-width: 992px) {
  .btn-soft-gold-glow {
    padding: 8px 16px;  /* 減小按鈕的內邊距 */
    font-size: 0.9rem;  /* 減小字體大小 */
    border-radius: 25px; /* 減小圓角 */
  }
}

/* 在螢幕寬度小於 768px 時，按鈕進一步縮小 */
@media (max-width: 768px) {
  .btn-soft-gold-glow {
    padding: 6px 12px;  /* 更小的內邊距 */
    font-size: 0.85rem;  /* 更小的字體大小 */
    border-radius: 20px; /* 更小的圓角 */
  }
}

/* 在螢幕寬度小於 576px 時，按鈕再縮小一些 */
@media (max-width: 576px) {
  .btn-soft-gold-glow {
    padding: 5px 10px;  /* 更小的內邊距 */
    font-size: 0.75rem;  /* 更小的字體大小 */
    border-radius: 15px; /* 更小的圓角 */
  }
}

/* 這是縮放效果的變化，按鈕在 hover 時的效果 */
.btn-soft-gold-glow:hover {
  background: linear-gradient(45deg, #e6b800, #ffbb33);
  transform: scale(1.05); /* 微微放大效果 */
  box-shadow: 0 0 10px rgba(255, 215, 0, 0.5), 0 0 15px rgba(255, 215, 0, 0.4);
}

/* 預設聚焦效果 */
.btn-soft-gold-glow:focus {
  outline: none;
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
