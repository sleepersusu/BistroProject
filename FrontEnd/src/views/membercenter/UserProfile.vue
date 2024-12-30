<template>
  <div class="container d-flex justify-content-center align-items-center">
    <div class="row g-5 w-100">
      <div class="col-md-7 col-lg-8 mx-auto">
        <BannerTop :title="'Member Profile'" />

        <!-- Profile Section -->
        <div class="card mb-4 mt-10">
          <!-- Card Header -->
          <div class="card-body d-flex align-items-center" @click="toggleProfile" style="cursor: pointer;">
            <div class="row align-items-center">
              <div class="col-auto">
                <form class="needs-validation" @submit.prevent="imageSubmit" novalidate>
                  <div class="circle-avatar" :style="{ backgroundImage: `url(${store.memberprofile.userAvatar})` }"
                    @click="handleAvatarClick"></div>
                  <input type="file" ref="fileInput" accept="image/jpeg, image/png" style="display: none"
                    @change="handleFileChange" />
                </form>
              </div>
              <div class="col">
                <div class="flex-grow-1">
                  <h2 class="h4 mb-3">{{ store.getProfile.userName || '未設定名稱' }}</h2>
                  <p class="mb-2 text-secondary">Email: {{ store.getProfile.userEmail || '未設定' }}</p>
                  <p class="text-secondary">電話: {{ store.getProfile.userPhone || '未設定' }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- Expanded Content -->
          <div class="collapse" :class="{ show: profileExpanded }">
            <div class="card-body border-top">

              <form class="needs-validation" @submit.prevent="handleSubmit" novalidate>
                <div class="row g-3">
                  <!-- ... [所有表單內容保持不變] ... -->
                  <div class="col-12">
                    <label for="Name" class="form-label">姓名</label>
                    <input type="text" class="form-control" id="Name" v-model="store.getProfile.userName"
                      :class="{ 'is-invalid': validationErrors.userName }" required />
                    <div class="invalid-feedback">
                      {{ validationErrors.userName }}
                    </div>
                  </div>

                  <div class="col-12">
                    <label for="email" class="form-label">Email</label>
                    <input type="email" class="form-control" id="email" v-model="store.getProfile.userEmail"
                      :class="{ 'is-invalid': validationErrors.userEmail }" required />
                    <div class="invalid-feedback">
                      {{ validationErrors.userEmail }}
                    </div>
                  </div>

                  <div class="col-12">
                    <label for="phone" class="form-label">Phone</label>
                    <input type="tel" class="form-control" id="phone" v-model="store.getProfile.userPhone"
                      :class="{ 'is-invalid': validationErrors.userPhone }" maxlength="10" required />
                    <div class="invalid-feedback">
                      {{ validationErrors.userPhone }}
                    </div>
                  </div>

                  <div class="col-6">
                    <label for="gender" class="form-label">性別</label>
                    <div id="gender" class="d-flex">
                      <div class="form-check me-3">
                        <input class="form-check-input" type="radio" id="male" v-model="store.getProfile.userGender"
                          :value="1" required />
                        <label class="form-check-label" for="male">男</label>
                      </div>
                      <div class="form-check">
                        <input class="form-check-input" type="radio" id="female" v-model="store.getProfile.userGender"
                          :value="0" required />
                        <label class="form-check-label" for="female">女</label>
                      </div>
                    </div>
                  </div>

                  <div class="col-6">
                    <label for="favor" class="form-label">興趣</label>
                    <div id="favor" class="d-flex">
                      <div class="form-check me-3">
                        <input class="form-check-input" type="radio" id="introvert" v-model="store.getProfile.userFavor"
                          :value="0" required />
                        <label class="form-check-label" for="introvert">內向</label>
                      </div>
                      <div class="form-check">
                        <input class="form-check-input" type="radio" id="extrovert" v-model="store.getProfile.userFavor"
                          :value="1" required />
                        <label class="form-check-label" for="extrovert">外向</label>
                      </div>
                    </div>
                  </div>

                  <div class="col-md-6">
                    <label for="city" class="form-label">城市</label>
                    <select class="form-select" id="city" v-model="selectedCity" @change="updateDistricts" required>
                      <option value="">請選擇城市</option>
                      <option v-for="(districts, city) in cities" :key="city" :value="city">
                        {{ city }}
                      </option>
                    </select>
                  </div>

                  <div class="col-md-6">
                    <label for="district" class="form-label">地區</label>
                    <select class="form-select" id="district" v-model="selectedDistrict" @change="updateAddress"
                      required>
                      <option value="">請選擇地區</option>
                      <option v-for="district in currentDistricts" :key="district" :value="district">
                        {{ district }}
                      </option>
                    </select>
                  </div>

                  <div class="col-12">
                    <label for="address" class="form-label">地址</label>
                    <input type="text" class="form-control" id="address" v-model="store.getProfile.userAddress"
                      :class="{ 'is-invalid': validationErrors.userAddress }" required />
                    <div class="invalid-feedback">
                      {{ validationErrors.userAddress }}
                    </div>
                  </div>

                  <div class="col-12">
                    <label for="birthdate" class="form-label">出生日期</label>
                    <input type="date" class="form-control" id="birthdate" v-model="store.getProfile.userBirthdate"
                      :class="{ 'is-invalid': validationErrors.userBirthdate }" required />
                    <div class="invalid-feedback">
                      {{ validationErrors.userBirthdate }}
                    </div>
                  </div>
                </div>

                <hr class="my-4" />
                <button class="w-100 btn btn-primary btn-lg" type="submit" :disabled="isSubmitting">
                  {{ isSubmitting ? '儲存中...' : '儲存設定' }}
                </button>
              </form>
            </div>
          </div>
        </div>



        <!-- Password Change Card -->
        <div class="card mb-4">
          <div class="card-body" @click="startPasswordChange" style="cursor: pointer;">
            <div class="row align-items-center">
              <div class="col">
                <h5 class="card-title">修改密碼</h5>
                <p class="card-text">發送驗證信修改密碼，確保您的帳戶安全</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Account Deletion Section -->
        <div class="card mb-3">
          <div class="card-header bg-warning text-danger " @click="openDeleteModal" style="cursor: pointer;">
            <h5 class="mb-0">註銷帳號</h5>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Verification Modal -->
  <div class="modal fade" id="verificationModal" tabindex="-1" ref="verificationModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">驗證身份</h5>
          <button type="button" class="btn-close" @click="closeVerificationModal"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="verifyCode">
            <div class="mb-3">
              <label class="form-label">手機號碼</label>
              <div class="input-group">
                <input type="text" class="form-control" v-model="passwordChange.phone" minlength="10" maxlength="10"
                  :class="{ 'is-invalid': validationErrors.verifyPhone }" required />
                <button type="button" class="btn btn-outline-primary" @click="sendVerificationCode"
                  :disabled="countdown.value > 0">
                  {{ countdown.value > 0 ? `${countdown.value}秒後重新發送` : '發送驗證碼' }}
                </button>
                <div class="invalid-feedback">
                  {{ validationErrors.verifyPhone }}
                </div>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">請輸入驗證碼</label>
              <input type="tel" class="form-control" v-model="passwordChange.verificationCode" minlength="5"
                maxlength="5" required />
            </div>
            <button type="submit" class="btn btn-primary w-100">確認驗證</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- New Password Modal -->
  <div class="modal fade" id="newPasswordModal" tabindex="-1" ref="newPasswordModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">設置新密碼</h5>
          <button type="button" class="btn-close" @click="closeNewPasswordModal"></button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="updatePassword">
            <div class="mb-3">
              <label class="form-label">新密碼</label>
              <input type="password" class="form-control" v-model="passwordChange.newPassword"
                :class="{ 'is-invalid': validationErrors.passwordError }" required />
              <div class="invalid-feedback">
                {{ validationErrors.passwordError }}
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">確認新密碼</label>
              <input type="password" class="form-control" v-model="passwordChange.confirmPassword"
                :class="{ 'is-invalid': validationErrors.confirmPasswordError }" required />
              <div class="invalid-feedback">
                {{ validationErrors.confirmPasswordError }}
              </div>
            </div>
            <button type="submit" class="btn btn-primary w-100">更新密碼</button>
          </form>
        </div>
      </div>
    </div>
  </div>

  <!-- Account Deletion Modal -->
  <div class="modal fade" id="deleteModal" tabindex="-1" ref="deleteModal">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">註銷帳號</h5>
          <button type="button" class="btn-close" @click="closeDeleteModal"></button>
        </div>
        <div class="modal-body">
          <p class="text-danger">警告：此操作無法復原！請確認是否要註銷帳號？</p>
          <button type="button" class="btn btn-danger w-100" @click="confirmDeleteAccount">確認註銷</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import BannerTop from '@/components/BannerTop.vue'
import { Modal } from 'bootstrap'

export default {
  components: {
    BannerTop,
  },
  setup() {
    const store = useUserStore()
    const isSubmitting = ref(false)
    const validationErrors = reactive({})
    const selectedCity = ref(store.memberprofile.city)
    const selectedDistrict = ref(store.memberprofile.district)
    const fileInput = ref(null)
    const isValid = ref(true)
    const isValidform = ref(true)
    const deleteModal = ref(null)
    const profileExpanded = ref(false)
    const verificationModal = ref(null)
    const newPasswordModal = ref(null)
    const countdown = ref(0)

    const passwordChange = reactive({
      phone: '',
      verificationCode: '',
      newPassword: '',
      confirmPassword: '',
    })
    const toggleProfile = () => {
      profileExpanded.value = !profileExpanded.value
    }
    const startPasswordChange = () => {
      const modal = new Modal(verificationModal.value)
      modal.show()
    }
    const closeVerificationModal = () => {
      const modal = Modal.getInstance(verificationModal.value)
      modal.hide()
    }

    const closeNewPasswordModal = () => {
      const modal = Modal.getInstance(newPasswordModal.value)
      modal.hide()
    }
    const sendVerificationCode = () => {
      try {
        countdown.value = 60
        const timer = setInterval(() => {
          countdown.value--
          if (countdown.value <= 0) {
            clearInterval(timer)
          }
        }, 1000)
      } catch (error) {
        console.error('發送驗證碼失敗：', error)
        alert('發送驗證碼失敗，請稍後再試')
      }
      store.sendSMS(passwordChange)
    }

    const verifyCode = () => {//這個送資料給PINIA的checkVerifyCode
      closeVerificationModal()
      store.checkVerifyCode(passwordChange, (response, error) => {
        if (error) {
          console.log('驗證過程發生錯誤:', error);
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: '驗證碼驗證失敗',
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        } else {
          const modal = new Modal(newPasswordModal.value)//SHOW新的輸入密碼MODAL
          modal.show()//SHOW新的輸入密碼MODAL
        }

      })
    }

    const updatePassword = () => {//新的輸入密碼MODAL發送AXIOS發送AXIOS
      if (isValidform) {
        console.log('準備發送修改密碼')
        store.changePassword(passwordChange, (response, error) => {
          if (error) {
            console.log('驗證過程發生錯誤:', error);
            alert('驗證失敗，請重試');
          } else {
            closeNewPasswordModal()
          }

        })
      }
    }

    // Account deletion modal handlers
    const openDeleteModal = () => {
      const modal = new Modal(deleteModal.value)
      modal.show()
    }

    const closeDeleteModal = () => {
      const modal = Modal.getInstance(deleteModal.value)
      modal.hide()
    }

    const confirmDeleteAccount = async() => {
      await store.cancelAccount()
      closeDeleteModal()
      // 設置延遲後跳轉
      setTimeout(() => {
        store.clearLoggedIn()
        window.location.replace('/index');  // 延遲後跳轉到 /index
      }, 2000);  // 延遲 2 秒 (2000 毫秒)

    }

    watch(
      () => store.getProfile,
      (newProfile) => {
        isValid.value = true
        if (!newProfile.userName) {
          validationErrors.userName = '請輸入姓名'
          isValid.value = false
        } else {
          validationErrors.userName = ''
        }
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!newProfile.userEmail) {
          validationErrors.userEmail = '請輸入有效的 email 地址'
          isValid.value = false
        } else if (!emailRegex.test(newProfile.userEmail)) {
          validationErrors.userEmail = '請輸入有效的 email 地址，如user@example.com'
          isValid.value = false
        } else {
          validationErrors.userEmail = ''
        }

        const phoneRegex = /^09\d{8}$/
        if (!newProfile.userPhone) {
          // validationErrors.userPhone = '請輸入有效的手機號碼'
          isValid.value = true
        } else if (!phoneRegex.test(newProfile.userPhone)) {
          validationErrors.userPhone = '請輸入有效的手機號碼，如09開頭+8位數字'
          isValid.value = false
        } else {
          validationErrors.userPhone = ''
        }
      },
      { immediate: true, deep: true }, // deep監聽內部數據變化
    );
    watch(
      () => passwordChange, (newVal) => {
        const phoneRegex = /^09\d{8}$/
        if (!newVal.phone.trim()) {
          validationErrors.verifyPhone = '請輸入有效的手機號碼'
          isValidform.value = false
        } else if (!phoneRegex.test(newVal.phone)) {
          validationErrors.verifyPhone = '請輸入有效的手機號碼，如09開頭+8位數字'
          isValidform.value = false
        } else {
          validationErrors.verifyPhone = ''
        }
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/;
        if (!newVal.newPassword.trim()) {
          validationErrors.passwordError = '未填入密碼';
          isValidform.value = false;
        } else if (!passwordRegex.test(newVal.newPassword)) {
          validationErrors.passwordError = '密碼格式錯誤，須含大小寫英文，1位特殊符號';
          isValidform.value = false;
        } else {
          validationErrors.passwordError = '';
        }

        if (newVal.newPassword !== newVal.confirmPassword) {
          validationErrors.confirmPasswordError = '密碼與二次密碼不一致';
          isValidform.value = false;
        } else {
          validationErrors.confirmPasswordError = '';
          isValidform.value = true;  // 確保當兩者一致時，表單有效
        }
      },
      { immediate: true, deep: true }, // deep監聽內部數據變化
    );

    // 初始化表單資料
    onMounted(async () => {
      console.log('初始化表單')
      let memberId = store.memberId
      await store.loadMemberData(memberId)
      selectedCity.value = store.getProfile.city || ''
      selectedDistrict.value = store.getProfile.district || ''
    })

    // 當前可選擇的地區
    const currentDistricts = computed(() => {
      return cities[selectedCity.value] || []
    })

    // 更新地址
    const updateAddress = () => {
      if (selectedCity.value && selectedDistrict.value) {
        store.getProfile.userAddress = `${selectedCity.value}${selectedDistrict.value}`
      }
    }

    // 更新地區列表
    const updateDistricts = () => {
      selectedDistrict.value = ''
    }

    // 表單提交
    const handleSubmit = async () => {
      try {
        isSubmitting.value = true
        if (!isValid.value) {
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
        } else {
          let response = await store.submitProfile()
          if (response.data.status === 'success') {
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
          } else {
            Swal.fire({
              toast: false,
              position: 'top',
              icon: 'error',
              iconColor: 'red',
              title: `更新失敗！`,
              timer: 1500,
              showConfirmButton: false,
              timerProgressBar: true,
            })
          }
        }
      } catch (error) {
        console.error('更新失敗：', error)
        alert('更新失敗，請稍後再試')
      } finally {
        isSubmitting.value = false
      }
    }

    // 處理頭像點擊
    const handleAvatarClick = () => {
      if (fileInput.value) {
        fileInput.value.click()
      }
    }
    const handleFileChange = (event) => {
      const file = event.target.files[0]
      if (file) {
        const allowedTypes = ['image/jpeg', 'image/png']
        if (allowedTypes.includes(file.type)) {
          console.log('準備上傳')
          store.updateUserImage(file)
        } else {
          console.log('圖片格式不符')
          event.target.value = ''
          Swal.fire({
            toast: false,
            position: 'top',
            icon: 'warning',
            iconColor: 'red',
            title: `請上傳JPEG或PNG檔案！`,
            timer: 1500,
            showConfirmButton: false,
            timerProgressBar: true,
          })
        }
      } else {
        console.log('没有上傳圖片檔')
      }
    }

    return {
      store,
      cities,
      selectedCity,
      selectedDistrict,
      currentDistricts,
      validationErrors,
      isSubmitting,
      isValid,
      handleSubmit,
      handleAvatarClick,
      updateAddress,
      updateDistricts,
      fileInput,
      handleFileChange,
      //下拉
      profileExpanded,
      toggleProfile,
      //驗證修改密碼
      isValidform,
      countdown,
      passwordChange,
      newPasswordModal,
      startPasswordChange,
      updatePassword,
      closeNewPasswordModal,
      verificationModal,
      verifyCode,
      sendVerificationCode,
      closeVerificationModal,
      //註銷
      deleteModal,
      openDeleteModal,
      closeDeleteModal,
      confirmDeleteAccount
    }
  },
}
const cities = {
  台北市: ['中正區', '大安區', '士林區', '北投區', '內湖區', '南港區', '信義區', '文山區', '松山區', '大同區', '中山區',],
  基隆市: ['基隆市區', '仁愛區', '中正區', '信義區', '七堵區', '暖暖區', '四堵區'],
  新北市: ['板橋區', '三重區', '中和區', '永和區', '新莊區', '樹林區', '鶯歌區', '三峽區', '淡水區', '汐止區', '金山區', '萬里區', '林口區', '芦洲區', '五股區',
    '八里區', '平溪區', '雙溪區', '貢寮區'],
  桃園市: ['桃園市區', '中壢區', '平鎮區', '八德區', '大溪區', '楊梅區', '蘆竹區', '大園區', '觀音區', '新屋區', '龍潭區', '龜山區'],
  台中市: ['中區', '西區', '南區', '北區', '東區', '西屯區', '南屯區', '北屯區', '豐原區', '大甲區', '大安區', '清水區', '沙鹿區', '梧棲區', '大肚區', '龍井區', '霧峰區',
    '烏日區', '大里區', '太平區', '南投區', '大雅區'],
  台南市: ['中西區', '安平區', '東區', '南區', '北區', '永康區', '歸仁區', '新化區', '左鎮區', '玉井區', '楠西區', '南化區', '仁德區', '關廟區', '龍崎區', '官田區', '麻豆區', '佳里區',
    '西港區', '七股區', '將軍區', '學甲區', '北門區', '新營區', '鹽水區'],
  高雄市: ['新興區', '前金區', '苓雅區', '鹽埕區', '鼓山區', '旗津區', '前鎮區', '三民區', '楠梓區', '小港區', '左營區', '大社區', '東沙群島', '南沙群島',
    '橋頭區', '梓官區', '彌陀區', '永安區', '燕巢區', '鳳山區', '大寮區', '林園區', '三民區', '大樹區', '旗山區', '美濃區', '六龜區', '內門區', '杉林區', '甲仙區', '桃源區', '那瑪夏區'],
  新竹市: ['新竹市區', '東區', '北區', '香山區', '竹北市', '湖口鄉', '新豐鄉', '芎林鄉', '竹東鎮', '五峰鄉', '橫山鄉', '尖石鄉', '北埔鄉', '峨眉鄉'],
  嘉義市: ['嘉義市區', '東區', '西區', '南區', '北區', '太保市', '朴子市', '布袋鎮', '大林鎮', '民雄鄉', '溪口鄉', '新港鄉', '阿里山鄉'],
  台東市: ['台東市', '卑南鄉', '大武鄉', '太麻里鄉', '鹿野鄉', '關山鎮', '海端鄉', '池上鄉', '東河鄉', '長濱鄉', '成功鎮', '金峰鄉', '達仁鄉'],
  花蓮市: ['花蓮市', '鳳林鎮', '玉里鎮', '吉安鄉', '新城鄉', '秀林鄉', '光復鄉', '豐濱鄉', '瑞穗鄉', '萬榮鄉', '池上鄉', '富里鄉'],
  屏東市: ['屏東市', '三地門鄉', '霧台鄉', '瑪家鄉', '九如鄉', '里港鄉', '高樹鄉', '鹽埔鄉', '長治鄉', '麟洛鄉', '竹田鄉', '內埔鄉', '萬丹鄉', '潮州鎮', '東港鎮', '恆春鎮', '滿州鄉'],
  苗栗市: ['苗栗市', '三義鄉', '公館鄉', '大湖鄉', '銅鑼鄉', '三灣鄉', '南庄鄉', '獅潭鄉', '後龍鎮', '卓蘭鎮'],
  彰化市: ['彰化市', '員林市', '和美鎮', '伸港鄉', '員榮鄉', '大村鄉', '中寮鄉', '田中鎮', '北斗鎮', '溪州鄉', '二林鎮', '大城鄉', '埔心鄉'],
  南投市: ['南投市', '草屯鎮', '國姓鄉', '埔里鎮', '仁愛鄉', '名間鄉', '集集鎮', '中寮鄉', '鹿谷鄉'],
  雲林市: ['斗六市', '虎尾鎮', '西螺鎮', '土庫鎮', '崙背鄉', '麥寮鄉', '大埤鄉', '莿桐鄉', '林內鄉', '二崙鄉', '北港鎮', '水林鄉',],
  澎湖市: ['馬公市', '西嶼鄉', '望安鄉', '湖西鄉', '白沙鄉', '通梁鄉'],
  金門市: ['金城鎮', '金湖鎮', '金寧鄉', '烈嶼鄉', '烏坵鄉'],
  連江市: ['南竿鄉', '北竿鄉', '莒光鄉', '東引鄉'],
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
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  transition: box-shadow 0.3s ease;
}

.circle-avatar:hover {
  opacity: 0.8;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
}

.card {
  transition: transform 0.2s;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.modal-backdrop {
  background-color: rgba(0, 0, 0, 0.5);
}

.btn-link {
  color: #6c757d;
  text-decoration: none;
}

.btn-link:hover {
  color: #343a40;
}
</style>
