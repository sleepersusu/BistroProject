<template>

    <div class="card">
      <!--第1道菜-->
      <img :src="imageSrc" class="card-img-top img-fixed" :alt="menu.productImgUrl" />
      <div class="card-body">
        <h5 class="card-title">{{ menu.productName }}</h5>

        <p class="card-text">{{ menu.productDescribe }}</p>

        <div class="mb-3">
          <span>${{ menu.productPrice }}</span>
        </div>

        <div class="input-group mb-3">
          <button
            class="btn btn-outline-secondary btn-border-none"
            type="button"
            id="button-addon1"
            @click="minusOne"
          >
            <i class="bi bi-dash"></i>
          </button>
          <input
            type="number"
            class="form-control text-center"
            value=""
            placeholder="0"
            aria-label="Example number with button addon"
            aria-describedby="button-addon1"
            min="0"
          />
          <button
            class="btn btn-outline-secondary btn-border-none"
            type="button"
            id="button-addon2"
            @click="addOne"
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
        imageSrc: ''
        } 
    },
methods: {
    loadPicture(id){

    let API_URL = `http://localhost:8085/api/${id}/menuphoto`;

    axios.get(API_URL, {responseType: 'blob'})
        .then((response) => { 
            let url = URL.createObjectURL(response.data); 
            this.imageSrc = url; 
        })
        .catch((error) => { 
                console.error("Error fetching menus:", error); 
            }); 
    },
},
created(){
    this.loadPicture(this.menu.id);
}

}
</script>

<style scoped>
    .img-fixed {
    width: 100%; /* 讓圖片寬度符合卡片寬度 */
    height: 200px; /* 固定高度 */
    object-fit: cover; /* 圖片裁切，確保不變形 */
    border-radius: 5px; /* 可選：讓圖片有圓角效果 */
}
</style>
