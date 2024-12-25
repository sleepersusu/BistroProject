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
import 'sweetalert2/dist/sweetalert2.min.css'
import 'aos/dist/aos.css'
import 'animate.css'
import AOS from 'aos'
import { Form, Field, ErrorMessage, defineRule, configure } from 'vee-validate'
import { required, email, min, max, regex } from '@vee-validate/rules'
import { localize, setLocale } from '@vee-validate/i18n'
import zhTW from '@vee-validate/i18n/dist/locale/zh_TW.json'
import VueLuckyCanvas from '@lucky-canvas/vue'
import StarRating from 'vue-star-rating'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { faEnvelope } from '@fortawesome/free-regular-svg-icons'
import { Notifications } from '@kyvg/vue3-notification'
import velocity from 'velocity-animate'
import '@fortawesome/fontawesome-free/css/all.min.css'


library.add(faUserSecret,fas,faEnvelope)

axios.defaults.baseURL = import.meta.env.VITE_API
axios.defaults.withCredentials = true

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
app.use(Notifications, { velocity })

app.use(VueLuckyCanvas)

app.component('vue-star-rating', StarRating)
app.component('Loading', Loading)
app.component('Form', Form)
app.component('Field', Field)
app.component('ErrorMessage', ErrorMessage)
app.component('font-awesome-icon', FontAwesomeIcon)

app.mount('#app')
