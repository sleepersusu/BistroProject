<template>
  <div>
    <div class="container-fluid section-wrapper">
      <div class="row align-items-center">
        <div class="col-md-6 p-0">
          <img
            src="https://images.unsplash.com/photo-1521215997172-95c11d778b86?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            alt="Restaurant Interior"
            class="img-fluid"
          />
        </div>
        <div class="col-md-6 text-content section-content primary-section">
          <h4 class="section-title mb-4">經典義式饗宴</h4>
          <p class="subtitle mb-3">融合當代美饌的義大利料理</p>
          <p class="description">
            由米其林主廚精心調製的手工義大利麵，<br />搭配經典紅酒，呈現完美的味覺平衡。
          </p>
        </div>
      </div>
    </div>
    <div class="container-fluid section-wrapper">
      <div class="row align-items-center">
        <div class="col-md-6 text-content section-content secondary-section">
          <h4 class="section-title mb-4">極致牛排藝術</h4>
          <p class="subtitle mb-3">靈魂料理・完美醬汁</p>
          <p class="description">
            嚴選PRIME等級肋眼牛排，<br />搭配主廚特製醬汁，締造無與倫比的美食體驗。
          </p>
        </div>
        <div class="col-md-6 p-0">
          <img
            src="https://plus.unsplash.com/premium_photo-1669261882830-1e504a9abf1d?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            alt="Restaurant Interior"
            class="img-fluid"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CraftBeerSections',
}
</script>

<style scoped>
.section-wrapper {
  margin: 2rem 0;
}

.section-content {
  padding: 4rem 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 400px;
}

.primary-section {
  background-color: rgba(80, 121, 127, 0.1);

}

.secondary-section {
  background-color: rgba(143, 118, 98, 0.1);
}

.section-title {
  color: #494544;
  font-size: 1.75rem;
  font-weight: 600;
  position: relative;
  padding-bottom: 0.5rem;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 60px;
  height: 3px;
  background-color: #000000;
}

.subtitle {
  color: #8f7662;
  font-size: 1.2rem;
  font-weight: 500;
}

.description {
  color: #494544;
  font-size: 1.1rem;
  line-height: 1.6;
}

.img-fluid {
  width: 100%;
  height: 400px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.img-fluid:hover {
  transform: scale(1.02);
}

@media (max-width: 768px) {
  .section-content {
    padding: 2rem 1.5rem;
  }

  .section-title {
    font-size: 2rem;
  }

  .img-fluid {
    height: 300px;
  }
}
</style>
