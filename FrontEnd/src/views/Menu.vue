<template>

<div class="container">
  <div class="d-flex justify-content-start">

    <button class="btn btn-link" @click="loadAllMenu()">
    <h2>商品菜單</h2>
  </button>
  </div>
</div>
<div class="container">


  <section class="py-5 overflow-hidden">
      <div class="container-lg">
        <div class="row">
          <div class="col-md-12">

            <div class="section-header d-flex flex-wrap justify-content-between mb-5">
              <h2 class="section-title">Category</h2>

              <div class="d-flex align-items-center">
                <a href="#" class="btn btn-primary me-2">View All</a>
                <div class="swiper-buttons">
                  <button class="swiper-prev category-carousel-prev btn btn-yellow"
                  >❮</button>
                  <button class="swiper-next category-carousel-next btn btn-yellow" 
                  v-on:click.prevent="scrollRight">❯</button>
                </div>
              </div>
            </div>
            
          </div>
        </div>

        <div class="row">
          <div class="col-md-12">

            <div class="category-carousel swiper " refs="carousel">
              <div class="swiper-wrapper d-flex justify-content-around" style="overflow-x:auto;">
                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('開胃菜')">
                  <img src="/public/images/餐點/開胃菜.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >開胃菜</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('主菜')">
                  <img src="/public/images/餐點/主菜.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >主菜</h4>
                </button>


                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('飲品')">
                  <img src="/public/images/餐點/飲品.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >飲品</h4>
                </button>


                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>

                <button class="nav-link swiper-slide text-center" v-on:click.prevent="clickCategory('甜點')">
                  <img src="/public/images/餐點/甜點.jpg" class="rounded-circle button-image" alt="">
                  <h4 class="fs-6 mt-3 fw-normal category-title"  >甜點</h4>
                </button>
                
                
              </div>
            </div>

          </div>
        </div>
      </div>
    </section>       

    
>>>>>>> 3fab0f9a40b80ba87e426649a0949bf4818a8894
</div>
<div class="container">
  <div class="row mt-3">

    <div class="col-md-3 col-sm-6" v-for="menu in menus" :key="menu.id" >

    <MenuCard :menu="menu" 
      @update-count="updateCount" 
      @add-to-cart="handleAddToCart">
    </MenuCard>
    </div>


  </div>
</div>

</template>


<script >
import MenuCard from '@/components/MenuCard.vue';



export default {
  components:{

    MenuCard,

  },

  data() {
    return {
      menus:[],

      comment: {},

      menuCount: 0, 

      
    }
  },
  methods: {


    loadAllMenu(){

      let API_URL = `${import.meta.env.VITE_API}/api/menu/`;

      this.axios.get(API_URL)
      .then((response)=>{
        this.menus=response.data
      })

    },

  





    
    updateCount(newCount) {

      this.menuCount = newCount;
      console.log("Updated count:", this.menuCount);

    },

    handleAddToCart({ id, count }) {
      console.log(`Added to cart: ID=${id}, Count=${count}`);
    },

    // 按分類加載菜單數據
    clickCategory(category) {
      this.isLoading = true;

      let API_URL = `${import.meta.env.VITE_API}/api/menu/${category}`;


      this.axios
        .get(API_URL)
        .then((response) => {
          this.menus = response.data; // 假設 API 返回的數據是菜單數組
        })
        .catch((error) => {
          console.error("Error loading menus:", error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },


     // 向左滾動
     scrollLeft() {
      const carousel = this.$refs.carousel;
      carousel.scrollBy({
        left: -200,
        behavior: "smooth",
      });
      this.checkScrollPosition();
    },

    // 向右滾動
    scrollRight() {
      const carousel = this.$refs.carousel;
      carousel.scrollBy({
        left: 200,
        behavior: "smooth",
      });
      this.checkScrollPosition();
    },

  },


  
  computed: {},
  watch: {},
  created() {
    this.loadAllMenu();

  },
}
</script>

<style scoped>
  .btn-link{
    text-decoration:none ;
    color: black;
  }

/* Swiper carousel */
.swiper-prev,
.swiper-next {
  width: 38px;
  height: 38px;
  line-height: 38px;
  background: #F1F1F1;
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
  --bs-btn-disabled-border-color: #eaeaea
}
.swiper-prev:hover,
.swiper-next:hover {
  background: var(--bs-primary);
}

/* category carousel */
.category-carousel .category-item {
  background: #FFFFFF;
  border: 1px solid #FBFBFB;
  box-shadow: 0px 5px 22px rgba(0, 0, 0, 0.04);
  border-radius: 16px;
  text-align: center;
  padding: 60px 20px;
  margin: 20px 0;
  transition: box-shadow 0.3s ease-out, transform 0.3s ease-out;
}
.category-carousel .category-item:hover {
  transform: translate3d(0,-10px,0);
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
  width:150px;
  height: 150px; 
  margin-right: 8px; 
  object-fit: cover; 
  border-radius: 50%; 
}
</style>