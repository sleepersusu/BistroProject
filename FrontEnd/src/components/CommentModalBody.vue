<template>
  <div class="d-md-flex">
    <div>
      <section class="avatar-container">
        <img :src="memberSrc" alt="" class="avatar" />
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
      {{ comment.commentMessage || '沒有提供評論內容。' }}
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

.d-md-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h4 {
  font-size: 22px; /* 預設標題字體大小 */
  margin: 0;
  text-align: center;
}

section {
  margin-bottom: 5px;/* 預設段落間距 */
  width: 200px;
}
/* 手機螢幕寬度小於768px的樣式 */
@media (max-width: 768px) {
  .d-md-flex {
    flex-direction: column; /* 改為垂直排列 */
    align-items: center; /* 垂直居中 */
    text-align: center; /* 置中文字置中 */
  }

  .avatar-container {
    margin-bottom: 15px; /* 調整底部間距 */
  }

  .avatar-container > img {
    width: 100px; /* 手機上縮小圖片尺寸 */
    height: 100px;
  }

  h4 {
    font-size: 18px; /* 調整標題字體大小 */
    margin-top: 10px;
  }

  section {
    margin-bottom: 10px; /* 增加段落間距 */
    text-align: start;
  }

  .star-rating {
    justify-content: center; /* 星星評分置中 */
    margin-bottom: 10px;
  }
}
</style>
