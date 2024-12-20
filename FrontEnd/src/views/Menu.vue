<template>
  <div class="container">
    <section class="py-5 overflow-hidden">
      <div class="container-lg">
        <div class="row">
          <div class="col-md-12">
            <div class="section-header d-flex flex-wrap justify-content-between mb-5">
              <h2 class="section-title">Category</h2>
            </div>
          </div>
        </div>

        <div class="row">
          <div class="col-md-12">
            <div class="category-carousel swiper" refs="carousel">
              <div class="swiper-wrapper d-flex justify-content-around" style="overflow-x: auto">
                <button class="nav-link swiper-slide text-center" @click="loadAllMenu()">
                  <img
                    src="/public/images/餐點/全部.jpg"
                    class="rounded-circle button-image"
                    alt=""
                  />
                  <h4 class="fs-6 mt-3 fw-normal category-title">全部</h4>
                </button>

                <button
                  class="nav-link swiper-slide text-center"
                  v-on:click.prevent="clickCategory('開胃菜')"
                >
                  <img
                    src="/public/images/餐點/開胃菜.jpg"
                    class="rounded-circle button-image"
                    alt=""
                  />
                  <h4 class="fs-6 mt-3 fw-normal category-title">開胃菜</h4>
                </button>

                <button
                  class="nav-link swiper-slide text-center"
                  v-on:click.prevent="clickCategory('主菜')"
                >
                  <img
                    src="/public/images/餐點/主菜.jpg"
                    class="rounded-circle button-image"
                    alt=""
                  />
                  <h4 class="fs-6 mt-3 fw-normal category-title">主菜</h4>
                </button>

                <button
                  class="nav-link swiper-slide text-center"
                  v-on:click.prevent="clickCategory('飲品')"
                >
                  <img
                    src="/public/images/餐點/飲品.jpg"
                    class="rounded-circle button-image"
                    alt=""
                  />
                  <h4 class="fs-6 mt-3 fw-normal category-title">飲品</h4>
                </button>

                <button
                  class="nav-link swiper-slide text-center"
                  v-on:click.prevent="clickCategory('甜點')"
                >
                  <img
                    src="/public/images/餐點/甜點.jpg"
                    class="rounded-circle button-image"
                    alt=""
                  />
                  <h4 class="fs-6 mt-3 fw-normal category-title">甜點</h4>
                </button>

                <button
                  class="nav-link swiper-slide text-center"
                  v-on:click.prevent="clickCategory('甜點')"
                >
                  <img
                    src="/public/images/餐點/甜點.jpg"
                    class="rounded-circle button-image"
                    alt=""
                  />
                  <h4 class="fs-6 mt-3 fw-normal category-title">甜點</h4>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <div class="container">
      <div class="d-flex justify-content-start">
        <button class="btn btn-link" @click="loadAllMenu()">
          <h2>商品菜單</h2>
        </button>
      </div>
    </div>
  </div>

  <div class="container">
    <div class="row mt-3">
      <div class="col-md-6 col-lg-4 col-sm-6" v-for="menu in menus" :key="menu.id">
        <MenuCard
          :menu="menu"
          @view-menudescribe="openDescribeModal"
          @getcount="countCommentPeople"
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
  </div>
</template>

<script>
import axios from 'axios'
import MenuCard from '@/components/MenuCard.vue'
import MenuCommentModal from '@/components/MenuCommentModal.vue'
import MenuDescribeModal from '@/components/MenuDescribeModal.vue'

export default {
  components: {
    MenuCard,
    MenuCommentModal,
    MenuDescribeModal,
  },

  data() {
    return {
      menus: [],
      currentProduct: '',
      comments: [],
      menuCount: 0,
      menu: {},
    }
  },
  methods: {
    loadAllMenu() {
      let API_URL = `${import.meta.env.VITE_API}/api/menu`

      axios.get(API_URL).then((response) => {
        this.menus = response.data
      })
    },

    updateCount(newCount) {
      this.menuCount = newCount
    },

    handleAddToCart({ id, count }) {},

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
          console.log(response.data)
          this.comments = response.data
        })
        .catch((error) => {
          console.error('Error loading menus:', error)
        })

      this.$refs.commentModal.showModal()
    },
    async openDescribeModal(menu) {
      try {
        console.log('Opening modal for menu:', menu)
        let api = `${import.meta.env.VITE_API}/api/${menu.id}/menu`
        let response = await this.axios.get(api)
        this.menu = response.data
        this.$refs.menuDescribeModal.showModal()
      } catch (error) {
        console.error('Error loading menu:', error)
      }
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
.swiper-prev,
.swiper-next {
  width: 38px;
  height: 38px;
  line-height: 38px;
  background: #f1f1f1;
  color: #222222;
  padding: 0;
  text-align: center;
  border-radius: 10px;
  --bs-btn-border-color: transparent;
  --bs-btn-active-bg: #ec9b22;
  --bs-btn-active-border-color: transparent;
  --bs-btn-hover-bg: var(--bs-primary);
  --bs-btn-hover-border-color: transparent;
  --bs-btn-disabled-color: #ccc;
  --bs-btn-disabled-bg: #eaeaea;
  --bs-btn-disabled-border-color: #eaeaea;
}
.swiper-prev:hover,
.swiper-next:hover {
  background: var(--bs-primary);
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

.button-image {
  width: 150px;
  height: 150px;
  margin-right: 8px;
  object-fit: cover;
  border-radius: 50%;
}
</style>
