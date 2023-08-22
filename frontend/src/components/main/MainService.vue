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
        <th>레이블</th>
        <th>생성시간</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="item in serviceList" :key="item">
        <td>{{ item.serviceName }}</td>
        <td>{{ item.type }}</td>
        <td>{{ item.clusterIp }}</td>
        <td><label-list :labels="JSON.parse(item.labels)"></label-list></td>
        <td>{{ item.createdTime }}</td>
      <td>
        <button type="button" class="btn btn-secondary btn-sm">
          <router-link class="more" style="color: white; text-decoration: none;" :to="{
              name: 'serviceView',
              params: { name: item.serviceName },
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
      this.$axios.get("service/listall")
          .then(function (response) {
              //3개의 행만 불러온다.
            vm.serviceList = response.data.list.slice(0,3);
          })
    }
  },

  mounted() {
    this.getServicePreview();

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