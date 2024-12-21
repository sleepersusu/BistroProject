<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    ref="modal"
  >
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">刪除評論</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div>確定要刪除嗎?</div>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
          <button type="button" class="btn btn-primary">確定</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ModalMixin from '@/mixins/modalMixin-option'
import axios from 'axios'
import Swal from 'sweetalert2'
export default {
  props: {
    comment: {
      type: Object,
      required: true,
    },
  },

  components: {},

  mixins: [ModalMixin],

  data() {
    return {}
  },

  methods: {},
}
</script>

<style scoped></style>
