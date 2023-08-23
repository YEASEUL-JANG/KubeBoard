<template>
  <table-slot>
    <template v-slot:header>
      <router-link class="card-title" to="/pod">파드</router-link>
    </template>
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else-if="!isLoading && !error && items.length !== 0">
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
                      @getList="loadPods"/>
      </template>
  </table-slot>
</template>

<script>
import Pagination from '@/components/common/Pagination.vue';
import LabelList from '../common/LabelList.vue';
import {computed, onMounted, provide, ref} from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";
import axios from "axios";

export default {
    components: {
        Pagination,
        LabelList,
    },
    setup() {
        const items = ref([]);
        const currentPage = ref(1);
        const numberOflist = ref(0);
        const isLoading = ref(false);
        const error = ref(null);
        const route = useRoute();
        const router = useRouter();
        const store = useStore();
        const limit = 5;

        //총 페이지 수 계산
        const numberOfPages = computed(() => {
            return Math.ceil((numberOflist.value / limit));
        });

        const setLoading = () => {
            isLoading.value = true;
        }
        provide('setLoading', setLoading);

        const loadPods = async (page = currentPage.value) => {
            isLoading.value = true;
            currentPage.value = page;
            try {
                const {data} = await axios.get(
                    `/pod/list/search/` + route.params.searchInput + `?page=` + page);
                items.value = data.list;
                numberOflist.value = data.count;
            } catch (err) {
                console.log(err);
            } finally {
                isLoading.value = false;
            }
        };

        const paramExists = () => {
            return !(route.params !== undefined && route.params !== null);
        };
        /**
         * Pod 상세조회
         */
        const poddetail = (name) => {
            router.push('/pod/' + name);
        };

        onMounted(() =>{
            loadPods();
            setLoading();
        })
        return {items,poddetail, numberOfPages, currentPage, isLoading, error, router, store, loadPods, paramExists};
    }
}
</script>

<style scoped>
.card-title {
    font-size: 24px;
}
</style>
