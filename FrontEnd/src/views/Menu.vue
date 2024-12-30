<template>
  <div class="container">
    <BannerTop :title="' Menu'"></BannerTop>
    <section class="py-5 overflow-hidden">
      <div class="container-fluid" style="overflow: auto">
        <div class="row">
          <div class="col-md-12">
            <div class="category-carousel swiper">
              <div class="swiper-wrapper">
                <div>
                  <button class="nav-link category-item swiper-slide" @click="loadAllMenu()">
                    <div>
                      <font-awesome-icon :icon="['fas', 'list']" size="2xl" />
                      <h3 class="category-title">全部</h3>
                    </div>
                  </button>
                </div>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('開胃菜')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'plate-wheat']" size="2xl" />
                    <h3 class="category-title">開胃菜</h3>
                  </div>
                </button>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('主菜')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'utensils']" size="2xl" />
                    <h3 class="category-title">主菜</h3>
                  </div>
                </button>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('飲品')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'champagne-glasses']" size="2xl" />
                    <h3 class="category-title">飲品</h3>
                  </div>
                </button>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('甜點')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'ice-cream']" size="2xl" />
                    <h3 class="category-title">甜點</h3>
                  </div>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <div class="container">
    <div class="row mt-3 align-items-center">
      <div class="col-4">
        <!-- 篩選價錢-->

        <div class="price-range-slider px-4">
          <div class="slider-labels mb-2">
            <span>價格範圍: {{ priceRange.min }} - {{ priceRange.max }}</span>
          </div>

          <div class="slider-container">
            <div
              class="slider-track"
              :style="{
                background: `linear-gradient(
            to right,
            #e5e7eb ${(priceRange.min / 1000) * 100}%,
            #000 ${(priceRange.min / 1000) * 100}%,
            #000 ${(priceRange.max / 1000) * 100}%,
            #e5e7eb ${(priceRange.max / 1000) * 100}%
          )`,
              }"
            ></div>

            <input
              type="range"
              class="range-input min-range"
              min="0"
              max="1000"
              step="100"
              :value="priceRange.min"
              @input="updateMinPrice"
              @mouseup="handleMouseUp"
              @touchend="handleMouseUp"
            />

            <input
              type="range"
              class="range-input max-range"
              min="0"
              max="1000"
              step="100"
              :value="priceRange.max"
              @input="updateMaxPrice"
              @mouseup="handleMouseUp"
              @touchend="handleMouseUp"
            />
          </div>
        </div>
      </div>
      <div class="col-4"></div>

      <div class="col-4">
        <!-- 排序選單 -->
        <select v-model="selectedSort" class="form-select form-select-lg mb-3 styled-select" @change="handleSort">
          <!-- 排序選單選項 -->
          <option value="" disabled>選擇想要的排序方式</option>
          <option value="avgScoreHighToLow" class="selectOption">由評分高排到低</option>
          <option value="avgScoreLowToHigh" class="selectOption">由評分低排到高</option>
          <option value="priceHighToLow" class="selectOption">由價格高排到低</option>
          <option value="priceLowToHigh" class="selectOption">由價格低排到高</option>
        </select>
      </div>
    </div>
  </div>

  <section>
    <div class="container">
      <div v-if="isLoading" class="text-center py-5">
        <div class="loading-spinner">
          <div class="spinner-border text-dark" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
          <p class="mt-2">載入中...</p>
        </div>
      </div>

      <div v-else-if="!isLoading && menus.length === 0" class="no-products">
        <div class="text-center py-5">
          <font-awesome-icon :icon="['fas', 'box-open']" size="4x" class="text-gray-400 mb-3" />
          <h3 class="text-xl font-semibold text-gray-600">沒有符合條件的商品</h3>
          <p class="text-gray-500 mt-2">請調整篩選條件後重試</p>
        </div>
      </div>

      <div v-else-if="menus.length > 0" class="row mt-3">
        <div class="col-md-6 col-lg-4 col-sm-6" v-for="menu in paginatedMenus" :key="menu.id">
          <MenuCard
            :menu="menu"
            @view-menudescribe="openDescribeModal"
            @view-comment="openModal"
            @update-count="updateCount"
            @add-to-cart="handleAddToCart"
          >
          </MenuCard>
        </div>

        <MenuCommentModal
          ref="commentModal"
          :comments="comments"
          :productName="currentProduct"
          :cart-items="cartItems"
        ></MenuCommentModal>
        <MenuDescribeModal ref="menuDescribeModal" :menu="menu"></MenuDescribeModal>
      </div>

      <!-- 分頁按鈕 -->
      <div class="pagination-container">
        <ul class="pagination">
          <li class="page-item">
            <a class="page-link" :disabled="currentPage === 1" @click="changePage(currentPage - 1)"
              >Previous</a
            >
          </li>
          <!-- Page Numbers -->
          <li
            v-for="page in totalPages"
            :key="page"
            class="page-item"
            :class="{ active: page === currentPage }"
          >
            <a class="page-link" @click.prevent="changePage(page)">
              {{ page }}
            </a>
          </li>

          <li class="page-item">
            <a
              class="page-link"
              :disabled="currentPage === totalPages"
              @click="changePage(currentPage + 1)"
              >Next</a
            >
          </li>
        </ul>
      </div>
    </div>
  </section>
