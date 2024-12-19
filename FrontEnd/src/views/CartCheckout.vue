<template>
  <div>
    <BannerTop v-bind:title="'Check Out'"></BannerTop>
  </div>
  <!-- Step Indicator -->
  <div class="p-5">
    <!-- Step Indicator -->
    <div class="step-indicator mb-1 mt-3">
      <div class="step">1</div>
      <div class="step-connector"></div>
      <div class="step active">2</div>
      <div class="step-connector"></div>
      <div class="step">3</div>
    </div>
  </div>
  <!-- Checkout Section Begin -->
  <section class="checkout spad">
    <div class="container">
      <div class="checkout__form">
        <h4>Confirm Order</h4>


        <form @submit.prevent="placeOrder">
          <div class="row">
            <div class="col-lg-8 col-md-6">
              <div class="row">

                  <div class="col-lg-6">
                    <div class="checkout__input">
                      <p>姓名<span>*</span></p>
                      <input type="text" v-model="orderData.ordersName" placeholder="姓名" required />
                    </div>
                  </div>

                  <div class="col-lg-6">
                    <div class="checkout__input">
                      <p>電話<span>*</span></p>
                      <input type="text" v-model="orderData.ordersTel" placeholder="+886" required />
                    </div>
                  </div>

              </div>

              <div class="checkout__input">
                <p>Order notes<span>*</span></p>
                <input type="text" v-model="orderData.ordersRequest" placeholder="特殊要求" />
              </div>

              <div class="checkout__input__checkbox">
                <label for="acc">
                  Create an account?
                  <input type="checkbox" id="acc" />
                  <span class="checkmark"></span>
                </label>
                <p>Create an account by entering the information below</p>
              </div>

            </div>

            <div class="col-lg-4 col-md-6">
              <div class="checkout__order">
                <!-- 金額 -->
                <h4>Your Order</h4>
                <div class="checkout__order__products">Products <span>Total</span></div>
                <ul>
                  <li v-for="item in cartItems" :key="item.menu.id">
                    {{ item.menu.productName }}
                    <span>${{ (item.cartCount * item.menu.productPrice).toFixed(2) }}</span>
                  </li>
                </ul>
                <div class="checkout__order__subtotal">
                  Subtotal
                  <span>${{ calculateSubtotal }}</span>
                </div>
                <div class="checkout__order__total" >
                  Tax
                  <span style="color: #1c1c1c">${{ calculateTax }}</span>
                </div>
                <div class="checkout__order__total">
                  Total
                  <span>${{ calculateTotal }}</span>
                </div>

                <!-- 用餐方式 -->
                  <div class="checkout__input__checkbox">
                    <h4>用餐方式</h4>
                    <label for="eatin">
                      內用
                      <input type="radio" id="eatin" value="內用" v-model="orderData.seatType" required/>
                      <span class="checkmark"></span>
                    </label>
                  </div>

                  <div class="checkout__input__checkbox">
                    <label for="takeout">
                      外帶
                      <input type="radio" id="takeout" value="外帶" v-model="orderData.seatType" />
                      <span class="checkmark"></span>
                    </label>
                  </div>


                <!-- 付款方式 -->
                  <div class="checkout__input__checkbox">
                    <h4>付款方式</h4>
                    <label for="cash">
                      <img class="pay"
                           src="../../public/images/cash3.png"
                           alt="">
                      Cash
                      <input type="radio" id="cash" value="Cash" v-model="orderData.PaymentWay" />
                      <span class="checkmark"></span>
                    </label>
                  </div>

                  <div class="checkout__input__checkbox">
                    <label for="ECPay">
                      <img class="pay"
                           src="../../public/images/ecpay2.png"
                           alt="">
                      ECPay
                      <input type="radio" id="ECPay" value="ECPay" v-model="orderData.PaymentWay" />
                      <span class="checkmark"></span>
                    </label>
                  </div>

                  <div class="checkout__input__checkbox">
                    <label for="paypal">
                      <img class="pay" src="https://i.imgur.com/cMk1MtK.jpg" alt="">
                      Paypal
                      <input type="radio" id="paypal" value="Paypal" v-model="orderData.PaymentWay" />
                      <span class="checkmark"></span>
                    </label>
                  </div>

                <div>
                  <button type="submit" class="btn btn-dark w-100">PLACE ORDER</button>
                </div>

                <button class="btn btn-dark w-100">
                  <router-link class="nav-link" to="/cartCheckSuc">Success</router-link>
                </button>

                <button class="btn btn-dark w-100">
                  <router-link class="nav-link" to="/cartCheckFail">Fail</router-link>
                </button>
              </div>
            </div>
          </div>
        </form>



      </div>
    </div>
  </section>
  <!-- Checkout Section End -->
