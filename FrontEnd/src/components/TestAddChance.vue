<template>
  <isLoading :active="status.isLoading"></isLoading>
  <button class="btn btn-primary" @click.prevent="sendOrder(4, 5000)">送出訂單</button>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { statusStore } from '@/stores/statusStore'
import { useUserStore } from '@/stores/userStore'
import { lotteryStore } from '@/stores/lotteryStore'

const status = statusStore()
const user = useUserStore()
const lottery = lotteryStore()
const router = useRouter()

const sendOrder = async (campaignId, orderAmount) => {
  status.start()
  const res = await lottery.addChance(campaignId, user.memberId, orderAmount)
  if (res?.status === 200) {
    showAlert(res.data.newChances)
  }
  status.finish()
}

const showAlert = (chances) => {
  Swal.fire({
    toast: true,
    position: 'bottom-end',
    icon: 'success',
    iconColor: 'black',
    title: `恭喜！您已獲得${chances}次抽獎機會`,
    text: '點擊立即前往抽獎',
    showConfirmButton: false,
    timer: 8000,
    timerProgressBar: true,
    didOpen: (toast) => {
      toast.addEventListener('mouseenter', Swal.stopTimer)
      toast.addEventListener('mouseleave', Swal.resumeTimer)
      toast.addEventListener('click', () => {
        router.push('/campaign')
      })
      toast.style.cursor = 'pointer'
    },
  })
}
</script>
