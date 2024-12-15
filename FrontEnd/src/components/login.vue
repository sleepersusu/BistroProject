<template>
    <!-- 顯示登入/註冊按鈕或頭像 -->
    <li class="nav-item ms-lg-5">
        <!-- 如果已登入，顯示頭像；否則顯示登入/註冊按鈕 -->
        <div v-if="!isLoggedIn" class="btn btn-outline-light" v-on:click="openLoginModal">登入 / 註冊</div>
        <div v-else class="d-flex align-items-center">
            <router-link to="/membercenter" class="d-flex align-items-center">
                <!-- 頭像 -->
                <div class="circle-avatar" :style="{ backgroundImage: `url(${userAvatar})` }"></div>
                <!-- 會員名稱 -->
                <span class="text-light ms-2">{{ username }}</span>
            </router-link>
        </div>
    </li>


    <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="loginModalLabel">會員登入</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form v-on:submit.prevent="submitLogin" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">帳號</label>
                            <input type="text" class="form-control" id="username" name="Account" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密碼</label>
                            <input type="password" class="form-control" id="password" name="Password" required>
                        </div>
                        <button type="submit" class="btn btn-primary">登入</button>
                        <div class="mt-3 text-center">
                            <button class="btn btn-link p-0" data-bs-toggle="modal"
                                data-bs-target="#registerModal">註冊</button>
                            <span> | </span>
                            <button class="btn btn-link p-0" data-bs-toggle="modal"
                                data-bs-target="#forgotPasswordModal">忘記密碼</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</template>
<script>
import { useUserStore } from '@/stores/userStore';
import { Modal } from 'bootstrap/dist/js/bootstrap.bundle.min';
const userStore = useUserStore();
export default {
    data() {
        return {
            setShadow: false,
            userAvatar: '', // 頭像圖片的URL，從資料庫撈取
            isLoggedIn: false, // 記錄用戶是否已經登入
        };
    },
    methods: {
        openLoginModal() {
            // 手動開啟登入模態框
            const modalElement = document.getElementById('loginModal');
            this.modal = new Modal(modalElement);
            this.modal.show(); // 显示模态框
        },
        async submitLogin(event) {
            try {
                // 後端服務
                let API_URL = `${userStore.getApiUrl()}/user/login`;
                let form = new FormData(event.target);
                let formData = {};
                form.forEach((value, key) => {
                    formData[key] = value;
                });
                let formJsonData = JSON.stringify(formData);
                //this.axios.post(API_URL, formData})
                //由於formData內容{"key":"value","key":"value"}格式也可以由AXIOS判斷
                const response = await this.axios.post(API_URL, formJsonData, {//或用正是手法
                    headers: {
                        'Content-Type': 'application/json',
                    }
                })
                let memberId = response.data.memberId
                localStorage.setItem('memberId', memberId);

                // 假設登入成功，後端返回 token
                const token = response.data.token;
                // 儲存 token（例如使用 localStorage）
                localStorage.setItem('authToken', token);

                this.username = response.data.memberName;
                let imgUrl = `${userStore.getApiUrl()}/api/member/photo/${memberId}`;
                let imgData = await this.axios.get(imgUrl,
                {
                    responseType: 'blob',
                })
                if(imgData.data.size === 0){
                    console.log('進默認')
                    this.userAvatar = "/public/images/avatar.jpg";
                }else{
                    this.userAvatar = URL.createObjectURL(imgData.data);
                }
                this.isLoggedIn = true;
            } catch (error) {
                // 處理錯誤，設登入失敗
                console.error('登入失敗', error);
                this.isLoggedIn = false;
            } finally{
                console.log("準備關閉")
                this.modal.hide();
            }
        },
        logout() {
            // 處理登出邏輯
            this.isLoggedIn = false;
            localStorage.clear();
            this.userAvatar = ''; // 清除頭像
        }
    },
};
</script>
<style>
.modal-backdrop {
    z-index: 1000 !important;
    /* 背景遮罩层的 z-index */
}

.modal {
    z-index: 1500 !important;
    /* 模态框的 z-index */
}

.circle-avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    background-size: cover;
    background-position: center;
    cursor: pointer;
}

.circle-avatar:hover {
    opacity: 0.8;
}
</style>