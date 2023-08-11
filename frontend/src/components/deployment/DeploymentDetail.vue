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
        <th>네임스페이스</th>
        <th>이미지</th>
        <th>레이블</th>
        <th>상태(준비/생성)</th>
        <th>생성시간</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item,index) in currentdepl" :key="index">
        <td>{{ item.name }}</td>
        <td>{{ item.namespace }}</td>
        <td>{{ item.image }}</td>
        <td>
          <label-list :labels="JSON.parse(item.labels)"></label-list>
        </td>
        <td>{{ item.readyReplicas }}/{{ item.replicas }}</td>
        <td>{{ item.createdTime }}</td>
      </tr>
      </tbody>
      </table>
    </table-slot>

    <table-slot header="업데이트 내역">
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else>
      <thead>
      <tr>
        <th>이름</th>
        <th>이미지</th>
        <th>레이블</th>
        <th>상태(준비/생성)</th>
        <th>생성시간</th>
        <th>업데이트 시간</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(item,index) in depllist" :key="index">
        <td>{{ item.name }}</td>
        <td>{{ item.image }}</td>
        <td>
          <label-list :labels="JSON.parse(item.labels)"></label-list>
        </td>
        <td>{{ item.readyReplicas }}/{{ item.replicas }}</td>
        <td>{{ item.createdTime }}</td>
        <td>{{ item.writeTime }}</td>
      </tr>
      </tbody>
      </table>
      <template v-slot:pageSlot>
        <DeployPagination :currentPage="currentPage"
                          :numberOfPages="numberOfPages"
                          @getdepl="getdepl"/>
      </template>
    </table-slot>
  </div>
</template>

<script>
import axios from "axios";
import { ref, computed, provide } from 'vue';
import { useRoute } from 'vue-router';
import {useRouter} from 'vue-router';
import LabelList from "@/components/common/LabelList.vue";
import DeployPagination from "@/components/common/Pagination.vue";

export default {
  components: { DeployPagination, LabelList },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const depllist = ref([]);
    const currentdepl = ref([]);
    const name = route.params.name;
    const limit = 5;
    const currentPage = ref(1);
    const numberOflist = ref(0);
    const isLoading = ref(false);

    //총 페이지 수 계산
    const numberOfPages = computed(() => {
      return Math.ceil((numberOflist.value / limit));
    });

    const getdepl = async (page = currentPage.value) => {
      currentPage.value = page;
      const offset = (currentPage.value - 1) * limit;
      try {
        const { data } = await axios.get(
            `/deployment/${ name }?offset=${ offset }&limit=${ limit }`
        );
        numberOflist.value = data.count;
        depllist.value = data.depllist;
        currentdepl.value = data.currentdepl;
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

    //데이터 reload
    var i =0;
    var reload = setInterval(() => {
      getdepl(currentPage.value);
      i++;
      if(i == 5){
        clearInterval(reload);
      }
    }, 3000);
  //페이지 이동
    const moveList = () =>{
      router.push('/deployment');
    }
    return {
      depllist, currentdepl, numberOflist,moveList,
      numberOfPages, getdepl, currentPage, isLoading, setLoading
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