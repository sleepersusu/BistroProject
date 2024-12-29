<template>
  <div
    class="modal fade"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
    ref="modalRef"
  >
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content bg-primary">
        <div class="modal-header border-accent">
          <h3
            class="modal-title text-accent fw-bold text-center w-100 display-6"
            id="exampleModalLabel"
          >
            Lucky Draw
          </h3>
        </div>
        <div class="modal-body">
          <div class="lottery-container" :class="{ disabled: isDisabled }">
            <lucky-grid
              ref="myLucky"
              width="400px"
              height="400px"
              :prizes="prizes"
              :buttons="buttons"
              :blocks="blocks"
              :active-style="activeStyle"
              @start="startCallBack"
              @end="endCallBack"
            />
          </div>
          <p class="text-center text-light">
            剩餘抽獎次數：<strong class="mx-2 text-accent">{{ count }}</strong>
          </p>
        </div>
        <button type="button" class="btn btn-dark text-light rounded-0" data-bs-dismiss="modal">
          關閉
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onUnmounted, computed, defineExpose, ref } from 'vue'
import { useRouter } from 'vue-router'
import { campaignPrizeStore } from '@/stores/campaignPrizeStore'
import { lotteryStore } from '@/stores/lotteryStore'
import { storeToRefs } from 'pinia'
import { useLuckyCanvas } from '@/mixins/luckyCanvasMixin'
import { useModal } from '@/mixins/modalMixin'
import confetti from 'canvas-confetti'
window.confetti = confetti

const { buttons, blocks, activeStyle, myLucky } = useLuckyCanvas()
const { modalRef, showModal, hideModal } = useModal()

const router = useRouter()
const prizeStore = campaignPrizeStore()
const lottery = lotteryStore()
const { prizes } = storeToRefs(prizeStore)
const { clearPrizeImages } = prizeStore
const { chance, winner } = storeToRefs(lottery)
const { drawPrize } = lottery

const currentCampaignId = ref(null)

const setCurrentCampaignId = (id) => {
  currentCampaignId.value = id
}

const count = computed(() => lottery.chanceCount(currentCampaignId.value))
const isDisabled = computed(() => prizes.value.every((prize) => prize.fonts[1].text === '剩餘: 0'))

defineExpose({
  showModal,
  hideModal,
  setCurrentCampaignId,
})

onUnmounted(() => {
  clearPrizeImages()
})

const startCallBack = async () => {
  if (count.value < 1) {
    window.Swal.fire({
      title: '你已經沒有抽獎機會',
      icon: 'warning',
      confirmButtonColor: 'black',
      confirmButtonText: '確定',
    })
    return
  }

  myLucky.value.play()

  try {
    await handleDraw()

    const prizeIndex = prizes.value.findIndex(
      (prize) => prize.fonts[0].text === winner.value?.prize?.prizeName,
    )
    if (prizeIndex !== -1) {
      setTimeout(() => {
        myLucky.value.stop(prizeIndex)
      }, 3000)
    }
  } catch (e) {
    if (e.response?.data?.message) {
      window.Swal.fire({
        title: '錯誤',
        text: e.response.data.message,
        icon: 'error',
        confirmButtonColor: 'black',
        confirmButtonText: '確定',
      })
    }
  }
}

const emits = defineEmits(['update-chance'])
const endCallBack = async (prize) => {
  const currentQuantity = Number(prize.fonts[1].text.split(': ')[1])
  prize.fonts[1].text = `剩餘: ${currentQuantity - 1}`

  const unluckyMessage =
    winner.value.unluckyCount > 0
      ? `<span style="color: #ffd700; font-size: 0.9em;">
         連續未中獎 ${winner.value.unluckyCount} 次<br>
         下次抽獎機率提升 ${winner.value.unluckyBonus}%
       </span>`
      : ''

  const timeBonusMessage = winner.value.isBonusTime
    ? `<span style="color: #ff69b4; font-size: 0.9em;">
         <br>現在是頭獎加成時段 ! 頭獎機率提升至5%
       </span>`
    : ''

  await window.Swal.fire({
    title: `${prize.fonts[0].text === '銘謝惠顧' ? '再接再厲' : '恭喜中獎!'}`,
    html: `${
      prize.fonts[0].text === '銘謝惠顧'
        ? `${unluckyMessage}${timeBonusMessage}`
        : `獲得：<span style="color: #B8860B; font-weight: bold; font-size: 1.2em;">${prize.fonts[0].text}</span>！<br>請至中獎紀錄填寫配送資訊，以利我們寄送獎品。`
    }`,
    imageUrl: `${prize.imgs[0].src}`,
    imageWidth: 200,
    imageHeight: 200,
    imageAlt: `${prize.fonts[0].text}`,
    showCancelButton: true,
    confirmButtonColor: '#B8860B',
    cancelButtonColor: '#666',
    confirmButtonText: '填寫配送資訊',
    cancelButtonText: '關閉',
    background: '#000',
    color: '#fff',
    backdrop: `rgba(0,0,0,0.8)`,
    showClass: {
      popup: 'animate__animated animate__jackInTheBox',
    },
    hideClass: {
      popup: 'animate__animated animate__rollOut',
    },
    didOpen: () => {
      if (prize.fonts[0].text !== '銘謝惠顧') {
        const defaults = { startVelocity: 30, spread: 360, ticks: 60, zIndex: 9999 }
        confetti({
          ...defaults,
          particleCount: 50,
          origin: { x: 0.5, y: 0.3 },
        })
        setTimeout(() => {
          confetti({
            ...defaults,
            particleCount: 30,
            origin: { x: 0.3, y: 0.5 },
          })
          confetti({
            ...defaults,
            particleCount: 30,
            origin: { x: 0.7, y: 0.5 },
          })
        }, 250)
      }
    },
  }).then((result) => {
    if (result.isConfirmed) {
      hideModal()
      router.push('/membercenter/lotteryresult')
    }
  })
  emits('update-chance', currentCampaignId.value)
}

const handleDraw = async () => {
  const chanceId = chance.value?.[currentCampaignId.value]?.id
  if (!chanceId) {
    console.error('找不到抽獎機會 ID')
    return
  }
  if (count.value <= 0) {
    console.error('沒有剩餘抽獎次數')
    return
  }
  return await drawPrize(chanceId)
}
</script>

<style scoped>
.lottery-container {
  width: fit-content;
  margin: 20px auto;
  padding: 20px;
  background: black;
  border-radius: 15px;
  box-shadow:
    5px 5px 10px #fdd156,
    -5px -5px 10px #fdd156;
  cursor: pointer;
}

:deep(.lucky-grid) {
  border-radius: 10px;
  overflow: hidden;
}

.modal-title {
  font-family: 'Dancing Script', cursive !important;
}

.lottery-container.disabled {
  opacity: 0.6;
  pointer-events: none;
  cursor: not-allowed;
}
</style>
