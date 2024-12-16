<template>
  <div>
    <h1>兌換獎品列表</h1>

<div class="container d-flex">
  <ul class="d-flex flex-wrap">
    <li v-for="prize in pointPrizes" :key="prize.id" class="d-flex flex-column" style="width: 18rem; margin: 10px;">
      <div v-if="prize.rewardsStatus=='上架中'" class="card" style="height: 490px; position: relative;">
        <!-- 動態綁定圖片的 Base64 編碼 -->
        <img :src="'data:image/jpeg;base64,' + prize.base64Image" class="card-img-top w-100" alt="Prize Image"
        style="height: 300px; object-fit: cover;">
        <div class="card-body" style="overflow-y: auto; height: calc(100% - 300px);">
          <div class="d-flex justify-content-between">
            <h5 class="card-title">{{ prize.pointPrizesName }}</h5>
            <h5 class="card-text" style="color: red">{{ prize.pointPrizesPoints }} 點</h5>
          </div>
          <p class="card-text">{{ prize.pointPrizesDescription }}</p>
          <a href="#" class="btn btn-primary mt-auto" style="position: absolute; bottom: 16px;" @click="redeemPrize(prize)">兌換商品</a>
        </div>
      </div>
    </li>
  </ul>
</div>

<PointTotal ref="pointTotalRef"/>

  </div>
</template>

<script>
import PointTotal from '@/components/PointTotal.vue';

export default {
  components: {
    PointTotal
  },
  data() {
    return {
      pointPrizes: [],
    }
  },
  methods: {
    generateRandomCode() {
      const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
      let result = '';
      for (let i = 0; i < 6; i++) {
        const randomIndex = Math.floor(Math.random() * characters.length);
        result += characters[randomIndex];
      }
      return result;
    },

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

      const result = await Swal.fire({
        icon: 'question',
        title: `確定要兌換${prize.pointPrizesName}嗎?`,
        html: `<p>將會扣除 <span style="color: lightblue; font-size: 20px; font-weight: bold;">${prize.pointPrizesPoints}</span> 點，兌換後無法返還</p>`,
        showCancelButton: true,
        confirmButtonText: '兌換',
        cancelButtonText: '在想一下',
        reverseButtons: true,
      });

      if (result.isConfirmed) {
        // 生成優惠碼
        const promoCode = this.generateRandomCode();

        // 調用子組件的方法更新優惠碼和商品信息
        this.$refs.pointTotalRef.updateRedeemedPrize(
          prize.pointPrizesName,
          promoCode
        );

        const memberId = 1;  // 假資料，會員 ID 為 9
      if(memberId){
        // const pointPrizesId = prize.id;  // 商品 ID 來自按下的商品
        // const recordsDate = new Date().toISOString();  // 使用當前時間作為兌換日期

        const requestData = {
          memberId,
          pointPrizesId:prize.id,
          recordsDate:new Date().toISOString(),
          promoCode: promoCode  // 新增優惠碼
        };

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
              window.Swal.fire({
              toast: true,
              position: 'top-end',
              icon: 'success',
              title: `兌換成功！您已成功兌換 ${prize.pointPrizesName}`,
              timer: 1500,
              showConfirmButton: false,
              timerProgressBar: true,
            });

            

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
              console.error('減少商品數量失敗:', response2.status, await response2.text());
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
      } else {
        console.log('User canceled redemption');////////////////
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

<style scoped>
.card-body::-webkit-scrollbar {
  display: none;
}
</style>
