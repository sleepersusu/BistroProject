<template>
  <div class="col card bg-light">
    <img :src="menuSrc" class="card-img-top img-fixed" alt=""/>
      <div class="card-body text-primary">
        <h6 class="card-title">
          {{menu.productName}}
            <span class="float-end text-danger">{{menu.productPrice}}</span>
        </h6>
          <button class="btn btn-outline-primary w-100" @click="handleAddToCart()">加入購物車</button>
      </div>
  </div>


</template>


<script>
import { mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore.js'

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
      ...mapActions(cartStore, ['CountCart']),
      handleAddToCart() {
        const productName = this.menu.productName;
        this.CountCart(this.menu)
        this.count = 1
        // 黑灰底白字的提示框
        Swal.fire({
          toast:true,
          position:'top-end',
          title: '成功加入購物車',
          text: `「${productName}」已加入！`,
          icon: 'success',
          background: '#fff', // 黑灰底
          color: '#000000',     // 白字
          iconColor: '#000000', // 成功
          showConfirmButton: false, //不顯示確認按鈕
          timer: 1314, //時間
          timerProgressBar: true, //進度條
          didOpen: (toast) => {
            toast.style.marginTop = '80px'; // 動態調整位置
          },
        });
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
