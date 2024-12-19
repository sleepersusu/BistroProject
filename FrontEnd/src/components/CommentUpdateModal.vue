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

            <form>

                <div class="mb-3">
                    <input class="form-control"
                    type="text" value="評論id"
                    aria-label="Disabled input example" disabled readonly>
                </div>

                <div class="mb-3">
                    <input class="form-control"
                    type="text" value="商品名稱"
                    aria-label="Disabled input example" disabled readonly>
                </div>

                <div class="mb-3">
                    <input class="form-control"
                    type="text" value="會員id"
                    aria-label="Disabled input example" disabled readonly>
                </div>


                <star-rating :show-rating="false" @update:rating="rating = $event"
                  :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]">
                </star-rating>
                <div style="margin-top:10px;font-weight:bold;">{{currentRatingText}}</div>

                <div class="mb-3">
                    <input class="form-control"
                    type="text" value="" required>

                </div>

                <div class="mb-3">
                    <label for="exampleFormControlTextarea1" class="form-label">你的評論</label>
                    <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"
                    required></textarea>
                </div>


            </form>

          </div>
          <div class="modal-footer">
            <button
              type="button"
              class="btn btn-secondary">
              重設
            </button>
            <button type="button" class="btn btn-primary">確定</button>
          </div>
        </div>
      </div>
    </div>




</template>

<script>
import { useUserStore } from '@/stores/userStore';
import ModalMixin from '@/mixins/modalMixin-option'
import StarRating from 'vue-star-rating'
import { mapState } from 'pinia';
export default {
  props:{
    currentComment:{
      type: Object,
      required: true,
    }
  },
  components: {
    StarRating
  },
  computed:{
    ...mapState(useUserStore,['memberId'])
  }
  ,
  mixins:[ModalMixin],

  data() {
    return {


      localComment: { ...this.currentComment },
      rating: '',
      resetableRating: 2,
      currentRating: "No Rating",
      mouseOverRating: null
    }
  },
  methods: {

    showCurrentRating(rating) {
      this.currentSelectedRating = rating === 0? this.currentSelectedRating: "Click to select " + rating + " stars";
    },
    setCurrentSelectedRating(rating) {
      this.currentSelectedRating = "You have Selected: " + rating + " stars";
    },









  async updateComment(comment) {
  let API_URL = `${import.meta.env.VITE_API}/api/put/comment/${id}`

    axios
      .get(API_URL)
      .then(async (response) => {
        this.comments=response.data
      })
      .catch((error) => {
        console.error('Error fetching comment:', error)
      })
    },
  },
  computed: {
    currentRatingText() {
      return this.rating
        ? "You have selected " + this.rating + " stars"
        : "No rating selected";
    },
    mouseOverRatingText() {
      return this.mouseOverRating
        ? "Click to select " + this.mouseOverRating + " stars"
        : "No Rating";
    }
  },
  watch: {
    currentComment: {
      deep: true,
      handler(newComment) {
        this.localComment = { ...newComment };
      },
    },
  },

}
</script>

<style scoped></style>
