<template>
  <tr>
    <!-- 商品名稱 -->
    <td data-label="餐點名稱">
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
    <td data-label="評論內容" style="max-width: 300px">
      {{ comment.commentMessage }}
    </td>

    <!-- 時間和操作 -->
    <td data-label="評論時間" class="action-cell">
      <span class="time-text">{{ comment.commentTime }}</span>
    </td>

    <td>
      <div class="dropdown">
        <button
          class="btn btn-sm action-btn"
          type="button"
          @click="toggleDropdown"
          aria-expanded="false"
        >
          <i class="bi bi-three-dots"></i>
        </button>
        <ul
          class="dropdown-menu"
          :class="{ show: isOpen }"
          @click.stop
        >
          <li>
            <button class="dropdown-item" @click="updateComment(comment)">編輯</button>
          </li>
          <li>
            <button class="dropdown-item" @click="deleteComment(comment)">刪除</button>
          </li>
        </ul>
      </div>
    </td>
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
    return {
      isOpen: false
    }
  },

  mounted() {
    document.addEventListener('click', this.closeDropdown)
  },

  beforeUnmount() {
    document.removeEventListener('click', this.closeDropdown)
  },

  methods: {
    toggleDropdown() {
      this.isOpen = !this.isOpen
    },

    closeDropdown(e) {
      if (!this.$el.contains(e.target)) {
        this.isOpen = false
      }
    },

    async updateComment(comment) {
      this.isOpen = false
      this.$emit('update-comment-modal', comment)
    },

    async deleteComment(comment) {
      this.isOpen = false
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
        let API_URL = `${import.meta.env.VITE_API}/api/frontend/Bistro/deleteComment/${this.comment.id}`

        try {
          await this.axios.delete(API_URL)
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
        } catch (error) {
          console.error('Error deleting comments:', error)
          await Swal.fire({
            icon: 'error',
            title: `刪除失敗`,
            showCancelButton: false
          })
        }
      }
    },
  },
}
</script>

<style scoped>
.dropdown {
  position: relative;
  display: inline-block;
}

.action-btn {
  padding: 0.25rem 0.5rem;
  background: none;
  border: none;
  cursor: pointer;
}

.action-btn:hover {
  background-color: #f8f9fa;
  border-radius: 4px;
}

.dropdown-menu {
  position: absolute;
  right: 0;
  top: 100%;
  min-width: 8rem;
  padding: 0.5rem 0;
  margin: 0.125rem 0 0;
  background-color: #fff;
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 0.25rem;
  box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.175);
  z-index: 1060;
  display: none;
}

.dropdown-menu.show {
  display: block;
}

.dropdown-item {
  display: block;
  width: 100%;
  padding: 0.25rem 1rem;
  clear: both;
  font-weight: 400;
  text-align: inherit;
  white-space: nowrap;
  background-color: transparent;
  border: 0;
  cursor: pointer;
}

.dropdown-item:hover {
  background-color: #f8f9fa;
}

td {
  padding: 1rem 0.5rem;
  vertical-align: middle;
}

.myComment {
  word-wrap: break-word;
}

.rating-text {
  font-weight: bold;
}
</style>
