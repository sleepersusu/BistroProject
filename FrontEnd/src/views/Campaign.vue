<template>
  <div class="d-flex justify-content-center">
    <LuckyDraw></LuckyDraw>
  </div>
</template>

<script>
import LuckyDraw from '@/components/LuckyDraw.vue'

export default {
  data() {
    return {
      campaign: [],
    }
  },
  methods: {
    async getCampaigns() {
      const api = `${import.meta.env.VITE_API}/api/campaign`
      try {
        const res = await this.axios.get(api)
        this.campaign = res.data
        window.Swal.fire({
          toast: true,
          position: 'top-end',
          icon: 'success',
          title: 'fetch活動資料成功',
          timer: 1500,
          showConfirmButton: false,
          timerProgressBar: true,
        })
      } catch (e) {
        console.log(e)
      }
    },
  },
  created() {
    this.getCampaigns()
  },
  components: {
    LuckyDraw,
  },
}
</script>
