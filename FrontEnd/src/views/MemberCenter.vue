<template>
  <div class="container-fluid">
    <div class="row">
      <aside class="col-md-3 col-lg-2 p-3 bg-light">
        <MembercenterNav class="d-none d-lg-block"></MembercenterNav>
      </aside>
      <main class="col-md-9 col-lg-10 p-4">
        <router-view></router-view>
      </main>
    </div>
  </div>
</template>

<script>
import MembercenterNav from '@/components/memberCenter/membercenterNav.vue'

export default {
  data() {
    return {}
  },
  components: {
    MembercenterNav,
  },
  methods: {},
  computed: {},
  watch: {},
  created() {},
}
</script>
<style scoped></style>
