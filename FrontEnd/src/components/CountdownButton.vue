<template>
  <button
    type="button"
    class="btn btn-primary w-100 rounded-0 rounded-bottom"
    :disabled="!isActive || !user.memberId"
    @click="$emit('handle-draw')"
  >
    <div v-if="status === 'NOT_STARTED'">
      <div class="d-flex justify-content-center align-items-center">
        <div class="timer-block mx-1">
          <span class="timer-number">{{ timeLeft.days }}</span>
          <small>天</small>
        </div>
        <div class="timer-block mx-1">
          <span class="timer-number">{{ timeLeft.hours }}</span>
          <small>時</small>
        </div>
        <div class="timer-block mx-1">
          <span class="timer-number">{{ timeLeft.minutes }}</span>
          <small>分</small>
        </div>
        <div class="timer-block mx-1">
          <span class="timer-number">{{ timeLeft.seconds }}</span>
          <small>秒</small>
        </div>
      </div>
      <span v-if="count > 0" class="badge bg-danger position-absolute top-0 end-0 m-2">
        {{ count }}
      </span>
    </div>
    <span v-else>
      {{
        !user.memberId
          ? '請先登入會員'
          : status === 'EXPIRED'
            ? '活動已結束'
            : status === 'IN_PROGRESS'
              ? '立即抽獎'
              : ''
      }}
      <span v-if="count > 0 && props.isActive" class="badge bg-danger">
        {{ count }}
      </span>
    </span>
  </button>
</template>

<script setup>
import { onMounted, onUnmounted, ref, defineProps } from 'vue'
import { useUserStore } from '@/stores/userStore'
const user = useUserStore()

const props = defineProps({
  startDate: {
    type: String,
    required: true,
  },
  status: {
    type: String,
    required: true,
  },
  isActive: {
    type: Boolean,
    required: true,
  },
  count: {
    type: Number,
    default: 0,
  },
})

const timeLeft = ref({ days: 0, hours: 0, minutes: 0, seconds: 0 })
let timer = null

const calculateTimeLeft = () => {
  const now = new Date().getTime()
  const end = new Date(props.startDate).getTime()
  const diff = end - now

  if (diff > 0) {
    timeLeft.value = {
      days: Math.floor(diff / (1000 * 60 * 60 * 24)),
      hours: Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)),
      minutes: Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60)),
      seconds: Math.floor((diff % (1000 * 60)) / 1000),
    }
  }
}

onMounted(() => {
  if (props.status === 'NOT_STARTED') {
    calculateTimeLeft()
    timer = setInterval(calculateTimeLeft, 1000)
  }
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.timer-block {
  text-align: center;
  padding: 0.2rem;
  min-width: 40px;
}

.timer-number {
  font-size: 1.1rem;
  font-weight: bold;
}

small {
  display: block;
  font-size: 0.7rem;
}

button {
  position: relative;
  min-height: 60px;
}
</style>
