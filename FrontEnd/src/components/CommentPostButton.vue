<template>
  <button
    type="button"
    @click.stop="openAddCommentModal($event, item)"
    class="btn btn-link btn-sm text-primary p-0 ms-2"
    v-show="shouldShowButton"

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
      type: Object,
      required: true,
    },
    order: {
      type: Object,
      required: true,
    },
    hidebutton: {
        type: Boolean,
        default: false,
      },
  },

  emits: ['open-add-commentmodal'],

  data() {
    return {}
  },
  methods: {
    async openAddCommentModal($event, item) {
      this.$emit('open-add-commentmodal', $event, item)
    },
  },
  computed: {
    shouldShowButton() {
      return this.order.latestPaymentStatus === '已付款' && !this.hidebutton
    }
  },
}
</script>

<style scoped></style>
