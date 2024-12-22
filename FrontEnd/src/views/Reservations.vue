<template>
  <div
    class="container-fluid py-4"
    style="
      background-image: url('images/reservation-bg.jpg');
      color: white;
      padding-left: 50px;
      padding-right: 50px;
    "
  >
    <div class="row g-4 justify-content-center d-flex align-items-center" style="min-height: 100vh">
      <div class="col-lg-7 col-md-12">
        <div id="googlemap" class="mb-4" style="width: 100%; height: 350px">
          <iframe
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3671.9583749490967!2d120.22380107477024!3d23.025300516227354!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x346e771f2995cba5%3A0x3bfd449f1e46ffef!2z5Y2X6Ie656eR5oqA5aSn5a24!5e0!3m2!1szh-TW!2stw!4v1734080088780!5m2!1szh-TW!2stw"
            v-if="isMapVisible"
            class="w-100 h-100"
            allowfullscreen=""
            referrerpolicy="no-referrer-when-downgrade"
            style="width: 100%; height: 100%"
          ></iframe>
        </div>
        <div id="inform" class="container py-4" style="background-color: transparent">
          <div class="row g-4 justify-content-center">
            <div class="col-lg-5 col-md-6 col-12">
              <div class="card text-center p-4 position-relative">
                <div
                  class="position-absolute top-0 start-50 translate-middle bg-black text-white rounded-circle d-flex justify-content-center align-items-center"
                  style="width: 60px; height: 60px; z-index: 10"
                >
                  <font-awesome-icon
                    :icon="['fas', 'phone']"
                    style="font-size: 27px"
                  ></font-awesome-icon>
                </div>
                <div class="mt-3">
                  <h5 class="fw-bold">Phone Numbers</h5>
                  <p>07-656-8106</p>
                  <p>07-716-9502</p>
                </div>
              </div>
            </div>
            <div class="col-lg-5 col-md-6 col-12">
              <div class="card text-center p-4 position-relative">
                <div
                  class="position-absolute top-0 start-50 translate-middle bg-black text-white rounded-circle d-flex justify-content-center align-items-center"
                  style="width: 60px; height: 60px; z-index: 10"
                >
                  <font-awesome-icon
                    :icon="['far', 'envelope']"
                    style="font-size: 27px"
                  ></font-awesome-icon>
                </div>
                <div class="mt-3">
                  <h5 class="fw-bold">Emails</h5>
                  <p>NightSips1223@gmail.com</p>
                  <p>aiz777vic@gmail.com</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        id="app"
        class="col-lg-4 col-md-12"
        style="
          background-color: white;
          padding: 20px;
          border-radius: 10px;
          box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
          color: black;
        "
      >
        <div class="mb-4 text-center">
          <h4 class="fw-bold">訂位資訊</h4>
        </div>
        <form @submit.prevent="submitReservation">
          <div class="mb-3">
            <label for="customerName" class="form-label">訂位人姓名</label>
            <input
              type="text"
              class="form-control frame"
              id="customerName"
              v-model="reservations.customerName"
              required
            />
          </div>
          <div class="mb-3">
            <label class="form-label">性別</label>
            <div class="d-flex">
              <div class="form-check me-3">
                <input
                  type="radio"
                  class="form-check-input"
                  name="customerGender"
                  v-model="reservations.customerGender"
                  value="男"
                  id="genderMale"
                  required
                />
                <label class="form-check-label" for="genderMale">男</label>
              </div>
              <div class="form-check">
                <input
                  type="radio"
                  class="form-check-input"
                  name="customerGender"
                  v-model="reservations.customerGender"
                  value="女"
                  id="genderFemale"
                  required
                />
                <label class="form-check-label" for="genderFemale">女</label>
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label for="contactPhone" class="form-label">電話</label>
            <input
              type="tel"
              class="form-control frame"
              id="contactPhone"
              v-model="reservations.contactPhone"
              required
            />
          </div>
          <div class="row g-3">
            <div class="col-md-6">
              <label for="numberPeople" class="form-label">訂位人數</label>
              <select
                class="form-select frame"
                id="numberPeople"
                v-model.number="reservations.numberPeople"
                required
              >
                <option value="0" selected>選擇人數</option>
                <option value="1">1位</option>
                <option value="2">2位</option>
                <option value="3">3位</option>
                <option value="4">4位</option>
                <option value="5">5位</option>
                <option value="6">6位</option>
                <option value="7">7位</option>
                <option value="8">8位</option>
              </select>
            </div>
            <div class="col-md-6">
              <label for="reservationDate" class="form-label">日期</label>
              <input
                type="date"
                class="form-control frame"
                id="reservationDate"
                v-model="reservations.reservationDate"
                v-bind:min="minDate"
                v-bind:max="maxDate"
                required
              />
            </div>
          </div>
          <div class="mb-4 mt-3">
            <label class="form-label">時段</label>
            <div>
              <button
                v-for="time in availableTimeslots"
                :key="time"
                :class="{
                  'btn-outline-black': selectedTime === time,
                  'btn-black': selectedTime !== time,
                }"
                :style="{ color: selectedTime === time ? '#fff' : '' }"
                class="btn btn-black w-100 me-2 mb-2"
                @click="handleClick(time, $event)"
              >
                {{ time }}
              </button>
            </div>
            <input type="hidden" v-model="reservations.startTime" />
          </div>
          <div class="mb-3">
            <label for="notes" class="form-label">備註</label>
            <textarea
              class="form-control frame"
              id="notes"
              rows="4"
              v-model="reservations.notes"
            ></textarea>
          </div>
          <div class="text-center">
            <button type="submit" class="btn btn-black w-100">確認訂位</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      isMapVisible: false, 
      reservations: {
        customerName: '',
        customerGender: '',
        contactPhone: '',
        reservationDate: new Date().toISOString().split('T')[0],
        startTime: '',
        numberPeople: 0,
        notes: '',
      },
      minDate: '',
      maxDate: '',
      availableTimeslots: [],
      selectedTime: '',
    }
  },
  methods: {
    showSuccess() {
      Swal.fire({
        title: '新增訂位成功!',
        icon: 'success',
        confirmButtonText: '確定',
        confirmButtonColor: '#000000',
        customClass: {
        confirmButton: 'custom-button' 
    }
      })
    },
    handleClick(time, event) {
      event.preventDefault() // 阻止按鈕的默認行為，防止它觸發表單提交
      if (this.selectedTime === time) {
        this.selectedTime = ''
      } else {
        this.selectedTime = time
      }
      this.reservations.startTime = this.selectedTime
    },
    formatDate(date) {
      if (!date) return ''
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    },

    async displayTimes() {
      if (this.reservations.numberPeople === 0 || !this.reservations.reservationDate) {
        this.availableTimeslots = []
        return
      }
      try {
        const api = `${import.meta.env.VITE_API}/api/Bistro/remaining`
        let res = await this.axios.post(api, {
          reservationDate: this.reservations.reservationDate,
          numberPeople: this.reservations.numberPeople,
        })

        if (res.data.success) {
          this.availableTimeslots = res.data.availableTimeslots || []
        } else {
          this.availableTimeslots = []
        }
      } catch (e) {
        console.error('錯誤:', e)
        this.availableTimeslots = []
      }
    },
    startTime(time) {
      this.reservations.startTime = time
    },
    async submitReservation() {
      if (
        !this.reservations.customerName ||
        !this.reservations.customerGender ||
        !this.reservations.contactPhone ||
        !this.reservations.reservationDate ||
        !this.reservations.startTime ||
        !this.reservations.numberPeople
      ) {
        alert('請填寫所有必填欄位！')
        return
      }
      try {
        const api = `${import.meta.env.VITE_API}/api/Bistro/insert`
        const response = await this.axios.post(api, this.reservations)
        if (response.data.success) {
          // 發送簡訊要取得的資料
          const confirmationApi = `${import.meta.env.VITE_API}/api/Bistro/send`
          const reservationData = {
            contactPhone: this.reservations.contactPhone,
            reservationDate: this.reservations.reservationDate,
            startTime: this.reservations.startTime,
            numberPeople: this.reservations.numberPeople,
          }
          const smsResponse = await this.axios.post(confirmationApi, reservationData)
          if (smsResponse.data.success) {
            this.showSuccess()
            this.reservations = {
              customerName: '',
              customerGender: '',
              contactPhone: '',
              reservationDate: new Date().toISOString().split('T')[0],
              startTime: '',
              numberPeople: 0,
              notes: '',
            }
            this.selectedTime = ''
          } else {
            const errorMessage = smsResponse.data.message || '訂位確認簡訊發送失敗，請稍後再試。'
            alert(`錯誤: ${errorMessage}`)
          }
        } else {
          const errorMessage = response.data.message || '訂位失敗，請稍後再試。'
          alert(`錯誤: ${errorMessage}`)
        }
      } catch (error) {
        if (error.response && error.response.data) {
          const errorMessage = error.response.data.message || '提交訂位資料時發生錯誤，請稍後再試。'
          alert(`格式輸入錯誤: ${errorMessage}`)
        } else {
          console.error('錯誤:', error)
          alert('提交訂位資料時發生錯誤，請稍後再試。')
        }
      }
    },
  },
  mounted()
   {
    this.$nextTick(() => {
      this.isMapVisible = true; 
    });
    const today = new Date()
    this.minDate = today.toISOString().split('T')[0]
    const max = new Date()
    max.setDate(today.getDate() + 7)
    this.maxDate = max.toISOString().split('T')[0]
    
  },
  
  computed: {},
  watch: {
    'reservations.numberPeople': function () {
      this.displayTimes()
    },
    'reservations.reservationDate': function () {
      this.displayTimes()
    },
  },
  created() {
    this.displayTimes()
  },
}
</script>

<style scoped>

.btn-black {
  background-color: black;
  color: white;
  border: 1px solid black;
}

.btn-outline-black{
  background-color: darkgray;
  color: black;
  
}

.btn-black:hover {
  background-color: darkgray;
  color: white;
}
.custom-button {
  color: white;  
}

@media (min-width: 992px) {
  #googlemap iframe {
    max-width: calc(100% - 250px);
    margin-left: auto;
    margin-right: auto;
    display: block;
  }
}

/* 手機版樣式 */
@media (max-width: 991px) {
  #googlemap iframe {
    max-width: 100%;
  }
}
/* .btn-orange {
  background-color: #ff6600;
  border: 1px solid #ff6600;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, border-color 0.3s ease;
}

.btn-orange:hover {
  background-color: #e65c00;
  border-color: #e65c00;
}

.btn-outline-orange {
  background-color: transparent;
  border: 2px solid #ff6600;
  color: #ff6600;
  padding: 8px 18px;
  font-size: 16px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;
}

.btn-outline-orange:hover {
  background-color: #ff6600;
  color: white;
  border-color: #ff6600;
} */

/*:class="{
          'btn-outline-orange': selectedTime !== time,
          'btn-orange': selectedTime === time,  改顏色要加入這段
        }"  */
</style>
