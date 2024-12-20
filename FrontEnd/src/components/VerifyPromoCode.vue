<template>
  <div>

    <div class="card mt-4">
      <div class="card-body">
        <h5 class="card-title mb-3">Apply Promo Code</h5>
          <div class="input-group mb-3">
            <input type="text" class="form-control" placeholder="Enter promo code" v-model="inputPromoCode">
            <button class="btn btn-outline-secondary" type="button" @click="verifyPromoCode(inputPromoCode)">Apply</button>
          </div>
      </div>
    </div>

  </div>
</template>

<script>
import { pointStore } from '@/stores/pointStore';
import { mapState, mapActions } from 'pinia';

  export default {
  data() {
    return {
      inputPromoCode:""
    }
  },
  methods: {
   ...mapActions(pointStore,["verifyPromoCode"])
  },
  computed: {
    ...mapState(pointStore, ["promoCode"])
  },
  watch: {},
  created() {},
}
</script>

<style scoped>

</style>
