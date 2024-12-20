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
                required
                :maxlength="maxCommentLength"
              ></textarea>
              <small class="text-muted">
                剩餘字數: {{ maxCommentLength - commentMessage.length }}
              </small>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="handleReset">重設</button>
          <button type="button" class="btn btn-primary" @click="handleSubmit" :disabled="!isValid">
            確定
          </button>
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
      validator: (obj) => {
        return obj.id && obj.menuid
      },
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

    isValid() {
      return this.rating > 0
    },
  },

  mixins: [ModalMixin],

  data() {
    return {
      commentMessage: '',
      rating: this.comment.rating,
      maxCommentLength: 100,
      isSubmitting: false,
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
      if (!this.isValid || this.isSubmitting) return

      this.isSubmitting = true


        const API_URL = `${import.meta.env.VITE_API}/api/put/comment/${this.comment.id}`

        await axios.put(API_URL)


        this.handleClose()


    },
  },

  watch: {
    comment(newComment) {
      if (newComment.id) {
        this.rating = this.comment.commentRating
        this.commentMessage = this.comment.commentMessage
      }
    },
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
