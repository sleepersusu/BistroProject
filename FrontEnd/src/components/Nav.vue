<template>
  <nav
    class="navbar navbar-expand-lg bg-dark navbar-dark sticky-top"
    :class="{ 'nav-shadow': setShadow, 'navbar-shrink': setShadow }"
  >
    <div class="container">
      <router-link class="navbar-brand text-primary" to="/index">BistroRestaurant</router-link>
      <button
        class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav"
      >
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNav">
        <ul class="navbar-nav ms-auto align-items-center">
          <li class="nav-item">
            <a class="nav-link" href="#">關於我們</a>
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
            <router-link class="nav-link position-relative" to="/cart"
              ><i class="bi bi-cart fs-5"></i
              ><span
                class="position-absolute top-5 start-100 translate-middle badge rounded-pill bg-primary text-dark"
              >
                3
                <span class="visually-hidden">unread messages</span>
              </span></router-link
            >
          </li>
          <li class="nav-item ms-lg-5">
            <router-link class="btn btn-login" to="/login">登入 / 註冊</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  data() {
    return {
      setShadow: false,
    }
  },
  methods: {
    navShadow() {
      requestAnimationFrame(() => {
        this.setShadow = window.scrollY > 100
      })
    },
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
  box-shadow: 0 0 12px var(--bs-primary) !important;
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
  font-size: 1.75rem;
  font-weight: 600;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.navbar-shrink .navbar-brand {
  font-size: 1.5rem;
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
  background: var(--bs-primary);
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
  color: var(--bs-primary) !important;
}

.navbar-toggler {
  border: none;
  padding: 0.5rem 0.75rem;
}

.navbar-toggler:focus {
  box-shadow: none;
  outline: none;
}

.btn-login {
  background-color: var(--bs-primary);
  color: var(--bs-dark);
  padding: 0.5rem 1.5rem;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.btn-login:hover,
.btn-login.active {
  background-color: transparent;
  color: var(--bs-primary);
  border: 1px solid var(--bs-primary);
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
