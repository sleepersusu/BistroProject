<template>
  <table class="table">
    <thead>
      <tr>
        <th scope="col">商品</th>
        <th scope="col">評論分數</th>
        <th scope="col">你的評論</th>
        <th scope="col">評論時間</th>
      </tr>
    </thead>
    <tbody v-if="!NoComment" v-for="comment in comments" :key="comment.id">
      <CommentTable :comment="comment"
        @update-comment-modal="openUpdateCommentModal">
      </CommentTable>

      <CommentUpdateModal ref="commentUpdateModal" :id="currentCommentId"></CommentUpdateModal>

    </tbody>

    <tbody v-if="NoComment" >
      <tr >
        <td colspan="5">目前尚未有評論</td>
      </tr>
    </tbody>


  </table>
</template>

<script>
import CommentTable from '@/components/CommentTable.vue'
import CommentUpdateModal from '@/components/CommentUpdateModal.vue';

export default {
  components: {
    CommentTable,
    CommentUpdateModal,
  },

  data() {
    return {
      comments:[],
      NoComment:true,

      currentCommentId:''
    }
  },
  methods: {
    async loadAllCommentByMember() {
      let API_URL = `${import.meta.env.VITE_API}/api/member/comment`

      this.axios
        .get(API_URL)
        .then(async (response) => {
          this.comments=response.data
          this.NoComment=false
        })
        .catch((error) => {
          console.error('Error fetching comments:', error)
          this.NoComment=true
        })
    },

    async openUpdateCommentModal(comment){
      this.currentCommentId=comment.id
      this.$refs.commentUpdateModal.showModal()
    }

  },

  created(){
    this.loadAllCommentByMember()
  },
  updated(){
    this.loadAllCommentByMember()
  }

}
</script>

<style scoped></style>
