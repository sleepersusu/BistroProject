<template>
  <div class="container-fluid py-4">
    <div class="row justify-content-center">
      <div class="col-12">
        <div class="records-card">
          <div class="card-header">
            <h2 class="title">已使用的優惠券紀錄</h2>
            <div class="divider"></div>
          </div>

          <div class="table-container">
            <table class="custom-table">
              <thead>
                <tr>
                  <th>優惠券名稱</th>
                  <th>優惠券代碼</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="record in paginatedRecords" :key="record.id">
                  <td>{{ record.pointPrizesName }}</td>
                  <td>
                    <span class="coupon-code">{{ record.promoCode }}</span>
                  </td>
                </tr>
                <tr v-if="filteredPromoRecords.length === 0">
                  <td colspan="2" class="empty-message">目前尚未使用任何優惠券</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 分頁控制 -->
          <div class="pagination-container">
            <p class="page-info">Page {{ currentPage }} of {{ totalPages }}</p>
            <div class="pagination-buttons">
              <button class="page-btn" @click="previousPage" :disabled="currentPage === 1">
                Previous
              </button>
              <button class="page-btn" @click="nextPage" :disabled="currentPage === totalPages">
                Next
              </button>
            </div>
          </div>
        </div>
      </div>
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
.records-card {
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
  padding: 2rem;
  border: 1px solid #f5f5f5;
}

.card-header {
  text-align: center;
  margin-bottom: 2rem;
}

.title {
  color: #2c3e50;
  font-size: 1.8rem;
  font-weight: 600;
  margin-bottom: 1rem;
}

.divider {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 1.5rem 0;
}

.table-container {
  overflow-x: auto;
  margin: 1rem 0;
}

.custom-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
}

.custom-table th {
  background: #faf9f7;
  color: #2c3e50;
  font-weight: 600;
  padding: 1rem;
  text-align: left;
  border-bottom: 2px solid #f0ebe5;
}

.custom-table td {
  padding: 1rem;
  border-bottom: 1px solid #f0ebe5;
  color: #2c3e50;
}

.custom-table tr:hover {
  background: #faf9f7;
}

.coupon-code {
  background: rgba(155, 133, 121, 0.1);
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  font-family: 'Roboto Mono', monospace;
  color: #9b8579;
  letter-spacing: 1px;
}

.empty-message {
  text-align: center;
  color: #666;
  padding: 2rem;
}

.pagination-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 0;
  border-top: 1px solid #f0ebe5;
  margin-top: 1rem;
}

.page-info {
  color: #666;
  margin: 0;
  font-size: 0.9rem;
}

.pagination-buttons {
  display: flex;
  gap: 0.5rem;
}

.page-btn {
  background: #fff;
  border: 1px solid #e0e0e0;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  color: #2c3e50;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.page-btn:hover:not(:disabled) {
  background: #faf9f7;
  border-color: #9b8579;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .records-card {
    padding: 1rem;
  }

  .title {
    font-size: 1.5rem;
  }

  .pagination-container {
    flex-direction: column;
    gap: 1rem;
    align-items: center;
  }
}
</style>
