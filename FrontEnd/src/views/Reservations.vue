<template>
  <div class="container-fluid py-4" style="
      background-image: url('images/reservation-bg.jpg');
      color: white;
      padding-left: 50px;
      padding-right: 50px;
    ">
    <div class="row g-4 justify-content-center d-flex align-items-center" style="min-height: 100vh">
      <div class="col-lg-7 col-md-12">
        <div id="googlemap" class="mb-4" style="width: 100%; height: 350px">

          <iframe
            src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3614.4673315461455!2d121.5401541!3d25.0521449!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3442abde34f7352d%3A0xb2db9881628011f6!2zMTA0OTHlj7DljJfluILkuK3lsbHljYDljZfkuqzmnbHot6_kuInmrrUxMjPomZ8!5e0!3m2!1szh-TW!2stw!4v1735012291613!5m2!1szh-TW!2stw"
            v-if="isMapVisible" class="w-100 h-100" allowfullscreen="" referrerpolicy="no-referrer-when-downgrade"
            style="width: 100%; height: 100%"></iframe>
        </div>
        <div id="inform" class="container py-4" style="background-color: transparent">
          <div class="row g-4 justify-content-center">
            <div class="col-lg-5 col-md-6 col-12">
              <div class="card text-center p-4 position-relative">
                <div
                  class="position-absolute top-0 start-50 translate-middle bg-black text-white rounded-circle d-flex justify-content-center align-items-center"
                  style="width: 60px; height: 60px; z-index: 10">
                  <font-awesome-icon :icon="['fas', 'phone']" style="font-size: 27px"></font-awesome-icon>
                </div>
                <div class="mt-3">
                  <h5 class="fw-bold">Phone Numbers</h5>
                  <p>02-656-8106</p>

                </div>
              </div>
            </div>
            <div class="col-lg-5 col-md-6 col-12">
              <div class="card text-center p-4 position-relative">
                <div
                  class="position-absolute top-0 start-50 translate-middle bg-black text-white rounded-circle d-flex justify-content-center align-items-center"
                  style="width: 60px; height: 60px; z-index: 10">
                  <font-awesome-icon :icon="['far', 'envelope']" style="font-size: 27px"></font-awesome-icon>
                </div>
                <div class="mt-3">
                  <h5 class="fw-bold">Emails</h5>
                  <p>NightSips1223@gmail.com</p>

                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div id="app" class="col-lg-4 col-md-12" style="
          background-color: white;
          padding: 20px;
          border-radius: 10px;
          box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
          color: black;
        ">
        <div class="mb-4 text-center">
          <h4 class="fw-bold">訂位資訊</h4>
        </div>
        <form @submit.prevent="submitReservation">
          <div class="mb-3">
            <label for="customerName" class="form-label">訂位人姓名</label>
            <input type="text" class="form-control frame" id="customerName" v-model="reservations.customerName"
              maxlength="10" @input="validateName" :class="{ 'is-invalid': nameError }" />
            <small v-if="nameError" class="text-danger">{{ nameError }}</small>
          </div>
          <div class="mb-3">
            <label class="form-label">性別</label>
            <div class="d-flex">
              <div class="form-check me-3">
                <input type="radio" class="form-check-input" name="customerGender" v-model="reservations.customerGender"
                  value="男" id="genderMale" required />
                <label class="form-check-label" for="genderMale">男</label>
              </div>
              <div class="form-check">
                <input type="radio" class="form-check-input" name="customerGender" v-model="reservations.customerGender"
                  value="女" id="genderFemale" required />
                <label class="form-check-label" for="genderFemale">女</label>
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label for="contactPhone" class="form-label">電話</label>
            <input type="tel" class="form-control frame" id="contactPhone" v-model="reservations.contactPhone"
              maxlength="10" required @input="validatePhone" :class="{ 'is-invalid': phoneError }" />
            <small v-if="phoneError" class="text-danger">{{ phoneError }}</small>
          </div>
          <div class="row g-3">
            <div class="col-md-6">
              <label for="numberPeople" class="form-label">訂位人數</label>
              <select class="form-select frame" id="numberPeople" v-model.number="reservations.numberPeople">
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
              <input type="date" class="form-control frame" id="reservationDate" v-model="reservations.reservationDate"
                v-bind:min="minDate" v-bind:max="maxDate" @input="validateDate" :class="{ 'is-invalid': dateError }" />
              <small v-if="dateError" class="text-danger">{{ dateError }}</small>
            </div>
          </div>
          <div class="mb-4 mt-3">
            <label class="form-label">時段</label>
            <div>
              <button v-for="time in availableTimeslots" :key="time" :class="{
                'btn-outline-black': selectedTime !== time,
                'btn-black': selectedTime === time,
              }" :style="{ color: selectedTime === time ? '#fff' : '' }" class="btn btn-black w-100 me-2 mb-2"
                @click="handleClick(time, $event)">
                {{ time }}

              </button>
            </div>
            <input type="hidden" v-model="reservations.startTime" />
          </div>
          <div class="mb-3">
            <label for="notes" class="form-label">備註</label>
            <textarea class="form-control frame" id="notes" rows="4" v-model="reservations.notes"></textarea>
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
      nameError: '',
      phoneError: '',
      genderError: '',
      dateError: '',
      timeError: '',
    
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
      window.Swal.fire({
        title: '新增訂位成功!',
        iconColor: '#000000',
        icon: 'success',
        confirmButtonText: '確定',
        confirmButtonColor: '#000000',
        customClass: {
          confirmButton: 'custom-button',
          icon: 'custom-icon'
        }
      })
    },
    validatePhone() {
      const phoneRegex = /^09\d{8}$/ // 09開頭,8位數字
      if (!this.reservations.contactPhone.trim()) {
        this.phoneError = '電話為必填'
      } else if (!phoneRegex.test(this.reservations.contactPhone)) {
        this.phoneError = '電話格式錯誤，請輸入正確的手機號碼'
      } else {
        this.phoneError = ''
      }
    },
    validateName() {
      const nameRegex = /^[a-zA-Z\u4e00-\u9fa5\s]{2,15}$/ // 允许中文、英文、空格，长度2~15
      if (!this.reservations.customerName.trim()) {
        this.nameError = '姓名為必填'
      } else if (!nameRegex.test(this.reservations.customerName)) {
        this.nameError = '姓名格式錯誤，請輸入2-15個字的中文或英文'
      } else {
        this.nameError = ''
      }
    },
    validateDate() {
      if (!this.reservations.reservationDate.trim()) {
        this.dateError = '日期為必填'
      } else {
        this.dateError = ''
      }
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
        this.validatePhone();
        this.validateName();
        this.validateGender();
        this.validateDate();
        this.validateTime();
        this.validatePeople();
        const api = `${import.meta.env.VITE_API}/api/Bistro/insert`
        const response = await this.axios.post(api, this.reservations)
        if (response.data.success) {
          //發送簡訊要取得的資料
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

        } else {
          console.error('錯誤:', error)

        }
      }
    },
  },
  mounted() {
    this.$nextTick(() => {
      this.isMapVisible = true;
    });
    const today = new Date();
    const localToday = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    this.minDate = localToday.toLocaleDateString('zh-TW').split('/').join('-');
    const max = new Date(localToday);
    max.setDate(localToday.getDate() + 7);
    this.maxDate = max.toLocaleDateString('zh-TW').split('/').join('-');
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

.btn-outline-black {
  background-color: white;
  color: black;

}

.btn-black:hover {
  background-color: rgb(0, 0, 0);
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
</style>
