<template>
  <table-slot header="서비스">
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
          <td><label-list :labels="item.label ? JSON.parse(item.label) : null"></label-list></td>
          <td><button type="button" class="btn btn-secondary btn-sm"><router-link style="color: white; text-decoration: none;" :to="{
              name: 'serviceView',
              params: { name: item.name },
            }">조회</router-link></button></td>
        </tr>
      </tbody>
    </table>
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
import { ref, onMounted, computed } from "vue";
import LabelList from "@/components/common/LabelList.vue";

export default {
    name: "ServiceList",
    components: {
        LabelList,
    },
    setup() {
        const items = ref([]);
        const pageNum = ref(1);
        const pageSize = ref(5);
        const isLoading = ref(true);

        const getServiceDB = async () => {
            isLoading.value = true;
            try {
                const response = await this.$axios.get("/api/service/list");
                console.log(response.data);
                items.value = response.data;
            } finally {
                isLoading.value = false;
            }
        };

        const pageCount = computed(() => {
            const listLeng = items.value.length;
            const listSize = pageSize.value;
            let page = Math.floor(listLeng / listSize);
            if (listLeng % listSize > 0) page += 1;
            return page;
        });

        const paginatedData = computed(() => {
            const start = (pageNum.value - 1) * pageSize.value;
            const end = start + pageSize.value;
            console.log(start, end);
            return items.value.slice(start, end);
        });

        const changePage = (newPageNum) => {
            pageNum.value = newPageNum;
        };

        onMounted(() => {
            getServiceDB();
        });

        return {
            items,
            pageNum,
            pageSize,
            isLoading,
            pageCount,
            paginatedData,
            changePage
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