</template>

<script>
import axios from 'axios'
import MenuCard from '@/components/MenuCard.vue'
import MenuCommentModal from '@/components/MenuCommentModal.vue'
import MenuDescribeModal from '@/components/MenuDescribeModal.vue'
import PageComponent from '@/components/PageComponent.vue'
import BannerTop from '@/components/BannerTop.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import LoadingVue from 'vue3-loading-overlay'
export default {
  components: {
    MenuCard,
    MenuCommentModal,
    MenuDescribeModal,
    PageComponent,
    BannerTop,
    FontAwesomeIcon,
    LoadingVue,
  },

  data() {
    return {
      menus: [],
      currentProduct: '',
      comments: [],
      menuCount: 0,
      menu: {},
      title: '',
      //分頁
      currentPage: 1, // 當前頁面
      menusPerPage: 6, // 每頁顯示的菜單數量
      totalPages: 1,

      cartItems: [], // 購物車項目列表

      //排序
      selectedSort: '',
      originalMenus:[],

      //價格篩選
      priceRange: {
        min: 0,
        max: 1000,
      },

      isLoading: true,
    }
  },
  methods: {
    async loadAllMenu() {
      try {
        this.isLoading = true
        const response = await axios.get(`${import.meta.env.VITE_API}/api/menu`)
        this.originalMenus = response.data // 保存原始數據
        this.menus = [...response.data] // 用於顯示的拷貝
        this.totalPages = Math.ceil(this.menus.length / this.menusPerPage)
        if (this.selectedSort) this.handleSort()
        this.currentPage=1
      } catch (error) {
        console.error('載入菜單失敗:', error)
      } finally {
        this.isLoading = false
      }
    },

    async updateCount(newCount) {
      this.menuCount = newCount
    },

    // 按分類加載菜單數據
    async clickCategory(category) {
      this.isLoading = true
      try {
        const response = await axios.get(`${import.meta.env.VITE_API}/api/menu/${category}`)
        this.originalMenus = response.data

        // 整合處理數據，只處理一次
        let processedData = [...response.data]
        if (this.priceRange.min >= 0 || this.priceRange.max <= 1000) {
          processedData = this.handlePriceFilter(processedData)
        }else{


        }

          this.sortData(processedData)


        this.menus = processedData
        this.currentPage = 1
      } catch (error) {
        console.error('Error loading menus:', error)
      } finally {
        this.isLoading = false
      }
    },
    //價格篩選
    updateMinPrice(event) {
      const newMin = parseFloat(event.target.value)
      if (newMin < this.priceRange.max) {
        this.priceRange.min = newMin
      } else {
        // 如果超過最大值，將最小值設為最大值
        this.priceRange.min = this.priceRange.max - 100
        event.target.value = this.priceRange.max
      }
    },

    updateMaxPrice(event) {
      const newMax = parseFloat(event.target.value)
      if (newMax > this.priceRange.min) {
        this.priceRange.max = newMax
      } else {
        // 如果小於最小值，將最大值設為最小值
        this.priceRange.max = this.priceRange.min + 100
        event.target.value = this.priceRange.min
      }
    },

    handleMouseUp() {
      this.handlePriceFilter()
    },

    handlePriceFilter() {
      this.menus = this.originalMenus.filter((menu) => {
        const price = menu.productPrice
        return price >= this.priceRange.min && price <= this.priceRange.max
      })

      // 重置到第一頁
      this.currentPage = 1

      // 如果有選擇排序方式，重新應用排序
      if (this.selectedSort) {
        this.handleSort()
      }
    },
    //打開評論
    async openModal(menu) {
      this.currentProduct = menu.productName
      let api = `${import.meta.env.VITE_API}/api/${menu.productName}/comment`
      axios
        .get(api)
        .then((response) => {
          this.comments = response.data
        })
        .catch((error) => {
          console.error('Error loading menus:', error)
        })

      this.$refs.commentModal.showModal()
    },
    //打開描述
    async openDescribeModal(menu) {
      try {
        let api = `${import.meta.env.VITE_API}/api/${menu.id}/menu`
        let response = await this.axios.get(api)
        this.menu = response.data
        this.$refs.menuDescribeModal.showModal()
      } catch (error) {
        console.error('Error loading menu:', error)
      }
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page
      }
    },

    async viewComment(menu) {
      this.$emit('view-comment', menu)
    },
    async viewDescribeModal(menu) {
      this.$emit('view-menudescribe', menu)
    },
    //排序
    async handleSort() {
      switch (this.selectedSort) {
        case 'avgScoreHighToLow': // 當選擇"由高到低"時
          this.menus.sort((a, b) => b.avgScore - a.avgScore)
          break
        case 'avgScoreLowToHigh': // 當選擇"由低到高"時
          this.menus.sort((a, b) => a.avgScore - b.avgScore)
          break
        case 'priceHighToLow': // 當選擇"由高到低"時
          this.menus.sort((a, b) => b.productPrice - a.productPrice)
          break
        case 'priceLowToHigh': // 當選擇"由低到高"時
          this.menus.sort((a, b) => a.productPrice - b.productPrice)
          break
      }
    },
  },

  computed: {
    // 根據當前頁面計算要顯示的菜單
    paginatedMenus() {
      const startIndex = (this.currentPage - 1) * this.menusPerPage
      const endIndex = startIndex + this.menusPerPage
      return this.menus.slice(startIndex, endIndex)
    },
    // 計算總頁數
    totalPages() {
      return Math.ceil(this.menus.length / this.menusPerPage)
    },
  },
  watch: {
    'menus.length'(newLength) {
      if (this.currentPage > Math.ceil(newLength / this.menusPerPage)) {
        this.currentPage = Math.max(1, Math.ceil(newLength / this.menusPerPage))
      }
    },

    selectedSort: {
      handler(newVal) {
        if (newVal) {
          this.handleSort()
        }
      },
    },
  },
  created() {
    this.loadAllMenu() // 組件創建時執行一次菜單加載
  },
}
</script>

