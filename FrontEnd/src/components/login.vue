<template>
    <!-- 顯示登入/註冊按鈕或頭像 -->
    <component is="script" src="https://accounts.google.com/gsi/client" @load="initSignIn" async />
    <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title fs-4" style="font-family: 'Chakra Petch', cursive !important;"
                        id="loginModalLabel">Nightly Sips</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form v-on:submit.prevent="sendLogin" method="post">
                        <div class="mb-3">
                            <label for="username" class="form-label">帳號</label>
                            <input type="text" class="form-control" id="username" v-model="loginForm.account"
                                value="user2@example.com" required />
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密碼</label>
                            <input type="password" class="form-control" id="password" v-model="loginForm.password"
                                required />
                        </div>
                        <div class="mb-3 form-check ms-2">
                            <input type="checkbox" class="form-check-input" id="rememberMe"
                                v-model="loginForm.rememberMe" />
                            <label class="form-check-label" for="rememberMe">記住密碼</label>
                        </div>
                        <div class="d-flex justify-content-between">
                            <button type="submit" class="btn btn-outline-primary">登入</button>
                            <div class="text-center">
                                <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal"
                                    data-bs-target="#registerModal">
                                    註冊
                                </button>
                                <span> | </span>
                                <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal"
                                    data-bs-target="#forgotPasswordModal">
                                    忘記密碼
                                </button>
                            </div>
                        </div>
                    </form>
                    <div class="division">
                        <div class="line l"></div>
                        <span>or</span>
                        <div class="line r"></div>
                    </div>
                    <div class="d-flex justify-content-center w-100">
                        <div id="googlebutton" data-itp_support="true" data-bs-dismiss="modal">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title fs-4" style="font-family: 'Chakra Petch', cursive !important;"
                        id="loginModalLabel">Nightly Sips</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form v-on:submit.prevent="sendRegister" method="post">
                        <div class="mb-3">
                            <input type="text" class="form-control" id="username" v-model="registForm.userName" value=""
                                :class="{ 'is-invalid': validationErrors.userName }" placeholder="請輸入姓名" maxlength="15"
                                required>
                            <small v-if="validationErrors.nameError" class="text-danger">{{ validationErrors.nameError
                                }}</small>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="userphone" v-model="registForm.userPhone"
                                value="" :class="{ 'is-invalid': validationErrors.phoneError }" placeholder="請輸入聯絡手機"
                                maxlength="10" required>
                            <small v-if="validationErrors.phoneError" class="text-danger">{{ validationErrors.phoneError
                                }}</small>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="useraccount" v-model="registForm.userEmail"
                                value="" :class="{ 'is-invalid': validationErrors.emailError }"
                                placeholder="請輸入要申請的信箱帳號" required>
                            <small v-if="validationErrors.emailError" class="text-danger">{{ validationErrors.emailError
                                }}</small>
                        </div>
                        <div class="mb-3">
                            <div class="input-group">
                                <input :type="isPasswordVisible ? 'text' : 'password'" class="form-control"
                                    id="userpassword" v-model="registForm.userPassword" value=""
                                    :class="{ 'is-invalid': validationErrors.passwordError }" placeholder="請輸入密碼"
                                    maxlength="50" minlength="6" required>
                                <span class="input-group-text" @click="togglePasswordVisibility"
                                    style="cursor: pointer;">
                                    <i :class="isPasswordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                                </span>
                            </div>
                            <small v-if="validationErrors.passwordError" class="text-danger">{{
                                validationErrors.passwordError }}</small>
                        </div>

                        <!-- 二次密碼欄位 -->
                        <div class="mb-3">
                            <div class="input-group">
                                <input :type="isSecondPasswordVisible ? 'text' : 'password'" class="form-control"
                                    id="secondpassword" v-model="registForm.secondPassword" value=""
                                    :class="{ 'is-invalid': validationErrors.secondPasswordError }"
                                    placeholder="請輸入二次密碼驗證" minlength="6" required>
                                <span class="input-group-text" @click="toggleSecondPasswordVisibility"
                                    style="cursor: pointer;">
                                    <i :class="isSecondPasswordVisible ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                                </span>
                            </div>
                            <small v-if="validationErrors.secondPasswordError" class="text-danger">{{
                                validationErrors.secondPasswordError }}</small>
                        </div>
                        <div class="mb-3 d-flex justify-content-between">
                            <button type="submit" class="btn btn-outline-primary w-100 btn-lg">註冊帳號</button>
                        </div>
                    </form>
                    <div class="text-center">
                        <span>已經有帳號了? </span>
                        <a data-bs-toggle="modal" href="#loginModal" class="text-secondary">前往登入</a>
                    </div>
                    <div class="division">
                        <div class="line l"></div>
                        <span>or</span>
                        <div class="line r"></div>
                    </div>
                    <div class="d-flex justify-content-center w-100">
                        <div id="googlebutton2" data-itp_support="true" data-bs-dismiss="modal">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="forgotPasswordModal" tabindex="-1" aria-labelledby="forgotPasswordModalLabel"
        aria-hidden="false">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title fs-4" style="font-family: 'Chakra Petch', cursive !important;"
                        id="forgotPasswordModalLabel">Nightly Sips</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form v-on:submit.prevent="handleForgot" method="post">
                        <div class="mb-3">
                            <label for="restPasswordEmail" class="form-label">發送驗證信箱</label>
                            <input type="text" class="form-control" id="restPasswordEmail"
                                v-model="userRestEmail" value=""
                                :class="{ 'is-invalid': restEmailisError }" placeholder="請輸入要申請的信箱帳號"
                                required>
                            <small v-if="restEmailisError" class="text-danger">{{ restEmailisError
                                }}</small>
                        </div>
                        <div class="mb-3 text-center">
                            <span>回到登入或前往註冊? </span>
                            <a data-bs-toggle="modal" href="#loginModal" class="text-secondary">登入</a>
                            <span> | </span>
                            <a data-bs-toggle="modal" href="#registerModal" class="text-secondary">註冊</a>
                        </div>
                        <div class="d-flex justify-content-center">
                            <button type="submit" class="btn btn-outline-primary w-80">發送信件</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</template>
