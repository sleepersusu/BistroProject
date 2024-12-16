<template>
  <div
    style="display: flex; justify-content: center; align-items: center; gap: 20px; padding: 20px"
  >
    <!-- 左側：地圖 -->
    
    <div style="position: relative; flex: 1; max-width: 500px; height: 350px">
      <iframe
        src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3671.9583749490967!2d120.22380107477024!3d23.025300516227354!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x346e771f2995cba5%3A0x3bfd449f1e46ffef!2z5Y2X6Ie656eR5oqA5aSn5a24!5e0!3m2!1szh-TW!2stw!4v1734080088780!5m2!1szh-TW!2stw"
        width="100%"
        height="100%"
        style="border: 0; border-radius: 10px"
        allowfullscreen=""
        loading="lazy"
        referrerpolicy="no-referrer-when-downgrade"
      >
      </iframe>
    </div>

    <!-- 右側：資訊及訂位 -->
    <div
      style="
        flex: 1;
        max-width: 300px;
        background-color: #f9f9f9;
        align-items: center;
        padding: 20px;
        border-radius: 10px;
      "
    >
      <!-- 地點營業資訊 -->

      <h3>📍 位置</h3>
      <p>南台科技大學L棟/5F</p>

      <h3>📞 聯絡電話</h3>
      <p>07-656-8106</p>

      <h3>🕒 營業時間</h3>
      <p>營業至 24:00</p>

      <h3>🍽️ 料理類型</h3>
      <p>文化商圈, 餐酒館料理</p>
    </div>

    <!-- 訂位資訊表單 -->
    <div
      id="app"
      style="
        flex: 1;
        max-width: 500px;
        background-color: #fff;
        align-items: center;
        margin: 0 20px;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
      "
    >
      <div class="center" style="font-size: 30px; margin-bottom: 20px">訂位資訊</div>
      <div class="row g-3">
        <form @submit.prevent="submitReservation">
          <!-- 姓名 -->
          <div class="col-12">
            <label for="customerName" class="form-label">訂位人姓名</label>
            <input
              type="text"
              class="form-control frame"
              id="customerName"
              v-model="reservations.customerName"
              required
            />
          </div>
          <!-- 性別 -->
          <div class="col-12">
            <label class="form-label">性別</label>
            <input
              type="radio"
              name="customerGender"
              v-model="reservations.customerGender"
              required
              value="男"
            />男
            <input
              type="radio"
              name="customerGender"
              v-model="reservations.customerGender"
              required
              value="女"
            />女
          </div>
          <!-- 電話 -->
          <div class="col-12">
            <label for="contactPhone" class="form-label">電話</label>
            <input
              type="tel"
              class="form-control frame"
              id="contactPhone"
              v-model="reservations.contactPhone"
              required
            />
          </div>
          <!-- 訂位人數 -->
          <div class="col-md-6">
            <label for="numberPeople" class="form-label">訂位人數</label>
            <select
              class="form-control frame"
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
          <!-- 日期 -->
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
          <!-- 時段 -->
          <div class="col-md-6">
            <label for="startTime" class="form-label">時段</label>
            <div>
              <button
                v-for="time in availableTimeslots"
                :key="time"
                class="btn btn-outline-primary me-2"
                @click="handleClick(time, $event)"
              >
                {{ time }}
              </button>
            </div>
            <input type="hidden" v-model="reservations.startTime" />
          </div>
          <!-- 備註 -->
          <div class="col-12">
            <label for="notes" class="form-label">備註</label>
            <textarea
              class="form-control frame"
              style="height: 200px"
              id="notes"
              v-model="reservations.notes"
            ></textarea>
          </div>
          <!-- 確認按鈕 -->
          <div class="col-12">
            <button type="submit" class="btn btn-success">確認訂位</button>
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
.frame {
  border: 2px solid #eed9c4;
}
</style>
