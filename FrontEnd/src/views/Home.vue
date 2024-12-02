<template>
  <Nav></Nav>
  <router-view></router-view>
  <Footer></Footer>
</template>

<script>
import Footer from '@/components/Footer.vue'
import Nav from '@/components/Nav.vue'

export default {
  data() {
    return {}
  },
  components: {
    Nav,
    Footer,
  },
}
</script>
