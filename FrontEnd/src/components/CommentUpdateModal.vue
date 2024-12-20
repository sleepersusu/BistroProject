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
            <div class="mb-3">
              <input
                class="form-control"
                type="text"
                :value="comment.id"
                aria-label="評論編號"
                disabled
                readonly
              />
            </div>

            <div class="mb-3">
              <input
                class="form-control"
                type="text"
                :value="comment.menuid"
                aria-label="菜單編號"
                disabled
                readonly
              />
            </div>

            <div class="mb-3">
              <input
                class="form-control"
                type="text"
                :value="comment.commentProduct"
                aria-label="菜單編號"
                disabled
                readonly
              />
            </div>

            <div class="mb-3">
              <input
                class="form-control"
                type="text"
                :value="comment.memberid"
                aria-label="會員編號"
                disabled
                readonly
              />
            </div>

            <star-rating
              :show-rating="false"
              :rating="comment.commentRating"
              @update:rating="rating = $event"
              :star-points="[
                23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17,
              ]"
            >
            </star-rating>
            <div style="margin-top: 10px; font-weight: bold">{{ currentRatingText }}</div>

            <div class="mb-3">
              <input
                class="form-control"
                type="text"
                :value="rating"
                aria-label="分數"
                disabled
                readonly
              />
            </div>

            <div class="mb-3">
              <label for="commentMessage" class="form-label">你的評論</label>
              <textarea
                class="form-control"
                id="commentMessage"
                rows="3"
                v-model="commentMessage"
              ></textarea>
            </div>

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
      this.handleReset()
      this.$refs.modal.hide()
    },

    async handleSubmit() {
      // 使用當前的 comment 資料
      const updatedComment = {
        id: this.comment.id,
        menuid: this.comment.menuid,
        memberid: this.comment.memberid,
        commentMessage: this.comment.commentMessage,
        commentRating: this.comment.commentRating,
        commentProduct: this.comment.commentProduct,
        memberSex: this.comment.memberSex,
        memberName: this.comment.memberName,
        commentTime: this.currentDate,
      }

      const API_URL = `${import.meta.env.VITE_API}/api/put/comment/${this.comment.id}`

      try {
        // 發送 PUT 請求並傳遞更新的評論資料
        await axios.put(API_URL, updatedComment)

        // 處理成功後的操作
        this.handleClose()
      } catch (error) {
        console.error('更新評論失敗', error)
        // 可以加上錯誤處理邏輯
      }
    },

    getCurrentDate() {
      const now = new Date()
      // 將當前時間轉換為 UTC+8 時區
      const offset = 8 * 60 // 台灣是 UTC+8，所以 offset 是 8 小時，轉換為分鐘
      const localTime = new Date(now.getTime() + now.getTimezoneOffset() * 60000 + offset * 60000)

      // 格式化為 yyyy-MM-ddTHH:mm 格式，符合 datetime-local 的要求
      return localTime.toISOString().slice(0, 16)
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
