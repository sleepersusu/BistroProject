<template>
    <div class="container">
        <BannerTop :title="'Member Center'"></BannerTop>
        <div class="row">
            <div class="col-lg-6 col-12 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-center align-items-center" style="width: 150px;">
                            <font-awesome-icon :icon="['fas', 'user-pen'] "size="5x"/>

                        </div>
                        <div class="container">
                            <h5 class="card-title">會員資料</h5>
                            <p class="card-text">在這裡可以查看編輯你的資訊。</p>
                            <router-link class="btn btn-primary" to="/membercenter/profile">查看會員資料</router-link>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 col-12 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-center align-items-center" style="width: 150px;">
                            <font-awesome-icon :icon="['fas', 'dollar-sign']" size="5x"/>
                        </div>
                        <div class="container">
                            <h5 class="card-title">我的訂單</h5>
                            <p class="card-text">查看你所有歷史訂單訊息。</p>
                            <router-link class="btn btn-primary" to="/membercenter/orders">查看訂單</router-link>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-lg-6 col-12 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-center align-items-center" style="width: 150px;">
                            <font-awesome-icon :icon="['fas', 'comment-dots']"size="5x" />
                        </div>
                        <div class="container">
                            <h5 class="card-title">我的評論</h5>
                            <p class="card-text">查看你的餐點評論。</p>
                            <router-link class="btn btn-primary" to="/membercenter/comment">查看評論</router-link>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-lg-6 col-12 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-center align-items-center" style="width: 150px;">
                            <font-awesome-icon :icon="['fas', 'dice-five']"size="5x" />
                        </div>
                        <div class="container">
                            <h5 class="card-title">抽獎結果</h5>
                            <p class="card-text">參加抽獎活動，查看抽獎活動結果，贏得好禮相送。</p>
                            <router-link class="btn btn-primary" to="/membercenter/lotteryresult">查看抽獎結果</router-link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import BannerTop from '@/components/BannerTop.vue';
import { useUserStore } from '@/stores/userStore'
import { mapState } from 'pinia'
export default {
    components: {
        BannerTop
    },
    computed: {
        ...mapState(useUserStore, ['memberprofile']),
    },
};
</script>

<style scoped>
.card {
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.card-body {
    display: flex;
    justify-content: space-between;
}

.card-title {
    font-size: 1.25rem;
    font-weight: bold;
}

.card-text {
    margin-bottom: 1.25rem;
}

.btn {
    width: 100%;
    text-align: center;
}

@media (max-width: 991px) {
    .row>div {
        margin-bottom: 1.5rem;
    }
}
</style>