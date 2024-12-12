<template>
  <LuckyWheel
    ref="myLucky"
    width="300px"
    height="300px"
    :prizes="prizes"
    :blocks="blocks"
    :buttons="buttons"
    @start="startCallback"
    @end="endCallback"
  />
</template>

<script>
export default {
  data() {
    return {
      blocks: [{ padding: '13px', background: '#617df2' }],
      prizes: [
        { fonts: [{ text: '0', top: '10%' }], background: '#e9e8fe' },
        { fonts: [{ text: '1', top: '10%' }], background: '#b8c5f2' },
        { fonts: [{ text: '2', top: '10%' }], background: '#e9e8fe' },
        { fonts: [{ text: '3', top: '10%' }], background: '#b8c5f2' },
        { fonts: [{ text: '4', top: '10%' }], background: '#e9e8fe' },
        { fonts: [{ text: '5', top: '10%' }], background: '#b8c5f2' },
      ],
      buttons: [
        {
          radius: '35%',
          background: '#8a9bf3',
          pointer: true,
          fonts: [{ text: '开始', top: '-10px' }],
        },
      ],
    }
  },
  methods: {
    startCallback() {
      this.$refs.myLucky.play()
      setTimeout(() => {
        const index = 0
        this.$refs.myLucky.stop(index)
      }, 3000)
    },
    endCallback(prize) {
      window.Swal.fire({
        icon: 'success',
        title: '恭喜中獎',
        text: `得獎的是${prize}`,
        timer: 1500,
        showConfirmButton: false,
        timerProgressBar: true,
      })
      console.log(prize)
    },
  },
}
</script>
