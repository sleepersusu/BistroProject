<template>
  <div class="container-fluid">
    <div class="row">
      <main class="col-md-9 col-lg-10 p-4">
        <div class="container-fluid py-4 px-5">
          <div class="row">
            <div class="col-12">
              <div class="card border shadow-xs mb-4">
                <div class="card-header border-bottom pb-0">
                  <div class="d-sm-flex align-items-center">
                    <span class="h4">已使用過的兌換卷</span>
                    <div class="ms-auto d-flex"></div>
                  </div>
                </div>
                <div class="card-body px-0 py-0">
                  <div class="table-responsive p-0">
                    <table class="table align-items-center mb-0">
                      <thead class="bg-gray-100">
                        <tr>
                          <th class="text-dark text-xs font-weight-semibold opacity-7">
                            兌換卷名稱
                          </th>
                          <th class="text-dark text-xs font-weight-semibold opacity-7">
                            兌換卷代碼
                          </th>
                        </tr>
                      </thead>

                      <tbody>
                        <tr v-for="record in paginatedRecords" :key="record.id">
                          <td class="ps-4">
                            <p class="text-sm font-weight-bold mb-0">
                              {{ record.pointPrizesName }}
                            </p>
                          </td>
                          <td class="ps-4">
                            <p class="text-sm font-weight-bold mb-0">{{ record.promoCode }}</p>
                          </td>
                        </tr>
                      </tbody>

                      <tbody v-if="filteredPromoRecords.length === 0">
                        <tr>
                          <td colspan="2" class="text-center">目前尚未使用任何兌換券</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <!-- 分頁控制 -->
                  <div class="border-top py-3 px-3 d-flex align-items-center">
                    <p class="font-weight-semibold mb-0 text-dark text-sm">
                      Page {{ currentPage }} of {{ totalPages }}
                    </p>
                    <div class="ms-auto">
                      <button
                        class="btn btn-sm btn-white mb-0"
                        @click="previousPage"
                        :disabled="currentPage === 1"
                      >
                        Previous
                      </button>
                      <button
                        class="btn btn-sm btn-white mb-0"
                        @click="nextPage"
                        :disabled="currentPage === totalPages"
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
      </main>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'

export default {
  data() {
    return {
      promoRecord: [],
      currentPage: 1,
      itemsPerPage: 10, // 每頁顯示的記錄數
    }
  },
  methods: {
    async showPointRecord() {
      const api = `${import.meta.env.VITE_API}/api/showPromoRecord`
      const response = await this.axios.get(api)
      this.promoRecord = response.data
    },
    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
      }
    },
  },
  computed: {
    filteredPromoRecords() {
      const userStore = useUserStore()
      return this.promoRecord.filter((record) => record.members.id === userStore.memberId)
    },
    // 計算總頁數
    totalPages() {
      return Math.ceil(this.filteredPromoRecords.length / this.itemsPerPage)
    },
    // 取得當前頁面應該顯示的記錄
    paginatedRecords() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage
      const endIndex = startIndex + this.itemsPerPage
      return this.filteredPromoRecords.slice(startIndex, endIndex)
    },
  },
  created() {
    this.showPointRecord()
  },
}
</script>

<style scoped>
.btn-white {
  margin-left: 0.5rem;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
