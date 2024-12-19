<template>
  <div>
    <PageTop v-bind:page1="'Home'" v-bind:page2="'Cart'"></PageTop>
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
        <form action="#" ref="form">
          <div class="row">
            <div class="col-lg-8 col-md-6">
              <div class="row">
                <div class="col-lg-6">
                  <div class="checkout__input">
                    <p>Name<span>*</span></p>
                    <input type="text" placeholder="姓名" />
                  </div>
                </div>
<!--                <div class="col-lg-6">-->
<!--                  <div class="checkout__input">-->
<!--                    <p>Last Name<span>*</span></p>-->
<!--                    <input type="text" placeholder="姓" />-->
<!--                  </div>-->
<!--                </div>-->
              </div>

              <div class="row">
                <div class="col-lg-6">
                  <div class="checkout__input">
                    <p>Phone<span>*</span></p>
                    <input type="text" placeholder="+886 " />
                  </div>
                </div>
                <div class="col-lg-6">
                  <div class="checkout__input">
                    <p>Email<span>*</span></p>
                    <input type="text" placeholder="exexex@gmail.com" />
                  </div>
                </div>
              </div>

              <div class="checkout__input">
                <p>Order notes<span>*</span></p>
                <input type="text" placeholder="特殊要求" />
              </div>

              <div class="checkout__input__checkbox">
                <label for="acc">
                  Create an account?
                  <input type="checkbox" id="acc" />
                  <span class="checkmark"></span>
                </label>
              </div>
              <p>Create an account by entering the information below</p>
            </div>
            <div class="col-lg-4 col-md-6">
              <div class="checkout__order">
                <h4>Your Order</h4>
                <div class="checkout__order__products">Products <span>Total</span></div>
                <ul>
                  <li>Vegetable’s Package <span>$75.99</span></li>
                  <li>Fresh Vegetable <span>$151.99</span></li>
                  <li>Organic Bananas <span>$53.99</span></li>
                </ul>
                <div class="checkout__order__subtotal">Subtotal <span>$750.99</span></div>
                <div class="checkout__order__total">Total <span>$750.99</span></div>
                <!--                <div class="checkout__input__checkbox">-->
                <!--                  <label for="acc-or">-->
                <!--                    Create an account?-->
                <!--                    <input type="checkbox" id="acc-or">-->
                <!--                    <span class="checkmark"></span>-->
                <!--                  </label>-->
                <!--                </div>-->
                <!--                <p>加入會員享優會</p>-->

                <div class="checkout__input__checkbox">
                  <div class="checkout__order__cash">用餐方式</div>
                  <label for="eatin">
                    內用
                    <input type="radio" id="eatin" name="seatType" />
                    <span class="checkmark"></span>
                  </label>
                </div>

                <div class="checkout__input__checkbox">
                  <label for="takeout">
                    外帶
                    <input type="radio" id="takeout" name="seatType" />
                    <span class="checkmark"></span>
                  </label>
                </div>



                <div class="checkout__input__checkbox">
                  <div class="checkout__order__cash">PaymentWay</div>
                  <label for="cash">
                    Cash
                    <input type="radio" id="cash" name="paymentMethod" />
                    <span class="checkmark"></span>
                  </label>
                </div>

                <div class="checkout__input__checkbox">
                  <label for="paypal">
                    Paypal
                    <input type="radio" id="paypal" name="paymentMethod" />
                    <span class="checkmark"></span>
                  </label>
                </div>

<!--                <div class="checkout__input__checkbox">-->
<!--                  <label for="linePay">-->
<!--                    LinePay-->
<!--                    <input type="radio" id="linePay" name="paymentMethod" />-->
<!--                    <span class="checkmark"></span>-->
<!--                  </label>-->
<!--                </div>-->


                  <!-- 新增一個隱藏的 div 來放置綠界表單 -->
                  <div ref="ecpayFormContainer" style="display: none;"></div>

                <div>
                  <button type="button" class="btn btn-dark w-100">PLACE ORDER</button>
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
import PageTop from '@/components/PageTop.vue'
export default defineComponent({
  components: { PageTop, BannerTop },
  methods: {

    async jumpEcpay() {
      window.location.href = `${import.meta.env.VITE_API}/ecpayCheckout`;
    },
    async memberPointGet() {
      // const pointData {

      // }
    }

  }


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
  margin-bottom: 10px;
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
  -webkit-transform: rotate(127deg);
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
  padding-bottom: 20px;
  margin-bottom: 20px;
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
</style>