<style scoped>
.btn-link {
  text-decoration: none;
  color: black;
}

.category-carousel .swiper-wrapper {
  display: flex; /* 讓分類項目水平排列 */
  justify-content: space-evenly; /* 水平均勻分布 */
  align-items: center; /* 垂直居中對齊 */
  flex-wrap: nowrap; /* 防止換行 */
}

.category-carousel .swiper-slide {
  /* 限制每個分類項目的最小寬度，避免它們過小 */
  min-width: 200px;
  max-height: 180px;
  text-align: center;
}

.swiper-wrapper {
  display: flex; /* 讓分類項目水平排列 */
  justify-content: space-evenly; /* 項目均勻分布 */
  gap: 20px; /* 設定項目之間的間距 */
}

.category-item {
  background: #ffffff;
  border: 1px solid #fbfbfb;
  box-shadow: 0px 5px 22px rgba(0, 0, 0, 0.04);
  border-radius: 16px;
  text-align: center;
  padding: 20px;
  transition:
    box-shadow 0.3s ease-out,
    transform 0.3s ease-out;
}

.category-item:hover {
  transform: translate3d(0, -10px, 0);
  box-shadow: 0px 21px 44px rgba(0, 0, 0, 0.08);
}

.category-title {
  font-weight: 600;
  font-size: 16px;
  line-height: 24px;
  text-transform: capitalize;
  color: #222222;
  margin-top: 15px;
}

