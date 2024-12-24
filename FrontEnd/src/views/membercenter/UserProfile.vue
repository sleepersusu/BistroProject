<template>
    <div class="container d-flex justify-content-center align-items-center">
        <div class="row g-5 w-100">
            <div class="col-md-7 col-lg-8 mx-auto">
                <BannerTop :title="'Member Profile'" />
                <form class="needs-validation" @submit.prevent="imageSubmit" novalidate>
                    <div class="col-12 d-flex justify-content-center">
                        <div class="circle-avatar"
                            :style="{ backgroundImage: `url(${store.memberprofile.userAvatar})` }"
                            @click="handleAvatarClick">
                        </div>
                    </div>
                </form>
                <form class="needs-validation" @submit.prevent="handleSubmit" novalidate>
                    <div class="row g-3">

                        <div class="col-12">
                            <label for="Name" class="form-label">姓名</label>
                            <input type="text" class="form-control" id="Name" v-model="store.getProfile.userName"
                                :class="{ 'is-invalid': validationErrors.userName }" required>
                            <div class="invalid-feedback">
                                {{ validationErrors.userName }}
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" v-model="store.getProfile.userEmail"
                                :class="{ 'is-invalid': validationErrors.userEmail }" required>
                            <div class="invalid-feedback">
                                {{ validationErrors.userEmail }}
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="phone" class="form-label">Phone</label>
                            <input type="tel" class="form-control" id="phone" v-model="store.getProfile.userPhone"
                                :class="{ 'is-invalid': validationErrors.userPhone }" maxlength="10" required>
                            <div class="invalid-feedback">
                                {{ validationErrors.userPhone }}
                            </div>
                        </div>

                        <div class="col-6">
                            <label for="gender" class="form-label">性別</label>
                            <div id="gender" class="d-flex">
                                <!-- 男性選項 -->
                                <div class="form-check me-3">
                                    <input class="form-check-input" type="radio" id="male"
                                        v-model="store.getProfile.userGender" :value="1" required>
                                    <label class="form-check-label" for="male">男</label>
                                </div>
                                <!-- 女性選項 -->
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" id="female"
                                        v-model="store.getProfile.userGender" :value="0" required>
                                    <label class="form-check-label" for="female">女</label>
                                </div>
                            </div>
                        </div>

                        <div class="col-6">
                            <label for="gender" class="form-label">興趣</label>
                            <div id="gender" class="d-flex">
                                <!-- 內向選項 -->
                                <div class="form-check me-3">
                                    <input class="form-check-input" type="radio" id="male"
                                        v-model="store.getProfile.userFavor" :value="0" required>
                                    <label class="form-check-label" for="male">內向</label>
                                </div>
                                <!-- 外向選項 -->
                                <div class="form-check">
                                    <input class="form-check-input" type="radio" id="female"
                                        v-model="store.getProfile.userFavor" :value="1" required>
                                    <label class="form-check-label" for="female">外向</label>
                                </div>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <label for="city" class="form-label">城市</label>
                            <select class="form-select" id="city" v-model="selectedCity" @change="updateDistricts"
                                required>
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
                                :class="{ 'is-invalid': validationErrors.userAddress }" required>
                            <div class="invalid-feedback">
                                {{ validationErrors.userAddress }}
                            </div>
                        </div>

                        <div class="col-12">
                            <label for="birthdate" class="form-label">出生日期</label>
                            <input type="date" class="form-control" id="birthdate"
                                v-model="store.getProfile.userBirthdate"
                                :class="{ 'is-invalid': validationErrors.userBirthdate }" required>
                            <div class="invalid-feedback">
                                {{ validationErrors.userBirthdate }}
                            </div>
                        </div>
                    </div>

                    <hr class="my-4">
                    <button class="w-100 btn btn-primary btn-lg" type="submit" :disabled="isSubmitting">
                        {{ isSubmitting ? '儲存中...' : '儲存設定' }}
                    </button>
                </form>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import BannerTop from '@/components/BannerTop.vue'

