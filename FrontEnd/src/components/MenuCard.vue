<template>
  <loading :active="isLoading"></loading>

  <svg xmlns="http://www.w3.org/2000/svg" style="display: none">
    <defs>
      <symbol xmlns="http://www.w3.org/2000/svg" id="link" viewBox="0 0 24 24">
        <path
          fill="currentColor"
          d="M12 19a1 1 0 1 0-1-1a1 1 0 0 0 1 1Zm5 0a1 1 0 1 0-1-1a1 1 0 0 0 1 1Zm0-4a1 1 0 1 0-1-1a1 1 0 0 0 1 1Zm-5 0a1 1 0 1 0-1-1a1 1 0 0 0 1 1Zm7-12h-1V2a1 1 0 0 0-2 0v1H8V2a1 1 0 0 0-2 0v1H5a3 3 0 0 0-3 3v14a3 3 0 0 0 3 3h14a3 3 0 0 0 3-3V6a3 3 0 0 0-3-3Zm1 17a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1v-9h16Zm0-11H4V6a1 1 0 0 1 1-1h1v1a1 1 0 0 0 2 0V5h8v1a1 1 0 0 0 2 0V5h1a1 1 0 0 1 1 1ZM7 15a1 1 0 1 0-1-1a1 1 0 0 0 1 1Zm0 4a1 1 0 1 0-1-1a1 1 0 0 0 1 1Z"
        />
      </symbol>
      <symbol xmlns="http://www.w3.org/2000/svg" id="plus" viewBox="0 0 24 24">
        <path
          fill="currentColor"
          d="M19 11h-6V5a1 1 0 0 0-2 0v6H5a1 1 0 0 0 0 2h6v6a1 1 0 0 0 2 0v-6h6a1 1 0 0 0 0-2Z"
        />
      </symbol>
      <symbol xmlns="http://www.w3.org/2000/svg" id="minus" viewBox="0 0 24 24">
        <path fill="currentColor" d="M19 11H5a1 1 0 0 0 0 2h14a1 1 0 0 0 0-2Z" />
      </symbol>
      <symbol xmlns="http://www.w3.org/2000/svg" id="cart" viewBox="0 0 24 24">
        <path
          fill="currentColor"
          d="M8.5 19a1.5 1.5 0 1 0 1.5 1.5A1.5 1.5 0 0 0 8.5 19ZM19 16H7a1 1 0 0 1 0-2h8.491a3.013 3.013 0 0 0 2.885-2.176l1.585-5.55A1 1 0 0 0 19 5H6.74a3.007 3.007 0 0 0-2.82-2H3a1 1 0 0 0 0 2h.921a1.005 1.005 0 0 1 .962.725l.155.545v.005l1.641 5.742A3 3 0 0 0 7 18h12a1 1 0 0 0 0-2Zm-1.326-9l-1.22 4.274a1.005 1.005 0 0 1-.963.726H8.754l-.255-.892L7.326 7ZM16.5 19a1.5 1.5 0 1 0 1.5 1.5a1.5 1.5 0 0 0-1.5-1.5Z"
        />
      </symbol>

      <symbol xmlns="http://www.w3.org/2000/svg" id="star-outline" viewBox="0 0 15 15">
        <path
          fill="none"
          stroke="currentColor"
          stroke-linecap="round"
          stroke-linejoin="round"
          d="M7.5 9.804L5.337 11l.413-2.533L4 6.674l2.418-.37L7.5 4l1.082 2.304l2.418.37l-1.75 1.793L9.663 11L7.5 9.804Z"
        />
      </symbol>
      <symbol xmlns="http://www.w3.org/2000/svg" id="star-solid" viewBox="0 0 15 15">
        <path
          fill="#FFC43F"
          d="M7.953 3.788a.5.5 0 0 0-.906 0L6.08 5.85l-2.154.33a.5.5 0 0 0-.283.843l1.574 1.613l-.373 2.284a.5.5 0 0 0 .736.518l1.92-1.063l1.921 1.063a.5.5 0 0 0 .736-.519l-.373-2.283l1.574-1.613a.5.5 0 0 0-.283-.844L8.921 5.85l-.968-2.062Z"
        />
      </symbol>
    </defs>
  </svg>

  <div class="col" v-if="!isLoading">
    <div class="product-item">
      <div>
        <figure style="overflow: hidden; height: 250px; width: 100%;" >
          <img
            :src="menuSrc"
            @error="menuSrc = 'public/images/avatar.jpg'"
            class="img-fixed "
            v-on:click.prevent.stop="viewDescribeModal(menu)"
          />
        </figure>
      </div>

      <h3>{{ menu.productName }}</h3>

      <div class="d-flex align-items-bottom justify-content-between">
        <div>
          <span class="rating">
            <svg width="24" height="24" class="text-primary">
              <use xlink:href="#star-solid"></use></svg>{{ menu.avgScore }}
          </span>
          <span style="padding-left: 5px; font-size: smaller">({{ commentPeople }})</span>
        </div>

        <li
          class="lookComment"
          v-on:click.prevent.stop="viewComment(menu)"
          style="list-style-type: none"
        >
          <i class="bi bi-chat-left-text"></i>觀看評論
        </li>
      </div>

      <div class="d-flex justify-content-between">
        <span class="price">NT${{ menu.productPrice }}</span>

      </div>

      <div class="d-flex align-items-center justify-content-between">
        <div class="input-group product-qty">
          <span class="input-group-btn">
            <button
              type="button"
              class="quantity-left-minus btn btn-danger btn-number"
              data-type="minus"
              v-on:click.prevent="minusOne"
              :disabled="this.count == 0"
            >
              <svg width="16" height="16"><use xlink:href="#minus"></use></svg>
            </button>
          </span>

          <input
            type="text"
            name="quantity"
            class="form-control input-number quantity"
            v-model="count"
            v-on:input="updateQuantity($event)"
          />

          <span class="input-group-btn">
            <button
              type="button"
              class="quantity-right-plus btn btn-success btn-number"
              data-type="plus"

            >
              <svg width="16" height="16" v-on:click.prevent="addOne">
                <use xlink:href="#plus"></use>
              </svg>
            </button>
          </span>
        </div>

        <button
          class="btn btn-primary mt-3"
          @click="handleAddToCart(menu.id)"
          :disabled="count <= 0 "
        >
          Add to Cart
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import StarRating from 'vue-star-rating'
import LoadingVue from 'vue3-loading-overlay'
import { mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore'
import { useNotificationStore } from '@/stores/notificationStore.js'

export default {
  components: {
    'star-rating': StarRating,
    LoadingVue,
  },
  props: {
    menu: {
      type: Object, // menu 應該是一個物件
      required: true, // 如果 menu 是必需的
    },
    cartItems: {
      type: Array,
      required: true,
    },
  },

  emits: ['update-count', 'addToCart', 'image-loaded', 'view-comment', 'view-menudescribe'],

  data() {
    return {
      menuSrc: '',
      memberSrc: '',
      isLoading: false,
      count: 1,
      comments: [],
      commentPeople: 0,
      cartCount: 0,
      isAddDisabled:false,


      totalCount:0




    }
  },
  methods: {
    ...mapActions(cartStore, ['addToCart']),
    ...mapActions(useNotificationStore, ['showNotification', 'success', 'error', 'info', 'warn']),

    async loadPicture(ID) {
      this.isLoading = true

      let API_URL = `${import.meta.env.VITE_API}/api/menu/photo/${ID}`

      axios
        .get(API_URL, { responseType: 'blob' })
        .then(async (response) => {
          let url = URL.createObjectURL(response.data)
          this.menuSrc = url
          this.isLoading = false
        })
        .catch((error) => {
          console.error('Error fetching menus:', error)
          this.isLoading = false
        })
    },

    async minusOne() {
      if (this.count > 0) {
        this.count -= 1
        this.$emit('update-count', this.count)
      }
    },

    // 加一的方法需要考慮購物車數量
    async addOne() {

        this.count += 1
        this.$emit('update-count', this.count)

    },
    async getCommentPeople() {
      let API_URL = `${import.meta.env.VITE_API}/api/${this.menu.productName}/comment/people`
      axios
        .get(API_URL)
        .then(async (response) => {
          this.commentPeople = response.data
        })
        .catch((error) => {
          console.error('Error fetching commentPeople:', error)
        })
    },

    async viewComment(menu) {
      this.$emit('view-comment', menu)
    },
    async viewDescribeModal(menu) {
      this.$emit('view-menudescribe', menu)
    },
    handleAddToCart(id) {


      const productName = this.menu.productName
      this.addToCart({ id, count: this.count })


      this.count = 1
      // 黑灰底白字的提示框
      Swal.fire({
        toast: true,
        title: `「${productName}」成功加入購物車！`,
        position: 'top-end',
        icon: 'success',
        background: '#fff', // 黑灰底
        color: '#000000', // 白字
        iconColor: '#000000', // 成功
        showConfirmButton: false, //不顯示確認按鈕
        timer: 2330, //時間
        timerProgressBar: true, //進度條
        didOpen: (toast) => {
          toast.style.marginTop = '80px' // 動態調整位置
        },
      })


    },
    updateQuantity(event) {
      const value = parseInt(event.target.value, 10)
      if (value >= 0 && value <= this.menu.productCount) {
        this.count = value
      } else if (value > this.menu.productCount) {
        Swal.fire({
          title: '已到達庫存上限',
          text: '請重新選擇數量',
          icon: 'error',

          confirmButtonColor: 'black',
          confirmButtonText: '確定',

        })
        this.count = this.menu.productCount
      }
    },
  },



  async created() {
    await this.loadPicture(this.menu.id)
    await this.getCommentPeople()
  },


  computed: {
    // 計算剩餘可添加的庫存數量
    remainingStock() {
      return this.menu.productCount - this.cartCount
    },

    // 判斷是否應該禁用添加按鈕
    isAddDisabled() {
      // 當前選擇數量 + 購物車數量 >= 總庫存，或當前選擇數量 > 剩餘庫存
      return (this.count + this.cartCount > this.menu.productCount) ||
            (this.count >= this.remainingStock)
    }
  },
  watch: {

  }

}
</script>

<style scoped>
.img-fixed {
  min-height: 100%;
  width: 100%;
  object-fit: cover;
  border-radius: 5px;
}
.card {
  margin-bottom: 5px;
  overflow: hidden;
}

figure > img {
  cursor: pointer;
}

.modal-header,
.modal-body {
  color: black;
}

.avatar-container {
  background-repeat: no-repeat;
  background-size: contain;
  background-position: center;
}

.avatar {
  vertical-align: middle;
  width: 100px;
  height: 100px;
  border-radius: 50%;
}

.product-item .product-qty {
  width: 85px;
}
product-qty {
  min-width: 130px;
}
.quantity {
  height: auto;
  width: 28px;
  text-align: center;
  border: none;
  margin: 0;
  padding: 0;
}

.product-item .rating {
  font-weight: 600;
  font-size: 13px;
  line-height: 18px;
  text-transform: capitalize;
  color: #222222;
}

.cart .product-qty {
  min-width: 130px;
}

.btn-primary {
  --bs-btn-color: #fff;
  --bs-btn-bg: #111111;
  --bs-btn-border-color: transparent;
  --bs-btn-hover-color: #fff;
  --bs-btn-hover-bg: #3b3b3b;
  --bs-btn-hover-border-color: transparent;
  --bs-btn-focus-shadow-rgb: 49, 132, 253;
  --bs-btn-active-color: #fff;
  --bs-btn-active-bg: #111111;
  --bs-btn-active-border-color: transparent;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #fff;
  --bs-btn-disabled-bg: #d3d7dd;
  --bs-btn-disabled-border-color: transparent;
}
.btn-outline-primary {
  --bs-btn-color: #ffc43f;
  --bs-btn-border-color: #ffc43f;
  --bs-btn-hover-color: #fff;
  --bs-btn-hover-bg: #ffc107;
  --bs-btn-hover-border-color: #ffc107;
  --bs-btn-focus-shadow-rgb: 13, 110, 253;
  --bs-btn-active-color: #fff;
  --bs-btn-active-bg: #ffc107;
  --bs-btn-active-border-color: #ffc107;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #fff3cd;
  --bs-btn-disabled-bg: transparent;
  --bs-btn-disabled-border-color: #fff3cd;
  --bs-gradient: none;
}
.btn-outline-light {
  --bs-btn-color: #747474;
  --bs-btn-border-color: #efefef;
  --bs-btn-hover-color: #000;
  --bs-btn-hover-bg: #efefef;
  --bs-btn-hover-border-color: #efefef;
  --bs-btn-focus-shadow-rgb: 248, 249, 250;
  --bs-btn-active-color: #000;
  --bs-btn-active-bg: #efefef;
  --bs-btn-active-border-color: #efefef;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #efefef;
  --bs-btn-disabled-bg: transparent;
  --bs-btn-disabled-border-color: #efefef;
  --bs-gradient: none;
}

.btn-success {
  --bs-btn-color: #222;
  --bs-btn-bg: #eef5e4;
  --bs-btn-border-color: #eef5e4;
  --bs-btn-hover-color: #222;
  --bs-btn-hover-bg: #9de3c2;
  --bs-btn-hover-border-color: #9de3c2;
  --bs-btn-focus-shadow-rgb: 60, 153, 110;
  --bs-btn-active-color: #222;
  --bs-btn-active-bg: #9de3c2;
  --bs-btn-active-border-color: #9de3c2;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #222;
  --bs-btn-disabled-bg: #eef5e4;
  --bs-btn-disabled-border-color: #eef5e4;
}
.btn-danger {
  --bs-btn-color: #222;
  --bs-btn-bg: #ffeada;
  --bs-btn-border-color: #ffeada;
  --bs-btn-hover-color: #222;
  --bs-btn-hover-bg: #ecc9af;
  --bs-btn-hover-border-color: #ecc9af;
  --bs-btn-focus-shadow-rgb: 60, 153, 110;
  --bs-btn-active-color: #222;
  --bs-btn-active-bg: #ecc9af;
  --bs-btn-active-border-color: #ecc9af;
  --bs-btn-active-shadow: inset 0 3px 5px rgba(0, 0, 0, 0.125);
  --bs-btn-disabled-color: #222;
  --bs-btn-disabled-bg: #ffeada;
  --bs-btn-disabled-border-color: #ffeada;
}

.btn-number {
  width: 26px;
  height: 26px;
  line-height: 1;
  text-align: center;
  background: #ffffff;
  border: 1px solid #e2e2e2;
  border-radius: 6px;
  color: #222;
  padding: 0;
}

.product-item {
  position: relative;
  padding: 16px;
  background: #ffffff;
  border: 1px solid #fbfbfb;
  box-shadow: 0px 5px 22px rgba(0, 0, 0, 0.04);
  border-radius: 16px;
  margin-bottom: 30px;
  transition: box-shadow 0.3s ease-out;
}
.product-item:hover {
  box-shadow: 0px 21px 44px rgba(0, 0, 0, 0.08);
}
.product-item h3 {
  display: block;
  width: 100%;
  font-weight: 600;
  font-size: 18px;
  line-height: 25px;
  text-transform: capitalize;
  color: #333333;
  margin: 0;
}
.product-item figure {
  background: #f9f9f9;
  border-radius: 12px;
  text-align: center;
}
.product-item figure img {
  max-height: 210px;
  height: auto;
}
.product-item .btn-wishlist {
  position: absolute;
  top: 20px;
  right: 20px;
  width: 50px;
  height: 50px;
  border-radius: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  border: 1px solid #d8d8d8;
  transition: all 0.3s ease-out;
}
.product-item .btn-wishlist:hover {
  background: rgb(240, 56, 56);
  color: #fff;
}
.product-item .qty {
  font-weight: 400;
  font-size: 13px;
  line-height: 18px;
  letter-spacing: 0.02em;
  text-transform: uppercase;
  color: #9d9d9d;
}
.product-item .rating {
  font-weight: 600;
  font-size: 13px;
  line-height: 18px;
  text-transform: capitalize;
  color: #222222;
}
.product-item .rating iconify-icon {
  color: #ffc43f;
}
.product-item .price {
  display: block;
  width: 50%;
  font-weight: 600;
  font-size: 22px;
  line-height: 30px;
  text-transform: capitalize;
  color: #222222;
}
.product-item .product-qty {
  width: 85px;
}
.product-item .btn-link {
  text-decoration: none;
}
.product-item #quantity {
  height: auto;
  width: 28px;
  text-align: center;
  border: none;
  margin: 0;
  padding: 0;
}
.product-item .btn-number {
  width: 26px;
  height: 26px;
  line-height: 1;
  text-align: center;
  background: #ffffff;
  border: 1px solid #e2e2e2;
  border-radius: 6px;
  color: #222;
  padding: 0;
}

.nav-link {
  --bs-nav-pills-link-active-color: #111;
  --bs-nav-pills-link-active-bg: #f1f1f1;
}

.lookComment:hover {
  cursor: pointer;
}
</style>