</template>

<script>
import { defineComponent } from 'vue'
import BannerTop from '@/components/BannerTop.vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import PageTop from '@/components/PageTop.vue'
import { mapState, mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore.js'
import axios from 'axios'
import { useUserStore } from '@/stores/userStore.js'
axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true
const user = useUserStore()
export default defineComponent({

  components: { PageTop, BannerTop },
  data() {
    return {
      cartItems:[],
      orderData: {
        ordersName: '', // 姓名
        ordersTel: '', // 電話
        seatType: '', // 用餐方式
        ordersRequest: '', // 特殊需求
        PaymentWay: '', // 付款方式
        ordersSumPrice: 0, // 總金額
        ordersDetails: [], // 訂單詳情
        memberId: null, // 會員id
      },
    };
  },
  methods: {
    ...mapActions(cartStore, ["getCart","clearCart"]),

    async placeOrder() {
      try {
        // 檢查購物車是否為空
          if (!this.cartItems || this.cartItems.length === 0) {
            throw new Error("購物車是空的")
          }

        // 準備訂單數據，確保與 DTO 結構匹配
          const orderData = {
            ordersName: this.orderData.ordersName,
            ordersTel: this.orderData.ordersTel,
            seatType: this.orderData.seatType,
            ordersRequest: this.orderData.ordersRequest,
            ordersSumPrice: parseFloat(this.calculateTotal),
            latestPaymentStatus: '已付款', // 根據您的業務邏輯設置
            memberId: user.memberId, // 如果有會員系統，在此設置

            ordersDetails: this.cartItems.map(item => ({
              odName: item.menu.productName,
              odQuantity: item.cartCount,
              odPrice: item.menu.productPrice,
              odSumPrice: item.cartCount * item.menu.productPrice,
              menuId: item.menu.id,
            })),
            payments: [
              {
                paymentPrice: parseFloat(this.calculateTotal),
                paymentWay: this.orderData.PaymentWay,
                paymentStatus: '成功',
              }
            ]
          };

        // 使用完整的 URL 發送請求
        const response = await axios.post(`${import.meta.env.VITE_API}/api/orders/create`, orderData);
          console.log(orderData.memberId)
          if (response.status === 200) {
            console.log('Order created successfully:', response.data);
            // 清空購物車
              this.clearCart();
            //跳轉
            this.$router.push({
              path: '/cartCheckSuc',
              query: { orderNumber: response.data.ordersNumber }
            });
          } else {
            console.error('Order creation failed:', response.data);
            this.$router.push('/cartCheckFail'); // 跳失敗
          }
      }
      catch (error) {
          console.error('Error placing order:', error);
          this.$router.push('/cartCheckFail'); // 跳失敗
        }
    },



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

  },
  mounted() {
    //畫面動畫用的
  },
  computed: {
    //改變一個值，計算回來，賦值給一個新的值
    //ex:做篩選不一樣的人群，出現不一樣的結果
    //getter or state 放在computed
      ...mapState(cartStore,["calculateSubtotal","calculateTax","calculateTotal"]),
  },
  watch:{
    //副作用:watch個值，有一個值改變，其他也跟著改變，不會return值


  },
  created() {
    //撈資料用的
      this.getCart();
      this.fetchCartItems();
  },
})
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
  background-color: #0d6efd;
}
.checkout {
  padding-top: 40px;
  padding-bottom: 60px;
}

.checkout h6 {
  color: #999999;
  text-align: center;
  background: #f5f5f5;
  border-top: 1px solid #000;
  padding: 12px 0 12px;
  margin-bottom: 75px;
}

.checkout h6 span {
  font-size: 16px;
  color: #000;
  margin-right: 5px;
}

.checkout h6 a {
  text-decoration: underline;
  color: #999999;
}