<script>
import { useUserStore } from '@/stores/userStore'
import { mapActions } from 'pinia'
import Modal from 'bootstrap/js/dist/modal'
export default {
    data() {
        return {
            isPasswordVisible: false,  // 控制顯示密碼
            isSecondPasswordVisible: false,  // 控制二次密碼顯示
            loginModalElement: null,
            registerModalElement: null,
            isValid: true,
            loginForm: {
                account: '',
                password: '',
                rememberMe: false
            },
            registForm: {
                userName: '',
                userPhone: '',
                userEmail: '',
                userPassword: '',
                secondPassword: '',
            },
            validationErrors: {
                nameError: '',
                phoneError: '',
                emailError: '',
                passwordError: '',
                secondPasswordError: '',
            },
            userRestEmail:'',
            restEmailisValid:true,
            restEmailisError:'',
        }
    },
    mounted() {
        const savedCredentials = localStorage.getItem('userCredentials')
        if (savedCredentials) {
            const { account, password } = JSON.parse(savedCredentials)
            this.loginForm.account = account
            this.loginForm.password = password
            this.loginForm.rememberMe = true
        }
    },
    watch: {
        // 當任何一個字段變化時，檢查表單的有效性
        'registForm.userName'(newVal) {
            const nameRegex = /^[a-zA-Z\u4e00-\u9fa5\s]{2,15}$/;
            if (!newVal.trim()) {
                this.validationErrors.nameError = '未填寫姓名';
                this.isValid = false;
            } else if (!nameRegex.test(newVal)) {
                this.validationErrors.nameError = '姓名格式錯誤，請輸入2-15個字的中文或英文';
                this.isValid = false;
            } else {
                this.validationErrors.nameError = '';
                this.checkFormValidity();
            }
        },
        'registForm.userPhone'(newVal) {
            const phoneRegex = /^09\d{8}$/;
            if (!newVal.trim()) {
                this.validationErrors.phoneError = '電話未填寫';
                this.isValid = false;
            } else if (!phoneRegex.test(newVal)) {
                this.validationErrors.phoneError = '電話格式錯誤，需以09開頭，請輸入正確的手機號碼';
                this.isValid = false;
            } else {
                this.validationErrors.phoneError = '';
                this.checkFormValidity();
            }
        },
        'registForm.userEmail'(newVal) {
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            
            if (!newVal.trim()) {
                this.validationErrors.emailError = '信箱未填寫';
                this.isValid = false;
            } else if (!emailRegex.test(newVal)) {
                this.validationErrors.emailError = '信箱格式錯誤，需以user@example.com格式，請輸入正確的信箱';
                this.isValid = false;
            } else {
                this.validationErrors.emailError = '';
                this.checkFormValidity();
            }
        },

        'registForm.userPassword'(newVal) {
            const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
            if (!newVal.trim()) {
                this.validationErrors.passwordError = '未填入密碼';
                this.isValid = false;
            } else if (!passwordRegex.test(newVal)) {
                this.validationErrors.passwordError = '密碼格式錯誤，須含大小寫英文，1位特殊符號';
                this.isValid = false;
            } else {
                this.validationErrors.passwordError = '';
                this.checkFormValidity();
            }
        },
        'registForm.secondPassword'(newVal) {
            this.checkPasswords();
        },

        'userRestEmail'(newVal) {
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;           
            if (!newVal.trim()) {
                this.restEmailisError = '信箱未填寫';
                this.restEmailisValid = false;
            } else if (!emailRegex.test(newVal)) {
                this.restEmailisError = '信箱格式錯誤，需以user@example.com格式，請輸入正確的信箱';
                this.restEmailisValid = false;
            } else {
                this.restEmailisError = '';
                this.checkRestEmailFormValidity();
            }
        },
    },
    methods: {
        ...mapActions(useUserStore, ['submitLogin', 'submitRegister', 'handleGoogleLogin','submitRestPasswordEmail']),
        checkFormValidity() {
            const { userName, userPhone, userEmail, userPassword } = this.registForm;
            if (userName && userPhone && userEmail && userPassword &&
                !this.validationErrors.nameError &&
                !this.validationErrors.phoneError &&
                !this.validationErrors.emailError &&
                !this.validationErrors.passwordError) {
                this.isValid = true;
            } else {
                this.isValid = false;
            }
        },
        checkRestEmailFormValidity(){
            if (this.userRestEmail &&!this.restEmailisValid
                ) {
                    console.log('驗證PASS');
                this.restEmailisValid = true;
            } else {
                console.log('驗證Fail');
                this.restEmailisValid = false;
            }
        },
        checkPasswords() {
            if (this.registForm.userPassword !== this.registForm.secondPassword) {
                this.validationErrors.secondPasswordError = '密碼與二次密碼不一致';
                this.isValid = false;
            } else {
                this.validationErrors.secondPasswordError = '';
                this.isValid = true;  // 確保當兩者一致時，表單有效
            }
        },
        openLoginModal() {
            // 手動開啟登入模態框
            this.loginModalElement = document.getElementById('loginModal')
            this.registerModalElement = document.getElementById('registerModal')
            if (this.loginModalElement) {
                this.loginmodel = new Modal(this.loginModalElement)
                this.registermodel = new Modal(this.registerModalElement)
                this.loginmodel.show()
            }
        },
        async sendLogin() {
            if (this.loginForm.rememberMe) {
                localStorage.setItem('userCredentials', JSON.stringify({
                    account: this.loginForm.account,
                    password: this.loginForm.password
                }))
            } else {
                localStorage.removeItem('userCredentials')
            }
            await this.submitLogin(this.loginForm)
            this.loginmodel.hide()
        },
        sendRegister() {
            if (!this.isValid) {
                {
                    Swal.fire({
                        toast: false,
                        position: 'top',
                        icon: 'warning',
                        iconColor: 'red',
                        title: `資料格式驗證失敗！`,
                        timer: 1500,
                        showConfirmButton: false,
                        timerProgressBar: true,
                    });
                    return;
                }
            } else {
                this.submitRegister(this.registForm)
                this.registermodel.hide()
            }
        },
        closeLoginmodel() {//未知新增
            //會員獎品會用到
            this.loginmodel.hide()
        },

        handleForgot() {
            if(!this.restEmailisValid){
                this.submitRestPasswordEmail(this.userRestEmail)
            }else{
                Swal.fire({
                        toast: false,
                        position: 'top',
                        icon: 'warning',
                        iconColor: 'red',
                        title: `資料格式驗證失敗！`,
                        timer: 1500,
                        showConfirmButton: false,
                        timerProgressBar: true,
                    })
            }
        },
        initSignIn() {
            if (window.google && window.google.accounts && window.google.accounts.id) {
                google.accounts.id.initialize({
                    client_id: "196532514305-0fs4cjqo3g147um40o75umq24apsiho6.apps.googleusercontent.com",
                    callback: (response) => {
                        console.log('登入成功，回應內容：', response);
                        this.handleGoogleLogin(response.credential)
                    },
                    redirect_uri: "http://localhost:5173/callback"
                })
                // 渲染 Google 按鈕
                google.accounts.id.renderButton(
                    document.getElementById("googlebutton"),
                    {
                        type: "standard",          // 樣式類型
                        text: "signin_with",       // 顯示文本
                        theme: "filled_black",     // 按鈕樣式
                        size: "large",             // 按鈕大小
                        logo_alignment: "left",    // 圖標對齊方式
                        shape: "pill"              // 圓角樣式
                    }
                )
                google.accounts.id.renderButton(
                    document.getElementById("googlebutton2"),
                    {
                        type: "standard",          // 樣式類型
                        text: "signin_with",       // 顯示文本
                        theme: "filled_black",     // 按鈕樣式
                        size: "large",             // 按鈕大小
                        logo_alignment: "left",    // 圖標對齊方式
                        shape: "pill"              // 圓角樣式
                    }
                )

            } else {
                console.error("Google API client not loaded.")
            }
        },
        togglePasswordVisibility() {
            this.isPasswordVisible = !this.isPasswordVisible;  // 切換密碼顯示狀態
        },
        toggleSecondPasswordVisibility() {
            this.isSecondPasswordVisible = !this.isSecondPasswordVisible;  // 切換二次密碼狀態
        },
    },
    onMounted() {
        if (typeof google !== 'undefined') {
            initSignIn();
        }
    }
}
</script>


<style scoped>
.form-check-input {
    background-color: #ffffff;
    border-color: #404040;
}

.form-check-input:checked {
    background-color: #404040;
    border-color: #4a4a4a;
}

.form-check-input:focus {
    border-color: #404040;
    box-shadow: 0 0 0 0.25rem rgba(64, 64, 64, 0.25);
}

.form-check-label {
    color: #1a1a1a;
}


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
    margin: 0 10px;
    /* 橫線與文字距離 */
}

.l {
    margin-right: 10px;
    /* 左側線條 */
}

.r {
    margin-left: 10px;
    /* 右側線條 */
}

.division>span {
    font-weight: bold;
    color: #333;
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
}
</style>
