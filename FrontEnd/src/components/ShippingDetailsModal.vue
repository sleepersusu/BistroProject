<template>
  <div
    class="modal fade"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
    ref="modalRef"
  >
    <div class="modal-dialog modal-dialog-centered modal-lg">
      <div class="modal-content">
        <div class="modal-header bg-primary text-light">
          <h5 class="modal-title">配送資訊</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <Form v-slot="{ errors }" @submit="handleSubmit" ref="formRef">
          <div class="modal-body">
            <div class="mb-3">
              <label for="email" class="form-label"
                >Email <small class="text-danger">*</small></label
              >
              <Field
                type="email"
                class="form-control"
                id="email"
                name="Email"
                rules="email|required"
                :class="{ 'is-invalid': errors['Email'] }"
                placeholder="請輸入電子信箱"
                v-model="shippingDetails.email"
              ></Field>
              <ErrorMessage name="Email" class="invalid-feedback"></ErrorMessage>
            </div>
            <div class="mb-3">
              <label for="name" class="form-label">姓名 <small class="text-danger">*</small></label>
              <Field
                type="text"
                class="form-control"
                id="name"
                name="姓名"
                rules="required"
                :class="{ 'is-invalid': errors['姓名'] }"
                placeholder="請輸入姓名"
                v-model="shippingDetails.name"
              ></Field>
              <ErrorMessage name="姓名" class="invalid-feedback"></ErrorMessage>
            </div>
            <div class="mb-3">
              <label for="phone" class="form-label"
                >電話 <small class="text-danger">*</small></label
              >
              <Field
                type="text"
                class="form-control"
                id="phone"
                name="電話"
                :class="{ 'is-invalid': errors['電話'] }"
                :rules="{ required: true, regex: /^09[0-9]{8}$/ }"
                placeholder="請輸入電話"
                v-model="shippingDetails.phone"
              ></Field>
              <ErrorMessage name="電話" class="invalid-feedback"></ErrorMessage>
            </div>
            <div class="mb-3">
              <label for="address" class="form-label"
                >配送地址 <small class="text-danger">*</small></label
              >
              <Field
                type="text"
                class="form-control"
                id="address"
                name="配送地址"
                rules="required"
                :class="{ 'is-invalid': errors['配送地址'] }"
                placeholder="請輸入配送地址"
                v-model="shippingDetails.address"
              ></Field>
              <ErrorMessage name="配送地址" class="invalid-feedback"></ErrorMessage>
            </div>
            <div class="mb-3">
              <label for="notes" class="form-label">留言 (選填)</label>
              <textarea
                class="form-control"
                id="notes"
                rows="3"
                v-model="shippingDetails.notes"
              ></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary text-light" data-bs-dismiss="modal">
              關閉
            </button>
            <button type="submit" class="btn btn-primary" :disabled="loadingItem">
              <div
                v-if="loadingItem"
                class="spinner-border text-light spinner-border-sm"
                role="status"
              >
                <span class="visually-hidden">Loading...</span>
              </div>
              送出
            </button>
          </div>
        </Form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineExpose, ref, defineEmits } from 'vue'
import { useModal } from '@/mixins/modalMixin'
import { Field } from 'vee-validate'
const { showModal: showBootstrapModal, modalRef, hideModal } = useModal()

const loadingItem = ref(false)
const shippingDetails = ref({
  lotteryWinnerId: -1,
  name: '',
  phone: '',
  email: '',
  address: '',
  notes: '',
})

const emits = defineEmits(['submit-form'])
const handleSubmit = () => emits('submit-form', shippingDetails.value)

const formRef = ref(null)
const showModal = (winnerId) => {
  formRef.value.resetForm()
  shippingDetails.value = {
    lotteryWinnerId: winnerId,
    name: '',
    phone: '',
    email: '',
    address: '',
    notes: '',
  }
  showBootstrapModal()
}

defineExpose({
  showModal,
  hideModal,
  loadingItem,
})
</script>
