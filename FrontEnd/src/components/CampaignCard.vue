<template>
  <div class="card position-relative">
    <CampaignRibbon :status="campaignStatus"></CampaignRibbon>
    <div class="overview" style="cursor: pointer" @click.prevent.stop="openCampaign">
      <div class="overflow-hidden">
        <img :src="props.campaign.imageUrl" class="card-img-top" alt="活動圖片" />
      </div>
      <div class="card-body py-2">
        <span class="badge bg-primary mb-2">{{ props.campaign.campaignType }}</span>
        <h5 class="card-title">
          {{ props.campaign.campaignTitle }}
        </h5>
        <p class="card-text text-truncate">
          {{ props.campaign.campaignDescription }}
        </p>
        <ul class="list-unstyled">
          <li>💰 消費門檻：${{ props.campaign.minOrderAmount }}</li>
          <li class="text-truncate" v-if="campaignStatus === 'NOT_STARTED'">
            ⏰ 開始日期：{{ formatDate(props.campaign.startDate) }}
          </li>
          <li class="text-truncate" v-else>
            ⏰ 截止日期：{{ formatDate(props.campaign.endDate) }}
          </li>
          <li class="text-truncate">📢 {{ props.campaign.note }}</li>
        </ul>
      </div>
    </div>
    <CountdownButton
      :start-Date="props.campaign.startDate"
      :status="campaignStatus"
      :is-active="props.campaign.active"
      :count="count"
      @handle-draw="startDraw"
    />
  </div>
</template>

<script setup>
import { defineProps, computed, defineEmits } from 'vue'
import { lotteryStore } from '@/stores/lotteryStore'
import { utils } from '@/mixins/utils'
import CountdownButton from './CountdownButton.vue'
import CampaignRibbon from './CampaignRibbon.vue'

const { formatDate } = utils()
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

.card {
  overflow: hidden;
}
</style>
