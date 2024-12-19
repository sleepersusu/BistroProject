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
                                <button type="button" class="btn btn-outline-primary"
                                    data-bs-toggle="modal" data-bs-target="#registerModal">註冊</button>
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

    <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="registerModalLabel">會員註冊</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form v-on:submit.prevent="sendRegister" method="post">
                        <div class="mb-3">
                            <input type="text" class="form-control" id="username" name="userName" value=""
                                placeholder="Name" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="userphone" name="userPhone" value=""
                                placeholder="Phone" required>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="useraccount" name="userAccount" value=""
                                placeholder="Email Account" required>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" id="userpassword" name="userPassword" value=""
                                placeholder="Password" required>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" id="userpassword" name="userPassword" value=""
                                placeholder="Repeat Password" required>
                        </div>
                        <div class="mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" value="" id="defaultCheck1">
                                <label class="form-check-label" for="defaultCheck1">
                                    記住密碼
                                </label>
                            </div>
                        </div>
                        <div class="mb-3 d-flex justify-content-between">
                            <button type="submit" class="btn btn-outline-primary w-100 btn-lg">註冊帳號</button>
                        </div>
                        <div class="mb-3 text-center">
                            <span>Already have an account? </span>
                            <a data-bs-toggle="modal" href="#loginModal"
                                class="text-secondary">Login</a>
                        </div>
                        <div class="division">
                            <div class="line l"></div>
                            <span>or</span>
                            <div class="line r"></div>
                        </div>
                    </form>
                    <div class="modal-footer d-flex justify-content-center w-100">
                        <button v-google-signin-button="clientId" class="google-signin-button"> Continue with
                            Google</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>
<script>
import { useUserStore } from '@/stores/userStore';
import Modal from 'bootstrap/js/dist/modal';
import { mapActions } from 'pinia';
export default {
    data() {
        return {
            loginModalElement:null,
            registerModalElement:null
        };
    },
    methods: {
        ...mapActions(useUserStore, ['submitLogin', 'submitRegister']),
        openLoginModal() {
            // 手動開啟登入模態框
            this.loginModalElement= document.getElementById('loginModal');
            this.registerModalElement = document.getElementById('registerModal');
            if (this.loginModalElement) {
            this.loginmodel = new Modal(this.loginModalElement);
            this.registermodel = new Modal(this.registerModalElement);
            this.loginmodel.show();
            }
        },
        sendLogin(event) {
            this.submitLogin(event)
            this.loginmodel.hide();
        },
        sendRegister(event) {
            this.submitRegister(event);
            this.registermodel.hide();
        }
    },
};
</script>
<style scoped>
.division {
    display: flex;
    align-items: center;
    justify-content: center;
    text-align: center;
    margin: 20px 0;
}

.line {
    flex-grow: 1;
    border-top: 1px solid #ccc;
    /* 设定横线的颜色 */
    margin: 0 10px;
    /* 设定横线与文字之间的间距 */
}

.l {
    margin-right: 10px;
    /* 左侧线条右侧的间距 */
}

.r {
    margin-left: 10px;
    /* 右侧线条左侧的间距 */
}

.division>span {
    font-weight: bold;
    color: #333;
    /* "or"文字颜色 */
    padding: 0 10px;
}

.modal-footer {
    border-top: none !important;
}

input.form-control {
    background-color: #f0f0f0;
    /* 淺灰色背景 */
    color: #333333;
    /* 深灰色文字 */
}

/* 將 placeholder 文字顏色設為深灰色 */
input::placeholder {
    color: #666666;
    /* 深灰色提示文字 */
}
</style>
