<template>
  <div class="container">
    <BannerTop :title="' Menu'"></BannerTop>
    <section class="py-5 overflow-hidden">
      <div class="container-fluid " style="overflow:auto">
        <div class="row ">
          <div class="col-md-12 ">
            <div class="category-carousel swiper ">
              <div class="swiper-wrapper">


                <div>
                <button class="nav-link category-item swiper-slide" @click="loadAllMenu()">
                  <div>
                    <img src="/public/images/餐點/全部.jpg" alt="" />
                    <h3 class="category-title">全部</h3>
                  </div>
                </button>
              </div>

                <button class="nav-link category-item swiper-slide"v-on:click.prevent="clickCategory('開胃菜')">
                  <div>
                    <img src="/public/images/餐點/開胃菜.jpg" alt="" />
                    <h3 class="category-title">開胃菜</h3>
                  </div>
                </button>

                <button class="nav-link category-item swiper-slide"v-on:click.prevent="clickCategory('主菜')">
                  <div>
                    <img src="/public/images/餐點/主菜.jpg" alt="" />
                    <h3 class="category-title">主菜</h3>
                  </div>
                </button>

                <button class="nav-link category-item swiper-slide"v-on:click.prevent="clickCategory('飲品')">
                  <div>
                    <img src="/public/images/餐點/飲品.jpg" alt="" />
                    <h3 class="category-title">飲品</h3>
                  </div>
                </button>

                <button class="nav-link category-item swiper-slide"v-on:click.prevent="clickCategory('甜點')">
                  <div>
                    <img src="/public/images/餐點/甜點.jpg" alt="" />
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
      ></MenuCommentModal>
      <MenuDescribeModal ref="menuDescribeModal" :menu="menu"></MenuDescribeModal>
    </div>

    <!-- 分頁按鈕 -->
    <div class="pagination-container">
      <button
        class="btn btn-primary"
        :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)"
      >
        上一頁
      </button>
      <span class="mx-3">{{ currentPage }} / {{ totalPages }}</span>
      <button
        class="btn btn-primary"
        :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)"
      >
        下一頁
      </button>
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

export default {
  components: {
    MenuCard,
    MenuCommentModal,
    MenuDescribeModal,
    PageComponent,
    BannerTop,

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
}
img {
  height: 80px;

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

  display: flex;            /* 讓分類項目水平排列 */
  justify-content: space-evenly; /* 水平均勻分布 */
  align-items: center;
  flex-wrap: nowrap;        /* 防止換行 */
  gap: 110px; /* 設定項目之間的間距 */
}






</style>
