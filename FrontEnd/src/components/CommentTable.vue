<template>
  <tr>
    <td data-label="餐點名稱">
      <div class="d-flex px-2 py-1">
        <div class="d-flex flex-column justify-content-center ms-1">
          <h6 class="mb-0 text-sm font-weight-semibold text-primary">
            {{ comment.commentProduct }}
          </h6>
        </div>
      </div>
    </td>
    <td class="text-sm" data-label="評價分數">
      <star-rating
        :rating="comment.commentRating"
        :read-only="true"
        :increment="1"
        :rounded-corners="true"
        :border-width="4"
        :star-size="25"
        :show-rating="false"
        :star-points="[23, 2, 14, 17, 0, 19, 10, 34, 7, 50, 23, 43, 38, 50, 36, 34, 46, 19, 31, 17]"
      >
      </star-rating>
    </td>
    <td class="align-middle text-sm myComment" data-label="評論內容">
      {{ comment.commentMessage }}
    </td>
    <td
      class="align-middle text-primary text-sm font-weight-normal d-flex justify-content-between"
      data-label="評論時間"
    >
      {{ comment.commentTime }}
      <div class="btn-group dropend">
        <button
          class="btn btn-sm"
          type="button"
          id="dropdownEditAndDeleteLink"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          ...
        </button>
        <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownEditAndDeleteLink">
          <li>
            <a class="dropdown-item" v-on:click.prevent="updateComment(comment)">編輯</a>
          </li>
          <li>
            <a class="dropdown-item" v-on:click.prevent="deleteComment(comment)">刪除</a>
          </li>
        </ul>
      </div>
    </td>
  </tr>
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
  emits: ['update-comment-modal','delete-comment-modal'],

  data() {
    return {}
  },
  methods: {
    async updateComment(comment) {
      this.$emit('update-comment-modal', comment)
    },

    async deleteComment(comment) {

      const result = await Swal.fire({
        icon: 'question',
        title: `確定要刪除嗎?`,
        html: `<p>刪除後無法還原</p>`,
        showCancelButton: true,
        confirmButtonText: '確定',
        cancelButtonText: '取消',
        reverseButtons: true,
      })

      if (result.isConfirmed) {
        comment = this.comment
        let API_URL = `${import.meta.env.VITE_API}/api/Bistro/deleteComment/${this.comment.id}`

        this.axios
          .delete(API_URL)
          .then(async () => {
            this.$emit('delete-comment-modal')
            await Swal.fire({
              icon: 'success',
              title: `刪除成功`,
              showCancelButton: false,
            })

          })
          .catch(async (error) => {
            console.error('Error deletei comments:', error)
            await Swal.fire({ icon: 'error', title: `刪除失敗`, showCancelButton: false, })
          })
      }
    },
  },
  watch(){


  }
}
</script>

<style scoped>
.myComment {
  word-wrap: break-word;
}
@media (max-width: 768px) {
  .table-responsive thead {
    display: none;
  }
  .table-responsive tbody,
  .table-responsive tr,
  .table-responsive td {
    display: block;
    width: 100%;
  }
  .table-responsive td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-left: 50%;
    text-align: left;
    position: relative;
  }
  .table-responsive td::before {
    content: attr(data-label);
    position: absolute;
    left: 10px;
    white-space: nowrap;
    font-weight: bold;
  }
  .table-responsive td > div.dropdown {
    align-self: flex-end;
  }
}
</style>
