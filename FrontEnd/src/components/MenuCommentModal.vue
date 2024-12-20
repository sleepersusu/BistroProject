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

        <div v-for="comment in comments" :key="comment.id">
          <div class="modal-body">
            <CommentModalBody :comment="comment"></CommentModalBody>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModalMixin from '@/mixins/modalMixin-option'
import StarRating from 'vue-star-rating'
import LoadingVue from 'vue3-loading-overlay'
import CommentModalBody from './CommentModalBody.vue';
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
        })
        .catch((error) => {
          console.error('Error loading comments:', error)
        })
        .finally(() => {
          this.isLoading = false
        })
    },
  },

}
</script>


