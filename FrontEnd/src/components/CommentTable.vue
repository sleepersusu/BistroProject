<template>
  <tr>
    <!-- 商品名稱 -->
    <td data-label="餐點名稱" >
      <div class="d-flex">
        <div class="mb-0 text-sm font-weight-semibold text-primary">
          {{ comment.commentProduct }}
        </div>
      </div>
    </td>

    <!-- 評分星星 -->
    <td data-label="評價分數" style="max-width: 10px">
      <div class="rating-wrapper">
        <star-rating
          :rating="comment.commentRating"
          :read-only="true"
          :increment="1"
          :rounded-corners="true"
          :star-size="25"
          :show-rating="false"
          :star-points="[
            23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17,
          ]"
        >
        </star-rating>
      </div>
    </td>

    <!-- 評論內容 -->
    <td data-label="評論內容" style="max-width: 300px; ">
      {{ comment.commentMessage }}
    </td>

    <!-- 時間和操作 -->
    <td data-label="評論時間" class="action-cell">
      <span class="time-text">{{ comment.commentTime }}</span>
    </td>

    <div class="action-wrapper">
      <div class="btn-group" style="margin:10px 0px 10px 0px; ">
        <button
          class="btn btn-sm action-btn"
          type="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          <i class="bi bi-three-dots"></i>
        </button>
        <ul class="dropdown-menu dropdown-menu-start" style="z-index: 10;"
        data-bs-offset="0,10">
          <li>
            <button class="dropdown-item" @click.prevent="updateComment(comment)">編輯</button>
          </li>
          <li>
            <button class="dropdown-item" @click.prevent="deleteComment(comment)">刪除</button>
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
        icon: 'warning',
        iconColor: 'black',
        title: `確定要刪除嗎?`,
        html: `<p>刪除後無法還原</p>`,
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        reverseButtons: true,
        confirmButtonColor: 'red',
        customClass: {
          confirmButton: 'custom-button',
        },
      })

      if (result.isConfirmed) {
        comment = this.comment
        let API_URL = `${import.meta.env.VITE_API}/api/frontend/Bistro/deleteComment/${this.comment.id}`

        this.axios
          .delete(API_URL)
          .then(async () => {
            this.$emit('delete-comment-modal')
            await Swal.fire({
              icon: 'success',
              iconColor: 'black',
              title: `刪除成功`,
              showCancelButton: false,
              confirmButtonColor: 'black',
              customClass: {
                confirmButton: 'custom-button',
              },
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

.dropdown-menu {
  position: absolute !important;
  transform: translate(0, 10px) !important;
  z-index: 1055;
}


.action-wrapper {
  padding: 0;
}
td{
  padding: 50px 0px 50px 5px;
}


</style>
