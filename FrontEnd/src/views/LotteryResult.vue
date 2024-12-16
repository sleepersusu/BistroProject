<template>
  <BannerTop :title="'Lottery Result'"></BannerTop>
  <div class="container py-4 px-5">
    <div class="row">
      <div class="col-12">
        <div class="card border shadow-xs mb-4">
          <div class="card-body px-0 py-0">
            <div class="table-responsive p-0">
              <table class="table align-items-center mb-0">
                <thead class="bg-gray-100">
                  <tr>
                    <th class="text-dark text-xs font-weight-semibold opacity-7">會員名稱</th>
                    <th class="text-dark text-xs font-weight-semibold opacity-7">活動名稱</th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">
                      獎品名稱
                    </th>
                    <th class="text-center text-dark text-xs font-weight-semibold opacity-7">
                      中獎時間
                    </th>
                    <th class="text-dark opacity-7">活動狀態</th>
                    <th class="text-dark opacity-7">兌換紀錄</th>
                  </tr>
                </thead>

                <tbody>
                  <tr v-for="result in lotteryResults" :key="result.id">
                    <td>
                      <div class="d-flex px-2 py-1">
                        <div class="d-flex flex-column justify-content-center ms-1">
                          <h6 class="mb-0 text-sm font-weight-semibold text-primary">
                            {{ result.memberName }}
                          </h6>
                        </div>
                      </div>
                    </td>

                    <td>
                      <p class="text-sm text-dark font-weight-semibold mb-0">
                        {{ result.campaignName }}
                      </p>
                    </td>

                    <td class="align-middle text-center text-sm">
                      {{ result.prizeName }}
                    </td>

                    <td class="align-middle text-center">
                      <span class="text-primary text-sm font-weight-normal">{{
                        formatDate(result.createdAt)
                      }}</span>
                    </td>
                    <td
                      class="align-middle"
                      id="countdown"
                      :class="getStatusDisplay(result.campaignStatus).class"
                    >
                      {{ getStatusDisplay(result.campaignStatus).text }}
                    </td>
                    <td class="align-middle" id="countdown">尚未兌換</td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="border-top py-3 px-3 d-flex align-items-center">
              <p class="font-weight-semibold mb-0 text-dark text-sm">Page 1 of 10</p>
              <div class="ms-auto">
                <button class="btn btn-sm btn-white mb-0">Previous</button>
                <button class="btn btn-sm btn-white mb-0">Next</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import BannerTop from '@/components/BannerTop.vue'
import { statusStore } from '@/stores/statusStore'
import { ref } from 'vue'
import axios from 'axios'
import { utils } from '@/mixins/utils'
const { getStatusDisplay, formatDate } = utils()

const status = statusStore()

const lotteryResults = ref([])

const getResults = async () => {
  const api = `${import.meta.env.VITE_API}/api/winner/member/1`
  try {
    status.start()
    const res = await axios.get(api)
    console.log(res.data)
    lotteryResults.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    status.finish()
  }
}

getResults()
</script>
