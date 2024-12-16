<template>
  
    
      <div class="col card bg-light">
        <img
          :src="menuSrc"
          class="card-img-top img-fixed"
        />
        <div class="card-body text-primary">
          <h6 class="card-title">
            {{menu.productName}}
            <span class="float-end text-danger">{{menu.productPrice}}</span>
          </h6>
          <a href="#" class="btn btn-outline-primary w-100">加入購物車</a>
        </div>
      </div>
    
  
</template>


<script>
export default {
  components:{

    

  },
  props:{
    menu:{
      type: Object, // menu 應該是一個物件
      required: true, // 如果 menu 是必需的
    }
  },data() {
    return {
      menuSrc: '',
    }
  },
  methods: {

    async loadPicture(ID) {
      this.isLoading = true

      let API_URL = `${import.meta.env.VITE_API}/api/menu/photo/${ID}`

      this.axios
        .get(API_URL, { responseType: 'blob' })
        .then(async (response) => {
          let url = URL.createObjectURL(response.data)
          this.menuSrc = url
          this.isLoading = false
          this.$emit('image-loaded', this.menuSrc)
        })
        .catch((error) => {
          console.error('Error fetching menus:', error)
          this.isLoading = false
        })
    },

    

  },
  computed: {},
  watch: {},
  created() {
    this.loadPicture(this.menu.id)

  },
}
</script>

<style scoped>
.img-fixed {
  width: 100%; /* 讓圖片寬度符合卡片寬度 */
  height: 250px; /* 固定高度 */
  object-fit: cover; /* 圖片裁切，確保不變形 */
  border-radius: 5px; /* 可選：讓圖片有圓角效果 */
}
</style>
