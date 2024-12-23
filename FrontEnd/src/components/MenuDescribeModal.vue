<template>
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
            <img
              :src="menuSrc"
              @error="menuSrc = 'public/images/avatar.jpg'"
              alt=""
              class="img-fluid"
              style="border-radius: 5px"
            />

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
            <div class="d-flex align-items-center justify-content-between">
              <div class="input-group product-qty">
                <span class="input-group-btn">
                  <button
                    type="button"
                    class="quantity-left-minus btn btn-danger btn-number"
                    data-type="minus"
                    v-on:click.prevent="minusOne"
                  >
                    <svg width="16" height="16"><use xlink:href="#minus"></use></svg>
                  </button>
                </span>

                <input
                  type="text"
                  name="quantity"
                  class="input-number quantity"
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

              <button class="btn btn-primary mt-3" @click="handleAddToCart(menu.id)">
                Add to Cart
              </button>
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
import axios from 'axios'
import { mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore'
import { useNotificationStore } from '@/stores/notificationStore.js'
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
      count: 1,
    }
  },

  methods: {


    ...mapActions(cartStore, ['addToCart']),
    ...mapActions(useNotificationStore, ['showNotification', 'success', 'error', 'info', 'warn']),

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
    async minusOne() {
      if (this.count > 0) {
        this.count -= 1
        this.$emit('update-count', this.count)
      }
    },

    async addOne() {
      if (this.count < this.menu.productCount) {
        this.count += 1
        this.$emit('update-count', this.count)
      } else {
        this.count = this.menu.productCount
        this.$emit('update-count', this.count)
        Swal.fire({
          title: '已到達庫存上限',
          text: '請重新選擇數量',
          icon: 'error',
        })
      }
    },

    handleAddToCart(id) {
      this.addToCart({ id, count: this.count })
      this.addToCartAndMinusStock(id, this.count)
      this.count = 1
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
        })
        this.count = this.menu.productCount
      }
    },

    async addToCartAndMinusStock(productId, quantity) {
      const API_URL = `${import.meta.env.VITE_API}/api/menu/minusCartStock/${productId}`

      const payload = {
        productId: id,
        quantity: quantity, // 減少的數量
      }
      try {
        // 發送PUT請求到後端，更新庫存
        const response = await axios.put(API_URL, payload)

        console.log(this.menu.productCount)
        console.log('庫存已更新')
        // 更新本地對應商品的庫存

        if (response.data.success) {
          // 更新本地庫存（確保 UI 與後端同步）
          this.menu.productCount = response.data.productCount
          Swal.fire({
            text: '成功加入購物車',
            icon: 'success',
          })
        } else {
          throw new Error('庫存更新失敗')
        }
      } catch (error) {
        console.error('更新庫存時發生錯誤:', error)
        Swal.fire({
          title: '錯誤',
          text: '更新庫存時發生錯誤，請稍後再試。',
          icon: 'error',
        })
      }
    },
  },
  watch: {
    // 當菜單數據改變時重新加載圖片
    menu(newMenu) {
      if (newMenu.id) {
        this.loadPicture(newMenu.id)
      }
    },
  },
}
</script>

<style scoped>
img {
  max-width: 95%;
}

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

.product-item .product-qty {
  width: 85px;
}
product-qty {
  min-width: 130px;
}
.quantity {
  height: auto;
  max-width: 100px;
  text-align: center;
  border: none;
  margin: 0;
  padding: 0;
}

.cart .product-qty {
  min-width: 130px;
}


.product-item .product-qty {
  width: 85px;
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



.nav-link {
  --bs-nav-pills-link-active-color: #111;
  --bs-nav-pills-link-active-bg: #f1f1f1;
}
</style>
