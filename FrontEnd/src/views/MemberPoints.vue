<template>
  <div>
    <h1>兌換獎品列表</h1>

<div class="container">
  <ul class="d-flex flex-wrap">
    <li v-for="prize in pointPrizes" :key="prize.id" class="d-flex flex-column" style="width: 18rem; margin: 10px;">
      <div class="card" style="height: 490px; position: relative;">
        <!-- 動態綁定圖片的 Base64 編碼 -->
        <img :src="'data:image/jpeg;base64,' + prize.base64Image" class="card-img-top w-100" alt="Prize Image"
        style="height: 300px; object-fit: cover;">
        <div class="card-body" style="overflow-y: auto; height: calc(100% - 300px);">
          <h5 class="card-title">{{ prize.pointPrizesName }}</h5>
          <p class="card-text">{{ prize.pointPrizesDescription }}</p>
          <a href="#" class="btn btn-primary mt-auto" style="position: absolute; bottom: 16px;" @click="redeemPrize(prize)">兌換商品</a>
        </div>
      </div>
    </li>
  </ul>
</div>


  </div>
</template>

<script>
export default {
  data() {
    return {
      pointPrizes: [],
    }
  },
  methods: {
    async getPointPrizes() {
      const api = `${import.meta.env.VITE_API}/api/pointPrizes`;
      try {
        const response = await fetch(api);
        if (response.ok) {
          const data = await response.json();
          this.pointPrizes = data;
        } else {
          console.error('Failed to fetch point prizes:', response.status);
        }
      } catch (error) {
        console.error('Error fetching point prizes:', error);
      }
    },

    async redeemPrize(prize) {
      const memberId = 5;  // 假資料，會員 ID 為 9
      if(memberId){
        const pointPrizesId = prize.id;  // 商品 ID 來自按下的商品
        const recordsDate = new Date().toISOString();  // 使用當前時間作為兌換日期

        const requestData = {
          memberId,
          pointPrizesId,
          recordsDate,
        };

      // 顯示兌換成功的彈窗
      window.Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'success',
        title: `兌換成功！您已成功兌換 ${prize.pointPrizesName}`,
        timer: 1500,
        showConfirmButton: false,
        timerProgressBar: true,
      });

      try {
        const api = `${import.meta.env.VITE_API}/api/pointRecord`;
        const response = await fetch(api, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestData),  // 將資料轉換為 JSON 格式發送
        });

            if (response.ok) {
              console.log('兌換紀錄成功');

              const api2 = `${import.meta.env.VITE_API}/api/MinusOnePrizesCount`;
              const response2 = await fetch(api2, {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
              },
              body: JSON.stringify(requestData),  // 將資料轉換為 JSON 格式發送
            });
                if (response2.ok) {
                  console.log('減少商品數量成功');
                }else {
                  console.error('Failed to decrease prize count:', response2.status, await response2.text());
                }

        } else {
          console.error('Failed to record point:', response.status);
        }
      } catch (error) {
        console.error('Error sending request:', error);
      }
    }else {
      window.Swal.fire({
        title: '兌換商品需要點數',
        text: '請先登入會員，才能進行兌換。',
        icon: 'warning',
        showCancelButton: true, // 顯示取消按鈕
        confirmButtonText: '跳轉至登入',
        cancelButtonText: '取消',
        reverseButtons: true, // 反轉按鈕的顯示順序
      }).then((result) => {
        if (result.isConfirmed) {
          this.$router.push({ path: 'Login' });
        } else {
          console.log('User canceled the action');
        }
      });
    }

      }

  },
  computed: {},
  watch: {},
  created() {
    this.getPointPrizes();
  },
}
</script>

<style>
.card-body::-webkit-scrollbar {
  display: none;
}
</style>
