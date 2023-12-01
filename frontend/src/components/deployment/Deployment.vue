<template>
  <div>
    <table-slot header="디플로이먼트">
        <template v-slot:create>
            <button class="btn btn-outline-secondary btn-outline-bold" @click="openCreateModal">
                <span v-if="createLoading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                <span v-if="!createLoading">생성하기</span>
            </button>
        </template>
      <base-spinner v-if="isLoading"></base-spinner>
      <table v-else>
        <thead>
        <tr>
          <th>이름</th>
          <th>네임스페이스</th>
          <th>레이블</th>
          <th>상태(준비/생성)</th>
          <th>생성시간</th>
          <th>상세</th>
          <th>스케일</th>
          <th>삭제</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="items.length==0">
          <td colspan="8"><h5>표시할 데이터가 없습니다.</h5></td>
        </tr>
        <tr v-for="(item,index) in items" :key="index">
          <td>{{ item.deploymentName }}</td>
          <td>{{ item.namespace }}</td>
          <td>
            <label-list :labels="JSON.parse(item.labels)"></label-list>
          </td>
          <td>{{ item.readyReplicas }}/{{ item.replicaCount }}</td>
          <td>{{ item.createdTime }}</td>
          <td>
            <button type="button" class="btn btn-secondary btn-sm" @click="deploymentdetail(item.deploymentName)">조회</button>
          </td>
          <td>
            <button type="button" class="btn btn-outline-secondary btn-sm"
                    @click="openscalemodal(index)">
              스케일
            </button>
          </td>
            <td>
                <button class="btn btn-outline-secondary btn-sm" @click="deleteDeployment(item.deploymentName, item.namespace)">
                    <span v-if="deleteLoading[item.deploymentName]" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                    <span v-if="!deleteLoading[item.deploymentName]">삭제</span>
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
<!--      모달창-->
    <teleport to="#modal">
      <DeploymentModal v-if="showscalemodal"
                       :currentreplica="currentreplica"
                       :currentdeployment="currentdeployment"
                       :currentnamespace="currentnamespace"
                       @changeReplica="changeReplica"
                       @close="closeModal"/>
    </teleport>
      <teleport to="#modal">
          <CreateModal v-if="showcreatemodal"
                       :resource="resource"
                       @createResource="createDeployment"
                       @close="closeModal"/>
      </teleport>
  </div>
</template>

<script>
import axios from "axios";
import {computed, onMounted, provide, reactive, ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import LabelList from "@/components/common/LabelList.vue";
import Pagination from "@/components/common/Pagination.vue";
import DeploymentModal from "@/components/deployment/DeploymentModal.vue";
import CreateModal from "@/components/common/CreateModal.vue";
import store from "@/store/store";

export default {
  components: {CreateModal, DeploymentModal, Pagination, LabelList },
  setup() {
    const items = ref([]);
    const router = useRouter();
      const route = useRoute();
    const limit = 5;
    const currentPage = ref(1);
    const numberOflist = ref(0);
    const showscalemodal = ref(false);
    const showcreatemodal = ref(false);
    const currentreplica = ref(0);
    const currentdeployment = ref("");
    const currentnamespace = ref("");
    const isLoading = ref(false);
    const createLoading = ref(false);
    const deleteLoading = reactive({});
    const update = ref(false);
    const resource = "deployment";

    //총 페이지 수 계산
    const numberOfPages = computed(() => {
      return Math.ceil((numberOflist.value / limit));
    });

    //스케일 모달열기
    const openscalemodal = (index) => {
      showscalemodal.value = true;
      currentnamespace.value = items.value[index].namespace;
      currentreplica.value = items.value[index].replicaCount;
      currentdeployment.value = items.value[index].deploymentName;
    }
      //생성 모달열기
      const openCreateModal = () => {
          showcreatemodal.value = true;

      }

    //모달닫기
    const closeModal = () => {
      showscalemodal.value = false;
      showcreatemodal.value = false;
      currentreplica.value = 0;
      currentdeployment.value = "";
      currentnamespace.value = "";
    }

    const setLoading = () => {
      isLoading.value = true;
    }

    provide('setLoading', setLoading);

    //레플리카 수정
    const changeReplica = async (setreplica) => {
      try {
        const { data } = await axios.post(
            `/deployment-service/scale`,
             {
                name: currentdeployment.value,
                namespace: currentnamespace.value,
                scale: setreplica,
                userId: store.getters.getId
            });

        if (data === 1) {
          closeModal();
          var reload = setInterval( async () => {
            await getUpdate()
            //update값이 모두 false이면(업데이트 완료)
              if(!update.value){
              clearInterval(reload);

            }
          }, 2000);
        }
      } catch (err) {
        console.log(err);
      }
    }
      /**
       * deployment 생성하기
       */
      const createDeployment = async (payload) => {
          try {
              createLoading.value = true;
              closeModal();
              const {data} = await axios.post('/deployment-service/create', {
                  name: payload.name,
                  namespace: payload.namespace,
                  replica: payload.replica,
                  userId: store.getters.getId
              });
              if (data) {
                  createLoading.value = false;
                  //pod update
                  await axios.get(
                      `/pod-service/batch`);
                  window.location.reload();
              }else{
                  alert("생성실패")
                  createLoading.value = false;
              }
          } catch (err) {
              console.log(err);
          }
      };
      /**
       * deployment 삭제하기
       */
      const deleteDeployment = async (name, namespace) => {
          if(confirm("정말 삭제하시겠습니까?")){
              try {
                  deleteLoading[name] = true
                  const {data} = await axios.post('/deployment-service/delete', {
                      name: name,
                      namespace: namespace,
                      userId: store.getters.getId
                  });
                  if (data) {
                      deleteLoading[name] = false
                      //pod update
                      await axios.get(
                          `/pod-service/batch`);
                      alert("삭제가 완료되었습니다.")
                      window.location.reload();
                  }else{
                      deleteLoading[name] = false
                      alert("삭제 실패")
                  }
              } catch (err) {
                  console.log(err);
              }
          }

      };
      //데이터 업데이트
      const getUpdate = async () => {
          try {
              const result = await axios.get(
                  `/deployment-service/batch`);
             if(result.status===200){
                 await getdepl(currentPage.value);
             }
          } catch (err) {
              console.log(err);
          }
      };


    //데이터 불러오기
    const getdepl = async (page = currentPage.value) => {
      currentPage.value = page;
      try {
          const searchQuery = route.params.searchInput?`&search=${route.params.searchInput}`:""
        const { data } = await axios.get(
            `/deployment-service/list?page=${currentPage.value}${searchQuery}`);
        items.value = data.list;
        for(let item of data.list){
            console.log("readyReplicas, replicaCount",item.readyReplicas,item.replicaCount)
          if(item.readyReplicas !== item.replicaCount){ // 업데이트 중이면
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
      onMounted(() =>{
          setLoading();
          getdepl();
      })


    //디플로이먼트 데이터 페이지 이동
    const deploymentdetail = (name) => {
      router.push('/deployment/' + name);
    };

    return {
      items,
      deploymentdetail,
        getdepl,
        openscalemodal,
        currentreplica,
        currentdeployment,
      currentPage,
        numberOfPages,
        changeReplica,
        openCreateModal,
        closeModal,
        currentnamespace,
        isLoading,
        setLoading,
        resource,
        deleteLoading,
        createLoading,
        createDeployment,
        deleteDeployment,
        showscalemodal,
        showcreatemodal,
    }
  }
}

</script>

<style scoped>

</style>