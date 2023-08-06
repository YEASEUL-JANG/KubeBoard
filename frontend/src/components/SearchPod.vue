<template>
  <table-slot>
    <template v-slot:header>
      <router-link class="card-title" to="/pod">파드</router-link>
    </template>
    <base-spinner v-if="isLoading && !error"></base-spinner>
    <table v-else-if="!isLoading && !error && podList.length !== 0">
      <thead>
      <tr>
        <th>이름</th>
        <th>준비</th>
        <th>상태</th>
        <th>재시작</th>
        <th>생성시간</th>
        <th>IP</th>
        <th>노드 명</th>
        <th>레이블</th>
        <th>데이터 기록</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(pod, index) in podList" :key="index">
        <td>{{ pod.name }}</td>
        <td>{{ pod.ready }}</td>
        <td>{{ pod.status }}</td>
        <td>{{ pod.restarts }}</td>
        <td>{{ pod.createdTime }}</td>
        <td>{{ pod.ip }}</td>
        <td>{{ pod.nodeName }}</td>
        <td>
          <label-list :labels="JSON.parse(pod.labels)"></label-list>
        </td>
        <td>
          <a class="btn btn-secondary btn-sm" href="#"
             @click="this.$router.push({
                name: 'PodDetail',
                params: { name: pod.name }
           })"
          >조회</a>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-else-if="!isLoading && error">
      <h5>잠시 후 다시 시도해주세요.</h5>
      <h5>{{ '(' + error + ')' }}</h5>
    </div>
    <div v-else>
      <h3> 결과값이 존재하지 않습니다.</h3>
      <router-link class="more" to="/deployment">...more</router-link>
    </div>
    <template v-slot:pageSlot v-if="!isLoading && !error">
      <Pagination :pagination="this.pagination"></Pagination>
    </template>
  </table-slot>
</template>

<script>
import Pagination from '@/components/Pagination.vue';
import LabelList from './LabelList.vue';
import { provide, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useStore } from "vuex";

export default {
  components: {
    Pagination,
    LabelList,
  },
  setup() {
    const podList = ref([]);
    const pagination = ref([]);
    const isLoading = ref(false);
    const error = ref(null);
    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const loadPods = () => {
      isLoading.value = true;
      const requestedPageNum = route.query.page;
      const page = requestedPageNum === undefined && isNaN(requestedPageNum) ? 1 : requestedPageNum;
      const url = store.getters.uri + 'pod/search/' +route.params.searchInput + '?page=' + page;
      console.log(url);

      fetch(url, {
        headers: { "X-AUTH-TOKEN": store.getters.getToken }
      }).then((res) => {
        if (res.ok) {
          return res.json();
        } else {
          throw new Error(res.status);
        }
      }).then((data) => {
        podList.value = data.podList;
        pagination.value = data.pagination;
      }).catch((err) => {
        error.value = err.message;
      }).finally(() => {
        isLoading.value = false;
      })
    };

    const movePage = (pageNum) => {
      if (pageNum > 0 && pageNum <= pagination.value.totalPageCnt) {
        if (paramExists()) {
          router.push({
            name: route.name,
            query: { page: pageNum },
            params: route.params,
          });
        } else {
          router.push({
            name: route.name,
            query: { page: pageNum },
          });
        }
      }
    };

    const paramExists = () => {
      if (route.params !== undefined && route.params !== null) {
        return false;
      } else {
        return true;
      }
    };

    loadPods();

    provide('movePage', movePage)

    watch(route, () => {
      loadPods();
    })

    return { podList, pagination, isLoading, error, route, router, store, loadPods, movePage, paramExists };
  }
}
</script>

<style scoped>
.card-title {
  font-size: 24px;
}
</style>
