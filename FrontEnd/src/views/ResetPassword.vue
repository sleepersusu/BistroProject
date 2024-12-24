<template>
  <div class="container">
    <div class="row justify-content-center align-items-center min-vh-100">
      <div class="col-md-6">
        <div class="card shadow">
          <div class="card-body p-4">
            <h2 class="text-center mb-4">重設密碼</h2>
            <form @submit.prevent="handleSubmit">
              <div class="mb-3">
                <input
                  type="password"
                  v-model="password"
                  class="form-control"
                  placeholder="請輸入新密碼"
                  required
                />
              </div>
              <div class="mb-3">
                <input
                  type="password"
                  v-model="confirmPassword"
                  class="form-control"
                  placeholder="請再次輸入新密碼"
                  required
                />
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
    }
  },
  methods: {
    async handleSubmit() {
      if (this.password !== this.confirmPassword) {
        Swal.fire('錯誤', '兩次密碼輸入不一致', 'error')
        return
      }

      try {
        const encryptedEmail = this.$route.params.encryptedEmail
        await this.axios.post(`/api/reset-password/${encryptedEmail}/${this.password}`)

        Swal.fire({
          icon: 'success',
          title: '密碼重設成功',
          showConfirmButton: false,
          timer: 1500,
        })
        this.$router.push('/')
      } catch (e) {
        Swal.fire('錯誤', e.response?.data.message || '重設失敗', 'error')
      }
    },
  },
}
</script>
