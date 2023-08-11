<template>
  <table-slot>
    <template v-slot:header>
      <router-link class="card-title" to="/service">
        서비스
      </router-link>
    </template>
    <table>
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
    <tr v-for="item in serviceList" :key="item">
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
          <router-link class="more" style="color: white; text-decoration: none;" :to="{
              name: 'serviceView',
              params: { name: item.name },
            }">조회
          </router-link>
        </button>
      </td>
    </tr>
    </tbody>
    </table>
    <router-link class="more" to="/service">...more</router-link>
  </table-slot>
</template>

<script>
import TableSlot from '@/layout/TableSlot.vue';
import LabelList from "@/components/common/LabelList.vue";

export default {
  components: { TableSlot, LabelList },

  data() {
    return {
      serviceList: [],
    }
  },

  methods: {
    getServicePreview() {
      var vm = this;
      this.$axios
          .get("/api/service/mainPageList")
          .then(function (response) {
            console.log((response.data));
            vm.serviceList = response.data;

          })
    }
  },

  mounted() {
    //this.getServicePreview();

  },
};
</script>

<style scoped>
h4 {
  margin-top: 0;
}
.more {
  display: block;
  text-align: right;
}
</style>