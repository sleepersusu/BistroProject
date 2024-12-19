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
            <h1 class="modal-title fs-2" id="exampleModalLabel"> {{ productName }}</h1>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>

          <div  v-for="comment in comments" :key="comment.id">
            <div class="modal-body" >
              <div style="display: flex">
                <div>
                  <div>
                    <section class="avatar-container">
                      <img src="" alt="" class="avatar" />
                    </section>

                    <section>
                      <h4>{{ comment.memberName }}</h4>
                    </section>
                  </div>
                </div>

                <div>
                  <section>
                    <star-rating
                      v-model:rating="comment.commentRating"
                      :read-only="true"
                      :increment="1"
                      :star-size="20"
                      style="font-size: 15px"
                    ></star-rating>
                  </section>

                  <section>
                    {{ comment.commentMessage || '沒有提供評論內容。' }}
                  </section>
                </div>
              </div>
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
  export default {
      components: {
          'star-rating': StarRating,
          LoadingVue,

      },
      mixins:[ModalMixin],

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
      methods: {

      async viewComment(menu) {
          this.isLoading = true

          let api = `${import.meta.env.VITE_API}/api/${menu.productName}/comment`
          this.axios
              .get(api)
              .then((response) => {
              this.comments = response.data
              console.log(this.comments)
              })
          .catch((error) => {
              console.error('Error loading menus:', error)
          })
          .finally(() => {
              this.isLoading = false
          })
      },

      async loadMemberAvatar(comment){
          this.showComment=true
          try {
              const api = `${import.meta.env.VITE_API}/api/member/photo/${comment.memberid}`
              let res = await this.axios.get(api,{ responseType: 'blob' })
              let url = URL.createObjectURL(res.data)
              this.memberSrc = url
              this.isLoading = false
              this.$emit('image-loaded', this.memberSrc)

          }catch (e) {
          console.log(e)
          }
      }}
  }

  </script>


  <style scoped>



  </style>
