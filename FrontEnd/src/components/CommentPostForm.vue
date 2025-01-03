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
          <h1 class="modal-title fs-5" id="exampleModalLabel">新增評論</h1>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSubmit" novalidate>
            <!-- 商品名稱 -->
            <div class="mb-3">
              <label class="form-label">商品名稱</label>
              <input class="form-control" type="text" :value="commentProduct" readonly />
            </div>

            <!-- 評分星星 -->
            <div class="mb-3">
              <label class="form-label">評分<span style="color: red">*(必填)</span></label>
              <star-rating
                :show-rating="false"
                :rating="rating"
                :star-size="25"
                @update:rating="handleRatingUpdate"
                :star-points="starPoints"
              ></star-rating>
              <div
                :class="['rating-text mt-2', { 'text-danger': showValidationErrors && !rating }]"
              >
                {{ currentRatingText }}
              </div>
            </div>

            <!-- 評論內容 -->
            <div class="mb-3">
              <label for="commentMessage" class="form-label">評論內容</label>
              <span style="font-size: smaller; margin-left: 5px"
              :class="['char-count', { 'text-danger': commentLength <= 0 }]"
                >剩餘字數 : {{ commentLength }}字</span
              >
              <textarea
                class="form-control"
                id="commentMessage"
                rows="3"
                v-model.trim="commentMessage"
                placeholder="請分享您的心得..."
                maxlength="200"
              ></textarea>
            </div>

            <!-- 評論時間 -->
            <div class="mb-3">
              <label class="form-label">評論時間</label>
              <input class="form-control" type="text" :value="currentDate" readonly />
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button
            type="button"
            class="btn btn-secondary"
            @click="handleReset"
            :disabled="isSubmitting"
            style="color: white; background-color: red"
          >
            重設
          </button>
          <button
            id="submitButton"
            type="button"
            class="btn btn-primary"
            @click="handleSubmit"
            :disabled="isSubmitting"
          >
            {{ isSubmitting ? '提交中...' : '確定' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import StarRating from 'vue-star-rating'
import modalMixinOption from '@/mixins/modalMixin-option'
import axios from 'axios'
import Swal from 'sweetalert2'
import { comment } from 'postcss'

export default {
  name: 'CommentForm',

  components: {
    StarRating,
  },

  mixins: [modalMixinOption],

  props: {
    item: {
      type: Object,
      required: true,
      validator(value) {
        return value && typeof value.odName === 'string'
      },
    },
  },

  data() {
    return {
      commentProduct: '',
      commentMessage: '',
      rating: 0,
      isCommented: false,
      isSubmitting: false,
      currentDate: '',
      showValidationErrors: false,
      starPoints: [23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17],
      commentLength: 200
    }
  },

  emits: ['comment-submitted'],

  computed: {
    currentRatingText() {
      if (!this.rating) return '請選擇評分'

      const ratingTexts = {
        5: '太棒了！超乎期待',
        4: '很好！令人滿意',
        3: '普通，還可以',
        2: '不太理想',
        1: '非常不滿意',
      }

      return ratingTexts[Math.floor(this.rating)] || '請選擇評分'
    },

    isFormValid() {
      return this.rating > 0
    },
  },

  watch: {
    item: {
      immediate: true,
      handler(newItem) {
        if (newItem?.odName) {
          this.commentProduct = newItem.odName
          this.handleReset()
        }
      },
    },
    commentMessage: {
    handler(newVal) {
      this.commentLength = 200 - newVal.length;

      if (this.commentLength< 0) {

        document.getElementById("submitButton").disabled=true

      }
    },
    immediate: true
  }
},

  methods: {
    getCurrentDate() {
      const options = {
        timeZone: 'Asia/Taipei',
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
      }
      return new Intl.DateTimeFormat('zh-TW', options).format(new Date()).replace(/\//g, '-')
    },

    updateCurrentDate() {
      this.currentDate = this.getCurrentDate()
    },

    handleRatingUpdate(newRating) {
      this.rating = newRating
      this.showValidationErrors = false
    },

    handleReset() {
      this.commentMessage = ''
      this.rating = 0
      this.showValidationErrors = false
      this.updateCurrentDate()
      this.commentLength = 200 // 重置字數計數


    },

    async handleSubmit() {
      if (this.isSubmitting) return

      this.showValidationErrors = true
      if (!this.rating) {
        // 檢查是否有評分
        await Swal.fire({
          title: '請完整填寫',
          text: '請至少填寫評分',
          icon: 'warning',
          confirmButtonColor: 'black',
          iconColor: 'black',
          customClass: {
            confirmButton: 'custom-button',
          },
        })
        return
      }

      this.isSubmitting = true

      try {
        this.updateCurrentDate()

        const addComment = {
          commentMessage: this.commentMessage.trim() || '無評論內容', // 如果沒有評論內容，使用預設文字
          commentRating: this.rating,
          commentProduct: this.commentProduct,
          commentTime: this.currentDate,
        }

        await axios.post(`${import.meta.env.VITE_API}/api/frontend/comment/postComment`, addComment)

        await Swal.fire({
          title: '感謝你的評論!',
          text: '提交評論成功。',
          icon: 'success',
          confirmButtonText: '確定',
          confirmButtonColor: 'black',
          iconColor: 'black',
          customClass: {
            confirmButton: 'custom-button',
          },
        })

        this.isCommented = true
        // 向父組件發送評論狀態
        this.$emit('comment-submitted', {
          detailId: this.item.id,
          isCommented: true,
        })

        this.handleReset()
      } catch (error) {
        console.error('Comment submission error:', error)
      } finally {
        this.showValidationErrors = false
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
