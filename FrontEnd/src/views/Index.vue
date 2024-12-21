<template>
  <div class="site-wrapper">
    <div class="background-container">
      <img class="background-image" src="/images/index-banner-bg.jpg" alt="background" />
    </div>

    <div class="content-wrapper">
      <section class="hero-section">
        <div id="carouselExampleIndicators" class="carousel slide" data-bs-ride="carousel">
          <div class="carousel-indicators">
            <button
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide-to="0"
              class="active"
              aria-current="true"
              aria-label="Slide 1"
            ></button>
            <button
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide-to="1"
              aria-label="Slide 2"
            ></button>
            <button
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide-to="2"
              aria-label="Slide 3"
            ></button>
          </div>
          <div class="carousel-inner">
            <div class="carousel-item active">
              <img
                src="https://images.unsplash.com/photo-1514361659284-59851c90d011?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                class="d-block w-100"
                alt="..."
              />
            </div>
            <div class="carousel-item">
              <img
                src="https://images.unsplash.com/photo-1514933651103-005eec06c04b?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
                class="d-block w-100"
                alt="..."
              />
            </div>
            <div class="carousel-item">
              <img src="/images/DSC_9550-1.jpg" class="d-block w-100" alt="..." />
            </div>
          </div>
          <button
            class="carousel-control-prev"
            type="button"
            data-bs-target="#carouselExampleIndicators"
            data-bs-slide="prev"
          >
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Previous</span>
          </button>
          <button
            class="carousel-control-next"
            type="button"
            data-bs-target="#carouselExampleIndicators"
            data-bs-slide="next"
          >
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="visually-hidden">Next</span>
          </button>
        </div>

        <div class="hero-overlay d-flex flex-column justify-content-center">
          <div class="container text-white" data-aos="fade-up" data-aos-duration="1500">
            <h1 class="hero-title mb-2">Welcome to <span class="highlight">Nightly Sips</span></h1>
            <p class="hero-subtitle mb-md-4 mb-3">用餐不只是味蕾的享受，更是一場難忘的夜晚</p>
            <div
              class="hero-buttons mb-md-5"
              data-aos="fade-up"
              data-aos-duration="1500"
              data-aos-delay="500"
            >
              <router-link to="/menu" class="btn btn-outline-light me-3">查看菜單</router-link>
              <router-link to="/reservations" class="btn btn-primary">立即訂位</router-link>
            </div>
          </div>
        </div>
      </section>

      <section class="menu-section bg-light">
        <div class="container">
          <h2
            class="text-center text-dark pt-5"
            style="font-family: 'Chakra Petch', cursive !important"
          >
            Popular Dishes
          </h2>
          <div class="row py-5 mx-4" data-aos="fade-up" data-aos-duration="1000">
            <div class="col-md-4 mt-4" v-for="menu in menus" :key="menu.id">
              <ProductCard :menu="menu"></ProductCard>
            </div>
          </div>
        </div>
      </section>

      <section class="parallax-section">
        <div class="container">
          <div class="row justify-content-center">
            <div class="col-md-8 text-center text-white">
              <h2
                class="display-6 mb-4"
                data-aos="fade-up"
                data-aos-duration="1000"
                data-aos-delay="50"
              >
                夜色中的微醺時光
              </h2>
              <p class="lead fs-5" data-aos="fade-up" data-aos-duration="1000" data-aos-delay="100">
                精心調製的餐酒搭配，讓每一口都是味蕾的探索
              </p>
              <p class="lead fs-5" data-aos="fade-up" data-aos-duration="1000" data-aos-delay="150">
                在這裡，讓我們一同品味夜晚的寧靜與優雅
              </p>
            </div>
          </div>
        </div>
      </section>

      <section class="bg-light">
        <div class="container">
          <ZigzagSection></ZigzagSection>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import ProductCard from '@/components/ProductCard.vue'
import ZigzagSection from '@/components/ZigzagSection.vue'

export default {
  components: {
    ProductCard,
    ZigzagSection,
  },
  data() {
    return {
      menus: [],
    }
  },
  methods: {
    loadTop3Menu() {
      let API_URL = `${import.meta.env.VITE_API}/api/menu/topThree`
      this.axios.get(API_URL).then((response) => {
        this.menus = response.data
      })
    },
  },
  created() {
    this.loadTop3Menu()
  },
}
</script>

<style scoped>
.carousel-inner {
  height: 850px;
}

.carousel-item {
  height: 100%;
}

.carousel-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.site-wrapper {
  position: relative;
  min-height: 100vh;
  width: 100%;
  overflow: hidden;
}

.background-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  z-index: -1;
}

.background-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.content-wrapper {
  position: relative;
  z-index: 1;
}

.hero-section {
  position: relative;
}

.carousel-inner {
  height: 850px;
}

.carousel-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to right, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.3) 100%);
  z-index: 1;
  pointer-events: none;
}

.hero-buttons {
  position: relative;
  z-index: 3;
  pointer-events: auto;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 300;
  letter-spacing: 1px;
}

.highlight {
  color: #d4af37;
  font-weight: 400;
}

.hero-subtitle {
  font-size: 1.25rem;
  font-weight: 300;
  letter-spacing: 0.5px;
  opacity: 0.9;
}

.hero-buttons .btn {
  padding: 0.8rem 2rem;
  font-size: 0.9rem;
  letter-spacing: 2px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #d4af37;
  border-color: #d4af37;
}

.btn-primary:hover {
  background-color: #bf9b30;
  border-color: #bf9b30;
}

.btn-outline-light:hover {
  color: #bf9b30;
  background-color: rgba(255, 255, 255, 0.1);
}

.parallax-section {
  position: relative;
  min-height: 400px;
  display: flex;
  align-items: center;
  padding: 4rem 0;
}

@media (min-width: 768px) and (max-width: 991.98px) {
  .carousel-inner {
    height: 600px;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }

  .hero-buttons .btn {
    padding: 0.6rem 1.5rem;
    font-size: 0.8rem;
  }
}

@media (max-width: 767.98px) {
  .carousel-inner {
    height: 400px;
  }

  .hero-title {
    font-size: 2rem;
  }

  .parallax-section {
    min-height: 350px;
  }

  .hero-buttons .btn {
    padding: 0.5rem 0.8rem;
    font-size: 0.8rem;
  }

  .parallax-section {
    min-height: 300px;
    padding: 2rem 0;
  }
}

@media (max-width: 469px) {
  .hero-title {
    font-size: 1.5rem;
  }

  .hero-subtitle {
    font-size: 0.9rem;
  }

  .hero-buttons .btn {
    padding: 0.5rem 0.8rem;
    font-size: 0.8rem;
  }

  .parallax-section {
    min-height: 300px;
    padding: 2rem 0;
  }
}
</style>
