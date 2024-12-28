<template>
  <div class="container">
    <div class="row justify-content-center align-items-center min-vh-100">
      <div class="col-md-6">
        <div class="card shadow">
          <div class="card-body p-4">
            <h2 class="text-center mb-4">重設密碼</h2>
            <form @submit.prevent="handleSubmit">
              <!-- 密碼輸入框 -->
              <div class="mb-3 input-group">
                <input 
                  :type="isPasswordVisible ? 'text' : 'password'"
                  class="form-control"
                  v-model="password"
                  :class="{'is-invalid': validationErrors.passwordError}"
                  placeholder="請輸入新密碼"
                  maxlength="50"
                  minlength="6"
                  required
                />
                <span 
                  class="input-group-text" 
                  @click="togglePasswordVisibility" 
                  style="cursor: pointer;">
                  <i :class="isPasswordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                </span>
                <!-- 顯示密碼錯誤訊息 -->
                <small v-if="validationErrors.passwordError" class="text-danger mb-3">
                  {{ validationErrors.passwordError }}
                </small>
              </div>


              <!-- 確認密碼輸入框 -->
              <div class="mb-3">
                <input 
                  type="password"
                  v-model="confirmPassword"
                  class="form-control"
                  :class="{'is-invalid': validationErrors.confirmPasswordError}"
                  placeholder="請再次輸入新密碼"
                  required
                />
                <!-- 顯示確認密碼錯誤訊息 -->
                <small v-if="validationErrors.confirmPasswordError" class="text-danger">
                  {{ validationErrors.confirmPasswordError }}
                </small>
              </div>
              

              <button type="submit" class="btn btn-primary w-100">確認重設</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
export default {
  data() {
    return {
      password: '',
      confirmPassword: '',
      isPasswordVisible: false,
      validationErrors: {
        passwordError: '',
        confirmPasswordError: '',
      },
    };
  },
  methods: {
    togglePasswordVisibility() {
      this.isPasswordVisible = !this.isPasswordVisible;
    },
    async handleSubmit() {
      // 重置錯誤訊息
      this.validationErrors.passwordError = '';
      this.validationErrors.confirmPasswordError = '';

      // 密碼正則檢查
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;

      // 檢查密碼是否符合正則
      if (!passwordRegex.test(this.password)) {
        this.validationErrors.passwordError = '密碼需包含至少一個大寫字母、小寫字母、數字和特殊字符，且長度至少為 6 位';
        return;
      }

      // 檢查兩次密碼是否一致
      if (this.password !== this.confirmPassword) {
        this.validationErrors.confirmPasswordError = '兩次密碼輸入不一致';
        return;
      }
      try{
        const encryptedEmail = this.$route.params.encryptedEmail
        console.log(this.confirmPassword);
        
        await this.axios.post(`/api/reset-password/${encryptedEmail}/${this.confirmPassword}`)

        Swal.fire({
          icon: 'success',
          title: '密碼重設成功',
          showConfirmButton: false,
          timer: 1500,
        })
        this.$router.push('/')
      } catch (e) {
        Swal.fire('錯誤', e.response?.data.message || '重設失敗無此信箱帳號', 'error')
      }
    },
  },
}
</script>
