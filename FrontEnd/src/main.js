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

import VueLuckyCanvas from '@lucky-canvas/vue'
import StarRating from 'vue-star-rating'


window.Swal = Swal

const app = createApp(App)

AOS.init()
app.use(createPinia())
app.use(router)
app.use(VueAxios, axios)

app.use(VueLuckyCanvas)

app.component('vue-star-rating', StarRating)
app.component('Loading', Loading)



app.mount('#app')
''