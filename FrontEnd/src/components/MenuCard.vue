<template>
  <loading :active="isLoading"></loading>

    <div class="card" v-if="!isLoading">
      
      <img :src="imageSrc" class="card-img-top img-fixed" :alt="menu.productImgUrl" />
      <div class="card-body">
        <div class="d-flex justify-content-between">
          <span class="card-title">{{ menu.productName }} </span><span class="fs-6"> 剩餘:{{menu.productCount }}份</span>
        </div>
        <p class="card-text" style="white-space: nowrap; text-overflow:ellipsis; overflow: hidden;">{{ menu.productDescribe }}</p>

        <div class="mb-3">
          <span class="fs-3">${{ menu.productPrice }}</span>
          
        </div>

        <div class="input-group mb-3">
          <button
            class="btn btn-outline-secondary btn-border-none"
            type="button"
            id="button-addon1"
            v-on:click.prevent="minusOne"
          >
            <i class="bi bi-dash"></i>
          </button>
          <input
            type="number"
            class="form-control text-center"
            v-model=count
            placeholder=""
            aria-label="Example number with button addon"
            aria-describedby="button-addon1"
            min="0" :max="menu.productCount"
            id="count"
          />
          <button
            class="btn btn-outline-secondary btn-border-none"
            type="button"
            id="button-addon2"
            v-on:click.prevent="addOne"
            
          >
            <i class="bi bi-plus-lg"></i>
          </button>
        </div>

        <button class="mb-3 btn btn-outline-primary w-100">
          <i class="bi bi-cart-plus"></i>加入購物車
        </button>

        <button class="btn btn-outline-primary w-100">
          <i class="bi bi-chat-left-text"></i>觀看評論
        </button>
      </div>
    </div>





</template>



  




<script>
import axios from "axios";

export default {
props: {
    menu: {
        type: Object, // menu 應該是一個物件
        required: true, // 如果 menu 是必需的
        },
    },
    data() { 
        return {                 
        imageSrc: '',
        isLoading:false,
        count:0,
        } 
    },
methods: {
    loadPicture(id){
    this.isLoading = true;
    
    let API_URL = `${import.meta.env.VITE_API}/api/${id}/menuphoto`;

    axios.get(API_URL,{ responseType: 'blob' })
        .then(async(response) => { 

        let url = URL.createObjectURL(response.data);
        this.imageSrc = url;
        this.isLoading = false;
        this.$emit("image-loaded", this.imageSrc);
        })
        .catch((error) => { 
                console.error("Error fetching menus:", error); 
                this.isLoading = false;
            })
        
    },

    minusOne(){
      if(this.count>0){
        this.count-=1;
        this.$emit("update-count", this.count); 
      }   
      
    },

    addOne(){
      if(this.count<this.menu.productCount){
        this.count+=1;
        this.$emit("update-count", this.count); 
      }else{
        this.count=this.menu.productCount
        this.$emit("update-count", this.count); 
        alert("已達上限")
      }      
              
    }
  },
  created(){
    this.loadPicture(this.menu.id)
  }
};


</script>

<style scoped>
  .img-fixed{
    width: 100%; /* 讓圖片寬度符合卡片寬度 */
    height: 200px; /* 固定高度 */
    object-fit: cover; /* 圖片裁切，確保不變形 */
    border-radius: 5px; /* 可選：讓圖片有圓角效果 */    
}
  .card{
    margin-bottom: 5px;
  }

  
</style>
