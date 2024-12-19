<template>
  <div class="container-fluid py-4 px-5">
    <div class="row">
      <div class="col-12">
        <div class="card border shadow-xs mb-4">
          <div class="card-header border-bottom pb-0">
            <div class="d-sm-flex align-items-center">
              <div class="ms-auto d-flex">
                <button
                  type="button"
                  class="btn btn-sm btn-dark btn-icon d-flex align-items-center me-2 mb-2"
                >
                  <span class="btn-inner--icon">
                    <svg
                      width="20"
                      height="20"
                      xmlns="http://www.w3.org/2000/svg"
                      viewBox="0 0 15 18"
                      fill="currentColor"
                      class="d-block me-2"
                    >
                      <path
                        d="M3 6V2H1V7C1 8.65685 2.34315 10 4 10V16H6V10C7.65685 10 9 8.65685 9 7V2H7V6H6V2H4V6H3Z"
                      />
                      <path
                        d="M14 16V2H12L10.1012 8.64563C10.0341 8.88073 10 9.12404 10 9.36855C10 10.6042 10.8517 11.6409 12 11.9237V16H14Z"
                      />
                    </svg>
                  </span>
                  <span class="btn-inner--text" id="addDataButton"> Add New Food</span>
                </button>
              </div>
            </div>
          </div>

          <div class="card-body px-0 py-0">
            <div class="table-responsive p-0">
              <table class="table align-items-center mb-0">
                <thead class="bg-gray-100">
                  <tr>
                    <th class="text-dark text-xs font-weight-semibold opacity-7">訂單編號</th>
                    <th class="text-dark text-xs font-weight-semibold opacity-7 ps-2">金額</th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">付款狀態</th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">付款方式</th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">日期</th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-for="order in orderItems" :key="order.id">
                    <td>
                      <div class="d-flex px-2 py-1">
                        <div class="d-flex flex-column justify-content-center ms-1">
                          <h6 class="mb-0 text-sm font-weight-semibold text-primary">
                            {{ order.ordersNumber }}
                          </h6>
                        </div>
                      </div>
                    </td>

                    <td>
                      <p class="text-sm text-dark font-weight-semibold mb-0">
                        {{ order.ordersSumPrice }}
                      </p>
                    </td>




                    <td class="align-middle text-center text-sm">
                      <span
                        v-if="order.latestPaymentStatus === '已付款'"
                        class="badge badge-sm border border-success text-success"
                        style="background-color: white"
                      >
                        <svg
                          width="9"
                          height="9"
                          viewBox="0 0 10 9"
                          fill="none"
                          xmlns="http://www.w3.org/2000/svg"
                          stroke="currentColor"
                          class="me-1"
                        >
                          <path
                            d="M1 4.42857L3.28571 6.71429L9 1"
                            stroke-width="2"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                        </svg>
                        {{ order.latestPaymentStatus }}
                      </span>
                      <span
                        v-else-if="order.latestPaymentStatus === '已取消'"
                        class="badge badge-sm border border-danger text-danger"
                        style="background-color: white"
                      >
                        <svg
                          width="12"
                          height="12"
                          xmlns="http://www.w3.org/2000/svg"
                          fill="none"
                          viewBox="0 0 24 24"
                          stroke-width="1.5"
                          stroke="currentColor"
                          class="me-1"
                        >
                          <path
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            d="M6 18L18 6M6 6l12 12"
                          />
                        </svg>
                        {{ order.latestPaymentStatus }}
                      </span>
                    </td>

                    <td class="align-middle text-center">
                      <span class="text-primary text-sm font-weight-normal">
                        {{ order.payment[0]?.paymentWay || '未支付' }}
                      </span>
                    </td>

                    <td class="align-middle text-center">
                      <span class="text-primary text-sm font-weight-normal">
                        {{ order.payment[0]?.createdAt }}
                      </span>
                    </td>
                  </tr>

                </tbody>
              </table>
            </div>

            <div class="border-top py-3 px-3 d-flex align-items-center">
              <p class="font-weight-semibold mb-0 text-dark text-sm">Page 1 of 10</p>
              <div class="ms-auto">
                <button class="btn btn-sm btn-white mb-0">Previous</button>
                <button class="btn btn-sm btn-white mb-0">Next</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import PageTop from '@/components/PageTop.vue'
import { mapState, mapActions } from 'pinia'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { cartStore } from '@/stores/cartStore.js'
import { useUserStore } from '@/stores/userStore.js'
import { ref } from 'vue'
import { orderStore } from '@/stores/orderStore.js'


const user = useUserStore()
export default {
  name: 'Orders',
  components: {

  },

  data(){

  },
  methods: {
    ...mapActions(orderStore, ["getOrder"]),
  },
  computed:{
    ...mapState(orderStore, ['orderItems']),
  },
  watch:{},
  created() {
    this.getOrder();
  },
}
</script>
<style></style>
