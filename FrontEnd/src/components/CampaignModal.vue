<template>
  <div
    class="modal fade"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
    ref="modalRef"
  >
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header bg-primary text-white">
          <h5 class="modal-title">活動詳細資訊</h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="modal"
            aria-label="Close"
          ></button>
        </div>
        <div class="modal-body">
          <ul class="list-group">
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="fw-bold">活動名稱</span>
              <span>{{ campaign?.campaignTitle }}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="fw-bold">活動類型</span>
              <span class="badge bg-danger text-light">{{ campaign?.campaignType }}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="fw-bold">消費門檻</span>
              <span>${{ campaign?.minOrderAmount }}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="fw-bold">活動狀態</span>
              <span :class="getStatusDisplay(campaign?.campaignStatus).class">
                {{ getStatusDisplay(campaign?.campaignStatus).text }}
              </span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="fw-bold">開始時間</span>
              <span>{{ formatDate(campaign?.startDate) }}</span>
            </li>
            <li class="list-group-item d-flex justify-content-between align-items-center">
              <span class="fw-bold">結束時間</span>
              <span>{{ formatDate(campaign?.endDate) }}</span>
            </li>
            <div class="accordion" id="accordionExample">
              <div class="accordion-item rounded-top-0">
                <h2 class="accordion-header" id="headingOne">
                  <button
                    class="accordion-button ps-3"
                    type="button"
                    data-bs-toggle="collapse"
                    data-bs-target="#collapseOne"
                    aria-expanded="true"
                    aria-controls="collapseOne"
                  >
                    <span class="fw-bold">活動說明</span>
                  </button>
                </h2>
                <div
                  id="collapseOne"
                  class="accordion-collapse collapse show"
                  aria-labelledby="headingOne"
                  data-bs-parent="#accordionExample"
                >
                  <div class="accordion-body">
                    {{ campaign?.campaignDescription }}
                  </div>
                </div>
              </div>
            </div>
          </ul>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary text-light" data-bs-dismiss="modal">
            關閉
          </button>
          <button type="button" class="btn btn-primary">確認</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useModal } from '@/mixins/modalMixin'
const { modalRef, showModal, hideModal } = useModal()
import { defineProps } from 'vue'
const props = defineProps({
  campaign: {
    type: Object,
    required: true,
  },
})

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-TW')
}

const statusFilters = {
  IN_PROGRESS: {
    text: '進行中',
    class: 'text-success',
  },
  EXPIRED: {
    text: '已結束',
    class: 'text-danger',
  },
  NOT_STARTED: {
    text: '未開始',
    class: 'text-secondary',
  },
}

const getStatusDisplay = (status) => statusFilters[status] || statusFilters['NOT_STARTED']

defineExpose({
  showModal,
  hideModal,
})
</script>
