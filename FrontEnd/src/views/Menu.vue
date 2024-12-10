<template>

<div class="container">
    <div class=" d-flex justify-content-around">
      
        <button class="btn btn-outline-success" v-on:click.prevent="clickCategory('開胃菜')">開胃菜</button>
      
          
              
        <button class="btn btn-outline-success"  v-on:click.prevent="clickCategory('主菜')">主菜</button>
      

      
        <button class="btn btn-outline-success"  v-on:click.prevent="clickCategory('飲品')">飲品</button>
      

      
        <button class="btn btn-outline-success" v-on:click.prevent="clickCategory('甜點')">甜點</button>
    
    </div>
</div>

<div class="row mt-3" v-for="menu in menus" :key="menu.ID" :menu="menu">
  <MenuCard></MenuCard>
</div>


</template>


<script>
import MenuCard from '@/components/MenuCard.vue';


export default {
  components:{
    MenuCard
  },

  data() {
    return {
      menus:[]
      



    }
  },
  methods: {
    clickCategory(productCategory){

      
      let API_URL = `http://localhost:8085/api/menu/${productCategory}`;

      this.axios.get(API_URL).then((response)=>{
        alert(response.data)
        console.log('API response:', response.data)
        this.menus=response.data;
      }).catch((error) => {
        console.error("Error fetching menus:", error);
        // 顯示錯誤提示給用戶（例如使用 Vue 的狀態管理或 UI 組件庫）
        alert("無法獲取菜單，請稍後再試！");
      });
  }
},


  
  computed: {},
  watch: {},
  created() {},
}
</script>