.checkout__form h4 {
  color: #1c1c1c;
  font-weight: 700;
  border-bottom: 1px solid #e1e1e1;
  padding-bottom: 20px;
  margin-bottom: 25px;
}

.checkout__form p {
  column-rule: #b2b2b2;
}

.checkout__input {
  margin-bottom: 24px;
}

.checkout__input p {
  color: #1c1c1c;
  margin-bottom: 20px;
}

.checkout__input p span {
  color: #dd2222;
}

.checkout__input input {
  width: 100%;
  height: 46px;
  border: 1px solid #ebebeb;
  padding-left: 20px;
  font-size: 16px;
  color: #b2b2b2;
  border-radius: 4px;
}

.checkout__input input.checkout__input__add {
  margin-bottom: 20px;
}

.checkout__input input::placeholder {
  color: #b2b2b2;
}

.checkout__input__checkbox {
  margin-bottom: 12px;
}

.checkout__input__checkbox label {
  position: relative;
  font-size: 16px;
  color: #1c1c1c;
  padding-left: 40px;
  cursor: pointer;
}

.checkout__input__checkbox label input {
  position: absolute;
  visibility: hidden;
}

.checkout__input__checkbox label input:checked ~ .checkmark {
  background: #000;
  border-color: #000;
}

.checkout__input__checkbox label input:checked ~ .checkmark:after {
  opacity: 1;
}

.checkout__input__checkbox label .checkmark {
  position: absolute;
  left: 0;
  top: 4px;
  height: 16px;
  width: 14px;
  border: 1px solid #a6a6a6;
  content: '';
  border-radius: 4px;
}

.checkout__input__checkbox label .checkmark:after {
  position: absolute;
  left: 1px;
  top: 1px;
  width: 10px;
  height: 8px;
  border: solid white;
  border-width: 3px 3px 0px 0px;
  -ms-transform: rotate(127deg);
  transform: rotate(127deg);
  content: '';
  opacity: 0;
}

.checkout__order {
  background: #f5f5f5;
  padding: 40px;
  padding-top: 30px;
}

.checkout__order h4 {
  color: #1c1c1c;
  font-weight: 700;
  border-bottom: 1px solid #e1e1e1;
  padding-bottom: 10px;
  margin-bottom: 10px;
}

.checkout__order .checkout__order__products {
  font-size: 18px;
  color: #1c1c1c;
  font-weight: 700;
  margin-bottom: 10px;
}

.checkout__order .checkout__order__products span {
  float: right;
}

.checkout__order ul {
  margin-bottom: 12px;
}

.checkout__order ul li {
  font-size: 16px;
  color: #6f6f6f;
  line-height: 40px;
  list-style: none;
}

.checkout__order ul li span {
  font-weight: 700;
  float: right;
}

.checkout__order .checkout__order__subtotal {
  font-size: 18px;
  color: #1c1c1c;
  font-weight: 700;
  border-bottom: 1px solid #e1e1e1;
  border-top: 1px solid #e1e1e1;
  padding-bottom: 15px;
  margin-bottom: 15px;
  padding-top: 15px;
}

.checkout__order .checkout__order__subtotal span {
  float: right;
}

.checkout__order .checkout__input__checkbox label {
  padding-left: 20px;
}

.checkout__order .checkout__order__total {
  font-size: 18px;
  color: #1c1c1c;
  font-weight: 700;
  border-bottom: 1px solid #e1e1e1;
  padding-bottom: 15px;
  margin-bottom: 15px;
}
.checkout__order .checkout__order__cash {
  font-size: 18px;
  color: #1c1c1c;
  font-weight: 700;
  border-bottom: 1px solid #e1e1e1;
  padding-bottom: 10px;
  margin-bottom: 10px;
}

.checkout__order .checkout__order__total span {
  float: right;
  color: #dd2222;
}

.checkout__order button {
  font-size: 18px;
  letter-spacing: 2px;
  width: 100%;
  margin-top: 10px;
}
.pay {
  width: 77px;
  height: 45px;
  border-radius: 5px;
  border: 1px solid #000;
  margin: 10px 20px 10px 0px;
  cursor: pointer;
  box-shadow: 1px 5px 10px 1px rgba(0,0,0,0.2);
}
</style>
