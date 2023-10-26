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
    import {ref} from "vue";
    export default {
        props:['resource'],
        emits:['createResource'],
        setup(props,{emit}){
            const name = ref("");
            const namespace = ref("");
            const createResource = ()=>{
                emit('createResource', {name:name.value, namespace:namespace.value});
            };
            const onclose = () =>{
                emit('close');
            }
            return{
                createResource,onclose,name, namespace
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
    .warningtext{
        background-color: #6E6E6E;
        color:white;
        font-size: 13px;
        padding: 5px;
    }
</style>