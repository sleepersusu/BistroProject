import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
import VueAxios from 'vue-axios'
import Loading from 'vue3-loading-overlay'
import 'vue3-loading-overlay/dist/vue3-loading-overlay.css'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'
import Swal from 'sweetalert2'
import 'aos/dist/aos.css'
import AOS from 'aos'
import { Form, Field, ErrorMessage, defineRule, configure } from 'vee-validate'
import { required, email, min, max, regex } from '@vee-validate/rules'
import { localize, setLocale } from '@vee-validate/i18n'
import zhTW from '@vee-validate/i18n/dist/locale/zh_TW.json'

import VueLuckyCanvas from '@lucky-canvas/vue'
import StarRating from 'vue-star-rating'

defineRule('required', required)
defineRule('email', email)
defineRule('min', min)
defineRule('max', max)
defineRule('regex', regex)
configure({
  generateMessage: localize({ zh_TW: zhTW }),
  validateOnInput: true,
})

setLocale('zh_TW')

window.Swal = Swal

const app = createApp(App)

AOS.init()
app.use(createPinia())
app.use(router)
app.use(VueAxios, axios)

app.use(VueLuckyCanvas)

app.component('vue-star-rating', StarRating)
app.component('Loading', Loading)
app.component('Form', Form)
app.component('Field', Field)
app.component('ErrorMessage', ErrorMessage)

app.mount('#app')
