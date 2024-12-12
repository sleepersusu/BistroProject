<template>
  <div
    class="modal fade"
    id="exampleModal"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content bg-dark">
        <div class="modal-header border-secondary">
          <h3 class="modal-title text-secondary fw-bold text-center w-100" id="exampleModalLabel">
            命運九宮格
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
        </div>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">關閉</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onUnmounted, defineProps } from 'vue'
import { campaignPrizeStore } from '@/stores/campaignPrizeStore'
import { storeToRefs } from 'pinia'

const prizeStore = campaignPrizeStore()

const { prizes } = storeToRefs(prizeStore)
const { getPrizesByCampaign, clearPrizeImages } = prizeStore

const props = defineProps({
  id: {
    type: Number,
    required: true,
  },
})

getPrizesByCampaign(props.id)

onUnmounted(() => {
  clearPrizeImages()
})

const myLucky = ref(null)
const buttons = ref([
  {
    x: 1,
    y: 1,
    background: 'salmon',
    fonts: [
      {
        text: '開始抽獎',
        fontSize: '16px',
        fontWeight: 'bold',
        fontColor: 'MistyRose',
        top: '40px',
      },
    ],
  },
])
const blocks = ref([
  { padding: '15px', background: '#eed9c4' },
  { padding: '15px', background: 'black' },
])
const activeStyle = {
  background: '#fdd156',
  shadow: '0 0 10px rgba(253, 209, 86, 0.5)',
}

const startCallBack = () => {
  const index = Math.floor(Math.random() * 8)
  myLucky.value.play()
  setTimeout(() => {
    myLucky.value.stop(index)
  }, 3000)
}

const endCallBack = (prize) => {
  window.Swal.fire({
    title: '恭喜中獎!',
    text: `獲得：${prize.fonts[0].text}！`,
  })
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
</style>
