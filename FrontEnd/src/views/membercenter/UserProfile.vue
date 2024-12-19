<template>
    <div class="container d-flex justify-content-center align-items-center">
        <div class="row g-5 w-100">
            <div class="col-md-7 col-lg-8 mx-auto">
                <BannerTop :title="'Member Profile'"></BannerTop>
                <form class="needs-validation" v-on:submit.prevent="sendLogin" novalidate="">
                    <div class="row g-3">
                        <div class="col-12 d-flex justify-content-center">
                            <div class="circle-avatar" v-on:click=""
                                :style="{ backgroundImage: `url(${memberprofile.userAvatar})` }"></div>
                        </div>
                        <div class="col-12">
                            <label for="firstName" class="form-label">姓名</label>
                            <input type="text" class="form-control" id="firstName" placeholder="輸入姓名" value=""
                                required="">
                            <div class="invalid-feedback">
                                Valid first name is required.
                            </div>
                        </div>
                        <div class="col-12">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" placeholder="you@example.com"
                                required="">
                            <div class="invalid-feedback">
                                Please enter a valid email address for shipping updates.
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="email" class="form-label">Phone</label>
                            <input type="email" class="form-control" id="email" placeholder="輸入電話" required="">
                            <div class="invalid-feedback">
                                Please enter a valid Phone for shipping updates.
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="gender" class="form-label">性別</label>
                            <div id="gender" class="d-flex">
                                <div class="form-check me-3">
                                    <input class="form-check-input" type="radio" name="gender" id="male" value="0"
                                        required>
                                    <label class="form-check-label" for="male">
                                        男
                                    </label>
                                </div>
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" name="gender" id="female" value="1"
                                        required>
                                    <label class="form-check-label" for="female">
                                        女
                                    </label>
                                </div>
                            </div>
                            <div class="invalid-feedback">
                                Please select a gender.
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="country" class="form-label">城市</label>
                            <select class="form-select" id="country" required>
                                <option value="">Choose...</option>
                                <option>United States</option>
                            </select>
                            <div class="invalid-feedback">
                                Please select a valid country.
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="state" class="form-label">地區</label>
                            <select class="form-select" id="state" required>
                                <option value="">Choose...</option>
                                <option>California</option>
                            </select>
                            <div class="invalid-feedback">
                                Please provide a valid state.
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="address" class="form-label">地址</label>
                            <input type="text" class="form-control" id="address" placeholder="1234 Main St" required="">
                            <div class="invalid-feedback">
                                Please enter your shipping address.
                            </div>
                        </div>
                        <div class="col-12">
                            <label for="address" class="form-label">出生日期</label>
                            <input type="date" class="form-control" id="address" placeholder="1234 Main St" required="">
                            <div class="invalid-feedback">
                                Please enter your shipping address.
                            </div>
                        </div>

                    </div>
                    <!-- <h4 class="mb-3">Payment</h4>
                        <div class="my-3">
                        <div class="form-check">
                            <input id="credit" name="paymentMethod" type="radio" class="form-check-input" checked="" required="">
                            <label class="form-check-label" for="credit">Credit card</label>
                        </div>
                        <div class="form-check">
                            <input id="debit" name="paymentMethod" type="radio" class="form-check-input" required="">
                            <label class="form-check-label" for="debit">Debit card</label>
                        </div>
                        <div class="form-check">
                            <input id="paypal" name="paymentMethod" type="radio" class="form-check-input" required="">
                            <label class="form-check-label" for="paypal">PayPal</label>
                        </div>
                    </div> -->
                    <hr class="my-4">
                    <button class="w-100 btn btn-primary btn-lg" type="submit">儲存設定</button>
                </form>
            </div>
        </div>
    </div>

</template>
<script>
import { useUserStore } from '@/stores/userStore'
import { mapState,mapActions} from 'pinia'
import BannerTop from '@/components/BannerTop.vue';
export default {
    components: {
        BannerTop
    },
    computed: {
        ...mapState(useUserStore, ['memberprofile']),
    },
    methods: {
        sendLogin(event) {
            console.log(event);

        }
    }
}
</script>
<style scoped>
.circle-avatar {
  width: 100px; 
  height: 100px; 
  border-radius: 50%;
  background-size: cover;
  background-position: center;
  cursor: pointer;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* Adding shadow */
  transition: box-shadow 0.3s ease; /* Smooth transition for shadow */
}


.circle-avatar:hover {
  opacity: 0.8;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3); /* Darker shadow on hover */
}
</style>

