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
              <img src="/images/banner2.jpg" class="d-block w-100" alt="..." />
            </div>
            <div class="carousel-item">
              <img src="/images/banner3.jpg" class="d-block w-100" alt="..." />
            </div>
            <div class="carousel-item">
              <img src="/images/banner1.jpg" class="d-block w-100" alt="..." />
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
            <h1 class="hero-title mb-md-3">
              Welcome to
              <span
                class="highlight"
                data-aos="fade-up"
                data-aos-duration="3000"
                data-aos-delay="500"
              >Nightly Sips</span
              >
            </h1>
            <p class="hero-subtitle mb-md-4 mb-3">
              用餐不只是味蕾的享受，更是一場難忘的夜晚
              <br />在這裡，每道料理都是故事，每個夜晚都值得紀念。
            </p>
            <div class="hero-buttons mb-md-5">
              <router-link to="/menu" class="btn btn-outline-light me-3">View Menu</router-link>
              <router-link to="/reservations" class="btn btn-primary text-dark"
              >Book Now
              </router-link>
            </div>
          </div>
        </div>
      </section>

      <section class="menu-section bg-light">
        <div class="container">
          <h2
            class="text-center text-dark pt-5 mb-3"
            style="font-family: 'Chakra Petch', cursive !important"
          >
            Popular Dishes
          </h2>
          <div v-if="isLoggedIn" class="text-center mb-1 text-muted">
            <small>為您精選菜單，讓每一次用餐都充滿驚喜與感動。</small>
          </div>

          <!-- Loading Animation -->
          <div v-if="isLoading" class="loading-container">
            <div class="loading-spinner"></div>
            <p class="loading-text">載入推薦菜色中....</p>
          </div>

          <!-- Menu Cards -->
          <div v-else class="row py-5 mx-4" data-aos="fade-up" data-aos-duration="1000">
            <div class="col-md-4 mt-4" v-for="(menu, index) in menus" :key="menu.ID">
              <div class="position-relative">
                <!-- Ranking Ribbon -->
                <div v-if="index < 3" class="ribbon" :class="'ribbon-' + (index + 1)">
                  {{ index + 1 }}
                </div>
                <ProductCard :menu="menu">
                  <template v-if="menu.score" #extra>
                    <div class="recommendation-score">
                      {{ getRecommendationText(menu.score) }}
                    </div>
                  </template>
                </ProductCard>
              </div>
            </div>
          </div>
        </div>
      </section>

      <section class="parallax-section">
        <div class="container">
          <div class="row justify-content-center">
            <div class="col-md-8 text-center text-white">
              <h2
                class="lead fs-2 mb-4"
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
import { useUserStore } from '@/stores/userStore.js'
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
      isLoading: false
    }
  },
  computed: {
    userStore() {
      return useUserStore()
    },
    isLoggedIn() {
      return this.userStore.checkLogin
    },
    userId() {
      return this.userStore.memberId
    },
  },
  methods: {
    async loadTop3Menu() {
      try {
        this.isLoading = true  // 開始載入
        if (this.isLoggedIn && this.userId) {
          await this.loadPersonalizedRecommendations()
        } else {
          await this.loadDefaultMenu()
        }
      } catch (error) {
        console.error('載入菜單失敗:', error)
        this.menus = []
      } finally {
        setTimeout(() => {
          this.isLoading = false  // 結束載入
        }, 500) // 添加一個小延遲使過渡更順暢
      }
    },

    async loadPersonalizedRecommendations() {
      try {
        console.log('當前用戶ID:', this.userId)
        const API_URL = `${import.meta.env.VITE_API}/api/recommendations/user/${this.userId}`

        const response = await this.axios.get(API_URL)
        console.log('API回傳的推薦數據:', response.data)

        const { recommendations } = response.data
        if (!recommendations || !Array.isArray(recommendations)) {
          console.warn('推薦數據格式不正確')
          this.menus = []
          return
        }

        // 根據分數排序並取前三名
        const topThreeRecommendations = recommendations
          .sort((a, b) => b.score - a.score)
          .slice(0, 3)

        console.log('前三名推薦:', topThreeRecommendations)

        // 獲取推薦菜單詳情
        const menuPromises = topThreeRecommendations.map(async (item) => {
          try {
            const findByNameURL = `${import.meta.env.VITE_API}/api/recommendations/menu/findByName`
            console.log('處理商品:', item.productName)

            const menuResponse = await this.axios.get(findByNameURL, {
              params: {
                productName: item.productName
              }
            })

            if (menuResponse.data) {
              console.log(`成功獲取商品 ${item.productName} 的詳情:`, menuResponse.data)
              return {
                ...menuResponse.data,
                score: item.score
              }
            }
            return null

          } catch (error) {
            console.error(`無法找到菜單: ${item.productName}`, error)
            console.error('錯誤詳情:', {
              status: error.response?.status,
              data: error.response?.data
            })
            return null
          }
        })

        const menuDetails = await Promise.all(menuPromises)
        const validMenus = menuDetails.filter(menu => menu !== null)

        if (validMenus.length === 0) {
          console.log('沒有找到任何推薦菜品')
        } else {
          console.log('處理後的推薦菜單數據:', validMenus)
        }

        this.menus = validMenus

      } catch (error) {
        console.error('獲取推薦失敗:', error)
        this.menus = []
      }
    },
    async loadDefaultMenu() {
      if (this.isLoggedIn) {
        console.log('使用者已登入，不載入預設菜單')
        return
      }

      try {
        const API_URL = `${import.meta.env.VITE_API}/api/menu/topThree`
        const response = await this.axios.get(API_URL)
        this.menus = response.data
        console.log('載入預設菜單:', this.menus)
      } catch (error) {
        console.error('載入預設菜單失敗:', error)
        this.menus = []
      }
    },

    getRecommendationText(score) {
      const percentage = score * 100
      if (percentage >= 70) return '最佳推薦'
      if (percentage >= 50) return '強烈推薦'
      if (percentage >= 30) return '推薦嚐鮮'
      return '可能喜歡'
    }
  },

  watch: {
    isLoggedIn: {
      immediate: true,
      async handler(newValue, oldValue) {
        console.log('登入狀態變化:', {
          newValue,
          oldValue,
          userId: this.userId
        })

        // 清空當前菜單
        this.menus = []

        if (newValue && this.userId) {
          console.log('使用者登入，載入個人推薦')
          await this.loadPersonalizedRecommendations()
        } else if (newValue !== oldValue) {
          console.log('使用者登出，載入預設菜單')
          await this.loadDefaultMenu()
        }
      }
    },

    userId: {
      immediate: true,
      async handler(newId, oldId) {
        console.log('使用者 ID 變化:', { newId, oldId })

        if (newId !== oldId && this.isLoggedIn) {
          console.log('使用者 ID 變化，重新載入推薦')
          this.menus = [] // 清空當前菜單
          await this.loadPersonalizedRecommendations()
        }
      }
    }
  },
  created() {
    this.loadTop3Menu()
  },
}
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 4rem 0;
  min-height: 300px;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #000000;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

