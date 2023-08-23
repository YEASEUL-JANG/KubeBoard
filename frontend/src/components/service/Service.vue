<template>
  <table-slot header="서비스">
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
      <thead>
      <tr>
          <th>이름</th>
          <th>타입</th>
          <th>클러스터 IP</th>
          <th>생성시간</th>
          <th>레이블</th>
          <th>상세</th>
      </tr>
      </thead>
        <tbody>
        <tr v-if="items.length === 0">
            <td colspan="8"><h5>표시할 데이터가 없습니다.</h5></td>
        </tr>
        <tr v-for="(item, index) in items" :key="index">
            <td>{{ item.serviceName }}</td>
            <td>{{ item.type }}</td>
            <td>{{ item.clusterIp }}</td>
            <td>{{ item.createdTime }}</td>
            <td><label-list :labels="JSON.parse(item.labels)"></label-list></td>
            <td>
                <button type="button" class="btn btn-secondary btn-sm" @click="serviceDetail(item.serviceName)">조회</button>
            </td>
        </tr>
      </tbody>
    </table>
      <template v-slot:pageSlot>
          <Pagination :currentPage="currentPage"
                      :numberOfPages="numberOfPages"
                      @getList="getServiceList"/>
      </template>
  </table-slot>
</template>

<script>
import {ref, onMounted, computed, provide} from "vue";
import LabelList from "@/components/common/LabelList.vue";
import axios from "axios";
import Pagination from "@/components/common/Pagination.vue";
import router from "@/router";
import {useRoute} from "vue-router";


export default {
    name: "serviceView",
    components: {
        Pagination,
        LabelList,
    },
    setup() {
        const items = ref([]);
        const numberOflist = ref(0);
        const currentPage = ref(1);
        const isLoading = ref(false);
        const limit = 5;
        const route = useRoute();
        const setLoading = () => {
            isLoading.value = true;
        }
        const getServiceList = async (page = currentPage.value) => {
            currentPage.value = page;
            try {
                const searchQuery = route.params.searchInput?`&search=${route.params.searchInput}`:""
                const {data} = await axios.get(`/service/list?page=${currentPage.value}${searchQuery}`);
                console.log("serviceList : ", data)
                items.value = data.list;
                numberOflist.value = data.count;
            }catch(e){
                console.log(e);
            }finally {
                isLoading.value = false;
            }
        };
        //총 페이지 수 계산
        const numberOfPages = computed(() => {
            return Math.ceil((numberOflist.value / limit));
        });

        onMounted(() =>{
            getServiceList();
            setLoading();
        })
        //setLoading 함수를 하위 컴포넌트에 주입 -> 하위 컴포넌트에서 'inject'함수를 사용해 가져와서 사용가능.
        provide('setLoading', setLoading);

        /**
         * service 상세조회
         */
        const serviceDetail = (name) => {
            router.push('/service/' + name);
        };

        return {
            items,
            isLoading,
            serviceDetail,
            getServiceList,
            numberOfPages,
            currentPage,
            setLoading
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
.active>.page-link {
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
