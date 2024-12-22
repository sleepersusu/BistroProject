<template>
  <button
    type="button"
    data-bs-toggle="modal"
    data-bs-target="#exampleModal"
    @click.stop="openAddCommentModal($event,item)"
    class="btn btn-link btn-sm text-primary p-0 ms-2"
    v-show="order.latestPaymentStatus === '已付款'"
  >
    <i class="bi">
      <font-awesome-icon v-bind:icon="['fas', 'fa-pen-fancy']" />
    </i>
  </button>
</template>

<script>
import axios from 'axios'
import StarRating from 'vue-star-rating'
import LoadingVue from 'vue3-loading-overlay'
import { mapActions } from 'pinia'
import { cartStore } from '@/stores/cartStore'
import { useNotificationStore } from '@/stores/notificationStore.js'


export default {
  components: {
    'star-rating': StarRating,
    LoadingVue,
  },
  props: {
    item: {
      type: Object, // menu 應該是一個物件
      required: true, // 如果 menu 是必需的
    },
    order: {
    type: Object,
    required: true,
  },
  },

  emits: ['open-add-commentmodal'],

  data() {
    return {

    }
  },
  methods: {
    async openAddCommentModal($event,item) {


      this.$emit('open-add-commentmodal', $event,item)
    },
  },
  created() {
  },
}
</script>

<style scoped></style>
