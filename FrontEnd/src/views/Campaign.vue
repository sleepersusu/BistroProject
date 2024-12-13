<template>
  <Loading :active="isLoading"></Loading>
  <div class="container">
    <div class="row">
      <div class="col-lg-3 col-md-6" v-for="campaign in campaigns" :key="campaign.id">
        <LuckyDraw :id="campaign.id" @update-chance="updateChance"></LuckyDraw>
        <CampaignCard :campaign="campaign"></CampaignCard>
      </div>
    </div>
  </div>
</template>

<script setup>
import CampaignCard from '@/components/CampaignCard.vue'
import LuckyDraw from '@/components/LuckyDraw.vue'
import { campaignStore } from '@/stores/campaignStore'
import { statusStore } from '@/stores/statusStore'
import { lotteryStore } from '@/stores/lotteryStore'
import { storeToRefs } from 'pinia'
import { onUnmounted, onMounted } from 'vue'

const store = campaignStore()
const status = statusStore()
const lottery = lotteryStore()
const { campaigns } = storeToRefs(store)
const { isLoading } = storeToRefs(status)
const { getCampaigns, clearCampaignImages } = store
const { getChancesByCampaign } = lottery

onMounted(async () => {
  await getCampaigns()
  await Promise.all(campaigns.value.map((campaign) => getChancesByCampaign(1, campaign.id)))
})

const updateChance = async () => {
  await Promise.all(campaigns.value.map((campaign) => getChancesByCampaign(1, campaign.id)))
}

onUnmounted(() => {
  clearCampaignImages()
})
</script>
