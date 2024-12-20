<template>
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
          src="https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="d-block w-100"
          alt="..."
        />
      </div>
      <div class="carousel-item">
        <img
          src="https://images.unsplash.com/photo-1489209909448-8926a2640f1f?q=80&w=1932&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          class="d-block w-100"
          alt="..."
        />
      </div>
      <div class="carousel-item">
        <img
          src="/images/DSC_9550-1.jpg"
          class="d-block w-100"
          alt="..."
        />
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
  <div class="container">
    <h2
      class="text-center text-dark mt-5"
      style="font-family: 'Chakra Petch', cursive !important"
    >
      Popular Dishes
    </h2>
    <div class="row  my-5 mx-4" data-aos="fade-up" >
      <div class="col-md-4 mt-4" v-for="menu in menus" :key="menu.id">
        <ProductCard :menu="menu"></ProductCard>
      </div>
    </div>
    <ZigzagSection></ZigzagSection>
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
      menus:[],

    }
  },
  methods: {


    loadTop3Menu(){

      let API_URL = `${import.meta.env.VITE_API}/api/menu/topThree`;

      this.axios.get(API_URL)
      .then((response)=>{
        this.menus=response.data
      })

    },

  },
  computed: {},
  watch: {},
  created() {
    this.loadTop3Menu();
  },
}


</script>

<style>
@media (min-width: 992px) {
  .carousel-inner {
    height: 900px;
  }
}

@media (min-width: 768px) and (max-width: 991.98px) {
  .carousel-inner {
    height: 400px;
  }
}

@media (max-width: 767.98px) {
  .carousel-inner {
    height: 300px;
  }
}

.carousel-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}
</style>



