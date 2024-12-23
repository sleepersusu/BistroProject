<template>

  <tr>
    <!-- 商品名稱 -->
    <td data-label="餐點名稱">
      <div class="d-flex align-items-center">
        <h6 class="mb-0 text-sm font-weight-semibold text-primary">
          {{ comment.commentProduct }}
        </h6>
      </div>
    </td>

    <!-- 評分星星 -->
    <td data-label="評價分數">
      <div class="rating-wrapper">
        <star-rating
          :rating="comment.commentRating"
          :read-only="true"
          :increment="1"
          :rounded-corners="true"
          :star-size="25"
          :show-rating="false"
          :star-points="[23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17]"
        >
        </star-rating>
      </div>
    </td>

    <!-- 評論內容 -->
    <td data-label="評論內容" class="comment-content" style="max-width: 300px;">
      {{ comment.commentMessage }}
    </td>

    <!-- 時間和操作 -->
    <td data-label="評論時間" class="action-cell">
      <span class="time-text">{{ comment.commentTime }}</span>
    </td>


    <div class="action-wrapper">
        <div class="btn-group">
          <button
            class="btn btn-sm action-btn"
            type="button"
            data-bs-toggle="dropdown"
            aria-expanded="false"
          >
            <i class="bi bi-three-dots"></i>
          </button>
          <ul class="dropdown-menu dropdown-menu-end">
            <li>
              <button class="dropdown-item" @click.prevent="updateComment(comment)">
                編輯
              </button>
            </li>
            <li>
              <button class="dropdown-item" @click.prevent="deleteComment(comment)">
                刪除
              </button>
            </li>
          </ul>
        </div>
      </div>
  </tr>
</template>

<script>
import StarRating from 'vue-star-rating'

export default {
  components: {
    StarRating,
  },

  props: {
    comment: {
      type: Object,
      required: true,
    },
  },
  emits: ['update-comment-modal', 'delete-comment-modal'],

  data() {
    return {}
  },
  methods: {
    async updateComment(comment) {
      this.$emit('update-comment-modal', comment)
    },

    async deleteComment(comment) {
      const result = await Swal.fire({
        icon: 'question',
        title: `確定要刪除嗎?`,
        html: `<p>刪除後無法還原</p>`,
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        reverseButtons: true,
      })

      if (result.isConfirmed) {
        comment = this.comment
        let API_URL = `${import.meta.env.VITE_API}/api/Bistro/deleteComment/${this.comment.id}`

        this.axios
          .delete(API_URL)
          .then(async () => {
            this.$emit('delete-comment-modal')
            await Swal.fire({
              icon: 'success',
              title: `刪除成功`,
              showCancelButton: false,
            })
          })
          .catch(async (error) => {
            console.error('Error deletei comments:', error)
            await Swal.fire({ icon: 'error', title: `刪除失敗`, showCancelButton: false })
          })
      }
    },
  },
  watch() {},
}
</script>

<style scoped>

.myComment {
  word-wrap: break-word;
}
/* 基本樣式 */
.rating-text {
  font-weight: bold;
}

.text-danger {
  color: #dc3545;
}

.invalid-feedback {
  display: block;
}

/* 響應式樣式 */
@media (max-width: 768px) {

  thead {
    display: none;
  }
  /* 模態框調整 */
  .modal-dialog {
    margin: 0.5rem;
    max-width: calc(100% - 1rem);
  }

  .modal-header {
    padding: 0.75rem;
  }

  .modal-body {
    padding: 0.75rem;
  }

  .modal-footer {
    padding: 0.75rem;
    flex-wrap: nowrap;
  }

  /* 表單元素調整 */
  .form-label {
    font-size: 0.875rem;
    margin-bottom: 0.25rem;
  }

  .form-control {
    font-size: 0.875rem;
    padding: 0.375rem 0.5rem;
  }

  textarea.form-control {
    min-height: 80px;
  }

  /* 按鈕調整 */
  .btn {
    padding: 0.375rem 0.75rem;
    font-size: 0.875rem;
  }

  /* 評論列表響應式 */
  .table-responsive {
    border: 0;
  }

  .table-responsive thead {
    display: none;
  }

  .table-responsive tbody,
  .table-responsive tr,
  .table-responsive td {
    display: block;
    width: 100%;
  }

  .table-responsive tr {
    margin-bottom: 1rem;
    border: 1px solid #dee2e6;
    border-radius: 0.375rem;
  }

  .table-responsive td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0.75rem;
    text-align: right;
    position: relative;
    border: none;
  }

  .table-responsive td::before {
    content: attr(data-label);
    float: left;
    font-weight: bold;
    text-transform: uppercase;
    font-size: 0.75rem;
  }

  /* 評論內容特殊處理 */
  .table-responsive td.myComment {
    display: block;
    padding-left: 0.75rem;
  }

  .table-responsive td.myComment::before {
    width: 100%;
    margin-bottom: 0.5rem;
  }

  /* 評分星星調整 */
  .star-rating {
    margin: 0.5rem 0;
  }

  /* 下拉選單調整 */
  .dropdown-menu {
    min-width: 120px;
    font-size: 0.875rem;
  }

  .dropdown-item {
    padding: 0.5rem 1rem;
  }

  /* 按鈕組調整 */
  .btn-group {
    margin-left: auto;
  }

  /* 時間戳記格式調整 */
  td[data-label='評論時間'] {
    font-size: 0.75rem;
  }

  /* 商品名稱調整 */
  .text-sm {
    font-size: 0.875rem !important;
  }

  /* 卡片內容間距調整 */
  .card-body {
    padding: 1rem;
  }

  /* 評論文字換行處理 */
  .myComment {
    word-break: break-all;
    white-space: pre-wrap;
  }
}

/* 超小螢幕調整 */
@media (max-width: 576px) {

  thead {
    display: none;
  }
  .modal-dialog {
    margin: 0;
    height: 100vh;
    max-width: 100%;
  }

  .modal-content {
    height: 100vh;
    border: 0;
    border-radius: 0;
  }

  .btn {
    padding: 0.25rem 0.5rem;
    font-size: 0.8125rem;
  }

  .star-rating {
    transform: scale(0.5);
    transform-origin: left;
  }
  .table-responsive {
    border: 0;
  }

  .table-responsive thead {
    display: none;
  }
}
</style>
