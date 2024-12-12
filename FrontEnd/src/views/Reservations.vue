<template>

  <div class="center" style="font-size: 30px;">訂位資訊</div>

  <div id="app" class="center">
    <div class="row g-3" style="width: 500px;">
      <div class="col-12">
        <label for="customerName" class="form-label">訂位人姓名</label>
        <input type="text" class="form-control frame" id="customerName" v-model="reservations.customerName" required
          aria-required="true" />
      </div>
      <div class="col-12">
        <label class="form-label col-md-2">性別</label>
        <input type="radio" name="customerGender" v-model="reservations.customerGender" required value="男" />男
        <input type="radio" name="customerGender" v-model="reservations.customerGender" required value="女" />女
      </div>
      <div class="col-12">
        <label for="contactPhone" class="form-label">電話</label>
        <input type="tel" class="form-control frame" id="contactPhone" v-model="reservations.contactPhone" required
          aria-required="true" />
      </div>
      <div class="col-md-6">
        <label for="numberPeople" class="form-label">訂位人數</label>
        <select class="form-control frame" id="numberPeople" v-model.number="reservations.numberPeople" required
          aria-required="true">
          <option value="0" selected>選擇人數</option>
          <option value="1">1</option>
          <option value="2">2</option>
          <option value="3">3</option>
          <option value="4">4</option>
        </select>
      </div>
      <div class="col-md-6">
        <label for="reservationDate" class="form-label">日期</label>
        <input type="date" class="form-control frame" id="reservationDate" v-model="reservations.reservationDate"
          required aria-required="true" />
      </div>

      <hr />
      
      <div class="col-md-6">
        <label for="startTime" class="form-label">時段</label>
        <div>
          <button v-for="time in availableTimeslots" :key="time" class="btn btn-outline-primary me-2"
            @click="handleClick(time)">
            {{ time }}
          </button>
        </div>
        <input type="text" v-model="reservations.selectedTime"/>
      </div>
      <!-- <input type="text" v-model="reservations.selectedTime" /> -->

      <div class="col-12">
        <label for="notes" class="form-label">備註</label>
        <textarea class="form-control frame" style="height: 200px" id="notes" v-model="reservations.notes"></textarea>
      </div>
      <div class="col-12">
        <button type="submit" class="btn btn-success">確認訂位</button>
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
        selectedTime : '',
        numberPeople: 0,
        notes: '',        
      },
      
      availableTimeslots: [],
    }
  },
  methods: {
    handleClick(time) {
    console.log("按鈕點擊時間:", time);  // 確認時間是否正確傳遞
    this.reservations.selectedTime = time;
    // this.$set(this.reservations, 'selectedTime', time); 
    // this.selectTime(time);  // 呼叫原來的選擇時間方法

  },

  // selectTime(time) {
  //     // 更新選中的時間段
  //   console.log("選擇的時間:", this.reservations.selectedTime);  // 確認這裡的值
  // },
    formatDate(date) {
      if (!date) return '';
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    
    async displayTimes() {
      if (this.reservations.numberPeople === 0 || !this.reservations.reservationDate) {
        this.availableTimeslots = []
        return
      }
      // 確保日期格式是 yyyy-MM-dd
      // const formattedDate = this.formatDate(this.reservations.reservationDate);

      try {
        const api = `${import.meta.env.VITE_API}/api/Bistro/remaining`;
        let res = await this.axios.post(api, {
          reservationDate: this.reservations.reservationDate,
          numberPeople: this.reservations.numberPeople,
        });

        console.log('API Response:', res.data); // 確認 API 回應結構
        this.availableTimeslots = res.data || []; 
      
      } catch (e) {
        console.error('這是錯的:', e);
        this.availableTimeslots = [];
      }
      
    },

    selectTime(time) {
      this.reservations.startTime = time
    },
  },
  computed: {},
  watch: {
    'reservations.numberPeople': function () {
      this.displayTimes();
    },
    'reservations.reservationDate': function () {
      this.displayTimes();
    },
  },
  created() {
    this.displayTimes()
  },
}
</script>

<style scoped>
.center {
  display: flex;
  justify-content: center;
}

.frame {
  border: 2px solid #eed9c4;
}
</style>
