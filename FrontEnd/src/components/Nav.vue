<template>
  <nav class="navbar navbar-expand-lg bg-dark navbar-dark sticky-top"
    :class="{ 'nav-shadow': setShadow, 'navbar-shrink': setShadow }">
    <div class="container">
      <router-link class="navbar-brand text-light" to="/index">Nightly Sips</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto align-items-center">
          <li class="nav-item">
            <router-link class="nav-link" to="/profile">關於我們</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/order">立即點餐</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/campaign">限時抽獎</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/menu">精選菜單</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/reservations">馬上訂位</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/memberPoints">會員獎品</router-link>
          </li>

          <li class="nav-item">
            <router-link class="nav-link position-relative" to="/cart">
              <i class="bi bi-cart fs-5"></i>
              <span class="position-absolute top-5 start-100 translate-middle badge rounded-pill bg-light text-primary">
                3
                <span class="visually-hidden">unread messages</span>
              </span></router-link>
          </li>
          <li class="nav-item ms-lg-5">
            <!-- 如果已登入，顯示頭像；否則顯示登入/註冊按鈕 -->
            <div v-if="!userStore.isLoggedIn" class="btn btn-outline-light" v-on:click="openLoginModal">登入 / 註冊</div>
            <div v-else class="d-flex align-items-center">
              <router-link to="/membercenter" class="d-flex align-items-center">
                <!-- 頭像 -->
                <div class="circle-avatar" :style="{ backgroundImage: `url(${userAvatar})` }"></div>
                <!-- 會員名稱 -->
                <span class="text-light ms-2">{{ username }}</span>
              </router-link>
            </div>
          </li>
          <login ref="loginModal" @user-login="handleLogin"></login>
        </ul>
      </div>
    </div>
  </nav>


</template>

<script>
import Login from './login.vue';
import { useUserStore } from '@/stores/userStore';
const userStore=useUserStore();
export default {
  data() {
    return {
      setShadow: false,
      userAvatar: '',
      username:'',
      userStore: useUserStore(),
    }
  }, components: {
    Login
  },
  methods: {
    navShadow() {
      requestAnimationFrame(() => {
        this.setShadow = window.scrollY > 100
      })
    },
    openLoginModal(){
      this.$refs.loginModal.openLoginModal()
    },
    handleLogin(userImg,username){
      userStore.setLoggedIn();
      this.userAvatar = userImg;
      this.username = username;
    }
  },
  mounted() {
    window.addEventListener('scroll', this.navShadow)
    this.navShadow()
  },
  unmounted() {
    window.removeEventListener('scroll', this.navShadow)
  },
}
</script>

<style>
.nav-shadow {
  box-shadow: 0 0 12px #f5e6d3 !important;
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
  background: #f5e6d3;
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
  color: #f5e6d3 !important;
}

.navbar-toggler {
  border: none;
  padding: 0.5rem 0.75rem;
}

.navbar-toggler:focus {
  box-shadow: none;
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
