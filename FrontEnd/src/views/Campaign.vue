<template>
  <Loading :active="isLoading"></Loading>
  <div class="container">
    <div class="row">
      <div class="col-lg-3 col-md-6" v-for="campaign in campaigns" :key="campaign.id">
        <CampaignCard :campaign="campaign" @open-drawmodal="handleOpenModal"></CampaignCard>
      </div>
    </div>
  </div>
  <LuckyDraw @update-chance="updateChance" ref="drawModal"></LuckyDraw>
</template>

<script setup>
import CampaignCard from '@/components/CampaignCard.vue'
import LuckyDraw from '@/components/LuckyDraw.vue'
import { campaignStore } from '@/stores/campaignStore'
import { statusStore } from '@/stores/statusStore'
import { lotteryStore } from '@/stores/lotteryStore'
import { campaignPrizeStore } from '@/stores/campaignPrizeStore'
import { storeToRefs } from 'pinia'
import { onUnmounted, onMounted, ref } from 'vue'

const store = campaignStore()
const status = statusStore()
const lottery = lotteryStore()
const prize = campaignPrizeStore()
const { campaigns } = storeToRefs(store)
const { isLoading } = storeToRefs(status)
const { getCampaigns, clearCampaignImages } = store
const { getChancesByCampaign } = lottery
const { getPrizesByCampaign } = prize

const drawModal = ref(null)
const handleOpenModal = async (campaignId) => {
  if (!drawModal.value) return
  try {
    status.start()
    await getPrizesByCampaign(campaignId)
    drawModal.value.setCurrentCampaignId(campaignId)
    drawModal.value.showModal()
  } catch (error) {
    console.error('開啟抽獎失敗:', error)
  } finally {
    status.finish()
  }
}

onMounted(async () => {
  try {
    status.start()
    await getCampaigns()
    for (const campaign of campaigns.value) {
      await getChancesByCampaign(1, campaign.id)
    }
  } catch (error) {
    console.error('初始化失敗:', error)
  } finally {
    status.finish()
  }
})

const updateChance = async () => {
  status.start()
  await Promise.all(campaigns.value.map((campaign) => getChancesByCampaign(1, campaign.id)))
  status.finish()
}

onUnmounted(() => {
  clearCampaignImages()
})
</script>
