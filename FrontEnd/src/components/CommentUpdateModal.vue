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


export default {
  components: {

  },

  data() {
    return {

    }
  },
  methods: {

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

  }
}
</script>

<style scoped></style>
