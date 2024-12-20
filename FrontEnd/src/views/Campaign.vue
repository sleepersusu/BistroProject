<template>
  <Loading :active="isLoading"></Loading>
  <HeroSection></HeroSection>
  <div class="fireworks-container" ref="fireworksContainer"></div>
  <BannerTop :title="'Lucky Draw'" data-aos="fade-up" data-aos-duration="1000"></BannerTop>
  <div class="container my-5">
    <div class="row">
      <div class="col-lg-4 col-md-6 g-5" v-for="(campaign, index) in campaigns" :key="campaign.id">
        <CampaignCard
          :campaign="campaign"
          @open-drawmodal="handleOpenModal"
          @open-detailmodal="handleOpenDetail"
          data-aos="fade-up"
          data-aos-easing="ease-out-cubic"
          data-aos-duration="1000"
          :data-aos-delay="300 * (index + 1)"
        ></CampaignCard>
      </div>
    </div>
    <CampaignLineLink></CampaignLineLink>
  </div>
  <LuckyDraw @update-chance="updateChance" ref="drawModal"></LuckyDraw>
  <CampaignModal ref="campaignModal" :campaign="selectedCampaign"></CampaignModal>
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
import BannerTop from '@/components/BannerTop.vue'
import { useFireWorks } from '@/mixins/fireWorkMixin'
import CampaignModal from '@/components/CampaignModal.vue'
import HeroSection from '@/components/HeroSection.vue'
import { useUserStore } from '@/stores/userStore'
import CampaignLineLink from '@/components/CampaignLineLink.vue'

const { fireworksContainer } = useFireWorks(true)

const store = campaignStore()
const status = statusStore()
const lottery = lotteryStore()
const prize = campaignPrizeStore()
const user = useUserStore()
const { campaigns } = storeToRefs(store)
const { isLoading } = storeToRefs(status)
const { getCampaigns, clearCampaignImages } = store
const { getChancesByCampaign } = lottery
const { getPrizesByCampaign } = prize

const drawModal = ref(null)
const handleOpenModal = async (campaignId) => {
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

const selectedCampaign = ref(null)
const campaignModal = ref(null)
const handleOpenDetail = (campaign) => {
  selectedCampaign.value = campaign
  campaignModal.value.showModal()
}

onMounted(async () => {
  try {
    status.start()
    await getCampaigns()
    for (const campaign of campaigns.value) {
      await getChancesByCampaign(user.memberId, campaign.id)
    }
  } catch (e) {
    console.error(e)
  } finally {
    status.finish()
  }
})

const updateChance = async (campaignId) => {
  try {
    status.start()
    await getChancesByCampaign(user.memberId, campaignId)
  } catch (e) {
    console.error('更新抽獎機會失敗:', e)
  } finally {
    status.finish()
  }
}

onUnmounted(() => {
  clearCampaignImages()
})
</script>

<style scoped>
.fireworks-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  pointer-events: none;
}

.container {
  position: relative;
  z-index: 1;
}
</style>
