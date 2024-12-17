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
    <div class="row g-4 align-items-center justify-content-center">
      <div
        class="col-lg-5 col-md-12"
        style="
          display: flex;
          flex-direction: column;
          justify-content: center;
          margin-left: 50px;
          margin-right: 25px;
        "
      >
        <div id="googlemap" class="mb-4" style="width: 100%; height: 350px">
          <iframe
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3671.9583749490967!2d120.22380107477024!3d23.025300516227354!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x346e771f2995cba5%3A0x3bfd449f1e46ffef!2z5Y2X6Ie656eR5oqA5aSn5a24!5e0!3m2!1szh-TW!2stw!4v1734080088780!5m2!1szh-TW!2stw"
            class="w-100 h-100 border rounded"
            allowfullscreen=""
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade"
            style="border-radius: 10px"
          ></iframe>
        </div>
        <div
          id="inform"
          class="col-12"
          style="background-color: transparent; padding: 20px; border-radius: 10px"
        >
          <div class="row">
            <div class="col-lg-6 mb-3">
              <div class="phone">
                <i class="fa fa-phone fs-4 me-3"></i>
                <h5 class="fw-bold">Phone Numbers</h5>
                <span>
                  <a href="tel:07-656-8106" class="text-decoration-none text-light d-block"
                    >07-656-8106</a
                  >
                  <a href="tel:07-656-8107" class="text-decoration-none text-light d-block"
                    >07-656-8107</a
                  >
                </span>
              </div>
            </div>
            <div class="col-lg-6 mb-3">
              <div class="message">
                <i class="fa fa-envelope fs-4 me-3"></i>
                <h5 class="fw-bold">Emails</h5>
                <span>
                  <a href="mailto:hello@company.com" class="text-decoration-none text-light d-block"
                    >hello@company.com</a
                  >
                  <a href="mailto:info@company.com" class="text-decoration-none text-light d-block"
                    >info@company.com</a
                  >
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        id="app"
        class="col-lg-5 col-md-12"
        style="
          background-color: white;
          padding: 20px;
          border-radius: 10px;
          box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
          color: black;
          margin-left: 25px;
          margin-right: 50px;
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
                class="btn btn-outline-success w-100 me-2 mb-2"
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
            <button type="submit" class="btn btn-success w-100">確認訂位</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
//記得要修改成響應式
export default {
  data() {
    return {
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
    }
  },
  methods: {
    handleClick(time, event) {
      event.preventDefault() // 阻止按鈕的默認行為，防止它觸發表單提交
      console.log('按鈕點擊時間:', time)
      this.reservations.startTime = time
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
        console.error('這是錯的:', e)
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
          alert('訂位成功！')
          this.reservations = {
            customerName: '',
            customerGender: '',
            contactPhone: '',
            reservationDate: new Date().toISOString().split('T')[0],
            startTime: '',
            numberPeople: 0,
            notes: '',
          }
        } else {
          const errorMessage = response.data.message || '訂位失敗，請稍後再試。'
          alert(`錯誤: ${errorMessage}`)
        }
      } catch (error) {
        if (error.response && error.response.data) {
          const errorMessage = error.response.data.message || '提交訂位資料時發生錯誤，請稍後再試。'
          alert(`錯誤: ${errorMessage}`)
        } else {
          console.error('錯誤:', error)
          alert('提交訂位資料時發生錯誤，請稍後再試。')
        }
      }
    },
  },
  mounted() {
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
.phone,
.message {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.phone i,
.message i {
  font-size: 24px;
  color: #28a745;
  margin-right: 10px;
}

.phone h5,
.message h5 {
  font-size: 16px;
  margin-bottom: 5px;
}

.phone a,
.message a {
  color: #fff;
  font-weight: 500;
}

.phone a:hover,
.message a:hover {
  color: #007bff;
}

form {
  color: black;
}

.form-label,
.form-control {
  color: black;
}
</style>