.swiper-slide {
  min-height: 63px;
  display: flex;
  justify-content: center; /* 垂直居中對齊 */
}

/* Swiper carousel */
.swiper-slide:hover {
  /* transform: translateY(-20px)!important; */
  scale: 1.1;
}

/* category carousel */
.category-carousel .category-item {
  background: #ffffff;
  border: 1px solid #fbfbfb;
  box-shadow: 0px 5px 22px rgba(0, 0, 0, 0.04);
  border-radius: 16px;
  text-align: center;
  padding: 60px 20px;

  margin: 20px 0;
  transition:
    box-shadow 0.3s ease-out,
    transform 0.3s ease-out;
}
.category-carousel .category-item:hover {
  transform: translate3d(0, -10px, 0);
  box-shadow: 0px 21px 44px rgba(0, 0, 0, 0.08);
}
.category-carousel .category-item .category-title {
  font-weight: 600;
  font-size: 20px;
  line-height: 27px;
  letter-spacing: 0.02em;
  text-transform: capitalize;
  color: #222222;
  margin-top: 20px;
}

.button-image:hover {
  transform: scale(1.1);
}

.category-carousel .category-item {
  background: #ffffff;
  border: 1px solid #fbfbfb;
  box-shadow: 0px 5px 22px rgba(0, 0, 0, 0.04);
  border-radius: 16px;
  text-align: center;
  padding: 60px 20px;
  margin: 20px 0;
  transition:
    box-shadow 0.3s ease-out,
    transform 0.3s ease-out;
}
.category-carousel .category-item:hover {
  transform: translate3d(0, -10px, 0);
  box-shadow: 0px 21px 44px rgba(0, 0, 0, 0.08);
}
.category-carousel .category-item .category-title {
  font-weight: 600;
  font-size: 20px;
  line-height: 27px;
  letter-spacing: 0.02em;
  text-transform: capitalize;
  color: #222222;
  margin-top: 20px;
}

.swiper-slide {
  min-height: 63px;
  display: flex;
}

.nav-link {
  --bs-nav-pills-link-active-color: #111;
  --bs-nav-pills-link-active-bg: #f1f1f1;
}

.nav-link.active,
.nav-link {
  /* border: none; */
  border-bottom: 3px solid var(--accent-color);
  width: 120px;
}

.swiper-wrapper {
  max-width: 100px;
}

:root {
  /* widths for rows and containers
     */
  --header-height: 160px;
  --header-height-min: 80px;
}
/* on mobile devices below 600px
 */
@media screen and (max-width: 600px) {
  :root {
    --header-height: 100px;
    --header-height-min: 80px;
  }
}

.category-carousel .swiper-wrapper {
  display: flex; /* 讓分類項目水平排列 */
  justify-content: space-evenly; /* 水平均勻分布 */
  align-items: center;
  flex-wrap: nowrap; /* 防止換行 */
  gap: 60px; /* 設定項目之間的間距 */
}

