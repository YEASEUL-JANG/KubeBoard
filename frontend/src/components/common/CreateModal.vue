<template>
    <div class="black-bg">
        <div class="white-bg">
            <h4>리소스 생성</h4>
            <hr/>
            <p>생성하고자 하는 {{resource}}의 'namespace'와 '이름'을 정해주세요. </p>
            <div class="row mt-4">
                <div class="col-4">
                    <p style="font-size:14px">namespace</p>
                    <select v-model="namespace" class="custom-select">
                        <option value="default" selected>default</option>
                    </select>
                </div>
                <div class="col-4">
                    <p style="font-size:14px">{{resource}} 명</p>
                    <input id="name" v-model="name">
                </div>
                <div class="col-4" v-if="resource==='deployment'">
                    <p style="font-size:14px">replica 수</p>
                    <input type="number" id="replica"
                           v-model="replica" min="1" >
                </div>
                <div class="col-4" v-if="resource==='service'" >
                    <p style="font-size:14px" >Label</p>
                    <select v-model="label" class="custom-select">
                        <option v-for="(label,index) in labellist" :key="index" :value="label">
                        {{label}}
                        </option>
                    </select>
                </div>
            </div>
            <div class="row mt-4" v-if="resource==='service'">
                <div class="col-3">
                    <p style="font-size:14px">type</p>
                    <select v-model="type" class="service-select" >
                        <option value="LoadBalancer" selected>LoadBalancer</option>
                        <option value="ClusterIP">ClusterIP</option>
                    </select>
                </div>
                <div class="col-3">
                    <p style="font-size:14px">protocol</p>
                    <select v-model="protocol" class="service-select">
                        <option value="TCP" selected>TCP</option>
                        <option value="UDP">UDP</option>
                    </select>
                </div>
                <div class="col-3">
                    <p style="font-size:14px">port</p>
                    <input type="number" id="port" v-model="port" min="1" class="service-select">
                </div>
                <div class="col-3">
                    <p style="font-size:14px">target port</p>
                    <input type="number" id="targetport"
                           v-model="targetport" min="1" class="service-select" >
                </div>
            </div>
            <hr/>
            <div align="right">
            <button type="button" class="btn btn-outline-secondary" @click="createResource" >생성</button>
            <button type="button" class="btn btn-secondary ms-lg-3" @click="onclose">취소</button>
            </div>
        </div>
    </div>
</template>
<script>
import {onMounted, ref} from "vue";
import axios from "axios";
    export default {
        props:['resource'],
        emits:['createResource'],
        setup(props,{emit}){
            const name = ref("");
            const namespace = ref("");
            const protocol = ref("");
            const replica = ref(1);
            const port = ref(80);
            const targetport = ref(80);
            const labellist = ref({});
            const label = ref("");
            const type = ref("");
            const createResource = ()=>{

                if (!name.value || !namespace.value) {
                    alert("이름과 namespace 를 확인주세요.");
                    return;
                }
                if(props.resource === 'deployment'){
                    if(!replica.value) {
                        alert("레플리카 수를 확인해주세요")
                        return;
                    }
                }
                if(props.resource === 'service'){
                    if(!port.value || !targetport.value){
                        alert("포트를 확인해주세요")
                        return;
                    }
                    if(!type.value ){
                        alert("타입을 지정해주세요")
                        return;
                    }
                    if(!protocol.value ){
                        alert("프로토콜을 지정해주세요")
                        return;
                    }
                    if(!label.value){
                        alert("서비스를 적용 할 레이블을 확인해주세요")
                        return;
                    }
                }
                emit('createResource', {
                    name:name.value,
                    namespace:namespace.value,
                    replica:replica.value,
                    port:port.value,
                    type:type.value,
                    targetport:targetport.value,
                    protocol:protocol.value,
                    label:label.value});
            };
            const getLabelList = async() => {
                try {
                    const {data} = await  axios.get(
                        `/pod-service/getLabels`);
                    labellist.value = data
                }catch (err){
                    console.log(err);
                }
            }
            onMounted(()=> {
                getLabelList();
            })
            const onclose = () =>{
                emit('close');
            }
            return{
                createResource,onclose,name, namespace,replica,protocol,port,targetport,labellist,label,type
            };
        }
    }
</script>
<style scoped>
    body {
        margin : 0;
    }
    div {
        box-sizing: border-box;
    }
    .black-bg {
        z-index: 10000;
        top:0;
        left:0;
        width: 100%; height:100%;
        background: rgba(0, 0, 0, 0.5);
        position: fixed; padding-top: 100px;
    }
    .white-bg {
        background: white;
        width: 700px;
        border-radius: 8px;
        margin: 0 auto;
        padding: 20px;
    }
    .service-select{
        width: 120px;
        height: 30px;
    }
</style>