<template>
  <div>
    <div class="backlist">
      <button  type="button" class="btn btn-outline-secondary"
               @click="moveList">
        <i class="bi bi-chevron-double-left"></i> List</button>
    </div>
  <table-slot header="메타데이터">
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
    <thead>
    <tr>
      <th>이름</th>
      <th>네임스페이스</th>
      <th>생성시간</th>
      <th>uid</th>
      <th>레이블</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td>{{ items.serviceName }}</td>
      <td>{{ items.namespace }}</td>
      <td>{{ items.createdTime }}</td>
      <td>{{ items.uid }}</td>
      <td><label-list :labels="items.labels ? JSON.parse(items.labels) : {}"></label-list></td>
    </tr>
    </tbody>
    </table>
  </table-slot>
  <table-slot header="리소스 정보">
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
    <thead>
    <tr>
      <th>타입</th>
      <th>클러스터 IP</th>
      <th>세션어피니티</th>
      <th>셀렉터</th>
    </tr>
    </thead>
    <tbody>
    <tr>
      <td>{{ items.type }}</td>
        <td>{{ items.clusterIp }}</td>
      <td>{{ items.sessionAffinity }}</td>
      <td><label-list :labels="items.labels ? JSON.parse(items.selector) : {}"></label-list></td>
    </tr>
    </tbody>
    </table>
  </table-slot>
      <table-slot header="포트 정보">
          <base-spinner v-if="isLoading"></base-spinner>
          <table v-else>
              <thead>
              <tr>
                  <th>Node Port</th>
                  <th>Port</th>
                  <th>Protocol</th>
                  <th>Target Port</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(port,index) in items.ports" :key="index">
                  <td>{{ port.nodePort }}</td>
                  <td>{{ port.port }}</td>
                  <td>{{ port.protocol }}</td>
                  <td>{{ port.targetPort }}</td>
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
        LabelList
    },
    setup() {
        const items = ref([]);
        const isLoading = ref(false);
        const route = useRoute();
        const name = route.params.name;

        const getService = async () => {
            isLoading.value = true
            try {
                const {data} = await axios.get(`service-service/list/${name}`);
                console.log("service : ", data)
                items.value = data.list[0];
                for(let item in data.list[0].ports){
                    if(item.nodePort == null || item.nodePort===""){
                        item.nodePort === "-"
                    }
                }
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
