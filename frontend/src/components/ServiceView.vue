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
      <td><label-list :labels="JSON.parse(item.label)"></label-list></td>
    </tr>
    </tbody>
    </table>
    <template v-slot:list>
      <ul class="pagination justify-content-center">
        <li v-if="pageNum != 1" class="page-item">
            <a class="page-link" @click="changePage(pageNum - 1)" style="cursor:pointer">&lt;</a></li>
        <li v-for="page in totalPage" :key="page" class="page-item"
            :class="pageNum == page ? 'active' : ''">
            <a class="page-link" @click="changePage(page)" style="cursor:pointer">{{ page }}</a></li>
        <li class="page-item" v-if="totalPage != pageNum">
            <a class="page-link" @click="changePage(pageNum + 1)" style="cursor:pointer">&gt;</a></li>
      </ul>
    </template>
  </table-slot>
  </div>
</template>

<script>
import LabelList from "@/components/LabelList.vue";

export default {
  components: {
    LabelList,
  },
  name: "serviceView",
  data() {
    return {
      item: [],
      items: [],
      pageNum: 1,
      pageSize: 5,
      totalPage: 0,
      isLoading: false
    };
  },
  created() {
    this.getServiceDB();
    this.pageCount();
    this.getServiceAPI();
  },
  methods: {
    async getServiceAPI() {
      this.isLoading = true;
      this.item = await this.$axios
        .get("/api/service/getapi", {
          params : {
            name: this.$route.params.name
          }
        })
        .then(function (response) {
          console.log(response.data);
          return response.data;
        });
      this.isLoading = false;
    },
    pageCount() {
      this.$axios
        .get("/api/service/get-count", {
          params : {
            name: this.$route.params.name,
          }
        })
        .then((response) => {
          this.totalPage = parseInt(response.data / this.pageSize) + 1;
        });
    },

    getServiceDB() {
      this.$axios
        .get("/api/service/getdb", {
          params : {
            name: this.$route.params.name,
            page: (this.pageNum - 1) * this.pageSize
          }
        })
        .then((response) => {
          this.items = response.data;
          console.log(this.items);
        });
    },

    changePage(pageNum) {
      this.pageNum = pageNum;
      this.getServiceDB();
    },
    moveList(){
      this.$router.push('/service')
    }
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
