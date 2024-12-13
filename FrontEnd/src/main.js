import { createApp } from 'vue'
import { createPinia } from 'pinia'
import axios from 'axios'
import VueAxios from 'vue-axios'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import 'bootstrap-icons/font/bootstrap-icons.css'
import Swal from 'sweetalert2'
import 'aos/dist/aos.css'
import AOS from 'aos'
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/css/index.css';


window.Swal = Swal

const app = createApp(App)

AOS.init()
app.use(createPinia())
app.use(router)
app.use(VueAxios, axios)
app.component("Loading",Loading)

app.mount('#app')
