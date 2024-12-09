<template>
  <div>
    <button class="btn btn-secondary">lorem</button>
    <button class="btn btn-outline-secondary">lorem</button>
    <div class="text-light">Lorem, ipsum.</div>
    <div class="text-muted">Lorem, ipsum.</div>
  </div>
</template>

<script>
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
  computed: {},
  watch: {},
  created() {
    this.getCampaigns()
  },
}
</script>
