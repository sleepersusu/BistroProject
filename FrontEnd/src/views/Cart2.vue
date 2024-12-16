<template>
  <div>
    <input
      v-model="enteredPromoCode"
      placeholder="請輸入優惠碼"
    />
    <button @click="validatePromoCode">驗證優惠碼</button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      enteredPromoCode: '',
    }
  },
  methods: {
    async validatePromoCode() {
      try {
        const response = await fetch(`${import.meta.env.VITE_API}/api/validatePromoCode`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            promoCode: this.enteredPromoCode
          }),
        });

        if (response.ok) {
          const data = await response.json();
          if (data.valid) {
            // 優惠碼有效，可以應用優惠
            this.applyPromoCodeDiscount(data.prize);
          } else {
            // 優惠碼無效
            alert('無效的優惠碼');
          }
        }
      } catch (error) {
        console.error('驗證優惠碼時出錯', error);
      }
    },
    applyPromoCodeDiscount(prize) {
      alert('成功兌換');
    }
  }
}
</script>
