<template>
  <div>
    <PageTop v-bind:page1="'Home'" v-bind:page2="'Cart'"></PageTop>
    <BannerTop v-bind:title="'Shopping Cart'"></BannerTop>
  </div>
  <!-- Step Indicator -->
  <div class="p-5">
    <!-- Step Indicator -->
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
            <div class="row cart-item mb-3">
              <div class="col-md-3">
                <img
                  src="https://via.placeholder.com/100"
                  alt="Product 1"
                  class="img-fluid rounded"
                />
              </div>
              <div class="col-md-5 text-black">
                <h5 class="card-title">Product 1</h5>
                <p class="text-muted">Category: Electronics</p>
              </div>
              <div class="col-md-2">
                <div class="input-group">
                  <button class="btn btn-outline-secondary btn-sm" type="button">-</button>
                  <input
                    style="max-width: 100px"
                    type="text"
                    class="form-control form-control-sm text-center quantity-input"
                    value="1"
                  />
                  <button class="btn btn-outline-secondary btn-sm" type="button">+</button>
                </div>
              </div>
              <div class="col-md-2 text-end text-black">
                <p class="fw-bold">$99.99</p>
                <button class="btn btn-lg btn-outline-danger">
                  <i class="bi bi-trash"></i>
                </button>
              </div>
            </div>
            <hr />
            <div class="row cart-item">
              <div class="col-md-3">
                <img
                  src="https://via.placeholder.com/100"
                  alt="Product 2"
                  class="img-fluid rounded"
                />
              </div>
              <div class="col-md-5 text-black">
                <h5 class="card-title">Product 2</h5>
                <p class="text-muted">Category: Clothing</p>
              </div>
              <div class="col-md-2">
                <div class="input-group">
                  <button class="btn btn-outline-secondary btn-sm" type="button">-</button>
                  <input
                    style="max-width: 100px"
                    type="text"
                    class="form-control form-control-sm text-center quantity-input"
                    value="2"
                  />
                  <button class="btn btn-outline-secondary btn-sm" type="button">+</button>
                </div>
              </div>
              <div class="col-md-2 text-end text-black">
                <p class="fw-bold">$49.99</p>
                <button class="btn btn-lg btn-outline-danger">
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
              <span>$199.97</span>
            </div>

            <div class="d-flex justify-content-between mb-3">
              <span>Tax</span>
              <span>$20.00</span>
            </div>

            <hr />

            <div class="d-flex justify-content-between mb-4">
              <strong>Total</strong>
              <strong>$229.97</strong>
            </div>

            <button class="btn btn-dark w-100">
              <router-link class="nav-link" to="/cartCheckout">Proceed to Checkout</router-link>
            </button>

          </div>
        </div>
        <!--        Promo Code-->
        <!--            <div class="card mt-4">-->
        <!--              <div class="card-body">-->
        <!--                <h5 class="card-title mb-3">Apply Promo Code</h5>-->
        <!--                  <div class="input-group mb-3">-->
        <!--                    <input type="text" class="form-control" placeholder="Enter promo code">-->
        <!--                    <button class="btn btn-outline-secondary" type="button">Apply</button>-->
        <!--                  </div>-->
        <!--              </div>-->
        <!--            </div>-->
      </div>
    </div>
  </div>



</template>

<script>
//import { mapState, mapActions } from 'pinia'
//import cartStore from "/@..."
import CartButton from '@/components/cart/CartButton.vue'
import CartTable from '@/components/cart/CartTable.vue'
import CartTitle from '@/components/cart/CartTitle.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import BannerTop from '@/components/BannerTop.vue'
import PageTop from '@/components/PageTop.vue'

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
    return {}
  },
  methods: {
    //...mapActions(cartStore, ["addToCart"])
  },
  computed: {
    //...mapState(cartStore, ["carts"])
  },
  watch: {},
  created() {
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
