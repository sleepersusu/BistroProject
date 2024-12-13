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

        <button class="btn btn-outline-primary w-100" v-on:click.prevent="viewComment"
        data-bs-toggle="modal" data-bs-target="#exampleModal">
          <i class="bi bi-chat-left-text"></i>觀看評論
          
        </button>
          <div style="display: flex; justify-content: center; margin: 5px 0 0 0;">
            <star-rating :rating="menu.avgScore" :read-only="true" :increment="0.1" :star-size="20" style="font-size: 15px;"></star-rating>
          </div>
      
      </div>
    </div>




<div class="modal fade modal-dialog-scrollable" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true" v-if="showComment">
  <div class="modal-dialog modal-xl">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-2" id="exampleModalLabel">{{ menu.productName }}</h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>    
      </div>
      <h6 class="modal-header">{{ menu.productDescribe }}</h6>

      <div class="modal-body">

        <div>
          <div>
            <section>
              

            </section><!--頭像-->
            <section></section><!--會員名字-->

          </div>
        </div><!--左側頭像區-->


        <div>
          <section></section><!--星星 分數-->
          <section></section><!--評論文字-->


        </div><!--右側評論區-->




        
      </div>

    
    </div>
  </div>
</div>





</template>



  




<script>
import axios from "axios";
import StarRating from 'vue-star-rating'

export default {
  components:{
      StarRating
  },

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
        showComment:false
        } 
    },
methods: {
    async loadPicture(id){
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

    async minusOne(){
      if(this.count>0){
        this.count-=1;
        this.$emit("update-count", this.count); 
      }   
      
    },

    async addOne(){
      if(this.count<this.menu.productCount){
        this.count+=1;
        this.$emit("update-count", this.count); 
      }else{
        this.count=this.menu.productCount
        this.$emit("update-count", this.count);

        Swal.fire({
          title: "已到達庫存上限",
          text: "請重新選擇數量",
          icon: "error"
        });
      }      
              
    },
    async viewComment(){
      this.showComment=true
      try {
        const api = `${import.meta.env.VITE_API}/api/campaign`
        let res = await this.axios.get(api)
      console.log(res.data)
      }catch (e) {
        console.log(e)
      }
      


    }
  },
  created(){
    this.loadPicture(this.menu.id)
  },
  watched(){
    if(this.count==0){
      document.getElementById("button-addon1").disabled=true
    }else{
      document.getElementById("button-addon1").disabled=false
    }

    if(this.count>this.menu.productCount){
      
      Swal.fire({
          title: "已到達庫存上限",
          text: "請重新選擇數量",
          icon: "error"
        });
      
    }

    
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

  .modal-header,.modal-body{
    color: black;
  }
  
</style>
