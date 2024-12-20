<template>
  <div class="modal fade" ref="modal" tabindex="-1.1" aria-labelledby="modalLabel">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header border-0">
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body d-flex flex-column flex-md-row">
          <div class="col-md-6 col-lg-5 mb-3 mb-md-0">
            <img :src="menuSrc" alt="" class="img-fluid" style="border-radius: 5px" />

            <div style="padding-top: 20px" class="star-rating-wrapper">
              <star-rating
                :rating="menu.avgScore"
                :read-only="true"
                :rounded-corners="true"
                :increment="0.1"
                :border-width="4"
                :show-rating="false"
                :star-points="[
                  23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17,
                ]"
              ></star-rating>
            </div>
          </div>
          <div class="col-md-6 col-lg-7">
            <h4>{{ menu.productName }}</h4>
            <div
              id="describe"
              style="background-color: aliceblue; padding: 5px; border-radius: 5px"
            >
              {{ menu.productDescribe }}
            </div>

            <div style="margin-top: 10px">
              <div>* 餐點為現點現做，請耐心等候</div>
              <div style="font-weight: bold">NT${{ menu.productPrice }}</div>
            </div>
          </div>
        </div>
        <div class="modal-footer justify-content-between">
          <div class="row w-100">
            <div class="d-flex col-md-6 mb-2 mb-md-0">
              <button class="btn btn-outline-secondary">-</button>
              <input type="text" class="form-control mx-2" />
              <button class="btn btn-outline-secondary">+</button>
            </div>
            <div class="col-md-6 text-end">
              <button class="btn btn-primary">Add to cart</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModalMixin from '@/mixins/modalMixin-option'
import StarRating from 'vue-star-rating'
export default {
  props: {
    menu: {
      type: Object,
      required: true,
    },
  },
  components: {
    StarRating,
  },
  mixins: [ModalMixin],
  data() {
    return {
      menuSrc: '',
    }
  },

  methods: {
    async loadPicture(ID) {
      let API_URL = `${import.meta.env.VITE_API}/api/menu/photo/${ID}`
      console.log('Fetching URL:', API_URL)
      this.axios
        .get(API_URL, { responseType: 'blob' })
        .then(async (response) => {
          let url = URL.createObjectURL(response.data)
          this.menuSrc = url
          if (url == null) {
            this.menuSrc = ''
          }
        })
        .catch((error) => {
          console.error('Error fetching menus:', error)
          this.isLoading = false
        })
    },
  },
  created() {
    if (this.menu && this.menu.id) {
      this.loadPicture(this.menu.id)
    } else {
      console.error('Invalid menu ID:', this.menu)
      this.menuSrc = ''
    }

    this.loadPicture(this.menu.id)
  },
}
</script>

<style scoped>
.star-rating-wrapper {
  max-width: 100%;
  margin: 0 auto;
}
@media (max-width: 800px) {
  .star-rating-wrapper {
    transform: scale(0.6); /* 根據需要調整縮放比例 */
    max-width: 100%;
    margin: 0 auto;
    text-align: center;
  }
  .modal-body {
    text-align: center;
  }
  #describe {
    text-align: start;
  }
}
</style>
