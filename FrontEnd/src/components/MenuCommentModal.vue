<template>
  <div
    class="modal fade modal-dialog-scrollable"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    ref="modal"
  >
    <div class="modal-dialog modal-xl">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-2" id="exampleModalLabel">{{ productName }}</h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>

        <div v-for="comment in paginatedComment" :key="comment.id">
          <div class="modal-body">
            <CommentModalBody :comment="comment"></CommentModalBody>
          </div>
        </div>
        <!-- 分頁按鈕 -->
        <div class="pagination-container">
          <ul class="pagination">
            <li class="page-item">
              <a
                class="page-link"
                :disabled="currentPage === 1"
                @click="changePage(currentPage - 1)"
                >Previous</a
              >
            </li>
            <!-- Page Numbers -->
            <li
              v-for="page in totalPages"
              :key="page"
              class="page-item"
              :class="{ active: page === currentPage }"
            >
              <a class="page-link" href="#" @click.prevent="changePage(page)">
                {{ page }}
              </a>
            </li>

            <li class="page-item">
              <a
                class="page-link"
                :disabled="currentPage === totalPages"
                @click="changePage(currentPage + 1)"
                >Next</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModalMixin from '@/mixins/modalMixin-option'
import StarRating from 'vue-star-rating'
import LoadingVue from 'vue3-loading-overlay'
import CommentModalBody from './CommentModalBody.vue'
export default {
  components: {
    StarRating,
    CommentModalBody,
    LoadingVue,
  },
  mixins: [ModalMixin],

  props: {
    productName: {
      type: String,
      required: true,
    },
    comments: {
      type: Array,
      required: true,
    },
  },
  data() {
    return {
      //分頁
      currentPage: 1, // 當前頁面
      commentPerPage: 5, // 每頁顯示的數量
    }
  },
  methods: {
    async viewComment(menu) {
      this.isLoading = true

      let api = `${import.meta.env.VITE_API}/api/${menu.productName}/comment`
      this.axios
        .get(api)
        .then((response) => {
          this.comments = response.data
          this.totalPages = Math.ceil(this.comments.length / this.commentPerPage)
        })
        .catch((error) => {
          console.error('Error loading comments:', error)
        })
        .finally(() => {
          this.isLoading = false
        })
    },
    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page
      }
    },


  },
  computed: {
    paginatedComment() {
      const startIndex = (this.currentPage - 1) * this.commentPerPage
      const endIndex = startIndex + this.commentPerPage
      return this.comments.slice(startIndex, endIndex)
    },
    // 計算總頁數
    totalPages() {
      return Math.ceil(this.comments.length / this.commentPerPage)
    },
  },
}
</script>
