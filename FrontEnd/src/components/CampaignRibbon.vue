<template>
  <div v-if="status === 'IN_PROGRESS'" class="ribbon">
    <span>進行中</span>
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
const props = defineProps({
  status: {
    type: String,
    required: true,
  },
})
</script>

<style scoped>
.ribbon {
  position: absolute;
  left: -5px;
  top: -5px;
  z-index: 1;
  overflow: hidden;
  width: 93px;
  height: 93px;
  text-align: right;
}

.ribbon span {
  font-size: 0.8rem;
  font-weight: bold;
  color: #fff;
  text-align: center;
  line-height: 25px;
  transform: rotate(-45deg);
  width: 120px;
  display: block;
  background: #dc3545;
  background: linear-gradient(#dc3545 0%, #dc3545 100%);
  box-shadow: 0 3px 10px -5px rgba(0, 0, 0, 1);
  position: absolute;
  top: 22px;
  left: -29px;
}

.ribbon span::before {
  content: '';
  position: absolute;
  left: 0px;
  top: 100%;
  z-index: -1;
  border-left: 3px solid #8f0808;
  border-right: 3px solid transparent;
  border-bottom: 3px solid transparent;
  border-top: 3px solid #8f0808;
}

.ribbon span::after {
  content: '';
  position: absolute;
  right: 0px;
  top: 100%;
  z-index: -1;
  border-left: 3px solid transparent;
  border-right: 3px solid #8f0808;
  border-bottom: 3px solid transparent;
  border-top: 3px solid #8f0808;
}
</style>
