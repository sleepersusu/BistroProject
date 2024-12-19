<template>

  <div class="container-fluid">
    <div class="row">
      <nav class="col-md-3 col-lg-2 p-3 bg-light">
        <MembercenterNav></MembercenterNav>
      </nav>
      <main class="col-md-9 col-lg-10 p-4">
        <h1 class="text-center">我的評論</h1>
        <div class="container-fluid py-4 px-5">
          <div class="row">
            <div class="col-12">
              <div class="card border shadow-xs mb-4">
                <div class="card-header border-bottom pb-0">
                  <div class="d-sm-flex align-items-center">
                    <span class="h4">我的評論</span>
                    <div class="ms-auto d-flex"></div>
                  </div>
                </div>
                <div class="card-body px-0 py-0">
                  <div class="table-responsive p-0">
                    <table class="table align-items-center mb-0">
                      <thead class="bg-gray-100">
                        <tr>
                          <th class="text-dark text-xs font-weight-semibold opacity-7">餐點名稱</th>
                          <th class="text-dark text-xs font-weight-semibold opacity-7">評價分數</th>
                          <th class="text-dark text-xs font-weight-semibold opacity-7">評論內容</th>
                          <th class="text-dark text-xs font-weight-semibold opacity-7">用餐日期</th>
                        </tr>
                      </thead>

                      <tbody v-if="!NoComment" v-for="comment in comments" :key="comment.id">
                        <CommentTable
                          :comment="comment"
                          @update-comment-modal="openUpdateCommentModal"
                        >
                        </CommentTable>
                      </tbody>

                      <tbody v-if="NoComment">
                        <tr>
                          <td colspan="4">目前尚未有評論</td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <div class="border-top py-3 px-3 d-flex align-items-center">
                    <p class="font-weight-semibold mb-0 text-dark text-sm">Page 1 of 10</p>
                    <div class="ms-auto">
                      <button class="btn btn-sm btn-white mb-0">Previous</button>
                      <button class="btn btn-sm btn-white mb-0">Next</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import CommentTable from '@/components/CommentTable.vue'
import CommentUpdateModal from '@/components/CommentUpdateModal.vue'
import MembercenterNav from '@/components/memberCenter/membercenterNav.vue';

export default {
  components: {
    CommentTable,
    CommentUpdateModal,
    MembercenterNav,
  },

  data() {
    return {
      comments: [],
      NoComment: true,

      currentCommentId: '',
    }
  },
  methods: {
    async loadAllCommentByMember() {
      let API_URL = `${import.meta.env.VITE_API}/api/member/comment`

      this.axios
        .get(API_URL,{withCredentials: true})
        .then(async (response) => {

          this.comments = response.data
          this.NoComment = false
        })
        .catch((error) => {
          console.error('Error fetching comments:', error)
          this.NoComment = true
        })
    },

    async openUpdateCommentModal(comment) {
      this.currentCommentId = comment.id
      this.$refs.commentUpdateModal.showModal()
    },
  },

  created() {
    this.loadAllCommentByMember()
  },
  updated() {
    this.loadAllCommentByMember()
  },
}
</script>

<style scoped></style>
