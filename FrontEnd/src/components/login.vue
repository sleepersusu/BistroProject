<template>
    <!-- 顯示登入/註冊按鈕或頭像 -->

    <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="loginModalLabel">會員登入</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form v-on:submit.prevent="submitLogin" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">帳號</label>
                            <input type="text" class="form-control" id="username" name="Account"
                                value="user2@example.com" required>
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密碼</label>
                            <input type="password" class="form-control" id="password" name="Password" value="password2"
                                required>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button type="submit" class="btn btn-outline-primary">登入</button>
                            <div class="text-center">
                                <button class="btn btn-outline-primary" data-bs-toggle="modal"
                                    data-bs-target="#registerModal">註冊</button>
                                <span> | </span>
                                <button class="btn btn-outline-primary" data-bs-toggle="modal"
                                    data-bs-target="#forgotPasswordModal">忘記密碼</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</template>
<script>
import { useUserStore } from '@/stores/userStore';
import  Modal  from 'bootstrap/js/dist/modal';
const userStore = useUserStore();
export default {
    data() {
        return {
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
                let userAvatar = '';
                let username = response.data.memberName;
                let imgUrl = `${userStore.getApiUrl()}/api/member/photo/${memberId}`;
                let imgData = await this.axios.get(imgUrl,
                    {
                        responseType: 'blob',
                    })
                if (imgData.data.size === 0) {
                    userAvatar = "/public/images/avatar.jpg";
                } else {
                    userAvatar = URL.createObjectURL(imgData.data);
                }
                this.$emit('user-login', userAvatar, username)
            } catch (error) {
                // 處理錯誤，設登入失敗
                console.error('登入失敗', error);
                userStore.clearLoggedIn();
            } finally {
                console.log("準備關閉")
                this.modal.hide();
            }
        }
    },
};
</script>
<style scoped>
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
