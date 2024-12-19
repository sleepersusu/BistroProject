<template>

<nav aria-label="Page navigation example" >
  <ul class="pagination justify-content-center">
    <li class="page-item"><a class="page-link" href="#">Previous</a></li>
    <li @click="clickHandler(page)" class="page-item" v-for="page in total"><a class="page-link" href="#">{{ page }}</a></li>
    <li class="page-item"><a class="page-link" href="#">Next</a></li>
  </ul>
</nav>

</template>


<script>


</script>


<style scoped></style>
