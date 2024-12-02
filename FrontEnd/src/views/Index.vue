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
          src="https://images.pexels.com/photos/2387566/pexels-photo-2387566.jpeg"
          class="d-block w-100"
          alt="..."
        />
      </div>
      <div class="carousel-item">
        <img
          src="https://images.pexels.com/photos/2796105/pexels-photo-2796105.jpeg"
          class="d-block w-100"
          alt="..."
        />
      </div>
      <div class="carousel-item">
        <img
          src="https://images.pexels.com/photos/261043/pexels-photo-261043.jpeg"
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
    <h2 class="text-center mt-5">人氣菜色</h2>
    <ProductCard></ProductCard>
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
}
</script>

<style>
@media (min-width: 992px) {
  .carousel-inner {
    height: 800px;
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
