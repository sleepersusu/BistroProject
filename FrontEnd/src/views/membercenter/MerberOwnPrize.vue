<template>
  <BannerTop :title="'PromoCode'"></BannerTop>
  <div>
    <PointTotal/>
  </div>
<PromoRecord />

</template>

<script>
import BannerTop from '@/components/BannerTop.vue'
import PointTotal from '@/components/PointTotal.vue'
import PromoRecord from '@/components/PromoRecord.vue'
import { useUserStore } from '@/stores/userStore'

const user = useUserStore()

export default {
  components: {
    PointTotal,
    BannerTop,
    PromoRecord,
  },
  data() {
    return {
    }
  },
  methods: {
  },
}
</script>
