<template>
  <div class="container h-100" >

    <div class="col-12">
      <BannerTop :title="'My Comment'"></BannerTop>
      <div class="container-fluid comment-container">
        <div class="col-12">
          <div class="card border shadow-xs mb-4">
            <!-- 卡片標題 -->


            <!-- 卡片內容 -->
            <div class="card-body px-0 py-0">
              <div class="table-responsive p-0">
                <table class="table align-items-center mb-0">
                  <thead class="bg-gray-100">
                    <tr>
                      <th class="text-dark text-xs font-weight-semibold opacity-7" style="min-width: 100px;">餐點名稱</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7" style="min-width: 150px;">評價分數</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7" style="min-width: 300px;">評論內容</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7" style="min-width: 200px;">評論時間</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7"></th>
                    </tr>
                  </thead>

                  <tbody v-if="!NoComment" class="comment-list" style="overflow: none;" >
                    <CommentTable
                      v-for="comment in displayedComment"
                      :key="comment.id"
                      :comment="comment"
                      @update-comment-modal="openUpdateCommentModal"
                      @delete-comment-modal="deleteReload"
                    />
                  </tbody>

                  <tbody v-if="NoComment">
                    <tr>
                      <td colspan="5" class="text-center py-3 no-comment">目前尚未有評論</td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <!-- 分頁控制 -->
              <div class="border-top pagination-container">
                <p class="font-weight-semibold mb-0 text-dark text-sm">
                  Page {{ currentPage }} of {{ totalPages }}
                </p>
                <div class="pagination-controls">
                  <button
                    class="btn btn-sm btn-white pagination-btn"
                    :disabled="currentPage === 1"
                    @click="previousPage"
                  >
                    <font-awesome-icon :icon="['fas', 'angle-double-left']" />
                  </button>
                  <button
                    class="btn btn-sm btn-white pagination-btn"
                    :disabled="currentPage === totalPages || totalPages === 0"
                    @click="nextPage"
                  >
                    <font-awesome-icon :icon="['fas', 'angle-double-right']" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <CommentUpdateModal
    ref="commentUpdateModal"
    :comment="currentComment"
    @update-table="loadAllCommentByMember"
  />
</template>

<script>
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import CommentTable from '@/components/CommentTable.vue'
import CommentUpdateModal from '@/components/CommentUpdateModal.vue'
import CommentDeleteModal from '@/components/CommentDeleteModal.vue'
import BannerTop from '@/components/BannerTop.vue'

export default {
  name: 'CommentList',

  components: {
    CommentTable,
    CommentUpdateModal,
    CommentDeleteModal,
    BannerTop,
    FontAwesomeIcon
  },

  data() {
    return {
      comments: [],
      NoComment: true,
      currentComment: {},
      deleteComment: {},
      currentPage: 1,
      pageSize: 5
    }
  },

  computed: {
    totalPages() {
      return Math.ceil(this.comments.length / this.pageSize) || 1
    },
    displayedComment() {
      const start = (this.currentPage - 1) * this.pageSize
      const end = start + this.pageSize
      return this.comments.slice(start, end)
    }
  },

  methods: {
    async loadAllCommentByMember() {
      try {
        const response = await this.axios.get(
          `${import.meta.env.VITE_API}/api/frontend/member/comment`
        )
        this.comments = response.data
        this.NoComment = this.comments.length === 0
      } catch (error) {
        console.error('Error fetching comments:', error)
        this.NoComment = true
        this.comments = []
      }
    },

    async openUpdateCommentModal(comment) {
      this.currentComment = comment
      this.$refs.commentUpdateModal.showModal()
    },

    async deleteReload() {
      await this.loadAllCommentByMember()
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
      }
    },

    previousPage() {
      if (this.currentPage > 1) {
        this.currentPage--
      }
    }
  },

  watch: {
    comments: {
      handler(newVal) {
        this.NoComment = newVal.length === 0
        if (this.currentPage > this.totalPages) {
          this.currentPage = Math.max(1, this.totalPages)
        }
      },
      deep: true
    }
  },

  created() {
    this.loadAllCommentByMember()
  }
}
</script>

<style scoped>
/* 基本樣式 (桌面版) */
.comment-container {
  padding: 1.5rem;
}



.header-title {
  margin: 0;
  font-size: 1.25rem;
}

.pagination-container {
  padding: 1rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.pagination-info {
  margin: 0;
  font-weight: 600;
  font-size: 0.875rem;
}

.pagination-controls {
  display: flex;
  gap: 0.5rem;
}

.pagination-btn {
  padding: 0.375rem 0.75rem;
  border: none;
  background: transparent;
}

.no-comment {
  padding: 2rem;
  color: #666;
  font-size: 1rem;
}



</style>
