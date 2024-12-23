<template>
  <div>
    <div class="container-fluid section-wrapper">
      <div class="row align-items-center">
        <div class="col-md-6 p-0" style="overflow: hidden">
          <img src="/images/p8_0.jpg" alt="Restaurant Interior" class="img-fluid" />
        </div>
        <div class="col-md-6 text-content section-content primary-section">
          <div data-aos="fade-left" data-aos-duration="2000">
            <h4 class="section-title mb-4">法式香草雞腿排奶油燉飯</h4>
            <p class="subtitle mb-3">舒心的法式小餐館風味</p>
            <p class="description">
              香煎香草雞腿排佐以綿密奶油燉飯，<br />融入帕瑪森起司與白酒，層層交織出溫暖的味覺饗宴。
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="container-fluid section-wrapper pt-0">
      <div class="row align-items-center">
        <div class="col-md-6 text-content section-content secondary-section">
          <div data-aos="fade-right" data-aos-duration="2000">
            <h4 class="section-title mb-4">蟹黃煙燻臘腸風味麵</h4>
            <p class="subtitle mb-3">歐陸風味・奢華重組</p>
            <p class="description">
              西班牙煙燻臘腸與濃郁蟹黃完美結合，<br />搭配手工現製麵條，譜出異國美味的層次饗宴。
            </p>
          </div>
        </div>
        <div class="col-md-6 p-0" style="overflow: hidden">
          <img src="/images/p9_0.jpg" alt="Restaurant Interior" class="img-fluid" />
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
  padding: 2rem;
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
  transform: scale(1.05);
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
