<template>
  <div class="container">
    <div class="col-12">
      <BannerTop :title="'My Comment'"></BannerTop>
      <div class="container-fluid py-4 px-5">
        <div class="col-12">
          <div class="card border shadow-xs mb-4">
            <div class="card-header border-bottom pb-0">
              <div class="d-sm-flex align-items-center">
                <span class="h4">我的評論</span>
                <div class="ms-auto d-flex"></div>
              </div>
            </div>
            <div class="card-body px-0 py-0">
              <div class="table-responsive p-0">
                <table class="table align-items-center mb-0">
                  <thead class="bg-gray-100">
                    <tr>
                      <th class="text-dark text-xs font-weight-semibold opacity-7">餐點名稱</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7">評價分數</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7">評論內容</th>
                      <th class="text-dark text-xs font-weight-semibold opacity-7">評論時間</th>
                    </tr>
                  </thead>

                  <tbody v-if="!NoComment" v-for="comment in displayedComment" :key="comment.id">
                    <CommentTable
                      :comment="comment"
                      @update-comment-modal="openUpdateCommentModal"
                      @delete-comment-modal="deleteReload"
                    >
                    </CommentTable>
                  </tbody>

                  <tbody v-if="NoComment">
                    <tr>
                      <td colspan="4" class="text-center py-3">目前尚未有評論</td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <div class="border-top py-3 px-3 d-flex align-items-center">
                <p class="font-weight-semibold mb-0 text-dark text-sm">
                  第 {{ currentPage }} 頁，共 {{ totalPages }} 頁
                </p>
                <div class="ms-auto">
                  <button
                    class="btn btn-sm btn-white mb-0 me-2"
                    :disabled="currentPage === 1"
                    @click="previousPage"
                  >
                    <font-awesome-icon :icon="['fas', 'angle-double-left']" />
                  </button>
                  <button
                    class="btn btn-sm btn-white mb-0"
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
  ></CommentUpdateModal>
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
          `${import.meta.env.VITE_API}/api/member/comment`
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
.text-center {
  text-align: center;
}

.me-2 {
  margin-right: 0.5rem;
}

.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
