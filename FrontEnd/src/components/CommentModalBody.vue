<template>
  <div class="d-md-flex">
              <div>
                <section class="avatar-container">
                  <img
                    :src="memberSrc"

                    alt=""
                    class="avatar"
                  />
                </section>

                <section>
                  <h4>{{ comment.memberName }}</h4>
                </section>
              </div>

              <div>
                <section>
                  <star-rating
                    v-model:rating="comment.commentRating"
                    :read-only="true"
                    :increment="1"
                    :star-size="20"
                    style="font-size: 15px"
                    :rounded-corners="true"
                    :border-width="4"
                    :show-rating="false"
                    :star-points="[
                      23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17,
                    ]"
                  ></star-rating>
                </section>
              </div>
              <section>
                {{
                  comment.commentMessage || '沒有提供評論內容。'
                }}
              </section>
            </div>

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
  data() {
    return {
      memberSrc:'',
    }
  },
  methods: {

    async loadMemberAvatar(memberid) {
      this.showComment = true

        const api = `${import.meta.env.VITE_API}/api/member/photo/${memberid}`
        this.axios.
        get(api, { responseType: 'blob' })
        .then((response) => {

          let url = URL.createObjectURL(response.data)
          this.memberSrc = url
      })
    }
  },
  async created(){
    await this.loadMemberAvatar(this.comment.memberid)
  }

}
</script>

<style scoped>
.avatar-container > img {
  width: 150px; /* 設定圖片的寬度 */
  height: 150px; /* 設定圖片的高度 */
  text-align: center; /* 圖片的對齊方式，對於 img 通常無效 */
  border-radius: 50%; /* 圖片圓角，50% 將圖片變為圓形 */
  object-fit: cover; /* 保證圖片在框內填滿，不會變形 */
  border: 2px solid #ddd; /* 可選：增加一個細邊框使圖片更有層次感 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 可選：增加陰影效果 */
}
</style>