export default {
    components: {
        BannerTop
    },
    setup() {
        const store = useUserStore()
        const isSubmitting = ref(false)
        const validationErrors = reactive({})
        const selectedCity = ref(store.memberprofile.city)
        const selectedDistrict = ref(store.memberprofile.district)

        watch(
            () => store.getProfile,
            (newProfile) => {
                console.log(newProfile)
                if (!newProfile.userName) {
                    validationErrors.userName = '請輸入姓名';
                } else {
                    validationErrors.userName = '';
                }

                if (!newProfile.userEmail || !newProfile.userEmail.includes('@')) {
                    validationErrors.userEmail = '請輸入有效的 email 地址';
                } else {
                    validationErrors.userEmail = '';
                }
            },
            { immediate: true, deep: true } // deep深度監聽內部數據變化
        );

        // 初始化表單資料
        onMounted(async () => {
            let memberId = store.memberId
            await store.loadMemberData(memberId)
            console.log('初始化完')
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
                let checkValue = validateForm()
                if (!checkValue) {
                    window.Swal.fire({
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
                        window.Swal.fire({
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
                        window.Swal.fire({
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

        // 表單驗證
        const validateForm = () => {
            validationErrors.value = {}
            let isValid = true
            console.log('驗證開始');


            if (!store.getProfile.userName) {
                validationErrors.value.userName = '請輸入姓名'
                isValid = false
            }

            if (!store.getProfile.userEmail || !store.getProfile.userEmail.includes('@')) {
                validationErrors.value.userEmail = '請輸入有效的 email 地址'
                isValid = false
            }

            console.log(isValid)
            return isValid
        }

        // 處理頭像點擊
        const handleAvatarClick = () => {
            
            console.log('Avatar clicked')
        }

        return {
            store,
            cities,
            selectedCity,
            selectedDistrict,
            currentDistricts,
            validationErrors,
            isSubmitting,
            handleSubmit,
            handleAvatarClick,
            updateAddress,
            updateDistricts
        }
    },
}
const cities = {
    台北市: ['中正區', '大安區', '士林區', '北投區', '內湖區', '南港區', '信義區', '文山區', '松山區', '大同區', '中山區'],
    基隆市: ['基隆市區', '仁愛區', '中正區', '信義區', '七堵區', '暖暖區', '四堵區'],
    新北市: ['板橋區', '三重區', '中和區', '永和區', '新莊區', '樹林區', '鶯歌區', '三峽區', '淡水區', '汐止區', '金山區', '萬里區', '林口區', '芦洲區', '五股區', '八里區', '平溪區', '雙溪區', '貢寮區'],
    桃園市: ['桃園市區', '中壢區', '平鎮區', '八德區', '大溪區', '楊梅區', '蘆竹區', '大園區', '觀音區', '新屋區', '龍潭區', '龜山區'],
    台中市: ['中區', '西區', '南區', '北區', '東區', '西屯區', '南屯區', '北屯區', '豐原區', '大甲區', '大安區', '清水區', '沙鹿區', '梧棲區', '大肚區', '龍井區', '霧峰區', '烏日區', '大里區', '太平區', '南投區', '大雅區'],
    台南市: ['中西區', '安平區', '東區', '南區', '北區', '永康區', '歸仁區', '新化區', '左鎮區', '玉井區', '楠西區', '南化區', '仁德區', '關廟區', '龍崎區', '官田區', '麻豆區', '佳里區', '西港區', '七股區', '將軍區', '學甲區', '北門區', '新營區', '鹽水區'],
    高雄市: ['新興區', '前金區', '苓雅區', '鹽埕區', '鼓山區', '旗津區', '前鎮區', '三民區', '楠梓區', '小港區', '左營區', '大社區', '東沙群島', '南沙群島', '橋頭區', '梓官區', '彌陀區', '永安區', '燕巢區', '鳳山區', '大寮區', '林園區', '三民區', '大樹區', '旗山區', '美濃區', '六龜區', '內門區', '杉林區', '甲仙區', '桃源區', '那瑪夏區'],
    新竹市: ['新竹市區', '東區', '北區', '香山區', '竹北市', '湖口鄉', '新豐鄉', '芎林鄉', '竹東鎮', '五峰鄉', '橫山鄉', '尖石鄉', '北埔鄉', '峨眉鄉'],
    嘉義市: ['嘉義市區', '東區', '西區', '南區', '北區', '太保市', '朴子市', '布袋鎮', '大林鎮', '民雄鄉', '溪口鄉', '新港鄉', '阿里山鄉'],
    台東市: ['台東市', '卑南鄉', '大武鄉', '太麻里鄉', '鹿野鄉', '關山鎮', '海端鄉', '池上鄉', '東河鄉', '長濱鄉', '成功鎮', '金峰鄉', '達仁鄉'],
    花蓮市: ['花蓮市', '鳳林鎮', '玉里鎮', '吉安鄉', '新城鄉', '秀林鄉', '光復鄉', '豐濱鄉', '瑞穗鄉', '萬榮鄉', '池上鄉', '富里鄉'],
    屏東市: ['屏東市', '三地門鄉', '霧台鄉', '瑪家鄉', '九如鄉', '里港鄉', '高樹鄉', '鹽埔鄉', '長治鄉', '麟洛鄉', '竹田鄉', '內埔鄉', '萬丹鄉', '潮州鎮', '東港鎮', '恆春鎮', '滿州鄉'],
    苗栗市: ['苗栗市', '三義鄉', '公館鄉', '大湖鄉', '銅鑼鄉', '三灣鄉', '南庄鄉', '獅潭鄉', '後龍鎮', '卓蘭鎮'],
    彰化市: ['彰化市', '員林市', '和美鎮', '伸港鄉', '員榮鄉', '大村鄉', '中寮鄉', '田中鎮', '北斗鎮', '溪州鄉', '二林鎮', '大城鄉', '埔心鄉'],
    南投市: ['南投市', '草屯鎮', '國姓鄉', '埔里鎮', '仁愛鄉', '名間鄉', '集集鎮', '中寮鄉', '鹿谷鄉'],
    雲林市: ['斗六市', '虎尾鎮', '西螺鎮', '土庫鎮', '崙背鄉', '麥寮鄉', '大埤鄉', '莿桐鄉', '林內鄉', '二崙鄉', '北港鎮', '水林鄉'],
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
</style>