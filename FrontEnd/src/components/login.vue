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
                            <input type="text" class="form-control" id="username" name="account"
                                value="user2@example.com" required />
                        </div>
                        <div class="mb-3">
                            <label for="password" class="form-label">密碼</label>
                            <input type="password" class="form-control" id="password" name="password" value="password2"
                                required />
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
                                    data-bs-target="#forgotPasswordModal" @click="handleForgot">
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
                                :class="{ 'is-invalid': validationErrors.userName }" placeholder="請輸入姓名"
                                maxlength="15" :required>
                            <small v-if="validationErrors.nameError" class="text-danger">{{ validationErrors.nameError }}</small>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="userphone" v-model="registForm.userPhone"
                                value="" :class="{ 'is-invalid': validationErrors.phoneError }"
                                placeholder="請輸入聯絡手機" maxlength="10" required>
                            <small v-if="validationErrors.phoneError" class="text-danger">{{ validationErrors.phoneError }}</small>
                        </div>
                        <div class="mb-3">
                            <input type="text" class="form-control" id="useraccount" name="userAccount" value="" :class="{ 'is-invalid': validationErrors.phoneError }"
                                placeholder="請輸入要申請的信箱帳號" required>
                            <small v-if="validationErrors.phoneError" class="text-danger">{{ validationErrors.phoneError }}</small>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" id="userpassword" name="userPassword" value="" :class="{ 'is-invalid': validationErrors.phoneError }"
                                placeholder="請輸入密碼" required>
                            <small v-if="validationErrors.phoneError" class="text-danger">{{ validationErrors.phoneError }}</small>
                        </div>
                        <div class="mb-3">
                            <input type="password" class="form-control" id="userpassword" name="userPassword" value="" :class="{ 'is-invalid': validationErrors.phoneError }"
                                placeholder="請輸入二次密碼驗證" required>
                            <small v-if="validationErrors.phoneError" class="text-danger">{{ validationErrors.phoneError }}</small>
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

</template>
<script>
import { useUserStore } from '@/stores/userStore'
import { mapActions } from 'pinia'
import Modal from 'bootstrap/js/dist/modal'
export default {
    data() {
        return {
            loginModalElement: null,
            registerModalElement: null,
            registForm: {
                userName: '',
                userPhone: '',
                userEmail:'',
                userPassword:'',
            },
            validationErrors:{
                nameError:'',
                phoneError:'',
            }
        }
    },
    watch: {
        'registForm.userName'(newVal) {
            const nameRegex = /^[a-zA-Z\u4e00-\u9fa5\s]{2,15}$/
            if (!newVal.trim()) {
                this.validationErrors.nameError = '請輸入姓名';
            } else if(!nameRegex.test(newVal)){
                this.validationErrors.nameError = '姓名格式錯誤，請輸入2-15個字的中文或英文';
            }
        },
        'registForm.userPhone'(newVal) {
            const phoneRegex = /^09\d{8}$/
            if(!newVal.trim()){
                this.phoneError = '電話未填寫'
            }else if (!phoneRegex.test(newVal)) {
                // this.validationErrors.phoneError = '請輸入手機號碼';
                this.validationErrors.phoneError = '電話格式錯誤，請輸入正確的手機號碼'
                console.log(this.validationErrors.phoneError)
            } else {
                this.validationErrors.phoneError = '';
            }
        },
        'registForm.userEmail'(newVal) {
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        },
        'registForm.userEmail'(newVal) {
            const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        },
    },
    methods: {
        ...mapActions(useUserStore, ['submitLogin', 'submitRegister', 'handleGoogleLogin']),
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
        sendLogin(event) {
            this.submitLogin(event)
            this.loginmodel.hide()
        },
        sendRegister(event) {
            let checkValue = this.validateForm()
            if (!checkValue) {
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
                    })
                    return
                }
            } else {
                this.submitRegister(event)
                Swal.fire({
                    toast: false,
                    position: 'top',
                    icon: 'success',
                    iconColor: 'black',
                    title: `資料已更新！`,
                    timer: 1500,
                    showConfirmButton: false,
                    timerProgressBar: true,
                })
                this.registermodel.hide()
            }
        },
        closeLoginmodel() {//未知新增
            //會員獎品會用到
            this.loginmodel.hide()
        },
        validateForm() {
            let isValid = true;
            const phoneRegex = /^09\d{8}$/ // 09開頭,8位數字
            if (!this.registForm.userPhone.trim()) {
                isValid = false;
                this.phoneError = '電話為必填'
            } else if (!phoneRegex.test(this.registForm.userPhone)) {
                isValid = false;
                this.phoneError = '電話格式錯誤，請輸入正確的手機號碼'
            } else {
                isValid = false;
                this.phoneError = ''
            }
            return isValid;
        },

        handleForgot() {
            Swal.fire({
                title: '請輸入您的電子信箱',
                input: 'text',
                inputAttributes: {
                    autocapitalize: 'off',
                },
                showCancelButton: true,
                confirmButtonText: 'Look up',
                showLoaderOnConfirm: true,
                preConfirm: async (email) => {
                    try {
                        const response = await this.axios.post(`/api/forgot-password/${email}`)
                        return response.data
                    } catch (e) {
                        console.log(e)
                        Swal.showValidationMessage(e.response?.data.message || '發送失敗')
                    }
                },
                allowOutsideClick: () => !Swal.isLoading(),
            }).then((result) => {
                if (result.isConfirmed) {
                    Swal.fire({
                        toast: true,
                        position: 'top-end',
                        icon: 'success',
                        iconColor: 'black',
                        title: `重設密碼連結已發送到您的電子信箱`,
                        timer: 2500,
                        showConfirmButton: false,
                        timerProgressBar: true,
                    })
                }
            })
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
    },
    onMounted() {
        if (typeof google !== 'undefined') {
            initSignIn();
        }
    }
}
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
