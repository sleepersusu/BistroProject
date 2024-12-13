import { onMounted, ref } from 'vue'
import Modal from 'bootstrap/js/dist/modal'

export const useModal = () => {
  const modal = ref(null)
  const modalRef = ref(null)

  const showModal = () => {
    modal.value.show()
  }

  const hideModal = () => {
    modal.value.hide()
  }

  onMounted(() => {
    modal.value = new Modal(modalRef.value)
  })

  return {
    modalRef,
    showModal,
    hideModal,
  }
}
