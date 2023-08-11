<template>
    <table-slot>
      <template v-slot:header>
        <router-link class="card-title" to="/deployment">디플로이먼트</router-link>
      </template>
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else-if="!isLoading && items.length !== 0">
        <thead>
        <tr>
          <th>이름</th>
          <th>네임스페이스</th>
          <th>이미지</th>
          <th>레이블</th>
          <th>상태(준비/생성)</th>
          <th>생성시간</th>
          <th>데이터 기록</th>
          <th>스케일</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item,index) in items" :key="index">
          <td>{{ item.name }}</td>
          <td>{{ item.namespace }}</td>
          <td>{{ item.image }}</td>
          <td>
            <label-list :labels="JSON.parse(item.labels)"></label-list>
          </td>
          <td>{{ item.readyReplicas }}/{{ item.replicas }}</td>
          <td>{{ item.createdTime }}</td>
          <td>
            <button type="button" class="btn btn-secondary btn-sm" @click="deploymentdetail(item.name)">조회</button>
          </td>
          <td>
            <button type="button" class="btn btn-outline-secondary btn-sm"
                    @click="openmodal(index)">
              <i class="bi bi-three-dots-vertical"></i>
            </button>
          </td>
        </tr>
        </tbody>
      </table>
      <div v-else>
      <h3> 결과값이 존재하지 않습니다.</h3>
      <router-link class="more" to="/deployment">...more</router-link>
      </div>
      <template v-slot:pageSlot>
        <DeployPagination :currentPage="currentPage"
                          :numberOfPages="numberOfPages"
                          @getdepl="getdepl"/>
      </template>
    </table-slot>
    <teleport to="#modal">
      <DeploymentModal v-if="showmodal"
                       :currentreplica="currentreplica"
                       :currentdeployment="currentdeployment"
                       :currentnamespace="currentnamespace"
                       @changereplica="changereplica"
                       @close="closeModal"/>
    </teleport>
</template>

<script>
import axios from "axios";
import { computed, provide, ref } from "vue";
import { useRoute, useRouter } from 'vue-router';
import LabelList from "@/components/common/LabelList.vue";
import DeployPagination from "@/components/common/Pagination.vue";
import DeploymentModal from "@/components/deployment/DeploymentModal.vue";
import TableSlot from '@/layout/TableSlot.vue';

export default {
  components: { DeploymentModal, DeployPagination, LabelList, TableSlot },
  setup() {
    const items = ref([]);
    const router = useRouter();
    const limit = 5;
    const currentPage = ref(1);
    const numberOflist = ref(0);
    const showmodal = ref(false);
    const currentreplica = ref(0);
    const currentdeployment = ref("");
    const currentnamespace = ref("");
    const route = useRoute();
    const isLoading = ref(false);


    //총 페이지 수 계산
    const numberOfPages = computed(() => {
      return Math.ceil((numberOflist.value / limit));
    });
    //모달열기
    const openmodal = (index) => {
      showmodal.value = true;
      currentnamespace.value = items.value[index].namespace;
      currentreplica.value = items.value[index].replicas;
      currentdeployment.value = items.value[index].name;
    }
    //모달닫기
    const closeModal = () => {
      showmodal.value = false;
      currentreplica.value = 0;
      currentdeployment.value = "";
      currentnamespace.value = "";
    }
    //레플리카 수정
    const changereplica = async (setreplica) => {
      const name = currentdeployment.value;
      const namespace = currentnamespace.value;
      try {
        const { data } = await axios.get(
            `/deployment/scale?name=${ name }&namespace=${ namespace }&scale=${ setreplica }`);
        if (data == 1) {
          closeModal();
          if (currentPage.value == 1) {
            router.go(0);
          }
          getdepl(currentPage.value);
        }
      } catch (err) {
        console.log(err);
      } finally {
        isLoading.value = false;
      }
    }

    const setLoading = () => {
      isLoading.value = true;
    }

    provide('setLoading', setLoading);

    const getdepl = async (page = currentPage.value) => {
      currentPage.value = page;
      const offset = (currentPage.value - 1) * limit;
      const sublist = limit * currentPage.value;//0,5/5,

      try {
        const { data } = await axios.get(
            `/deployment/search/` + route.params.searchInput + `?offset=${ offset }&sublist=${ sublist }`);
        items.value = data.list;
        numberOflist.value = data.count;
      } catch (err) {
        console.log(err);
      } finally {
        isLoading.value = false;
      }
    };

    setLoading();
    getdepl();

    //데이터 reroad
    setInterval(() => {
      getdepl(currentPage.value);
    }, 3000);
    //디플로이먼트 데이터 페이지 이동
    const deploymentdetail = (name) => {
      router.push('/deployment/' + name);
    };
    return {
      items, deploymentdetail, getdepl, showmodal, currentreplica, currentdeployment,
      currentPage, numberOfPages, changereplica, openmodal, closeModal, currentnamespace, isLoading
    }
  },
  data() {
    return {
      searchInput: '',
    }
  },

  methods: {
    getSearchInput() {
      this.searchInput = this.$route.params.searchInput;
    }
  }
}

</script>

<style scoped>
.card-title {
  font-size: 24px;
}
</style>