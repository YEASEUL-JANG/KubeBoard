<template>
  <div>
    <div class="backlist">
      <button  type="button" class="btn btn-outline-secondary"
               @click="moveList">
        <i class="bi bi-chevron-double-left"></i> List</button>
    </div>
    <table-slot header="실시간 정보">
      <base-spinner v-if="isLoading && !error"></base-spinner>
      <table v-else-if="!isLoading && !error">
        <thead>
          <tr>
            <th>파드 명</th>
            <th>준비</th>
            <th>상태</th>
            <th>재시작</th>
            <th>생성시간</th>
            <th>IP</th>
            <th>노드 명</th>
            <th>레이블</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ currentPod.name }}</td>
            <td>{{ currentPod.ready }}</td>
            <td>{{ currentPod.status }}</td>
            <td>{{ currentPod.restarts }}</td>
            <td>{{ currentPod.createdTime }}</td>
            <td>{{ currentPod.ip }}</td>
            <td>{{ currentPod.nodeName }}</td>
            <td>
              <label-list
                :labels="
                  currentPod.labels ? JSON.parse(currentPod.labels) : null
                "
              ></label-list>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else>
        <h5>잠시 후 다시 시도해주세요.</h5>
        <h5>{{ '(' + error + ')' }}</h5>
      </div>
    </table-slot>

    <table-slot header="업데이트 내역">
      <base-spinner v-if="isLoading && !error"></base-spinner>
      <table v-else-if="!isLoading && !error">
        <thead>
          <tr>
            <th>파드 명</th>
            <th>준비</th>
            <th>상태</th>
            <th>재시작</th>
            <th>생성시간</th>
            <th>IP</th>
            <th>노드 명</th>
            <th>레이블</th>
            <th>업데이트 시간</th>
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
              <label-list
                :labels="pod.labels ? JSON.parse(pod.labels) : null"
              ></label-list>
            </td>
            <td>{{ pod.writeTime }}</td>
          </tr>
        </tbody>
      </table>
      <template v-slot:pageSlot v-if="!isLoading && !error">
        <Pagination :pagination="this.pagination"></Pagination>
      </template>
    </table-slot>
  </div>
</template>
<script>
import Pagination from '@/components/common/Pagination.vue';
import LabelList from '../common/LabelList.vue';
import BaseSpinner from '@/ui/BaseSpinner.vue';

export default {
  components: {
    BaseSpinner,
    Pagination,
    LabelList,
  },
  provide() {
    return {
      movePage: this.movePage,
    };
  },
  data() {
    return {
      podList: [],
      pagination: {},
      currentPod: {},
      isLoading: false,
      error: null,
    };
  },
  props: ['name'],

  async mounted() {
    this.loadPods(this.$route);
  },

  methods: {
    loadPods(route) {
      this.isLoading = true;
      let requestedPageNum = route.query.page;
      let page =
        requestedPageNum === undefined && isNaN(requestedPageNum)
          ? 1
          : requestedPageNum;
      let url = this.$store.getters.uri + 'pod/' + this.name + '?page=' + page;
      fetch(url, {
        headers: {
          'X-AUTH-TOKEN': this.$store.getters.getToken,
        },
      })
        .then((res) => {
          if (res.ok) {
            return res.json();
          } else {
            throw new Error(res.status);
          }
        })
        .then((data) => {
          this.podList = data.podList;
          this.pagination = data.pagination;
          this.currentPod = data.currentPod;
        })
        .catch((error) => {
          this.error = error.message;
        })
        .finally(() => {
          this.isLoading = false;
        });
    },

    movePage(pageNum) {
      if (pageNum > 0 && pageNum <= this.pagination.totalPageCnt) {
        if (this.paramExists) {
          this.$router.push({
            name: this.$route.name,
            query: { page: pageNum },
            params: this.$route.params,
          });
        } else {
          console.log('asdsad', this.$route.name);
          this.$router.push({
            name: this.$route.name,
            query: { page: pageNum },
          });
        }
      }
    },
    paramExists() {
      if (this.$route.params !== undefined && this.$route.params !== null) {
        return false;
      } else {
        return true;
      }
    },
    moveList() {
      this.$router.push('/pod');
    },
  },

  watch: {
    $route(route) {
      this.loadPods(route);
    },
  },
};
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
