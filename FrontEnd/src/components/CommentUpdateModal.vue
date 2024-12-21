<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    ref="modal"
  >
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-5" id="exampleModalLabel">更新評論</h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit">
            <!-- 評論編號 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="hidden"
                :value="comment.id"
                aria-label="評論編號"
                disabled
                readonly
              />
            </div>

            <!-- 菜單編號 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="hidden"
                :value="comment.menuid"
                aria-label="菜單編號"
                disabled
                readonly
              />
            </div>

            <!-- 菜單名稱 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="text"
                :value="comment.commentProduct"
                aria-label="菜單名稱"
                disabled
                readonly
              />
            </div>

            <!-- 會員編號 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="hidden"
                :value="comment.memberid"
                aria-label="會員編號"
                disabled
                readonly
              />
            </div>

            <!-- 評分 -->
            <star-rating
              :show-rating="false"
              :rating="rating"
              @update:rating="handleRatingUpdate"
              :star-points="[
                23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17,
              ]"
            >
            </star-rating>
            <div style="margin-top: 10px; font-weight: bold">{{ currentRatingText }}</div>

            <!-- 顯示評分 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="hidden"
                :value="rating"
                aria-label="分數"
                disabled
                readonly
              />
            </div>

            <!-- 評論訊息 -->
            <div class="mb-3">
              <label for="commentMessage" class="form-label">你的評論</label>
              <textarea
                class="form-control"
                id="commentMessage"
                rows="3"
                v-model="commentMessage"
              ></textarea>
            </div>

            <!-- 評論時間 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="datetime-local"
                :value="currentDate"
                aria-label="評論時間"
                disabled
                readonly
              />
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="handleReset">重設</button>
          <button type="button" class="btn btn-primary" @click="handleSubmit()">確定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'
import ModalMixin from '@/mixins/modalMixin-option'
import StarRating from 'vue-star-rating'
import { mapState } from 'pinia'
import axios from 'axios'
import Swal from 'sweetalert2'

export default {
  props: {
    comment: {
      type: Object,
      required: true,
    },
  },

  components: {
    StarRating,
  },

  computed: {
    ...mapState(useUserStore, ['memberId']),

    currentRatingText() {
      return this.rating ? `您已選擇 ${this.rating} 顆星` : '尚未評分'
    },
  },

  mixins: [ModalMixin],
  emits:['update-table'],
  data() {
    return {
      commentMessage: '',
      rating: this.comment.rating,
      isSubmitting: false,
      currentDate: this.getCurrentDate(),
    }
  },

  methods: {
    handleRatingUpdate(newRating) {
      this.rating = newRating
    },

    handleReset() {
      this.rating = this.comment.commentRating || 0
      this.commentMessage = this.comment.commentMessage || ''
    },

    handleClose() {
      this.$refs.modal.hide()
      this.$emit('close')
    },

    async handleSubmit() {
      // 使用當前的 comment 資料
      const updatedComment = {
        id: this.comment.id,
        menuId: this.comment.menuid,
        memberId: this.comment.memberid,
        commentMessage: this.commentMessage,
        commentRating: this.rating,
        commentProduct: this.comment.commentProduct,
        commentTime: this.currentDate,
      }

      let commentTime = this.currentDate // 例如 "2024-12-21T14:30"

      // 將前端的時間格式 (yyyy-MM-ddTHH:mm) 轉換為後端期望的格式 (yyyy-MM-dd HH:mm:ss)
      if (commentTime) {
        // 解析前端時間格式
        const dateObj = new Date(commentTime)

        // 格式化成後端需要的格式
        const formattedDate = dateObj.toISOString().slice(0, 19).replace('T', ' ') // "2024-12-21 14:30:00"
        updatedComment.commentTime = formattedDate // 更新為格式化後的時間
      }

      const API_URL = `${import.meta.env.VITE_API}/api/put/comment/${this.comment.id}`
      console.log(this.comment.id)
      try {
        // 發送 PUT 請求並傳遞更新的評論資料
        await axios.put(API_URL, updatedComment).then(async (response) => {
          this.$emit('update-table');


          await new Promise(resolve => setTimeout(resolve, 100));

          this.$refs.modal.querySelector('[data-bs-dismiss="modal"]').click();

          Swal.fire({
            title: '感謝你得評論!',
            text: '提交評論成功。',
            icon: 'success',
          })
        })
      } catch (error) {
        this.$refs.modal.querySelector('[data-bs-dismiss="modal"]').click();
        Swal.fire({
          title: '錯誤!',
          text: '提交評論時發生錯誤。',
          icon: 'error',
        })
      }
    },

    getCurrentDate() {
      const now = new Date()
      // 將當前時間轉換為 UTC+8 時區
      const offset = 8 * 60 // 台灣是 UTC+8，轉換為分鐘
      const localTime = new Date(now.getTime() + now.getTimezoneOffset() * 60000 + offset * 60000)

      // 格式化為 yyyy-MM-ddTHH:mm 格式，符合 datetime-local 的要求
      const year = localTime.getFullYear()
      const month = String(localTime.getMonth() + 1).padStart(2, '0') // 月份從 0 開始，所以加 1
      const day = String(localTime.getDate()).padStart(2, '0')
      const hours = String(localTime.getHours()).padStart(2, '0')
      const minutes = String(localTime.getMinutes()).padStart(2, '0')

      return `${year}-${month}-${day}T${hours}:${minutes}`
    },
    updateCurrentDate() {
      this.currentDate = this.getCurrentDate()
    },
  },
  watch: {
    comment(newComment) {
      if (newComment.id) {
        this.rating = this.comment.commentRating
        this.commentMessage = this.comment.commentMessage
        this.updateCurrentDate() // 當 comment 更新時也更新時間
      }
    },
  },
  mounted() {
    this.updateCurrentDate()
  },
}
</script>

<style scoped>
.modal-dialog {
  max-width: 500px;
}

textarea {
  resize: vertical;
  min-height: 100px;
}
</style>
