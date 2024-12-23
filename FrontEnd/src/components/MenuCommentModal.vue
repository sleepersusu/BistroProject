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

<style scoped>
/* 分頁 */
.pagination .page-link {
  background: linear-gradient(45deg, #333, #000); /* 黑色漸變背景 */
  color: white; /* 白字 */
  border: 1px solid #555; /* 灰色邊框 */
  border-radius: 5px; /* 圓角按鈕 */
  padding: 8px 12px; /* 內邊距 */
  margin: 0 5px; /* 間距 */
  font-size: 16px; /* 字體大小 */
  font-weight: bold; /* 粗體字 */
  transition: all 0.3s ease; /* 動畫過渡 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2); /* 按鈕陰影 */
}

.pagination .page-link:hover {
  background: linear-gradient(45deg, #444, #111); /* 懸停時的漸變 */
  color: #ffd700; /* 懸停時的金色字體 */
  transform: scale(1.1); /* 放大效果 */
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.3); /* 加強陰影 */
}

.pagination .page-item.active .page-link {
  background: linear-gradient(45deg, #ffd700, #ffa500); /* 活動按鈕金色漸變 */
  color: black; /* 黑字 */
  border: 1px solid #ff8c00; /* 橙色邊框 */
  box-shadow: 0 6px 12px rgba(255, 140, 0, 0.4); /* 活動按鈕高光 */
  transform: scale(1.15); /* 稍大 */
}

.pagination .page-item.disabled .page-link {
  background: #555; /* 禁用按鈕深灰背景 */
  color: #999; /* 灰字 */
  pointer-events: none; /* 禁止點擊 */
  opacity: 0.5; /* 降低透明度 */
}

.pagination-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;
  margin: 20px 0; /* 外邊距 */
}

.pagination .page-item {
  display: inline-block;
}

</style>
