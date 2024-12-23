<template>
  <div class="container-fluid py-4 px-5">
    <div class="row">
      <div class="col-12">
        <div class="card border shadow-xs mb-4">
          <div class="card-header border-bottom pb-0">
            <div class="d-sm-flex align-items-center">
              <div>
                <!-- 添加提示文字 -->
                <p class="text-sm mb-0 text-muted">
                  <i class="bi bi-info-circle me-1"></i>
                  點擊訂單列可查看詳細資訊
                </p>
              </div>
              <div class="ms-auto d-flex">
                <router-link
                  to="/menu"
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
                  <span class="btn-inner--text">Add New Food</span>
                </router-link>
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
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">
                      付款狀態
                    </th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">
                      付款方式
                    </th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">
                      日期
                    </th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7"></th>
                  </tr>
                </thead>

                <tbody>
                  <template v-for="order in displayedOrders" :key="order.id">
                    <tr
                      :class="{ 'table-active': expandedOrders.includes(order.ordersNumber) }"
                      @click="toggleOrderDetail(order.ordersNumber)"
                      class="cursor-pointer"
                    >
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
                          已付款
                        </span>
                        <span
                          v-else-if="order.latestPaymentStatus === '未付款'"
                          class="badge badge-sm border border-danger text-danger"
                          style="background-color: white"
                        >
                          已取消
                        </span>
                      </td>
                      <td class="align-middle text-center">
                        <span class="text-primary text-sm font-weight-normal">
                          {{ order.payment[0]?.paymentWay || '未付款' }}
                        </span>
                      </td>
                      <td class="align-middle text-center">
                        <span class="text-primary text-sm font-weight-normal">
                          {{ order.payment[0]?.createdAt }}
                        </span>
                      </td>
                      <td class="align-middle text-center">
                        <!-- 添加展開/收起指示器 -->
                        <i
                          :class="[
                            'bi',
                            expandedOrders.includes(order.ordersNumber)
                              ? 'bi-chevron-up'
                              : 'bi-chevron-down',
                          ]"
                          class="ms-1"
                        ></i>
                      </td>
                    </tr>
                    <!-- 訂單詳情行 -->
                    <tr v-if="expandedOrders.includes(order.ordersNumber)">
                      <td colspan="6">
                        <div class="px-4 py-3 bg-light">
                          <div v-if="orderDetails[order.ordersNumber]" class="row">
                            <div class="col-md-6">
                              <h6 class="text-dark mb-3">訂單詳情</h6>
                              <div class="mb-2">
                                <strong>訂購人：</strong>
                                {{ orderDetails[order.ordersNumber]?.ordersName || '未提供' }}
                              </div>
                              <div class="mb-2">
                                <strong>電話：</strong>
                                {{ orderDetails[order.ordersNumber].ordersTel }}
                              </div>
                              <div class="mb-2">
                                <strong>用餐方式：</strong>
                                {{ orderDetails[order.ordersNumber].seatType }}
                              </div>
                              <div class="mb-2">
                                <strong>獲得點數：</strong>
                                {{ orderDetails[order.ordersNumber].pointGetted }}
                              </div>
                              <div
                                class="mb-2"
                                v-if="orderDetails[order.ordersNumber].ordersRequest"
                              >
                                <strong>備註：</strong>
                                {{ orderDetails[order.ordersNumber].ordersRequest }}
                              </div>
                            </div>
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
                          已付款
                        </span>
                        <span
                          v-else-if="order.latestPaymentStatus === '已取消'"
                          class="badge badge-sm border border-danger text-danger"
                          style="background-color: white"
                        >
                          已取消
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
                      <td class="align-middle text-center">
                        <!-- 添加展開/收起指示器 -->
                        <i
                          :class="[
                            'bi',
                            expandedOrders.includes(order.ordersNumber)
                              ? 'bi-chevron-up'
                              : 'bi-chevron-down',
                          ]"
                          class="ms-1"
                        ></i>
                      </td>
                    </tr>
                    <!-- 訂單詳情行 -->
                    <tr v-if="expandedOrders.includes(order.ordersNumber)">
                      <td colspan="6">
                        <div class="px-4 py-3 bg-light">
                          <div v-if="orderDetails[order.ordersNumber]" class="row">
                            <div class="col-md-6">
                              <h6 class="text-dark mb-3">訂單詳情</h6>
                              <div class="mb-2">
                                <strong>訂購人：</strong>
                                {{ orderDetails[order.ordersNumber]?.ordersName || '未提供' }}
                              </div>
                              <div class="mb-2">
                                <strong>電話：</strong>
                                {{ orderDetails[order.ordersNumber].ordersTel }}
                              </div>
                              <div class="mb-2">
                                <strong>用餐方式：</strong>
                                {{ orderDetails[order.ordersNumber].seatType }}
                              </div>
                              <div class="mb-2">
                                <strong>獲得點數：</strong>
                                {{ orderDetails[order.ordersNumber].pointGetted }}
                              </div>
                              <div
                                class="mb-2"
                                v-if="orderDetails[order.ordersNumber].ordersRequest"
                              >
                                <strong>備註：</strong>
                                {{ orderDetails[order.ordersNumber].ordersRequest }}
                              </div>
                            </div>
                            <div class="col-md-6">
                              <h6 class="text-dark mb-3">商品明細</h6>
                              <div
                                v-for="item in orderDetails[order.ordersNumber].ordersDetails"
                                :key="item.id"
                                class="d-flex justify-content-between align-items-center mb-2"
                              >
                                <div class="d-flex align-items-center">
                                  <span>{{ item.odName }} x {{ item.odQuantity }}</span>
                                  <!-- 添加評論按鈕 -->
                                  <CommentPostButton
                                    :item="item"
                                    :order="order"
                                    @open-add-commentmodal="handleOpenAddCommentModal"
                                    :hidebutton="commentStatus[item.id]"
                                  >
                                  </CommentPostButton>
                                </div>
                                <span>NT$ {{ item.odSumPrice }}</span>
                              </div>

                              <div class="d-flex justify-content-between mb-2">
                                <strong>服務費 (10%)</strong>
                                <strong>NT$ {{ calculateServiceFee(order.ordersNumber) }}</strong>
                              </div>

                              <div class="border-top pt-2 mt-2">
                                <strong>總計：</strong> NT$
                                {{ orderDetails[order.ordersNumber].ordersSumPrice }}
                              </div>
                            </div>
                          </div>
                          <div v-else class="text-center py-3">
                            <div class="spinner-border text-primary" role="status">
                              <span class="visually-hidden">Loading...</span>
                            </div>
                          </div>
                        </div>
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>

            <!-- 分頁功能 -->
            <div class="border-top py-3 px-3 d-flex align-items-center">
              <p class="font-weight-semibold mb-0 text-dark text-sm">
                Page {{ currentPage }} of {{ totalPages }}
              </p>
              <div class="ms-auto">
                <button
                  class="btn btn-sm btn-white mb-0 no-border"
                  :disabled="currentPage === 1"
                  @click="previousPage"
                >
                  <font-awesome-icon v-bind:icon="['fas', 'fa-angle-double-left']" />
                </button>
                <button
                  class="btn btn-sm btn-white mb-0 no-border"
                  :disabled="currentPage === totalPages"
                  @click="nextPage"
                >
                  <font-awesome-icon v-bind:icon="['fas', 'fa-angle-double-right']" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <CommentPostForm
    ref="commentPostModal"
    :item="currentItem"
    @comment-submitted="handleCommentSubmitted"
  ></CommentPostForm>
