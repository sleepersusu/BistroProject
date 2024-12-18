

<template>

    <tr>
      <td>{{comment.commentProduct}}</td>
      <td>
        <star-rating :rating="comment.commentRating"
          :read-only="true" :increment="1"
          :rounded-corners="true" :border-width="4"
          :star-points="[23,2, 14,17, 0,19, 10,34, 7,50, 23,43, 38,50, 36,34, 46,19, 31,17]">
        </star-rating></td>
      <td>{{comment.commentMessage}}</td>
      <td>{{comment.commentTime}}</td>
      <td>
        <button v-on:click.prevent="updateComment(comment)">編輯</button>
        <button v-on:click.prevent="deleteComment(comment)">刪除</button>
      </td>
    </tr>

</template>

<script>
import axios from 'axios'
import StarRating from 'vue-star-rating'


export default {
  components: {
    'star-rating': StarRating,
  },

  props: {
    comment:{
      type:Object,
      required: true,
    }
  },

  data() {
    return {}
  },
  methods: {





    async updateComment(comment) {

      this.$emit("update-comment-modal",comment)
      
    },


  },
}
</script>

<style scoped></style>
