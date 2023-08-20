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
      <!--td>{{ item.label }}</td-->
    </tr>
    </tbody>
    </table>
  </table-slot>
  <table-slot header="데이터 기록 내역">
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
    <thead>
    <tr>
      <th>작성시간</th>
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
    <tr v-for="(item, index) in items" :key="index">
      <td>{{ item.writeTime }}</td>
      <td>{{ item.name }}</td>
      <td>{{ item.type }}</td>
      <td>{{ item.clusterIP }}</td>
      <td>{{ item.port }}</td>
      <td>{{ item.externalIP }}</td>
      <td>{{ item.createdTime }}</td>
      <td><label-list :labels="JSON.parse(item.labels)"></label-list></td>
    </tr>
    </tbody>
    </table>
      <template v-slot:pageSlot>
          <Pagination :currentPage="currentPage"
                      :numberOfPages="numberOfPages"
                      @getList="getServiceList"/>
      </template>
  </table-slot>
  </div>
</template>
<script>
import {ref, onMounted} from "vue";
import axios from "axios";
import LabelList from "@/components/common/LabelList.vue";
import Pagination from "@/components/common/Pagination.vue";
import router from "@/router";

export default {
    name: "serviceView",
    components: {
        Pagination,
        LabelList,
    },
    setup(props, {root}) {
        const items = ref([]);
        const numberOflist = ref(0);
        const currentPage = ref(1);
        const isLoading = ref(false);

        const setLoading = () => {
            isLoading.value = true;
        }
        const getServiceList = async (page = currentPage.value) => {
            currentPage.value = page;
            try {
                const {data} = await axios.get(`/service/list?page=${currentPage.value}`);
                console.log("serviceList : ", data)
                item.value = data.list;
                numberOflist.value = data.count;
            }catch(e){
                console.log(e);
            }finally {
                isLoading.value = false;
            }
        };

        onMounted(() =>{
            getServiceList();
            setLoading();
        })

        /**
         * service 상세조회
         */
        const serviceDetail = (name) => {
            router.push('/service/' + name);
        };

        const moveList = () => {
            root.$router.push('/service');
        };
        return {
            items,
            isLoading,
            moveList,
            serviceDetail,
            getServiceList,
            numberOfPages,
            currentPage,
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
