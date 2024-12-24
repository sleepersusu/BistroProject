<template>
  <div class="card bg-light">
    <div style="overflow: hidden; cursor: pointer">
      <img :src="menuSrc" class="card-img-top" alt="" />
    </div>
    <div class="card-body text-primary">
      <h6 class="card-title">
        {{ menu.productName }}
        <span class="float-end text-danger">NT${{ menu.productPrice }}</span>
      </h6>
      <button class="btn btn-outline-primary w-100" @click="handleAddToCart()">加入購物車</button>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore.js'

export default {
  props: {
    menu: {
      type: Object,
      required: true,
    },
  },
  data() {
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
      const productName = this.menu.productName
      this.CountCart(this.menu)
      this.count = 1
      Swal.fire({
        toast: true,
        position: 'top-end',
        title: '成功加入購物車',
        text: `「${productName}」已加入！`,
        icon: 'success',
        background: '#fff',
        color: '#000000',
        iconColor: '#000000',
        showConfirmButton: false,
        timer: 1314,
        timerProgressBar: true,
        didOpen: (toast) => {
          toast.style.marginTop = '80px'
        },
      })
    },
  },
  created() {
    this.loadPicture(this.menu.id)
  },
}
</script>

<style scoped>
.card {
  overflow: hidden;
}
.card-img-top {
  height: 250px;
  width: 100%;
  object-fit: cover;
  transition: all 0.3s ease;
}

.card-img-top:hover {
  transform: scale(1.1);
}
</style>
