import Modal from 'bootstrap/js/dist/modal'

export default {
  mounted() {
    this.modal = new Modal(this.$refs.modal)
  },
  methods: {
    showModal() {
      this.modal.show()
    },
    hideModal() {
      this.modal.hide()
    }
  }
}
