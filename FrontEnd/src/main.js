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

import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";
import {library} from "@fortawesome/fontawesome-svg-core";
import {faSearch,faUser} from "@fortawesome/free-solid-svg-icons";
library.add(faSearch,faUser);

window.Swal = Swal

const app = createApp(App)

AOS.init()
app.use(createPinia())
app.use(router)
app.use(VueAxios, axios)
app.component("font-awesome-icon",FontAwesomeIcon)



app.mount('#app')
