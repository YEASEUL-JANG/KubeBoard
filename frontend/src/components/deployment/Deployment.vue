<template>
  <div>
    <table-slot header="디플로이먼트">
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
          <th>데이터 기록</th>
          <th>스케일</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="items.length==0">
          <td colspan="8"><h5>표시할 데이터가 없습니다.</h5></td>
        </tr>
        <tr v-for="(item,index) in items" :key="index">
          <td>{{ item.name }}</td>
          <td>{{ item.namespace }}</td>
          <td>{{ item.image }}</td>
          <td>
            <label-list :labels="JSON.parse(item.labels)"></label-list>
          </td>
          <td>{{ item.readyReplicas }}/{{ item.replicas }}</td>
          <td v-show="false"></td>
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
      <template v-slot:pageSlot>
        <Pagination :currentPage="currentPage"
                          :numberOfPages="numberOfPages"
                          @getList="getdepl"/>
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
  </div>
</template>

<script>
import axios from "axios";
import { computed, provide, ref } from "vue";
import { useRouter } from 'vue-router';
import LabelList from "@/components/common/LabelList.vue";
import Pagination from "@/components/common/Pagination.vue";
import DeploymentModal from "@/components/deployment/DeploymentModal.vue";

export default {
  components: { DeploymentModal, Pagination, LabelList },
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
    const isLoading = ref(false);
    const update = ref(false);

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

    const setLoading = () => {
      isLoading.value = true;
    }

    provide('setLoading', setLoading);

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
          var reload = setInterval(async () => {
            await getdepl(currentPage.value);
            //update값이 모두 false이면
            if(!update.value){
              clearInterval(reload);
            }
          }, 2000);
        }
      } catch (err) {
        console.log(err);
      }
    }
    //데이터 불러오기
    const getdepl = async (page = currentPage.value) => {
      currentPage.value = page;
      try {
        const { data } = await axios.get(
            `/deployment/list?page=${currentPage.value}`);
        items.value = data.list;
        for(let item of data.list){
          if(item.readyReplicas != item.replicas){
            update.value = true;
            break;
          }
          update.value = false;
        }
        numberOflist.value = data.count;
      } catch (err) {
        console.log(err);
      } finally {
        isLoading.value = false;
      }
    };

    setLoading();
    getdepl();
    //페이지 reload
    var reload = setInterval(async() => {
      await getdepl(currentPage.value);
      //update값이 모두 false이면
      if(!update.value){
        clearInterval(reload);
      }
    }, 2000);

    //디플로이먼트 데이터 페이지 이동
    const deploymentdetail = (name) => {
      router.push('/deployment/' + name);
    };

    return {
      items, deploymentdetail, getdepl, showmodal, currentreplica, currentdeployment,
      currentPage, numberOfPages, changereplica, openmodal, closeModal, currentnamespace, isLoading, setLoading
    }
  }
}

</script>

<style scoped>

</style>