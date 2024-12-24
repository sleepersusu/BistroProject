<template>
  <div>
    <BannerTop v-bind:title="'Check Out'"></BannerTop>

    <!-- Step Indicator -->
    <div class="p-5">
      <div class="step-indicator mb-1 mt-3">
        <div class="step">1</div>
        <div class="step-connector"></div>
        <div class="step active">2</div>
        <div class="step-connector"></div>
        <div class="step">3</div>
      </div>
    </div>

    <!-- Checkout Section -->
    <section class="checkout spad">
      <div class="container">
        <div class="checkout__form">
          <h4>確認訂單資訊</h4>
          <form @submit.prevent="placeOrder">
            <div class="row">
              <!-- Left Column - Customer Info -->
              <div class="col-lg-8 col-md-6">
                <div class="row">
                  <div class="col-lg-6">
                    <div class="checkout__input">
                      <p>姓名<span>*</span></p>
                      <input
                        type="text"
                        v-model="orderData.ordersName"
                        placeholder="請輸入訂購人姓名"
                        maxlength="15"
                        @input="validateName"
                        :class="{ 'is-invalid': nameError }"
                      />
                      <small v-if="nameError" class="text-danger">{{ nameError }}</small>
                    </div>
                  </div>
                  <div class="col-lg-6">
                    <div class="checkout__input">
                      <p>電話<span>*</span></p>
                      <input
                        type="text"
                        v-model="orderData.ordersTel"
                        placeholder="請輸入手機號碼，EX:0912345678"
                        maxlength="10"
                        @input="validatePhone"
                        :class="{ 'is-invalid': phoneError }"
                      />
                      <small v-if="phoneError" class="text-danger">{{ phoneError }}</small>
                    </div>
                  </div>
                </div>

                <div class="checkout__input">
                  <p>訂單備註</p>
                  <input
                    type="text"
                    v-model="orderData.ordersRequest"
                    maxlength="100"
                    placeholder="特殊要求"
                  />
                </div>
              </div>

              <!-- Right Column - Order Summary -->
              <div class="col-lg-4 col-md-6">
                <div class="checkout__order">
                  <h4>訂單明細</h4>
                  <div class="checkout__order__products">商品 <span>金額</span></div>
                  <ul>
                    <li v-for="item in cartItems" :key="item.menu.id">
                      {{ item.menu.productName }}
                      <span>${{ item.cartCount * item.menu.productPrice }}</span>
                    </li>
                    <li v-for="prize in pointPrizes" :key="prize.name">
                      {{ prize.name }}<span>$0</span>
                    </li>
                  </ul>

                  <div class="checkout__order__subtotal">
                    小計
                    <span>${{ calculateSubtotal }}</span>
                  </div>
                  <div class="checkout__order__total">
                    稅金
                    <span style="color: #1c1c1c">${{ calculateTax }}</span>
                  </div>
                  <div class="checkout__order__total">
                    總計
                    <span>${{ calculateTotal }}</span>
                  </div>

                  <!-- 用餐方式 -->
                  <div class="checkout__input__checkbox">
                    <h4>用餐方式</h4>
                    <label for="eatin">
                      <span class="ms-2">內用</span>
                      <input
                        type="radio"
                        id="eatin"
                        value="內用"
                        v-model="orderData.seatType"
                        required
                      />
                      <span class="checkmark"></span>
                    </label>
                  </div>

                  <div class="checkout__input__checkbox">
                    <label for="takeout">
                      <span class="ms-2">外帶</span>
                      <input type="radio" id="takeout" value="外帶" v-model="orderData.seatType" />
                      <span class="checkmark"></span>
                    </label>
                  </div>

                  <!-- 付款方式 -->
                  <div class="checkout__input__checkbox">
                    <h4>付款方式</h4>
                    <label for="cash">
                      <img class="pay ms-3" src="../../public/images/cash3.png" alt="現金支付" />
                      Cash
                      <input type="radio" id="cash" value="Cash" v-model="orderData.PaymentWay" />
                      <span class="checkmark mt-3"></span>
                    </label>
                  </div>

                  <div class="checkout__input__checkbox align-middle">
                    <label for="ECPay">
                      <img class="pay ms-3" src="../../public/images/ecpay2.png" alt="綠界支付" />
                      ECPay
                      <input type="radio" id="ECPay" value="ECPay" v-model="orderData.PaymentWay" />
                      <span class="checkmark mt-3"></span>
                    </label>
                  </div>

                  <div class="checkout__input__checkbox">
                    <label for="paypal">
                      <img
                        class="pay ms-3"
                        src="https://i.imgur.com/cMk1MtK.jpg"
                        alt="PayPal支付"
                      />
                      Paypal
                      <input
                        type="radio"
                        id="paypal"
                        value="Paypal"
                        v-model="orderData.PaymentWay"
                      />
                      <span class="checkmark mt-3"></span>
                    </label>
                  </div>

                  <!-- Submit Button -->
                  <button type="submit" class="btn btn-dark w-100" :disabled="isProcessing">
                    {{ isProcessing ? '處理中...' : '確認結帳' }}
                  </button>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { defineComponent } from 'vue'
