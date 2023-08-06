import { createApp } from 'vue'
import App from './App.vue'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import TableSlot from "@/layout/TableSlot.vue";
import router from './router'
import store from "@/store/store";
import axios from 'axios';
import BaseSpinner from "@/ui/BaseSpinner.vue";
const app = createApp(App);

app.component('table-slot', TableSlot);
app.component('base-spinner', BaseSpinner);

app.use(router);
app.use(store);

app.mount('#app');

app.config.globalProperties.$axios = axios;

axios.defaults.baseURL = store.getters.uri;
axios.defaults.headers.common["X-AUTH-TOKEN"] = store.getters.getToken;
