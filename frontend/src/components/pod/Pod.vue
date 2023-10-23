<template>
  <table-slot header="파드">
    <base-spinner v-if="isLoading && !error"></base-spinner>
    <table v-else-if="!isLoading && !error">
      <thead>
      <tr>
        <th>파드 명</th>
        <th>네임스페이스</th>
        <th>상태</th>
        <th>생성시간</th>
        <th>PodIP</th>
        <th>노드 명</th>
        <th>레이블</th>
        <th>데이터 기록</th>
      </tr>
      </thead>
      <tbody>
      <tr v-if="items.length === 0">
          <td colspan="8"><h5>표시할 데이터가 없습니다.</h5></td>
      </tr>
      <tr v-for="(pod, index) in items" :key="index">
        <td>{{ pod.podName }}</td>
        <td>{{pod.namespace}}</td>
        <td>{{ pod.phase }}</td>
        <td>{{ pod.createdTime }}</td>
        <td>{{ pod.podIp }}</td>
        <td>{{ pod.nodeName }}</td>
        <td>
          <label-list :labels="JSON.parse(pod.labels)"></label-list>
        </td>
        <td>
          <a class="btn btn-secondary btn-sm" href="#"
             @click="poddetail(pod.podName)"
          >조회</a>
        </td>
      </tr>
      </tbody>
    </table>
      <template v-slot:pageSlot>
          <Pagination :currentPage="currentPage"
                            :numberOfPages="numberOfPages"
                            @getList="getPodList"/>
      </template>
  </table-slot>
</template>
<script>
import axios from "axios";
import {computed, onMounted, provide, ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import LabelList from '../common/LabelList.vue';
import Pagination from "@/components/common/Pagination.vue";
export default {
  components: {
      Pagination,
    LabelList,
  },
    setup() {
        const items = ref([]);
        const router = useRouter();
        const route = useRoute();
        const limit = 5;
        const numberOflist = ref(0);
        const currentPage = ref(1);
        const isLoading = ref(false);
        //총 페이지 수 계산
        const numberOfPages = computed(() => {
            return Math.ceil((numberOflist.value / limit));
        });
        const setLoading = () => {
            isLoading.value = true;
        }
        provide('setLoading', setLoading);

        /**
         * PodList 불러오기
         */
        const getPodList = async (page = currentPage.value) => {
            currentPage.value = page;
            try {
                const searchQuery = route.params.searchInput?`&search=${route.params.searchInput}`:""
                const {data} = await axios.get(
                    `/pod-service/list?page=${currentPage.value}${searchQuery}`);
                console.log("podlist:"+data)
                items.value = data.list;
                numberOflist.value = data.count;
            } catch (err) {
                console.log(err);
            } finally {
                isLoading.value = false;
            }
        };
        onMounted(() =>{
            getPodList();
            setLoading();
        })
        /**
         * Pod 상세조회
         */
        const poddetail = (name) => {
            router.push('/pod/' + name);
        };


        return {
            items,poddetail,numberOfPages,getPodList,currentPage

        }
    }
}


</script>
