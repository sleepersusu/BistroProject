<template>
  <div class="modal fade" ref="modal" tabindex="-1.1" aria-labelledby="modalLabel">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="container d-flex">
            <div>
              <section><img src="/public/images/餐點/飲品.jpg" alt="" /></section>
              <section class="starSection">
                <star-rating :rating="menu.avgScore" :read-only="true" :increment="0.1"
                :rounded-corners="true" :border-width="4" :show-rating="false"
                :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]"></star-rating>
              </section>
              <section><h4>({{menu.avgScore}})</h4></section>
            </div>

            <div>
              <h3></h3>
              <!-- <h3>{{ menu.productDescribe }}</h3> -->
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
    currentId:{
      type:Number,
      required: true,
    }
  },
  components:{
    StarRating,
  },
  mixins: [ModalMixin],
  data(){
    return{
      menuSrc: '',
    }
  },data(){

  },

  methods: {
    async loadPicture(ID) {
      let API_URL = `${import.meta.env.VITE_API}/api/menu/photo/${ID}`

      this.axios
        .get(API_URL, { responseType: 'blob' })
        .then(async (response) => {
          let url = URL.createObjectURL(response.data)
          this.menuSrc = url
        })
        .catch((error) => {
          console.error('Error fetching menus:', error)
          this.isLoading = false
        })
    },

  },
  created(){
    console.log('Menu ID:', this.menu.id);
    this.loadPicture(this.menu.id)
  }
}
</script>

<style scoped>
.starSection{
  margin-top:20px
}
</style>
