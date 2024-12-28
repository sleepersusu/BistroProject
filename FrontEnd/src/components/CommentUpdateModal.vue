<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    ref="modal"
  >
    <div class="modal-dialog modal-dialog-centered">
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
              <label class="form-label">商品名稱</label>
              <input
                class="form-control"
                type="text"
                :value="comment.commentProduct"
                aria-label="商品名稱"
                readonly
              />
            </div>


            <!-- 評分 -->
            <div class="mb-3">
              <label class="form-label">評分<span style="color: red">*(必填)</span></label>
              <star-rating
                :show-rating="false"
                :rating="rating"
                :star-size="25"
                @update:rating="handleRatingUpdate"
                :star-points="[
                  23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17,
                ]"
              >
              </star-rating>
              <div
                :class="['rating-text mt-2', { 'text-danger': showValidationErrors && !rating }]"
              >
                {{ currentRatingText }}
              </div>
            </div>
            <!-- 顯示評分 -->
            <div class="mb-3">
              <input
                class="form-control"
                type="hidden"
                :value="rating"
                aria-label="分數"
                readonly
              />
            </div>

            <!-- 評論內容 -->
            <div class="mb-3">
              <label for="commentMessage" class="form-label">評論內容</label>
              <span
                style="font-size: smaller; margin-left: 5px"
                :class="['char-count', { 'text-danger': commentLength <= 0 }]"
                >剩餘字數 : {{ commentLength }}字</span
              >
              <textarea
                class="form-control"
                id="commentMessage"
                rows="3"
                v-model="commentMessage"
                maxlength="200"
              ></textarea>
            </div>

            <!-- 評論時間 -->
            <div class="mb-3">
              <input
                class="form-control"
                style="display: none"
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
          <button
            type="button"
            class="btn btn-secondary"
            @click="handleReset"
            style="color: white; background-color: red"
          >
            重設
          </button>
          <button type="button" class="btn btn-primary"
          @click="handleSubmit()":disabled="isSubmitting">
          {{isSubmitting?'提交中...':'確定'}}</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore'
import modalMixinOption from '@/mixins/modalMixin-option'
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
      switch (true) {
        case this.rating == 5:
          return '太棒了！超乎期待'
        case this.rating == 4:
          return '很好！令人滿意'
        case this.rating == 3:
          return '普通，還可以'
        case this.rating == 2:
          return '不太理想'
        default:
          return '非常不滿意'
      }
    },
  },

  mixins: [modalMixinOption],
  emits: ['update-table'],
  data() {
    return {
      commentMessage: '',
      rating: this.comment.rating,
      isSubmitting: false,
      currentDate: this.getCurrentDate(),
      commentLength: '',
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
    getCurrentDate() {
      const taipeiDate = new Date().toLocaleString('en-US', {
        timeZone: 'Asia/Taipei',
        hour12: false,
      })

      // 解析台北時間字符串
      const date = new Date(taipeiDate)

      // 格式化為需要的格式 (YYYY-MM-DDTHH:mm)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')

      return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}`
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

      let commentTime = this.currentDate

      // 修改時間格式轉換邏輯
      if (commentTime) {
        // 將 T 替換為空格，並添加秒數
        const formattedDate = commentTime.replace('T', ' ') + ':00'
        updatedComment.commentTime = formattedDate

        const API_URL = `${import.meta.env.VITE_API}/api/frontend/put/comment/${this.comment.id}`
        console.log(this.comment.id)
        try {
          // 發送 PUT 請求並傳遞更新的評論資料
          await axios.put(API_URL, updatedComment).then(async (response) => {
            this.$emit('update-table')
            this.isSubmitting=true,
            await new Promise((resolve) => setTimeout(resolve, 100))

            this.$refs.modal.querySelector('[data-bs-dismiss="modal"]').click()

            Swal.fire({
              title: '感謝你的評論!',
              text: '提交評論成功。',
              icon: 'success',
              confirmButtonText: '確定',
              confirmButtonColor: 'black',
              iconColor: 'black',
            })
          })
        } catch (error) {
          this.$refs.modal.querySelector('[data-bs-dismiss="modal"]').click()
          Swal.fire({
            title: '錯誤!',
            text: '提交評論時發生錯誤。',
            icon: 'error',
            iconColor: 'black',
            customClass: {
              confirmButton: 'custom-button',
              icon: 'custom-button',
            },
          })
        }finally{
          this.isSubmitting=false;
        }
      }
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

    commentMessage: {
      handler(newVal) {
        this.commentLength = 200 - newVal.length

        if (this.commentLength < 0) {
          document.getElementById('submitButton').disabled = true
        }
      },
      immediate: true,
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

.rating-text {
  font-weight: bold;
}

.text-danger {
  color: #dc3545;
}

.invalid-feedback {
  display: block;
}

button {
  border: none;
}
</style>
