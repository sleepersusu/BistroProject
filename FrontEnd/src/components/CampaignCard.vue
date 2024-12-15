<template>
  <div class="card" style="cursor: pointer" @click.prevent.stop="openCampaign">
    <div class="overflow-hidden">
      <img :src="props.campaign.imageUrl" class="card-img-top" alt="活動圖片" />
    </div>
    <div class="card-body">
      <span class="badge bg-primary mb-2">{{ props.campaign.campaignType }}</span>
      <h5 class="card-title">
        {{ props.campaign.campaignTitle }}
      </h5>
      <p class="card-text text-truncate">
        {{ props.campaign.campaignDescription }}
      </p>
      <ul class="list-unstyled">
        <li>💰 消費門檻：${{ props.campaign.minOrderAmount }}</li>
        <li class="text-truncate">⏰ 截止日期：{{ formatDate(props.campaign.endDate) }}</li>
        <li class="text-truncate">📢 {{ props.campaign.note }}</li>
      </ul>
      <button
        type="button"
        class="btn btn-primary w-100"
        :disabled="!props.campaign.active"
        @click.prevent.stop="startDraw"
      >
        {{
          campaignStatus === 'IN_PROGRESS'
            ? '立即抽獎'
            : campaignStatus === 'EXPIRED'
              ? '活動已結束'
              : '活動未開始'
        }}
        <span v-if="count > 0 && props.campaign.active" class="badge bg-danger">
          {{ count }}
        </span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { defineProps, computed, defineEmits } from 'vue'
import { lotteryStore } from '@/stores/lotteryStore'
const lottery = lotteryStore()

const count = computed(() => lottery.chanceCount(props.campaign.id))
const props = defineProps({
  campaign: {
    type: {},
    required: true,
  },
})
const { campaignStatus } = props.campaign

const emits = defineEmits(['open-drawmodal', 'open-detailmodal'])
const startDraw = () => emits('open-drawmodal', props.campaign.id)
const openCampaign = () => emits('open-detailmodal', props.campaign)

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-TW')
}
</script>

<style scoped>
.card-img-top {
  height: 300px;
  width: 100%;
  object-fit: cover;
  overflow: hidden;
  transition: all 0.5s ease;
}
.card-img-top:hover {
  transform: scale(1.2);
}
</style>
