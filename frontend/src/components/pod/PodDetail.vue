<template>
  <div>
    <div class="backlist">
      <button  type="button" class="btn btn-outline-secondary"
               @click="moveList">
        <i class="bi bi-chevron-double-left"></i> List</button>
    </div>
    <table-slot header="메타데이터">
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else-if="!isLoading">
        <thead>
          <tr>
            <th>파드 명</th>
            <th>네임스페이스</th>
            <th>생성시간</th>
            <th>UID</th>
            <th>레이블</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>{{ currentPod.podName }}</td>
            <td>{{ currentPod.namespace }}</td>
            <td>{{ currentPod.createdTime }}</td>
              <td>{{ currentPod.uid }}</td>
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
      </div>
    </table-slot>
      <table-slot header="제어 정보" v-if="currentPod.ownerName != null">
          <base-spinner v-if="isLoading"></base-spinner>
          <table v-else-if="!isLoading">
              <thead>
              <tr>
                  <th>이름</th>
                  <th>종류</th>
                  <th>Uid</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                  <td>{{ currentPod.ownerName }}</td>
                  <td>{{ currentPod.ownerKind }}</td>
                  <td>{{ currentPod.ownerUid }}</td>
              </tr>
              </tbody>
          </table>
      </table-slot>

      <table-slot header="리소스 정보">
          <base-spinner v-if="isLoading"></base-spinner>
          <table v-else-if="!isLoading">
              <thead>
              <tr>
                  <th>노드 명</th>
                  <th>상태</th>
                  <th>IP</th>
                  <th>QoS 클래스</th>
                  <th>서비스 어카운트</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                  <td>{{ currentPod.nodeName }}</td>
                  <td>{{ currentPod.phase }}</td>
                  <td>{{ currentPod.podIp }}</td>
                  <td>{{ currentPod.qosClass }}</td>
                  <td>{{ currentPod.serviceAccountName }}</td>
              </tr>
              </tbody>
          </table>
      </table-slot>

    <table-slot header="컨테이너 정보">
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else-if="!isLoading">
        <thead>
          <tr>
            <th>컨테이너 명</th>
            <th>이미지</th>
            <th>준비상태</th>
            <th>시작상태</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(container, index) in containerList" :key="index">
            <td>{{ container.containerName }}</td>
            <td>{{ container.containerImage }}</td>
            <td>{{ container.ready }}</td>
            <td>{{ container.started }}</td>
          </tr>
        </tbody>
      </table>
    </table-slot>
  </div>
</template>
<script>
import { ref, onMounted } from 'vue'
import BaseSpinner from "@/ui/BaseSpinner.vue";
import LabelList from "@/components/common/LabelList.vue";
import axios from "axios";
import {useRoute} from "vue-router";
import router from "@/router";

export default {
    components: {
        BaseSpinner,
        LabelList,
    },
    props: ['name'],

    setup() {
        const route = useRoute();
        const name = route.params.name;
        const containerList = ref([])
        const currentPod = ref({})
        const isLoading = ref(false)

        const loadPods = async() => {
            isLoading.value = true
                try {
                    const {data} = await axios.get(
                        `/pod/list/${name}`);
                    console.log("podDetail :", data.list[0])
                    currentPod.value = data.list[0];
                    containerList.value = data.list[0].containerList;
                } catch (err) {
                    console.log(err);
                } finally {
                    isLoading.value = false;
                }
            };


        onMounted(() => {
            loadPods()
        })
        const moveList = () =>{
            router.push('/pod');
        }

        return {
            currentPod,
            isLoading,
            moveList,
            containerList,
        }
    },
}
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