import BannerTop from '@/components/BannerTop.vue'
import { mapState, mapActions } from 'pinia'
import { pointStore } from '@/stores/pointStore'
import { cartStore } from '@/stores/cartStore.js'
import { useUserStore } from '@/stores/userStore.js'
import axios from 'axios'
const user = useUserStore()

export default defineComponent({
  name: 'CartCheckout',
  components: { BannerTop },

  data() {
    return {
      nameError: '',
      phoneError: '',
      cartItems: [],
      isProcessing: false,
      orderData: {
        ordersName: '',
        ordersTel: '',
        seatType: '',
        ordersRequest: '',
        PaymentWay: '',
        ordersSumPrice: 0,
        ordersDetails: [],
        memberId: null,
      },
    }
  },

  computed: {
    ...mapState(cartStore, ['calculateSubtotal', 'calculateTax', 'calculateTotal']),
    ...mapState(pointStore, ['pointPrizes']),
  },

  methods: {
    ...mapActions(cartStore, ['getCart', 'clearCart']),
    ...mapActions(pointStore, ['removePointPrize', 'clearPointPrizes', 'verifyPromoCode']),

    // 驗證訂單數據
    validateOrderData() {
      if (!this.orderData.ordersName?.trim()) {
        alert('請輸入姓名')
        return false
      }
      if (!this.orderData.ordersTel?.trim()) {
        alert('請輸入電話')
        return false
      }
      if (!this.orderData.seatType) {
        alert('請選擇用餐方式')
        return false
      }
      if (!this.orderData.PaymentWay) {
        alert('請選擇付款方式')
        return false
      }
      if (!this.cartItems?.length) {
        alert('購物車是空的')
        return false
      }
      return true
    },

    // 驗證電話格式
    validatePhone() {
      const phoneRegex = /^09\d{8}$/ // 09開頭,8位數字
      if (!this.orderData.ordersTel.trim()) {
        this.phoneError = '電話為必填'
      } else if (!phoneRegex.test(this.orderData.ordersTel)) {
        this.phoneError = '電話格式錯誤，請輸入正確的手機號碼'
      } else {
        this.phoneError = ''
      }
    },

    // 驗證姓名格式
    validateName() {
      const nameRegex = /^[a-zA-Z\u4e00-\u9fa5\s]{2,15}$/ // 允许中文、英文、空格，长度2~15
      if (!this.orderData.ordersName.trim()) {
        this.nameError = '姓名為必填'
      } else if (!nameRegex.test(this.orderData.ordersName)) {
        this.nameError = '姓名格式錯誤，請輸入2-15個字的中文或英文'
      } else {
        this.nameError = ''
      }
    },

    // 處理現金支付
    async handleCashPayment(orderNumber) {
      try {
        await this.clearCart()
        this.$router.push({
          path: '/cartCheckSuc',
          query: { orderNumber },
        })
      } catch (error) {
        console.error('現金支付處理失敗:', error)
        throw error
      }
    },
    //處理Ecpay
    async jumpEcpay() {
      try {
        // 將金額轉為整數（確保沒有小數點）
        const integerAmount = Math.round(this.calculateTotal)
        // 構建查詢字串
        const queryParams = new URLSearchParams({
          amount: integerAmount.toString(), // 轉為字串
          ordersName: this.orderData.ordersName,
          ordersTel: this.orderData.ordersTel,
        }).toString()
        // 使用查詢字串進行轉導
        window.location.href = `${import.meta.env.VITE_API}/ecpayCheckout?${queryParams}`
        await this.clearCart()
      } catch (error) {
        console.error('綠界支付發生錯誤:', error)
        this.$router.push('/cartCheckFail')
      }
    },

    // 處理 PayPal 支付
    async handlePayPalPayment(orderNumber) {
      try {
        const response = await axios.post(`${import.meta.env.VITE_API}/payment/create`, null, {
          params: {
            amount: Math.round(this.calculateTotal),
            orderNumber,
          },
        })

        if (response.data?.redirectUrl) {
          const paypalWindow = window.open(
            response.data.redirectUrl,
            'paypal',
            'width=800,height=600',
          )

          if (!paypalWindow) {
            throw new Error('彈出視窗被阻擋')
          }

          const checkPaypal = setInterval(async () => {
            if (paypalWindow.closed) {
              clearInterval(checkPaypal)
              await this.checkPaymentStatus(orderNumber)
            }
          }, 1000)



        } else {
          throw new Error('PayPal 付款連結獲取失敗')
        }
      } catch (error) {
        console.error('PayPal 支付處理失敗:', error)
        await Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'PayPal 付款失敗，請確認您的帳戶餘額足夠或使用其他支付方式。',
          confirmButtonText: '返回購物車',
        }).then(() => {
          this.$router.push('/cart')
        })
      }
    },

    // 檢查支付狀態
    async checkPaymentStatus(orderNumber) {
      try {
        await new Promise((resolve) => setTimeout(resolve, 1000))
        const response = await axios.get(`${import.meta.env.VITE_API}/payment/status`, {
          params: { orderNumber },
        })

        if (response.data?.paymentStatus === '成功') {
          await this.clearCart()

          this.$router.push({
            path: '/cartCheckSuc',
            query: { orderNumber },
          })
        } else {
          this.$router.push('/cartCheckFail')
        }
      } catch (error) {
        console.error('支付狀態檢查失敗:', error)
        this.$router.push('/cartCheckFail')
      }
    },

    // 主要下單方法
    async placeOrder() {
      // 防止重複提交
      if (this.isProcessing) return
      this.isProcessing = true
      try {
        // 重新執行姓名和電話驗證
        this.validateName()
        this.validatePhone()
        // 如果有驗證錯誤，停止提交並返回
        if (this.nameError || this.phoneError) {
          this.isProcessing = false
          return
        }
        // 確保其他必要的訂單數據都已正確填寫
        if (!this.validateOrderData()) {
          this.isProcessing = false
          return
        }

        Swal.fire({
          icon: 'info',
          title: '訂單處理中...',
          text: '請稍候，我們正在處理您的訂單。',
          showConfirmButton: false,
          allowOutsideClick: false,
        })

        const orderData = {
          ordersName: this.orderData.ordersName,
          ordersTel: this.orderData.ordersTel,
          seatType: this.orderData.seatType,
          ordersRequest: this.orderData.ordersRequest,
          ordersSumPrice: Math.round(this.calculateTotal),
          latestPaymentStatus:
            this.orderData.PaymentWay === 'Cash' || this.orderData.PaymentWay === 'ECPay'
              ? '已付款'
              : '未付款',
          memberId: user.memberId,
          ordersDetails: this.cartItems.map((item) => ({
            odName: item.menu.productName,
            odQuantity: item.cartCount,
            odPrice: item.menu.productPrice,
            odSumPrice: item.cartCount * item.menu.productPrice,
            menuId: item.menu.id,
          })),
          payments: [
            {
              paymentPrice: Math.round(this.calculateTotal),
              paymentWay: this.orderData.PaymentWay,
              paymentStatus:
                this.orderData.PaymentWay === 'Cash' || this.orderData.PaymentWay === 'ECPay'
                  ? '成功'
                  : '失敗',
            },
          ],
        }

        const response = await axios.post(
          `${import.meta.env.VITE_API}/api/orders/create`,
          orderData,
        )

        if (response.status === 200) {
          //測試測試測試測試測試測試
          // 記錄消費者獲得點數
          try {
            const pointRequestData = {
              memberId: user.memberId,
              pointGetted: Math.floor(this.calculateTotal / 100), // 使用實際總金額計算點數
            }

            const pointResponse = await axios.post(
              `${import.meta.env.VITE_API}/api/updateMemberPoint`,
              pointRequestData,
            )

            if (pointResponse.status === 200) {
              console.log('點數更新成功')
            }
          } catch (error) {
            console.error('點數更新失敗:', error)
          }

          try {
            // 先檢查 pointPrizes 的內容
            console.log('pointPrizes:', this.pointPrizes)

            const requestData = {
              memberId: user.memberId,
              promoCodes: this.pointPrizes.map((prize) => prize.promoCode), // 將優惠碼轉為陣列
              pointPrizesName: this.pointPrizes.map((prize) => prize.name), // 將獎品名稱轉為陣列
            }
            console.log(requestData.memberId)
            console.log(requestData.promoCodes)
            console.log(requestData.pointPrizesName)

            const api = `${import.meta.env.VITE_API}/api/createPromoRecord`
            const response = await this.axios.post(api, requestData)
            if (response.status === 200 || response.status === 201) {
              console.log('優惠券記錄新增成功！', response.data)
            } else {
              console.error('優惠券記錄新增失敗，請稍後再試', response.data)
            }
          } catch (error) {
            console.error('發生錯誤', error)
            alert('請求失敗，請檢查您的網路或稍後重試！')
          }
          // 新增：清空點數獎品
          this.clearPointPrizes()
          //測試結束測試結束測試結束
          Swal.close() // 關閉進度提示框
          const orderNumber = response.data.ordersNumber

          switch (this.orderData.PaymentWay) {
            case 'Cash':
              await this.handleCashPayment(orderNumber)
              break

            case 'ECPay':
              await this.jumpEcpay()
              break

            case 'Paypal':
              await this.handlePayPalPayment(orderNumber)
              break
          }
          // 僅支付成功時彈出成功提示
          if (response.data.paymentStatus === '成功') {

            Swal.fire({
              icon: 'success',
              title: '交易成功！',
              text: `您的訂單編號是 ${orderNumber}，感謝您的支持！`,
              confirmButtonText: '查看訂單',
            }).then(() => {
              this.$router.push({
                path: '/membercenter/orders',
                query: { orderNumber },
              })
            })
          }
        } else {
          throw new Error('訂單創建失敗')
        }
      } catch (error) {
        console.error('訂單處理失敗:', error)
        this.$router.push('/cartCheckFail')
      } finally {
        this.isProcessing = false
      }
    },

    // 獲取購物車項目
    async fetchCartItems() {
      try {
        const result = await this.getCart()
        if (result?.data) {
          this.cartItems = result.data
        }
      } catch (error) {
        console.error('Failed to fetch cart items:', error)
      }
    },
  },

  created() {
    this.getCart()
    this.fetchCartItems()
  },
})
</script>

<style scoped>
.is-invalid {
  border-color: #dc3545 !important;
  background-color: #ffffff !important;
}

.text-danger {
  color: #dc3545 !important;
  font-size: 0.875rem;
  margin-top: 0.25rem;
  display: block;
}

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
  box-shadow: 1px 5px 10px 1px rgba(0, 0, 0, 0.2);
  object-fit: contain;
}
</style>
