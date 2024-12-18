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
                    <form v-on:submit.prevent="sendLogin" method="post">
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
import { mapActions } from 'pinia';
export default {
    data() {
        return {
        };
    },
    methods: {
        ...mapActions(useUserStore,['submitLogin']),
        openLoginModal() {
            // 手動開啟登入模態框
            const modalElement = document.getElementById('loginModal');
            this.modal = new Modal(modalElement);
            this.modal.show(); // 显示模态框
        },
        sendLogin(event){
            this.submitLogin(event)
            this.modal.hide();
        }
    },
};
</script>
