<template>
  <div>
    <BannerTop v-bind:title="'About Us'"></BannerTop>
  </div>
  <div class="container-xxl py-5">
    <div class="container">
      <div class="row g-5 align-items-end">
        <div class="col-lg-3 col-md-5">
          <img class="img-fluid rounded" data-wow-delay="0.1s" src="/public/images/aboutus.jpg" />
        </div>
        <div class="col-lg-6 col-md-7">
          <h1 class="display-1 text-primary mb-0" style="font-weight: bold; font-size: 8rem">10</h1>
          <p class="text-primary mb-4">Year of Experience</p>
          <h1 class="display-5 mb-4">
            <span style="font-weight: bold">「 酌夜語 Night Sips 」</span>
          </h1>
          <span class="mb-4 pt-5" style="line-height: 1.8">
            酌夜語，寓意在夜晚中「小酌一杯，輕語心聲」。
            這裡是一個舒適的避風港，讓顧客在繁忙的日常生活中找到一個放鬆的角落，與朋友、家人或獨自與自己對話，享受一段沉浸於美食、美酒與情感交流的美好時光。
            <br />
            酌夜語，不僅是一家餐酒館，更是一個屬於每個人的心靈避風港。
            在這裡，無論你是與三五好友共度美好時光，還是獨自一人享受寧靜時光，我們都希望你能在這裡找到那份屬於自己的夜晚。當你推開這扇門，進入這片夜色的懷抱時，所有的疲憊與壓力都能暫時放下，心靈會在這裡得到釋放與平靜。
          </span>
          <br />

          <a class="btn btn-primary py-3 px-4 mt-3" href="">Explore More</a>
        </div>

        <div class="col-lg-3 col-md-12">
          <div class="row g-5">
            <div class="col-12 col-sm-6 col-lg-12">
              <div
                class="border-start ps-4"
                data-aos="fade-right"
                data-aos-duration="2000"
                data-aos-delay="800"
              >
                <i class="fa fa-3x text-primary mb-3"></i>
                <h4 class="mb-3">酌</h4>
                <span>
                  代表小酌，帶著一絲微醺的意味，意味著在忙碌的日常中，給自己留下一段放慢腳步、享受當下的時間。
                </span>
              </div>
            </div>
            <div
              class="col-12 col-sm-6 col-lg-12"
              data-aos="fade-right"
              data-aos-duration="2000"
              data-aos-delay="1300"
            >
              <div class="border-start ps-4">
                <i class="fa fa-3x text-primary mb-3"></i>
                <h4 class="mb-3">夜</h4>
                <span> 代表著那份隱秘的力量，讓人放下外界的喧囂與矯飾，回到最真實的自己。 </span>
              </div>
            </div>
            <div class="col-12 col-sm-6 col-lg-12">
              <div
                class="border-start ps-4"
                data-aos="fade-right"
                data-aos-duration="2000"
                data-aos-delay="1800"
              >
                <i class="fa fa-3x text-primary mb-3"></i>
                <h4 class="mb-3">語</h4>
                <span> 一句不經意的對話、一段微醺的輕語，往往能溫暖心底最柔軟的角落。 </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- About End -->

  <!-- Team Start -->
  <div class="container-xxl py-5 mt-5">
    <div class="container">
      <div class="text-center mx-auto wow fadeInUp" data-wow-delay="0.1s" style="max-width: 500px">
        <h1 class="mb-5" style="font-family: 'Chakra Petch', cursive !important ; font-size: 3rem">
          Team Members
        </h1>
      </div>

      <div class="row g-4">
        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.1s">
          <div class="team-item rounded p-4">
            <img class="img-fluid rounded mb-4" src="/public/images/SU大頭照.jpg" alt="" />
            <h5>SU YUHSIANG</h5>
            <p class="text-primary">Team Leader</p>
            <div class="d-flex justify-content-center pt-1">
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-github fs-4"></i
              ></a>
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-linkedin fs-4"></i
              ></a>
            </div>
          </div>
        </div>

        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.3s">
          <div class="team-item rounded p-4">
            <img class="img-fluid rounded mb-4" src="/images/修大頭照.jpg" alt="" />
            <h5>Darren Xiao</h5>
            <p class="text-primary">Team Technology Officer</p>
            <div class="d-flex justify-content-center pt-1">
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-github fs-4"></i
              ></a>
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-linkedin fs-4"></i
              ></a>
            </div>
          </div>
        </div>

        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.5s">
          <div class="team-item rounded p-4">
            <img class="img-fluid rounded mb-4" src="/images/YU大頭照.jpg" alt="" />
            <h5>Sie ChenYu</h5>
            <p class="text-primary">Team Member</p>
            <div class="d-flex justify-content-center pt-1">
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-github fs-4"></i
              ></a>
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-linkedin fs-4"></i
              ></a>
            </div>
          </div>
        </div>

        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.5s">
          <div class="team-item rounded p-4">
            <img class="img-fluid rounded mb-4" src="..." alt="" />
            <h5>Jack Dawson</h5>
            <p class="text-primary">Member</p>
            <div class="d-flex justify-content-center pt-1">
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-github fs-4"></i
              ></a>
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-linkedin fs-4"></i
              ></a>
            </div>
          </div>
        </div>

        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.5s">
          <div class="team-item rounded p-4">
            <img class="img-fluid rounded mb-4" src="" alt="" />
            <h5>Jack Dawson</h5>
            <p class="text-primary">Member</p>
            <div class="d-flex justify-content-center pt-1">
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-github fs-4"></i
              ></a>
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-linkedin fs-4"></i
              ></a>
            </div>
          </div>
        </div>

        <div class="col-lg-4 col-md-6 wow fadeInUp" data-wow-delay="0.5s">
          <div class="team-item rounded p-4">
            <img class="img-fluid rounded mb-4" src="..." alt="" />
            <h5>Jack Dawson</h5>
            <p class="text-primary">Member</p>
            <div class="d-flex justify-content-center pt-1">
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-github fs-4"></i
              ></a>
              <a
                class="btn btn-square btn-outline-primary rounded-circle mx-1"
                target="_blank"
                href="https://www.facebook.com/"
                ><i class="bi bi-linkedin fs-4"></i
              ></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- Team End -->
</template>

<script>
import BannerTop from '@/components/BannerTop.vue'
import PageTop from '@/components/PageTop.vue'

export default {
  name: 'Profile',
  components: {
    PageTop,
    BannerTop,
  },
  data() {
    return {}
  },
  methods: {},
  computed: {},
  watch: {},
  created() {},
}
</script>

<style scoped>
.img-fluid {
  height: 500px;
  width: 100%;
  object-fit: cover;
}
</style>
