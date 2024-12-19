<template>
  <div>
    <BannerTop v-bind:title="'Shopping Cart'"></BannerTop>
  </div>
  <!-- Step Indicator -->
  <div class="p-5">
    <div class="step-indicator mb-1 mt-3">
      <div class="step active">1</div>
      <div class="step-connector"></div>
      <div class="step">2</div>
      <div class="step-connector"></div>
      <div class="step">3</div>
    </div>
  </div>

  <div class="container py-5">
    <div class="row">
      <div class="col-lg-8">
        <!-- Cart Items -->
        <div class="card mb-4">
          <div class="card-body">
            <div
              v-for="item in cartItems"
              :key="item.cartId"
              class="row cart-item mb-3">
              <div class="col-md-3">
                <img
                  :src="item.menu.productImgUrl"
                  :alt="item.menu.productName"
                  class="img-fluid rounded"
                />
              </div>
              <div class="col-md-5 text-black">
                <h5 class="card-title">{{ item.menu.productName }}</h5>
                <p class="text-muted">Category: {{ item.menu.productCategory }}</p>
              </div>
              <div class="col-md-2">
                <div class="input-group">
                  <button
                    class="btn btn-outline-secondary btn-sm"
                    type="button"
                    @click="decreaseQuantity(item)">-
                  </button>
                  <input
                    style="max-width: 100px"
                    type="text"
                    class="form-control form-control-sm text-center quantity-input"
                    v-model="item.cartCount"
                    readonly
                  />
                  <button
                    class="btn btn-outline-secondary btn-sm"
                    type="button"
                    @click="increaseQuantity(item)">+
                  </button>

                </div>
              </div>
              <div class="col-md-2 text-end text-black">
                <p class="fw-bold">${{ (item.cartCount * item.menu.productPrice).toFixed(2) }}</p>
                <button
                  class="btn btn-lg btn-outline-danger"
                  @click="removeFromCart(item)">
                  <i class="bi bi-trash"></i>
                </button>
              </div>
            </div>
          </div>
        </div>
        <!-- Continue Shopping Button -->
        <div class="text-start mb-4">
          <a href="#" class="btn btn-outline-primary">
            <i class="bi bi-arrow-left me-2"></i>Continue Shopping
          </a>
        </div>
      </div>

      <div class="col-lg-4">
        <!-- Cart Summary -->
        <div class="card cart-summary">
          <div class="card-body text-black">
            <h5 class="card-title mb-4">Order Summary</h5>

            <div class="d-flex justify-content-between mb-3">
              <span>Subtotal</span>
              <span>${{ calculateSubtotal }}</span>
            </div>

            <div class="d-flex justify-content-between mb-3">
              <span>Tax</span>
              <span>${{ calculateTax }}</span>
            </div>

            <hr/>

            <div class="d-flex justify-content-between mb-4" style="color: #dd2222">
              <strong>Total</strong>
              <strong>${{ calculateTotal }}</strong>
            </div>
            <button class="btn btn-dark w-100">
              <router-link class="nav-link" to="/cartCheckout">Proceed to Checkout</router-link>
            </button>
          </div>
        </div>

        <!-- Promo Code -->
        <div class="card mt-4">
          <div class="card-body">
            <h5 class="card-title mb-3">Apply Promo Code</h5>
            <div class="input-group mb-3">
              <input type="text" class="form-control" placeholder="Enter promo code">
              <button class="btn btn-outline-secondary" type="button">Apply</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>




<script>
import { mapState, mapActions } from 'pinia'
import CartButton from '@/components/cart/CartButton.vue'
import CartTable from '@/components/cart/CartTable.vue'
import CartTitle from '@/components/cart/CartTitle.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import BannerTop from '@/components/BannerTop.vue'
import PageTop from '@/components/PageTop.vue'
import { cartStore } from '@/stores/cartStore.js'
import { useUserStore } from '@/stores/userStore.js'
const user = useUserStore()


export default {
  name: 'Cart',
  components: {
    PageTop,
    BannerTop,
    FontAwesomeIcon,
    CartTitle,
    CartTable,
    CartButton,
  },
  data() {
    return {
      cartItems: [],
    };
  },
  methods: {
    ...mapActions(cartStore, ["getCart","CountCart","MinusCart","removeItem"]),

  //all
    async fetchCartItems() {
      try {
        const result = await this.getCart();
        if (result && result.data) {
          console.log('Fetched cart items:', result.data);
          this.cartItems = result.data;
        }
      } catch (error) {
        console.error('Failed to fetch cart items:', error);
      }
    },


    //++
      async increaseQuantity(item) {
        try {
          // 確保傳遞完整的 menu 對象
          const result = await this.CountCart(item.menu);
          if (result && result.data) {
            this.cartItems = result.data;
          }
        } catch (error) {
          console.error('Failed to increase quantity:', error);
        }
      },
    //--
    async decreaseQuantity(item) {
      if (item.cartCount > 1) {
        try {
          const result = await this.MinusCart(item.menu);
          if (result && result.data) {
            this.cartItems = result.data;
          }
        } catch (error) {
          console.error('Failed to decrease quantity:', error);
        }
      } else {
        // 數量=1，直接刪掉
        try {
          const result = await this.MinusCart(item.menu);
          if (result && result.data) {
            this.cartItems = result.data;
          }
        } catch (error) {
          console.error('Failed to remove item:', error);
        }
      }
    },
  //刪除的controller還沒做 ==
    async removeFromCart(item) {
      if (user.memberId) {
        try {
          await this.removeItem(item)
          await this.fetchCartItems() // 重新獲取購物車數據
        } catch (error) {
          console.error('刪除商品失敗:', error)
        }
      } else {
        console.error('未登入會員')
      }
    }
  },
  computed: {
    //getter or state 放在computed
    ...mapState(cartStore,["calculateSubtotal","calculateTax","calculateTotal"]),

  },
  watch: {},
  created() {
    this.fetchCartItems()
  },
}
</script>


<style scoped>
.step-indicator {
  display: flex;
  justify-content: center;
  margin-bottom: 2rem;
}

.step {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #e9ecef;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #6c757d;
  position: relative;
  z-index: 1;
}

.step.active {
  background-color: #000;
  color: white;
}

.step.completed {
  background-color: #198754;
  color: white;
}

.step-connector {
  width: 100px;
  height: 2px;
  background-color: #e9ecef;
  margin: 17px 10px;
}

.step-connector.active {
  background-color: #000;
}
</style>
