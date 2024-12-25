<template>
  <div class="container">
    <div class="row">
      <!-- 左側：用戶圖片和名稱 -->
      <div class="col-md-3 d-flex flex-column align-items-center">
        <section class="avatar-container">
          <img :src="memberSrc" alt="" class="avatar" @error="memberSrc = 'public/images/avatar.jpg'"/>
        </section>

        <section>
          <h4 class="text-center">{{ comment.memberName }}</h4>
        </section>
      </div>

      <!-- 右側：星級評分和評論內容 -->
      <div class="col-md-9">
        <section class="d-flex flex-column align-items-start">
          <!-- 星級評分 -->
          <star-rating
            v-model:rating="comment.commentRating"
            :read-only="true"
            :increment="1"
            :star-size="20"
            style="font-size: 15px"
            :rounded-corners="true"
            :show-rating="false"
            :star-points="[23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17]"
          ></star-rating>

          <!-- 評論內容 -->
          <div class="comment-message mt-2">
            {{ comment.commentMessage || '沒有提供評論內容。' }}
          </div>
        </section>
      </div>
    </div>
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
      memberSrc: '',
    }
  },
  methods: {
    async loadMemberAvatar(memberid) {
      this.showComment = true

      const api = `${import.meta.env.VITE_API}/api/member/photo/${memberid}`
      this.axios.get(api, { responseType: 'blob' }).then((response) => {
        let url = URL.createObjectURL(response.data)
        this.memberSrc = url
      })
    },
  },
  async created() {
    await this.loadMemberAvatar(this.comment.memberid)
  },
}
</script>

<style scoped>
.avatar-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 10px;
}

.avatar-container > img {
  width: 150px; /* 預設圖片寬度 */
  height: 150px; /* 預設圖片高度 */
  border-radius: 50%; /* 圓形圖片 */
  object-fit: cover; /* 圖片填滿框架 */
  border: 2px solid #ddd; /* 增加邊框 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 增加陰影效果 */
}

.comment-message {
  font-size: 16px;
  line-height: 1.5;
}

/* 手機及平板設計 */
@media (max-width: 768px) {
  .avatar-container > img {
    width: 100px; /* 手機上縮小圖片尺寸 */
    height: 100px;
  }

  h4 {
    font-size: 18px; /* 調整標題字體大小 */
    margin-top: 10px;
  }

  .comment-message {
    font-size: 14px; /* 調整評論字體大小 */
  }
}
</style>
