<template>
    <div>
  <table-slot header="서비스">
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
          <th>타입</th>
          <th>클러스터 IP</th>
          <th>생성시간</th>
          <th>레이블</th>
          <th>상세</th>
          <th>삭제</th>
      </tr>
      </thead>
        <tbody>
        <tr v-if="items.length === 0">
            <td colspan="8"><h5>표시할 데이터가 없습니다.</h5></td>
        </tr>
        <tr v-for="(item, index) in items" :key="index">
            <td>{{ item.serviceName }}</td>
            <td>{{ item.type }}</td>
            <td>{{ item.clusterIp }}</td>
            <td>{{ item.createdTime }}</td>
            <td><label-list :labels="JSON.parse(item.labels)"></label-list></td>
            <td>
                <button type="button" class="btn btn-secondary btn-sm" @click="serviceDetail(item.serviceName)">조회</button>
            </td>
            <td>
                <button class="btn btn-outline-secondary btn-sm" @click="deleteService(item.serviceName, item.namespace)">
                    <span v-if="deleteLoading[item.serviceName]" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                    <span v-if="!deleteLoading[item.serviceName]">삭제</span>
                </button>
            </td>
        </tr>
      </tbody>
    </table>
      <template v-slot:pageSlot>
          <Pagination :currentPage="currentPage"
                      :numberOfPages="numberOfPages"
                      @getList="getServiceList"/>
      </template>
  </table-slot>
    <teleport to="#modal">
        <CreateModal v-if="showcreatemodal"
                     :resource="resource"
                     @createResource="createService"
                     @close="closeModal"/>
    </teleport>
    </div>
</template>
<script>
import {ref, onMounted, computed, provide, reactive} from "vue";
import LabelList from "@/components/common/LabelList.vue";
import axios from "axios";
import Pagination from "@/components/common/Pagination.vue";
import router from "@/router";
import {useRoute} from "vue-router";
import CreateModal from "@/components/common/CreateModal.vue";
import store from "@/store/store";


export default {
    name: "serviceView",
    components: {
        CreateModal,
        Pagination,
        LabelList,
    },
    setup() {
        const items = ref([]);
        const numberOflist = ref(0);
        const currentPage = ref(1);
        const isLoading = ref(false);
        const limit = 5;
        const route = useRoute();
        const showcreatemodal = ref(false);
        const resource = "service";
        const createLoading = ref(false);
        const deleteLoading = reactive({});
        const setLoading = () => {
            isLoading.value = true;
        }
        const getServiceList = async (page = currentPage.value) => {
            currentPage.value = page;
            try {
                const searchQuery = route.params.searchInput?`&search=${route.params.searchInput}`:""
                const {data} = await axios.get(`/service-service/list?page=${currentPage.value}${searchQuery}`);
                console.log("serviceList : ", data)
                items.value = data.list;
                numberOflist.value = data.count;
            }catch(e){
                console.log(e);
            }finally {
                isLoading.value = false;
            }
        };
        //총 페이지 수 계산
        const numberOfPages = computed(() => {
            return Math.ceil((numberOflist.value / limit));
        });

        onMounted(() =>{
            getServiceList();
            setLoading();
        })
        //setLoading 함수를 하위 컴포넌트에 주입 -> 하위 컴포넌트에서 'inject'함수를 사용해 가져와서 사용가능.
        provide('setLoading', setLoading);

        /**
         * service 상세조회
         */
        const serviceDetail = (name) => {
            router.push('/service/' + name);
        };
        //생성 모달열기
        const openCreateModal = () => {
            showcreatemodal.value = true;

        }
        //모달닫기
        const closeModal = () => {
            showcreatemodal.value = false;
        }
        /**
         * service 생성하기
         */
        const createService = async (payload) => {
            try {
                createLoading.value = true;
                closeModal();
                console.log(payload)
                const {data} = await axios.post('/service-service/create', {
                    name: payload.name,
                    namespace: payload.namespace,
                    port: payload.port,
                    targetport: payload.targetport,
                    label: payload.label,
                    protocol: payload.protocol,
                    type: payload.type,
                    userId: store.getters.getId
                });
                if (data) {
                    createLoading.value = false;
                    // window.location.reload();
                  router.go(0)
                }else{
                    alert("생성실패")
                    createLoading.value = false;
                }
            } catch (err) {
                console.log(err);
            }
        };
        /**
         * service 삭제하기
         */
        const deleteService = async (name, namespace) => {
            if(confirm("정말 삭제하시겠습니까?")){
                try {
                    deleteLoading[name] = true
                    const {data} = await axios.post('/service-service/delete', {
                        name: name,
                        namespace: namespace,
                        userId: store.getters.getId
                    });
                    if (data) {
                        deleteLoading[name] = false
                        alert("삭제가 완료되었습니다.")
                        // window.location.reload();
                       router.go(0)
                    }else{
                        deleteLoading[name] = false
                        alert("삭제 실패")
                    }
                } catch (err) {
                    console.log(err);
                }
            }

        };
        return {
            items,
            isLoading,
            serviceDetail,
            getServiceList,
            numberOfPages,
            currentPage,
            setLoading,
            closeModal,
            openCreateModal,
            createService,
            resource,
            deleteService,
            showcreatemodal,
            createLoading,
            deleteLoading
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
