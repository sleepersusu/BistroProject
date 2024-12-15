<template>

<div class="container">
  <div class="d-flex justify-content-start">
    <h2>商品菜單</h2>
  </div>
</div>
<div class="container">
    <div class=" d-flex justify-content-around">
      
        <button class="btn btn-outline-success" v-on:click.prevent="clickCategory('開胃菜')">開胃菜</button>

        <button class="btn btn-outline-success"  v-on:click.prevent="clickCategory('主菜')">主菜</button>

        <button class="btn btn-outline-success"  v-on:click.prevent="clickCategory('飲品')">飲品</button>

        <button class="btn btn-outline-success" v-on:click.prevent="clickCategory('甜點')">甜點</button>
    
    </div>
</div>
<div class="container">
  <div class="row mt-3">

    <div class="col-md-3 col-sm-6" v-for="menu in menus" :key="menu.id" >
    <MenuCard :menu="menu" 
      @update-count="updateCount" 
      @add-to-cart="handleAddToCart">
    </MenuCard>
    </div>

  </div>
</div>

</template>


<script >
import MenuCard from '@/components/MenuCard.vue';



export default {
  components:{
    MenuCard
  },

  data() {
    return {
      menus:[],

      totalCount: 0, // 總數量計算
      
    }
  },
  methods: {


    loadAllMenu(){

      let API_URL = `${import.meta.env.VITE_API}/api/menu/`;

      this.axios.get(API_URL)
      .then((response)=>{
        this.menus=response.data
      })

    },





    
    updateCount(newCount) {
      this.totalCount = newCount;
      console.log("Updated count:", this.totalCount);
    },

    handleAddToCart({ id, count }) {
      console.log(`Added to cart: ID=${id}, Count=${count}`);
    },

    // 按分類加載菜單數據
    clickCategory(category) {
      this.isLoading = true;
      const API_URL = `${import.meta.env.VITE_API}/api/menu/${category}`;

      this.axios
        .get(API_URL)
        .then((response) => {
          this.menus = response.data; // 假設 API 返回的數據是菜單數組
        })
        .catch((error) => {
          console.error("Error loading menus:", error);
        })
        .finally(() => {
          this.isLoading = false;
        });
    },
  },


  
  computed: {},
  watch: {},
  created() {
    this.loadAllMenu();



  },
}
</script>
