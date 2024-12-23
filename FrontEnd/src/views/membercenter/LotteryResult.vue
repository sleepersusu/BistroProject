<template>
  <Loading :active="isLoading"></Loading>
  <BannerTop :title="'Lottery Result'"></BannerTop>
  <div
    class="d-flex justify-content-center align-items-center flex-column my-5"
    v-if="lotteryResults?.length === 0"
  >
    <i class="bi bi-emoji-frown display-1 mb-3"></i>
    <h3 class="mb-5">目前還沒有抽獎紀錄!</h3>
    <router-link to="/campaign" class="btn btn-primary btn-lg py-3 px-5">來試試手氣吧!</router-link>
  </div>
  <div class="container-fulid py-4 px-5" v-else>
    <div class="row">
      <div class="col-12">
        <div class="input-group mb-3">
          <span class="input-group-text bg-dark px-4">
            <i class="bi bi-search text-light fs-5"></i>
          </span>
          <input
            type="text"
            class="form-control shadow-none"
            placeholder="搜尋獎品名稱"
            aria-label="搜尋獎品"
            v-model="searchInput"
          />
        </div>
        <div class="card border shadow-xs mb-4">
          <div class="card-body px-0 py-0">
            <div class="table-responsive p-0">
              <table class="table align-items-center mb-0">
                <thead class="bg-gray-100">
                  <tr>
                    <th
                      style="min-width: 100px"
                      class="text-dark text-xs font-weight-semibold opacity-7"
                    >
                      會員名稱
                    </th>
                    <th
                      style="min-width: 150px"
                      class="text-dark text-xs font-weight-semibold opacity-7"
                    >
                      活動名稱
                    </th>
                    <th
                      style="min-width: 150px"
                      class="text-dark text-xs font-weight-semibold opacity-7"
                    >
                      獎品名稱
                    </th>
                    <th
                      style="min-width: 150px"
                      class="text-dark text-xs font-weight-semibold opacity-7"
                    >
                      中獎時間
                    </th>
                    <th style="min-width: 100px" class="text-dark opacity-7">活動狀態</th>
                    <th style="min-width: 150px" class="text-dark opacity-7">兌換獎品</th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-for="result in paginatedResults" :key="result.id">
                    <td class="align-middle">
                      <div class="d-flex px-2 py-1">
                        <div class="d-flex flex-column justify-content-center ms-1">
                          <h6 class="align-middle mb-0 font-weight-semibold text-primary">
                            {{ result.memberName }}
                          </h6>
                        </div>
                      </div>
                    </td>

                    <td class="align-middle">
                      <p class="text-sm text-dark font-weight-semibold mb-0">
                        {{ result.campaignName }}
                      </p>
                    </td>

                    <td class="align-middle">
                      <p class="text-sm text-dark font-weight-semibold mb-0">
                        {{ result.prizeName }}
                      </p>
                    </td>

                    <td class="align-middle">
                      <p class="text-sm text-dark font-weight-semibold mb-0">
                        {{ formatDate(result.createdAt) }}
                      </p>
                    </td>
                    <td
                      class="align-middle"
                      id="countdown"
                      :class="getStatusDisplay(result.campaignStatus).class"
                    >
                      {{ getStatusDisplay(result.campaignStatus).text }}
                    </td>
                    <td class="align-middle" id="countdown">
                      <template v-if="result.prizeName === '銘謝惠顧'">
                        <span class="badge bg-secondary">下次加油!</span>
                      </template>

                      <template v-else>
                        <button
                          v-if="!result.shippingCompleted"
                          class="btn btn-primary"
                          @click.prevent.stop="openModal(result.id)"
                        >
                          填寫資訊
                        </button>
                        <span v-else class="badge bg-success"> 已完成 </span>
                      </template>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="border-top py-3 px-3 d-flex align-items-center">
              <p class="font-weight-semibold mb-0 text-dark text-sm">
                Page {{ currentPage }} of {{ totalPages }}
              </p>
              <div class="ms-auto">
                <button
                  class="btn btn-sm btn-white mb-0 border-0"
                  :disabled="currentPage === 1"
                  @click="handlePageChange(currentPage - 1)"
                >
                  Previous
                </button>
                <button
                  class="btn btn-sm btn-white mb-0 border-0"
                  :disabled="currentPage === totalPages"
                  @click="handlePageChange(currentPage + 1)"
                >
                  Next
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <ShippingDetailsModal
    ref="shippingModal"
    @submit-form="handleSubmitShipping"
  ></ShippingDetailsModal>
</template>

<script setup>
import BannerTop from '@/components/BannerTop.vue'
import ShippingDetailsModal from '@/components/ShippingDetailsModal.vue'
import { statusStore } from '@/stores/statusStore'
import { useUserStore } from '@/stores/userStore'
import { ref, computed, watchEffect } from 'vue'
import axios from 'axios'
import { utils } from '@/mixins/utils'
import { storeToRefs } from 'pinia'

const user = useUserStore()
const { getStatusDisplay, formatDate } = utils()

const status = statusStore()
const { isLoading } = storeToRefs(status)

const shippingModal = ref(null)
const openModal = (id) => {
  shippingModal.value.showModal(id)
}

const handleSubmitShipping = async (shippingDetails) => {
  shippingModal.value.loadingItem = true
  const api = `${import.meta.env.VITE_API}/api/shippingDetails`
  try {
    status.start()
    await axios.post(api, shippingDetails)
    window.Swal.fire({
      toast: true,
      position: 'top-end',
      icon: 'success',
      iconColor: 'black',
      title: `感謝您的填寫，我們已收到資料！`,
      timer: 2500,
      showConfirmButton: false,
      timerProgressBar: true,
    })
    await getResults()
    shippingModal.value.hideModal()
  } catch (e) {
    const errorMessage = e.response?.data.message || '發生錯誤，請稍後再試'
    window.Swal.fire({
      title: '錯誤',
      text: errorMessage,
      icon: 'error',
      confirmButtonColor: 'black',
      confirmButtonText: '確定',
    })
  } finally {
    shippingModal.value.loadingItem = false
    status.finish()
  }
}

const lotteryResults = ref([])
const getResults = async () => {
  const api = `${import.meta.env.VITE_API}/api/winner/member/${user.memberId}`
  try {
    status.start()
    const res = await axios.get(api)
    console.log(res.data)
    lotteryResults.value = res.data.sort((a, b) => b.id - a.id)
    currentPage.value = 1
  } catch (e) {
    console.error(e)
  } finally {
    status.finish()
  }
}
getResults()

const searchInput = ref('')
const filterResults = ref([])
watchEffect(() => {
  filterResults.value = lotteryResults.value.filter((item) => {
    return item.prizeName.toLowerCase().includes(searchInput.value.toLowerCase())
  })
})

const currentPage = ref(1)
const perPage = ref(10)

const totalPages = computed(() => Math.ceil(filterResults.value.length / perPage.value))

const paginatedResults = computed(() => {
  const start = (currentPage.value - 1) * perPage.value
  const end = start + perPage.value
  return filterResults.value.slice(start, end)
})

const handlePageChange = (newPage) => {
  if (newPage >= 1 && newPage <= totalPages.value) {
    currentPage.value = newPage
  }
}
</script>
