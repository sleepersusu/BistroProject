<template>
  <Nav></Nav>
  <router-view></router-view>
  <Footer></Footer>
  <CampaignLineLink></CampaignLineLink>
</template>

<script>
import Footer from '@/components/Footer.vue'
import Nav from '@/components/Nav.vue'
import CampaignLineLink from '@/components/CampaignLineLink.vue'

export default {
  data() {
    return {}
  },
  components: {
    Nav,
    Footer,
    CampaignLineLink,
  },
}
</script>
