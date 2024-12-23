<template>
  <div v-if="showRibbon" class="ribbon">
    <span :class="ribbonClass">{{ ribbonText }}</span>
  </div>
</template>

<script setup>
import { defineProps, computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    required: true,
  },
})

const showRibbon = computed(() => {
  return ['IN_PROGRESS', 'NOT_STARTED'].includes(props.status)
})

const ribbonText = computed(() => {
  const textFilter = {
    IN_PROGRESS: '進行中',
    NOT_STARTED: '即將到來',
  }
  return textFilter[props.status]
})

const ribbonClass = computed(() => {
  const classFilter = {
    IN_PROGRESS: 'in-progress',
    NOT_STARTED: 'coming-soon',
  }
  return classFilter[props.status]
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
  border-right: 3px solid transparent;
  border-bottom: 3px solid transparent;
}

.ribbon span::after {
  content: '';
  position: absolute;
  right: 0px;
  top: 100%;
  z-index: -1;
  border-left: 3px solid transparent;
  border-bottom: 3px solid transparent;
}

.in-progress {
  background: #dc3545;
  background: linear-gradient(#dc3545 0%, #dc3545 100%);
}

.in-progress::before {
  border-left: 3px solid #8f0808;
  border-top: 3px solid #8f0808;
}

.in-progress::after {
  border-right: 3px solid #8f0808;
  border-top: 3px solid #8f0808;
}

.coming-soon {
  background: #007bff;
  background: linear-gradient(#007bff 0%, #0056b3 100%);
}

.coming-soon::before {
  border-left: 3px solid #004085;
  border-top: 3px solid #004085;
}

.coming-soon::after {
  border-right: 3px solid #004085;
  border-top: 3px solid #004085;
}
</style>
