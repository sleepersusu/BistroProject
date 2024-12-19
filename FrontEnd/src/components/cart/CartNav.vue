<template>
  <div class="container">
    <div class="shopping-cart" v-show="showCart">
      <div class="shopping-cart-header">
        <i class="fa fa-shopping-cart cart-icon"></i><span class="badge">{{ cartItems.length }}</span>
        <div class="shopping-cart-total">
          <span class="lighter-text">Total:</span>
          <span class="main-color-text">${{ totalPrice.toFixed(2) }}</span>
        </div>
      </div> <!--end shopping-cart-header -->

      <ul class="shopping-cart-items">
        <li class="clearfix" v-for="item in cartItems" :key="item.id">
          <img :src="item.image" :alt="item.name" />
          <span class="item-name">{{ item.name }}</span>
          <span class="item-price">${{ item.price }}</span>
          <span class="item-quantity">Quantity: {{ item.quantity }}</span>
        </li>
      </ul>

      <a href="#" class="button">Checkout</a>
    </div> <!--end shopping-cart -->
  </div> <!--end container -->
</template>

<script>
export default {
  data() {
    return {
      showCart: false, // 控制购物车显示状态
      cartItems: [
        {
          id: 1,
          name: "Sony DSC-RX100M III",
          price: 849.99,
          quantity: 1,
          image: "https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/cart-item1.jpg",
        },
        {
          id: 2,
          name: "KS Automatic Mechanic...",
          price: 1249.99,
          quantity: 1,
          image: "https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/cart-item2.jpg",
        },
        {
          id: 3,
          name: "Kindle Glare-Free To...",
          price: 129.99,
          quantity: 1,
          image: "https://s3-us-west-2.amazonaws.com/s.cdpn.io/195612/cart-item3.jpg",
        },
      ],
    };
  },
  computed: {
    totalPrice() {
      return this.cartItems.reduce((total, item) => total + item.price * item.quantity, 0);
    },
  },
  methods: {
    toggleCart() {
      this.showCart = !this.showCart;
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Lato:300,400,700");
@import url("https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css");

body {
  font: 14px/22px "Lato", Arial, sans-serif;
  background: #6394F8;
}

.lighter-text {
  color: #ABB0BE;
}

.main-color-text {
  color: #6394F8;
}

nav {
  padding: 20px 0 40px 0;
  background: #F8F8F8;
  font-size: 16px;
}

nav .navbar-left {
  float: left;
}

nav .navbar-right {
  float: right;
}

nav ul li {
  display: inline;
  padding-left: 20px;
}

nav ul li a {
  color: #777777;
  text-decoration: none;
}

nav ul li a:hover {
  color: black;
}

.container {
  margin: auto;
  width: 80%;
}

.badge {
  background-color: #6394F8;
  border-radius: 10px;
  color: white;
  display: inline-block;
  font-size: 12px;
  line-height: 1;
  padding: 3px 7px;
  text-align: center;
  vertical-align: middle;
  white-space: nowrap;
}

.shopping-cart {
  margin: 20px 0;
  float: right;
  background: white;
  width: 320px;
  position: relative;
  border-radius: 3px;
  padding: 20px;
}

.shopping-cart-header {
  border-bottom: 1px solid #E8E8E8;
  padding-bottom: 15px;
}

.shopping-cart-header .shopping-cart-total {
  float: right;
}

.shopping-cart-items {
  padding-top: 20px;
}

.shopping-cart-items li {
  margin-bottom: 18px;
}

.shopping-cart-items img {
  float: left;
  margin-right: 12px;
}

.shopping-cart-items .item-name {
  display: block;
  padding-top: 10px;
  font-size: 16px;
}

.shopping-cart-items .item-price {
  color: #6394F8;
  margin-right: 8px;
}

.shopping-cart-items .item-quantity {
  color: #ABB0BE;
}

.shopping-cart::after {
  bottom: 100%;
  left: 89%;
  border: solid transparent;
  content: " ";
  height: 0;
  width: 0;
  position: absolute;
  pointer-events: none;
  border-bottom-color: white;
  border-width: 8px;
  margin-left: -8px;
}

.cart-icon {
  color: #515783;
  font-size: 24px;
  margin-right: 7px;
  float: left;
}

.button {
  background: #6394F8;
  color: white;
  text-align: center;
  padding: 12px;
  text-decoration: none;
  display: block;
  border-radius: 3px;
  font-size: 16px;
  margin: 25px 0 15px 0;
}

.button:hover {
  background: #557ed1;
}

.clearfix::after {
  content: "";
  display: table;
  clear: both;
}
</style>

<!--有意義嗎???? 好像沒有，因為手機打開這種navbar感覺超卡畫面-->