</template>

<script>
import { mapState, mapActions } from 'pinia'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { cartStore } from '@/stores/cartStore.js'
import { ref } from 'vue'
import { useUserStore } from '@/stores/userStore.js'
import { orderStore } from '@/stores/orderStore.js'
import CommentPostForm from './CommentPostForm.vue'
import CommentPostButton from './CommentPostButton.vue'
const user = useUserStore()
export default {
  name: 'Orders',
  components: {
    FontAwesomeIcon,
    CommentPostForm,
    CommentPostButton,
  },
  props: {
    orderItems: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      currentPage: 1,
      pageSize: 10, // 每頁顯示數量
      expandedOrders: [], // 存儲已展開的訂單編號
      orderDetails: {}, // 存儲訂單詳情
      currentItem: {},
      commentStatus: {},
    }
  },
  methods: {
    ...mapActions(orderStore, ['getOrderDetail']),

    calculateServiceFee(ordersNumber) {
      const total = this.orderDetails[ordersNumber]?.ordersSumPrice || 0
      const serviceFee = (total / 1.1) * 0.1 // 假設總計金額已包含 10% 服務費
      return Math.round(serviceFee) // 四捨五入為整數
    },

    handleOpenAddCommentModal(event, item) {
      // 阻止事件冒泡，避免觸發展開/收起
      event.stopPropagation()
      // 這裡實現打開評論modal的邏輯
      this.currentItem = {
        id: item.id,
        odName: item.odName,
      }

      this.$refs.commentPostModal.showModal()
    },

    //用來隱藏以評論按鈕
    handleCommentSubmitted({ detailId, isCommented }) {
      this.commentStatus[detailId] = isCommented
      this.saveCommentStatus()
      this.$refs.commentPostModal.hideModal()
    },
    // 添加保存方法
    saveCommentStatus() {
      localStorage.setItem('commentStatus', JSON.stringify(this.commentStatus))
    },

    // 添加讀取方法
    loadCommentStatus() {
      const savedStatus = localStorage.getItem('commentStatus')
      if (savedStatus) {
        this.commentStatus = JSON.parse(savedStatus)
      }
    },

    async toggleOrderDetail(ordersNumber) {
      const index = this.expandedOrders.indexOf(ordersNumber)

      if (index === -1) {
        // 展開訂單
        this.expandedOrders.push(ordersNumber)
        // 檢查是否還沒有詳情數據
        if (!this.orderDetails[ordersNumber]) {
          try {
            const details = await this.getOrderDetail(ordersNumber)
            if (details) {
              // 直接更新對象
              this.orderDetails[ordersNumber] = details
            }
          } catch (error) {
            console.error('獲取訂單詳情失敗:', error)
            // 如果獲取詳情失敗，從展開列表中移除
            const failedIndex = this.expandedOrders.indexOf(ordersNumber)
            if (failedIndex !== -1) {
              this.expandedOrders.splice(failedIndex, 1)
            }
          }
        }
      } else {
        // 收起訂單
        this.expandedOrders.splice(index, 1)
      }
    },
    // 切換頁面時清空展開的訂單
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
        this.expandedOrders = []
      }
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--
        this.expandedOrders = []
      }
    },
  },
  computed: {
    // 計算總頁數
    totalPages() {
      return Math.ceil(this.orderItems.length / this.pageSize)
    },
    // 獲取當前頁的訂單
    displayedOrders() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.orderItems.slice(start, end)
    },
  },
  created() {
    // 載入保存的評論狀態
    this.loadCommentStatus()
  },
}
</script>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}

.cursor-pointer:hover {
  background-color: rgba(0, 0, 0, 0.02);
}

.no-border {
  border: none; /* 移除邊框 */
  box-shadow: none; /* 移除陰影 */
  outline: none; /* 移除焦點時的外框 */
}
</style>
