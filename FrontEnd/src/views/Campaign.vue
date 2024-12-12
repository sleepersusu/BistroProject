<template>
  <div class="d-flex justify-content-center">
    <button
      type="button"
      class="btn btn-primary"
      data-bs-toggle="modal"
      data-bs-target="#exampleModal"
    >
      Launch demo modal
    </button>
    <LuckyDraw :id="1"></LuckyDraw>
  </div>
</template>

<script setup>
import LuckyDraw from '@/components/LuckyDraw.vue'
</script>
