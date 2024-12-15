<template>

  <nav class="navbar navbar-main navbar-expand-lg px-0 mx-3 shadow-none border-radius-xl" id="navbarBlur" data-scroll="true">
    <div class="container-fluid py-1 px-3">
      <nav aria-label="breadcrumb">
        <ol class="breadcrumb bg-transparent mb-0 pb-0 pt-1 px-0 me-sm-6 me-5">
          <li class="breadcrumb-item text-sm text-dark active" aria-current="page" >{{page1}}</li>
          <li class="breadcrumb-item text-sm"><a class="opacity-5 text-dark" href="javascript:">{{page2}}</a></li>
        </ol>
      </nav>
    </div>
  </nav>

</template>

<script>
import Home from '@/views/Home.vue'

export default {
  computed: {
    Home() {
      return Home
    }
  },
  props:["page1","page2"]
}

</script>
