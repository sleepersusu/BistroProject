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
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <div class="lottery-container">
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
import { campaignPrizeStore } from '@/stores/campaignPrizeStore'
import { lotteryStore } from '@/stores/lotteryStore'
import { storeToRefs } from 'pinia'
import { useLuckyCanvas } from '@/mixins/luckyCanvasMixin'
import { useModal } from '@/mixins/modalMixin'

const { buttons, blocks, activeStyle, myLucky } = useLuckyCanvas()
const { modalRef, showModal, hideModal } = useModal()

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
  await window.Swal.fire({
    title: `${prize.fonts[0].text === '銘謝惠顧' ? '再接再厲' : '恭喜中獎!'}`,
    text: `獲得：${prize.fonts[0].text}！`,
    imageUrl: `${prize.imgs[0].src}`,
    imageWidth: 200,
    imageHeight: 200,
    imageAlt: `${prize.fonts[0].text}`,
    confirmButtonColor: 'black',
    confirmButtonText: '確定',
  })
  emits('update-chance')
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
}

:deep(.lucky-grid) {
  border-radius: 10px;
  overflow: hidden;
}

.modal-title {
  font-family: 'Dancing Script', cursive !important;
}
</style>
