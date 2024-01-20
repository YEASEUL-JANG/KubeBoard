<template>
  <div>
    <div class="backlist">
      <button  type="button" class="btn btn-outline-secondary"
               @click="moveList">
        <i class="bi bi-chevron-double-left"></i> List</button>
    </div>
    <table-slot header="내 정보">
      <base-spinner v-if="isLoading"></base-spinner>
        <table v-else-if="!isLoading">
            <tbody>
            <tr>
                <th>아이디</th>
                <td>{{ userInfo.userId }}</td>
            </tr>
            <tr>
                <th>이메일</th>
                <td>{{ userInfo.email }}</td>
            </tr>
            <tr>
                <th>가입일</th>
                <td>{{ userInfo.createdAt }}</td>
            </tr>
            </tbody>
        </table>
      <div v-else>
        <h5>잠시 후 다시 시도해주세요.</h5>
      </div>
    </table-slot>

    <table-slot header="접속 정보">
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else-if="!isLoading">
        <thead>
          <tr>
            <th>요청서비스</th>
            <th>작업대상</th>
            <th>요청개수</th>
              <th>요청일</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(log, index) in userLogList" :key="index">
            <td>{{ log.requestMs }}</td>
            <td>{{ log.requestSource }}</td>
            <td>{{ log.requestNum }}</td>
            <td>{{ log.requestAt }}</td>
          </tr>
        </tbody>
      </table>
    </table-slot>
  </div>
</template>
<script>
import { ref, onMounted } from 'vue'
import BaseSpinner from "@/ui/BaseSpinner.vue";
import axios from "axios";
import {useRoute} from "vue-router";
import router from "@/router";

export default {
    components: {
        BaseSpinner,
    },
    props: ['userId'],

    setup() {
        const route = useRoute();
        const userId = route.params.name;
        const userLogList = ref([])
        const userInfo = ref({})
        const isLoading = ref(false)

        const loadUserInfo = async() => {
            isLoading.value = true
                try {
                    const {data} = await axios.get(
                        `/user-service/users/${userId}`);
                    console.log("userInfo :", data)
                    userInfo.value = data;
                    userLogList.value = data.logs;
                } catch (err) {
                    console.log(err);
                } finally {
                    isLoading.value = false;
                }
            };


        onMounted(() => {
            loadUserInfo()
        })
        const moveList = () =>{
            router.push('/pod');
        }

        return {
            userInfo,
            isLoading,
            moveList,
            userLogList,
        }
    },
}
</script>

<style scoped>
page-link {
  background-color: #bdbdbd;
  border-color: #e6e6e6;
}

.pagination a {
  color: #6e6e6e;
}

.pagination {
  margin-top: 30px;
}
</style>
