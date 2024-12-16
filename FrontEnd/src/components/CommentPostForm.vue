<template>
  <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">新增評論</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form>
        


        <star-rating :show-rating="false" @update:rating="rating = $event"></star-rating>
        <div style="margin-top:10px;font-weight:bold;">{{currentRatingText}}</div>


        
        </form>
      </div>
      <div class="modal-footer">
        <button type="reset" class="btn btn-secondary" data-bs-dismiss="modal">重設</button>
        <button type="button" class="btn btn-primary">確定</button>
      </div>

    </div>
  </div>
</div>


</template>

<script>

import axios from 'axios'
import StarRating from 'vue-star-rating'

import LoadingVue from 'vue3-loading-overlay'
export default {
  components: {
    'star-rating': StarRating,
    LoadingVue,
  },

  props: {
    menu: {
      type: Object, // menu 應該是一個物件
      required: true, // 如果 menu 是必需的
    },
  },


  data() {
    return {
      
    }
  },
  methods: {

    
  },
  
}
</script>

<style scoped>

</style>

