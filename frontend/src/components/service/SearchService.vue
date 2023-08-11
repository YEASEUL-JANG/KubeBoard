<template>
  <table-slot>
    <template v-slot:header>
      <router-link class="card-title" to="/service">서비스</router-link>
    </template>
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else-if="!isLoading && serviceList.length !== 0">
      <thead>
      <tr>
        <th>이름</th>
        <th>타입</th>
        <th>클러스터 IP</th>
        <th>포트</th>
        <th>외부 IP</th>
        <th>생성시간</th>
        <th>레이블</th>
        <th>데이터 기록</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in paginatedData" :key="item">
        <td>{{ item.name }}</td>
        <td>{{ item.type }}</td>
        <td>{{ item.clusterIP }}</td>
        <td>{{ item.port }}</td>
        <td>{{ item.externalIP }}</td>
        <td>{{ item.createdTime }}</td>
        <td>
          <label-list :labels="item.label ? JSON.parse(item.label) : null"></label-list>
        </td>
        <td>
          <button type="button" class="btn btn-secondary btn-sm">
            <router-link style="color: white; text-decoration: none;" :to="{
              name: 'serviceView',
              params: { name: item.name },
            }">조회
            </router-link>
          </button>
        </td>
      </tr>
      </tbody>
    </table>
    <div v-else>
      <h3> 결과값이 존재하지 않습니다.</h3>
      <router-link class="more" to="/service">...more</router-link>
    </div>
    <ul class="pagination justify-content-center">
      <li v-if="pageNum != 1" class="page-item">
        <a class="page-link" @click="changePage(pageNum - 1)" style="cursor:pointer">&lt;</a></li>
      <li v-for="page in pageCount" :key="page" class="page-item"
          :class="pageNum == page ? 'active' : ''">
        <a class="page-link" @click="changePage(page)" style="cursor:pointer">{{ page }}</a></li>
      <li class="page-item" v-if="pageCount != pageNum">
        <a class="page-link" @click="changePage(pageNum + 1)" style="cursor:pointer">&gt;</a></li>
    </ul>

  </table-slot>
</template>

<script>
import TableSlot from '@/layout/TableSlot.vue'
import LabelList from "@/components/common/LabelList.vue";

export default {
  components: { TableSlot, LabelList },

  name: 'SearchService',

  data() {
    return {
      serviceList: [],
      pageNum: 1,
      pageSize: 5,
      isLoading: false
    }
  },

  mounted() {
    this.getServiceSearch();
  },

  computed: {
    pageCount() {
      const listLeng = this.serviceList.length;
      const listSize = this.pageSize;
      let page = Math.floor(listLeng / listSize);
      if (listLeng % listSize > 0) page += 1;

      return page;
    },

    paginatedData() {
      const start = (this.pageNum - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.serviceList.slice(start, end);
    },
  },

  methods: {
    getServiceSearch() {
      this.isLoading = true;
      this.$axios
          .get("/api/service/search/" + this.$route.params.searchInput)
          .then((response) => {
            this.serviceList = response.data;
          })
          .finally(() => {
            this.isLoading = false;
          });

    },
    changePage(pageNum) {
      this.pageNum = pageNum;
    },
  },


};

</script>

<style>
.card-title {
  font-size: 24px;
}
</style>