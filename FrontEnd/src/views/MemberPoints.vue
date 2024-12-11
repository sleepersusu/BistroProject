<template>
  <div>
    <h1>Member Points</h1>
    <ul>
      <li v-for="prize in pointPrizes" :key="prize.id">{{ prize.pointPrizesName }}</li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {
      pointPrizes: [],
    }
  },
  methods: {
    async getPointPrizes() {
      const api = `${import.meta.env.VITE_API}/api/pointPrizes`;
      try {
        const response = await fetch(api);
        if (response.ok) {
          const data = await response.json();
          this.pointPrizes = data;
        } else {
          console.error('Failed to fetch point prizes:', response.status);
        }
      } catch (error) {
        console.error('Error fetching point prizes:', error);
      }
    }
  },
  computed: {},
  watch: {},
  created() {
    this.getPointPrizes();
  },
}
</script>
