<template>
  <div>
    <div class="backlist">
      <button  type="button" class="btn btn-outline-secondary"
               @click="moveList">
        <i class="bi bi-chevron-double-left"></i> List</button>
    </div>
  <table-slot header="실시간 정보">
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
    <thead>
    <tr>
      <th>이름</th>
      <th>타입</th>
      <th>클러스터 IP</th>
      <th>포트</th>
      <th>외부 IP</th>
      <th>생성시간</th>
      <th>레이블</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td>{{ item.name }}</td>
      <td>{{ item.type }}</td>
      <td>{{ item.clusterIP }}</td>
      <td>{{ item.port }}</td>
      <td>{{ item.externalIP }}</td>
      <td>{{ item.createdTime }}</td>
      <td><label-list :labels="item.label ? JSON.parse(item.label) : null"></label-list></td>
    </tr>
    </tbody>
    </table>
  </table-slot>
  <table-slot header="데이터 기록 내역">
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
    <thead>
    <tr>
      <th>이름</th>
        <th>네임스페이스</th>
        <th>타입</th>
        <th>클러스터 IP</th>
      <th>생성시간</th>
      <th>레이블</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="(item, index) in items" :key="index">
      <td>{{ item.name }}</td>
        <td>{{ item.namespace }}</td>
        <td>{{ item.type }}</td>
        <td>{{ item.clusterIp }}</td>
      <td>{{ item.createdTime }}</td>
      <td><label-list :labels="JSON.parse(item.labels)"></label-list></td>
    </tr>
    </tbody>
    </table>
  </table-slot>
  </div>
</template>
<script>
import {ref, onMounted} from "vue";
import axios from "axios";
import LabelList from "@/components/common/LabelList.vue";
import router from "@/router";
import {useRoute} from "vue-router";

export default {
    name: "serviceView",
    components: {
        LabelList,
    },
    setup() {
        const items = ref([]);
        const isLoading = ref(false);
        const route = useRoute();
        const name = route.params.name;

        const getService = async () => {
            isLoading.value = true
            try {
                const {data} = await axios.get(`/service/${name}`);
                console.log("service : ", data)
                items.value = data.list[0];
            }catch(e){
                console.log(e);
            }finally {
                isLoading.value = false;
            }
        };

        onMounted(() =>{
            getService();
        })

        const moveList = () =>{
            router.push('/service');
        }

        return {
            items,
            isLoading,
            moveList,
            getService
        };
    },
};
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 150px;
}

.active > .page-link {
    background-color: #BDBDBD;
    border-color: #E6E6E6;
}

.pagination a {
    color: #6E6E6E;
}

.pagination {
    margin-top: 30px;
}
</style>