.loading-text {
  color: #666;
  font-size: 1.1rem;
  margin-top: 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.ribbon {
  position: absolute;
  left: -5px;
  top: -5px;
  z-index: 3;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 1.2rem;
  clip-path: polygon(0 0, 100% 0, 100% 100%, 50% 80%, 0 100%);
}

.ribbon-1 {
  background-color: #f6b704; /* 金色 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.ribbon-2 {
  background-color: #C0C0C0; /* 銀色 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.ribbon-3 {
  background-color: #CD7F32; /* 銅色 */
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

@keyframes shimmer {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.ribbon::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    rgba(255,255,255,0) 0%,
    rgba(255,255,255,0.3) 50%,
    rgba(255,255,255,0) 100%
  );
  animation: shimmer 2s infinite;
}
.recommendation-score {
  position: absolute;
  top: 10px;
  right: 10px;
  background: rgba(255, 220, 53, 0.9);
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: bold;
  z-index: 2;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

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
  font-size: 2.5rem;
  font-weight: 400;
  letter-spacing: 1px;
}

.highlight {
  color: #ffdc35;
  font-size: 4rem;
  text-shadow: 0 4px 35px #ffdc35;
  font-weight: 500;
  font-family: 'Chakra Petch', cursive !important;
}

.hero-subtitle {
  font-size: 1.15rem;
  font-weight: 300;
  letter-spacing: 0.5px;
  opacity: 0.9;
}

.hero-buttons .btn {
  padding: 0.8rem 2rem;
  font-size: 0.9rem;
  letter-spacing: 2px;
  border-radius: 10px;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #ffdc35;
  border-color: #ffdc35;
}

.btn-primary:hover {
  background-color: #ffdc35;
  border-color: #ffdc35;
}

.btn-outline-light:hover {
  color: #ffdc35;
  background-color: rgba(255, 255, 255, 0.1);
}

.parallax-section {
  position: relative;
  min-height: 400px;
  display: flex;
  align-items: center;
  padding: 4rem 0;
  background-color: rgba(0, 0, 0, 0.7);
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

  .highlight {
    font-size: 2.5rem;
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

  .highlight {
    font-size: 2rem;
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

  .parallax-section h2 {
    font-size: 1.25rem;
  }

  .parallax-section p {
    font-size: 1rem !important;
  }
}

.menu-section h2 {
  font-size: 2.8rem; /* 將字體大小設定為 2.5rem */
  letter-spacing: 2px; /* 增加字母間距 */
  color: #333; /* 深色文字顏色 */
}

@media (max-width: 767.98px) {
  .menu-section h2 {
    font-size: 2rem; /* 在較小螢幕上稍微縮小 */
  }
}

@media (max-width: 469px) {
  .menu-section h2 {
    font-size: 1.5rem; /* 在更小的裝置上進一步縮小 */
  }
}
</style>
