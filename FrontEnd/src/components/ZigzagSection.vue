<template>
  <div class="container-fluid pt-5">
    <div class="row align-items-center">
      <div class="col-md-6 p-0">
        <img
          src="https://images.pexels.com/photos/588575/pexels-photo-588575.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
          alt="Restaurant Interior"
          class="img-fluid"
        />
      </div>
      <div class="col-md-6 text-content section-content">
        <h2 class="mb-4">藝術釀酒世界</h2>
        <p class="mb-3">精釀啤酒專門店 - 翻轉味蕾</p>
        <p>每一杯都是一場味覺探險，讓您體驗不一樣的精釀世界。</p>
      </div>
    </div>
  </div>

  <!-- Second Section -->
  <div class="container-fluid">
    <div class="row align-items-center">
      <div class="col-md-6 text-content section-content">
        <h2 class="mb-4">啤酒品味美學</h2>
        <p class="mb-3">精釀啤酒餐廳 - 微醺空間</p>
        <p>從世界各地精選超過300款精釀啤酒，打造獨特的品味空間。</p>
      </div>
      <div class="col-md-6 p-0">
        <img
          src="https://images.unsplash.com/photo-1668533889350-21a05111310b?q=80&w=2035&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt="Restaurant Interior"
          class="img-fluid"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {}
</script>

<style>
.section-content {
  padding: 4rem 2rem;
}
.text-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.img-fluid {
  width: 100%;
  height: 600px;
  object-fit: cover;
}
</style>
