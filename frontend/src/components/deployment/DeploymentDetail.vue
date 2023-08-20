<template>
  <div>
    <div class="backlist">
    <button  type="button" class="btn btn-outline-secondary"
            @click="moveList">
      <i class="bi bi-chevron-double-left"></i> List</button>
    </div>
    <table-slot header="메타데이터">
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else>
      <thead>
      <tr>
        <th>이름</th>
        <th>네임스페이스</th>
        <th>레이블</th>
        <th>생성시간</th>
        <th>uid</th>
        <th>어노테이션</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td>{{ currentdepl.deploymentName }}</td>
        <td>{{ currentdepl.namespace }}</td>
        <td>
          <label-list :labels="JSON.parse(currentdepl.labels)"></label-list>
        </td>
        <td>{{ currentdepl.createdTime }}</td>
        <td>{{ currentdepl.uid }}</td>
        <td>
            <label-list :labels="JSON.parse(currentdepl.annotation)"></label-list>
        </td>
      </tr>
      </tbody>
      </table>
    </table-slot>

    <table-slot header="리소스 정보">
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else>
      <thead>
      <tr>
        <th>전략</th>
        <th>리비전 내역 한도</th>
        <th>Selector</th>
      </tr>
      </thead>
      <tbody>
      <tr>
        <td>{{ currentdepl.strategyType }}</td>
        <td>{{ currentdepl.revisionHistoryLimit }}</td>
        <td>
          <label-list :labels="JSON.parse(currentdepl.strategySelector)"></label-list>
        </td>
      </tr>
      </tbody>
      </table>
    </table-slot>


      <table-slot header="롤링 업데이트 정책">
          <base-spinner v-if="isLoading"></base-spinner>
          <table v-else>
              <thead>
              <tr>
                  <th>최대 증가율(surge)</th>
                  <th>최대 비가용</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                  <td>{{ currentdepl.maxSurge }}</td>
                  <td>{{ currentdepl.maxUnavilable }}</td>
              </tr>
              </tbody>
          </table>
      </table-slot>

      <table-slot header="파드 상태">
          <base-spinner v-if="isLoading"></base-spinner>
          <table v-else>
              <thead>
              <tr>
                  <th>Ready</th>
                  <th>Total</th>
                  <th>Available</th>
              </tr>
              </thead>
              <tbody>
              <tr>
                  <td>{{ currentdepl.readyReplicas }}</td>
                  <td>{{ currentdepl.replicaCount }}</td>
                  <td>{{ currentdepl.availableReplicas }}</td>
              </tr>
              </tbody>
          </table>
      </table-slot>

      <table-slot header="Condition">
          <base-spinner v-if="isLoading"></base-spinner>
          <table v-else>
              <thead>
              <tr>
                  <th>타입</th>
                  <th>상태</th>
                  <th>트랜지션 시간</th>
                  <th>이유</th>
                  <th>메시지</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item,index) in currentdepl.deploymentConditions" :key="index">
                  <td>{{ item.type }}</td>
                  <td>{{ item.status }}</td>
                  <td>{{ item.lastTransitionTime }}</td>
                  <td>{{ item.reason }}</td>
                  <td>{{ item.message }}</td>
              </tr>
              </tbody>
          </table>
      </table-slot>
  </div>
</template>

<script>
import axios from "axios";
import { ref, provide } from 'vue';
import { useRoute } from 'vue-router';
import {useRouter} from 'vue-router';
import LabelList from "@/components/common/LabelList.vue";

export default {
  components: {LabelList },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const currentdepl = ref([]);
    const name = route.params.name;
    const numberOflist = ref(0);
    const isLoading = ref(false);


    const getdepl = async () => {
      try {
        const { data } = await axios.get(
            `/deployment/list/${ name }`
        );
        console.log(data);
        numberOflist.value = data.count;
        currentdepl.value = data.list[0];
      } catch (err) {
        console.log(err);
      } finally {
        isLoading.value = false;
      }
    };

    const setLoading = () => { isLoading.value = true; };

    provide('setLoading', setLoading);

    setLoading();
    getdepl();

  //페이지 이동
    const moveList = () =>{
      router.push('/deployment');
    }
    return {
      currentdepl, numberOflist ,moveList,
      getdepl, isLoading, setLoading
    };
  }
}
</script>

<style>
  .backlist {
    display: flex;
    margin: 0 auto 0 auto;
    min-width: 60rem;
    max-width: 80rem;
  }
</style>