import { ref } from 'vue'

export const useLuckyCanvas = () => {
  const buttons = ref([
    {
      x: 1,
      y: 1,
      background: 'salmon',
      fonts: [
        {
          text: 'START',
          fontSize: '16px',
          fontWeight: 'bold',
          fontColor: '#fdd156',
          top: '40%',
        },
      ],
    },
  ])
  const blocks = ref([
    { padding: '15px', background: 'GoldenRod' },
    { padding: '15px', background: 'black' },
  ])
  const activeStyle = {
    background: '#FFD700',
    fontColor: '#000',
    shadow: '0 0 10px rgba(253, 209, 86, 0.5)',
  }

  const myLucky = ref(null)

  return {
    buttons,
    blocks,
    activeStyle,
    myLucky,
  }
}
