<template>
    <div>
  <table-slot header="파드">
      <template v-slot:create>
          <button class="btn btn-outline-secondary btn-outline-bold" @click="openCreateModal">
              <span v-if="createLoading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
              <span v-if="!createLoading">생성하기</span>
          </button>
<!--          <a class="btn btn-outline-secondary btn-outline-bold" href="#" @click="openCreateModal">생성하기</a>-->
      </template>
    <base-spinner v-if="isLoading"></base-spinner>
    <table v-else>
      <thead>
      <tr>
        <th>이름</th>
        <th>네임스페이스</th>
        <th>상태</th>
        <th>생성시간</th>
        <th>PodIP</th>
        <th>노드 명</th>
        <th>레이블</th>
        <th>데이터 기록</th>
        <th>삭제</th>
      </tr>
      </thead>
      <tbody>
      <tr v-if="items.length === 0">
          <td colspan="9"><h5>표시할 데이터가 없습니다.</h5></td>
      </tr>
      <tr v-for="(pod, index) in items" :key="index">
        <td>{{ pod.podName }}</td>
        <td>{{pod.namespace}}</td>
        <td>{{ pod.phase }}</td>
        <td>{{ pod.createdTime }}</td>
        <td>{{ pod.podIp }}</td>
        <td>{{ pod.nodeName }}</td>
        <td>
          <label-list :labels="JSON.parse(pod.labels)"></label-list>
        </td>
        <td>
          <a class="btn btn-secondary btn-sm" href="#"
             @click="poddetail(pod.podName)"
          >조회</a>
        </td>
          <td>
              <button class="btn btn-outline-secondary btn-sm" @click="deletePod(pod.podName, pod.namespace)">
                  <span v-if="deleteLoading[pod.podName]" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                  <span v-if="!deleteLoading[pod.podName]">삭제</span>
              </button>
          </td>
      </tr>
      </tbody>
    </table>
      <template v-slot:pageSlot>
          <Pagination :currentPage="currentPage"
                            :numberOfPages="numberOfPages"
                            @getList="getPodList"/>
      </template>
  </table-slot>

<!--  모달창-->
    <teleport to="#modal">
        <CreateModal v-if="showmodal"
                         :resource="resource"
                         @createResource="createPod"
                         @close="closeModal"/>
    </teleport>
    </div>
</template>
<script>
import axios from "axios";
import {computed, onMounted, provide, reactive, ref} from "vue";
import {useRoute, useRouter} from 'vue-router';
import LabelList from '../common/LabelList.vue';
import Pagination from "@/components/common/Pagination.vue";
import CreateModal from "@/components/common/CreateModal.vue";
export default {
  components: {
      CreateModal,
      Pagination,
    LabelList,
  },
    setup() {
        const items = ref([]);
        const router = useRouter();
        const route = useRoute();
        const limit = 5;
        const numberOflist = ref(0);
        const currentPage = ref(1);
        const isLoading = ref(false);
        const createLoading = ref(false);
        const deleteLoading = reactive({});
        const showmodal = ref(false);
        const resource = "pod";
        //총 페이지 수 계산
        const numberOfPages = computed(() => {
            return Math.ceil((numberOflist.value / limit));
        });
        const setLoading = () => {
            isLoading.value = true;
        }
        provide('setLoading', setLoading);

        /**
         * PodList 불러오기
         */
        const getPodList = async (page = currentPage.value) => {
            currentPage.value = page;
            try {
                const searchQuery = route.params.searchInput?`&search=${route.params.searchInput}`:""
                const {data} = await axios.get(
                    `/pod-service/list?page=${currentPage.value}${searchQuery}`);
                console.log("podlist:"+data)
                items.value = data.list;
                numberOflist.value = data.count;
            } catch (err) {
                console.log(err);
            } finally {
                isLoading.value = false;
            }
        };
        onMounted(() =>{
            getPodList();
            setLoading();
        })
        /**
         * Pod 상세조회
         */
        const poddetail = (name) => {
            router.push('/pod/' + name);
        };

        /**
         * Pod 생성하기
         */
        const createPod = async (payload) => {
            try {
                createLoading.value = true;
                closeModal();
                //console.log(typeof payload.name, typeof payload.namespace)
                const {data} = await axios.post('/pod-service/create', {
                    name: payload.name,
                    namespace: payload.namespace
                });
                if (data) {
                    createLoading.value = false;
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
         * Pod 삭제하기
         */
        const deletePod = async (name, namespace) => {
            if(confirm("정말 삭제하시겠습니까?")){
                try {
                    deleteLoading[name] = true
                    const {data} = await axios.post('/pod-service/delete', {
                        name: name,
                        namespace: namespace
                    });
                    if (data) {
                        deleteLoading[name] = false
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
        //모달열기
        const openCreateModal = () => {
            showmodal.value = true;

        }
        //모달닫기
        const closeModal = () => {
            showmodal.value = false;
        }

        return {
            items,
            poddetail,
            numberOfPages,
            getPodList,
            currentPage,
            isLoading,
            openCreateModal,
            closeModal,
            resource,
            createPod,
            showmodal,
            createLoading,
            deletePod,
            deleteLoading

        }
    }
}


</script>