/* 分頁 */
.pagination .page-link {
  background: linear-gradient(#fffefe); /* 黑色漸變背景 */
  color: rgb(0, 0, 0); /* 白字 */
  border: 1px solid #ffffff; /* 灰色邊框 */
  border-radius: 5px; /* 圓角按鈕 */
  padding: 8px 12px; /* 內邊距 */
  margin: 0 5px; /* 間距 */
  font-size: 16px; /* 字體大小 */
  font-weight: bold; /* 粗體字 */
  transition: all 0.3s ease; /* 動畫過渡 */
}

.pagination .page-link:hover {
  background: linear-gradient(45deg, #444, #111); /* 懸停時的漸變 */
  color: #ffffff; /* 懸停時的金色字體 */
  transform: scale(1.1); /* 放大效果 */
}

.pagination .page-item.active .page-link {
  background: linear-gradient(45deg, #ffffff, #ffffff); /* 活動按鈕金色漸變 */
  color: black; /* 黑字 */
  border: 1px solid #000000; /* 橙色邊框 */

  transform: scale(1.15); /* 稍大 */
}

.pagination .page-item.disabled .page-link {
  background: #555; /* 禁用按鈕深灰背景 */
  color: #999; /* 灰字 */
  pointer-events: none; /* 禁止點擊 */
  opacity: 0.5; /* 降低透明度 */
}

.pagination-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;
  margin: 20px 0; /* 外邊距 */
}

.pagination .page-item {
  display: inline-block;
}

/* 篩選價格 */
.price-range-slider {
  width: 100%;
}

.slider-container {
  position: relative;
  width: 100%;
  height: 24px;
}

.slider-track {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 100%;
  height: 4px;
  border-radius: 2px;
  pointer-events: none;
}

.range-input {
  position: absolute;
  width: 100%;
  appearance: none;
  background: transparent;
  pointer-events: auto;
}

.range-input::-webkit-slider-thumb {
  appearance: none;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #000000;
  cursor: pointer;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: transform 0.1s ease;
}

.range-input::-webkit-slider-thumb:hover {
  transform: scale(1.2);
}

/* 去除範圍輸入的默認樣式 */
.range-input::-webkit-slider-runnable-track {
  width: 100%;
  height: 4px;
  cursor: pointer;
  background: transparent;
  border-radius: 2px;
  border: none;
}

.range-input::-moz-range-thumb {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background: #000000;
  cursor: pointer;
  border: 2px solid white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: transform 0.1s ease;
}

.range-input::-moz-range-thumb:hover {
  transform: scale(1.2);
}

.slider-labels {
  text-align: center;
  color: #4b5563;
}

.min-range,
.max-range {
  position: absolute;
  top: 30%;
  transform: translateY(-50%);
}

.min-range {
  z-index: 1;
}

.max-range {
  z-index: 2;
}

/* 手機板 */
@media (max-width: 768px) {
  /* 重設容器樣式 */
  .container {
    padding: 0 15px;
  }

  /* 重設 row 樣式 */
  .row {
    margin: 0;
    width: 100%;
  }

  /* 重設所有 col-4 的基本樣式 */
  .container .row .col-4 {
    width: 100%;
    padding: 0;
    margin-bottom: 15px;
  }

  /* 確保滑塊容器和選單容器有相同的寬度和對齊 */
  .price-range-slider,
  .form-select-lg {
    width: 100%;
    margin: 0;
    padding: 0.5rem;
  }

  /* 特別處理排序選單容器 */
  .col-4:last-child {
    padding: 0;
    margin-bottom: 15px;
  }

  /* 排序選單本身的樣式 */
  .form-select-lg {
    width: 100%;
    margin: 0;
    height: 45px; /* 固定高度 */
  }
}

/* 在更小的螢幕上的額外調整 */
@media (max-width: 576px) {
  .container {
    padding: 0 10px;
  }
}

.styled-select {
  appearance: none; /* 隱藏原生的箭頭 */
  -webkit-appearance: none; /* 修正 Safari */
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 10px;
  font-size: 16px;
  cursor: pointer;
}

/* 模擬懸停效果（透過背景） */
.styled-select:hover {
  border-color: black;
}

/* 無法直接控制 option:hover，但可以模擬效果 */
.styled-select option {
  background-color: white; /* 預設背景 */
  color: black;            /* 預設文字顏色 */
}

.styled-select option:hover {
  background-color: black !important; /* 模擬背景為黑色 */
  color: white !important;           /* 模擬文字為白色 */
}
</style>
