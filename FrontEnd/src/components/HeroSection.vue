<template>
  <div class="container-fluid">
    <div class="row bg-primary">
      <div class="col-lg-4 overflow-hidden d-lg-block d-none">
        <img
          src="/images/wine.png"
          data-aos="fade-right"
          data-aos-duration="2000"
          data-aos-delay="300"
        />
      </div>
      <div
        class="col-lg-4 my-5 text-center text-accent center-content"
        data-aos="fade-up"
        data-aos-duration="2000"
        data-aos-delay="1500"
      >
        <div data-aos="fade-up" data-aos-duration="2300" data-aos-delay="2200">
          <h1 class="display-2 mb-4 title">品味驚喜</h1>
          <h3 class="mb-3">新年微醺 轉運夜語</h3>
          <p class="lead">每消費 1,500 元可獲得一次神秘九宮格抽獎。</p>
        </div>

        <div class="mb-5">
          <img
            src="/images/macbook1.png"
            class="img-fluid mb-3"
            data-aos="fade-up"
            data-aos-duration="3000"
            data-aos-delay="3000"
          />
          <h4 data-aos="fade-up" data-aos-duration="2000" data-aos-delay="2000">
            頭獎：MacBook Pro 1TB
          </h4>
        </div>
        <div class="scroll-down">
          <i class="bi bi-chevron-down"></i>
        </div>
      </div>

      <div class="col-lg-4 position-relative overflow-hidden d-lg-block d-none">
        <div class="position-absolute end-0">
          <img
            src="/images/wine2.jpg"
            style="object-fit: cover; min-height: 700px; width: 500px"
            data-aos="fade-left"
            data-aos-duration="2000"
            data-aos-delay="1000"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@600&family=Dancing+Script:wght@700&family=Noto+Serif+TC:wght@600&display=swap');

.title {
  font-family: 'Dancing Script', 'Noto Serif TC', serif !important;
}

.scroll-down {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  font-size: 2rem;
  animation: bounce 2s infinite;
}

.center-content {
  background-image: url('/images/wine-bg.jpg');
  background-position: center;
  background-size: cover;
  background-repeat: no-repeat;
}

.spinning-image {
  animation: spin 10s linear infinite;
}

@keyframes spin {
  from {
    transform: rotateZ(0deg);
  }
  to {
    transform: rotateZ(360deg);
  }
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(0) translateX(-50%);
  }
  50% {
    transform: translateY(20px) translateX(-50%);
  }
}
</style>
