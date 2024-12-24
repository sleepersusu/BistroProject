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
                      <font-awesome-icon :icon="['fas', 'list']"size="2xl" />
                      <h3 class="category-title">全部</h3>
                    </div>
                  </button>
                </div>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('開胃菜')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'plate-wheat']"size="2xl" />
                    <h3 class="category-title">開胃菜</h3>
                  </div>
                </button>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('主菜')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'utensils']" size="2xl"/>
                    <h3 class="category-title">主菜</h3>
                  </div>
                </button>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('飲品')"
                >
                  <div>
                    <font-awesome-icon :icon="['fas', 'champagne-glasses']" size="2xl"/>
                    <h3 class="category-title">飲品</h3>
                  </div>
                </button>

                <button
                  class="nav-link category-item swiper-slide"
                  v-on:click.prevent="clickCategory('甜點')">
                  <div>
                    <font-awesome-icon :icon="['fas', 'ice-cream']" size="2xl"/>
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
    <div class="row mt-3">
      <div class="col-md-6 col-lg-4 col-sm-6" v-for="menu in paginatedMenus" :key="menu.id">
        <MenuCard
          :menu="menu"
          @view-menudescribe="openDescribeModal"
          @getcount="getCommentPeople"
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
</template>

<script>
import axios from 'axios'
import MenuCard from '@/components/MenuCard.vue'
import MenuCommentModal from '@/components/MenuCommentModal.vue'
import MenuDescribeModal from '@/components/MenuDescribeModal.vue'
import PageComponent from '@/components/PageComponent.vue'
import BannerTop from '@/components/BannerTop.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

export default {
  components: {
    MenuCard,
    MenuCommentModal,
    MenuDescribeModal,
    PageComponent,
    BannerTop,
    FontAwesomeIcon,
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



      cartItems: [] // 購物車項目列表
    }
  },
  methods: {
    loadAllMenu() {
      let API_URL = `${import.meta.env.VITE_API}/api/menu`

      axios.get(API_URL).then((response) => {
        this.menus = response.data
        this.totalPages = Math.ceil(this.menus.length / this.menusPerPage)
      })
    },

    updateCount(newCount) {
      this.menuCount = newCount
    },

    // 按分類加載菜單數據
    clickCategory(category) {
      this.isLoading = true
      let API_URL = `${import.meta.env.VITE_API}/api/menu/${category}`

      this.axios
        .get(API_URL)
        .then((response) => {
          this.menus = response.data // 假設 API 返回的數據是菜單數組
        })
        .catch((error) => {
          console.error('Error loading menus:', error)
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    openModal(menu) {
      this.currentProduct = menu.productName
      let api = `${import.meta.env.VITE_API}/api/${menu.productName}/comment`
      this.axios
        .get(api)
        .then((response) => {
          this.comments = response.data
        })
        .catch((error) => {
          console.error('Error loading menus:', error)
        })

      this.$refs.commentModal.showModal()
    },
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
    menus: {
      handler(newVal) {
        if (this.currentPage > this.totalPages) {
          this.currentPage = Math.max(1, this.totalPages) // 如果當前頁面大於總頁數，重置為最後一頁
        }
      },
      deep: true,
    },
  },
  created() {
    this.loadAllMenu()
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
  transition: box-shadow 0.3s ease-out, transform 0.3s ease-out;
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
  width:120px;

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
  background: linear-gradient( #fffefe); /* 黑色漸變背景 */
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


</style>
