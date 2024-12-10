<template>

<div class="col-md-3 col-sm-6">
    <div class="card"><!--第1道菜-->            
        <img :src="menu.productImgUrl" class="card-img-top" alt="牛排.jpg" />
            <div class="card-body">
            <h5 class="card-title">{{menu.productName}}</h5>

            <p class="card-text">{{menu.productDescribe}}</p>

            <div class="mb-3">
                <span>${{menu.productPrice}}</span> 
            </div>
                    
            <div class="input-group mb-3">
                <button class="btn btn-outline-secondary btn-border-none" type="button" id="button-addon1" @click="minusOne"><i class="bi bi-dash"></i></button>
                <input type="number" class="form-control  text-center" placeholder="" aria-label="Example number with button addon" aria-describedby="button-addon1"  min="0">
                <button class="btn btn-outline-secondary btn-border-none" type="button" id="button-addon2" @click="addOne"><i class="bi bi-plus-lg"></i></button>
            </div>

            <button class= "mb-3 btn btn-outline-primary w-100">
                <i class="bi bi-cart-plus"></i>加入購物車
            </button>

            <button class="btn btn-outline-primary w-100">
                <i class="bi bi-chat-left-text"></i>觀看評論
            </button>
            </div>
    </div>   
</div>
</template>


<script>
export default{
    props: ['menu']
}


</script>


<style scoped></style>

