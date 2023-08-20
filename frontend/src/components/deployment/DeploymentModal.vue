<template>
    <div class="black-bg">
        <div class="white-bg">
            <h4>리소스 스케일링</h4>
            <hr/>
            <p>deployment '{{currentdeployment}}' 가 의도한 레플리카 수를 반영하도록 업데이트 됩니다.</p>
            <div class="row mt-4">
                <div class="col-4">
                    <p style="font-size:12px">의도한 레플리카</p>
                    <input type="number" min="1" v-model="setreplica">
                </div>
                <div class="col-4">
                    <p style="font-size:12px">실제 레플리카</p>
                    {{currentreplica}}
                </div>
            </div>
           <br>
            <span class="warningtext">
                <i class="bi bi-exclamation-circle-fill"></i>
                이 액션은 다음 커맨드와 동일합니다.
                kubectl scale -n {{currentnamespace}} deployment {{currentdeployment}} --replicas={{setreplica}}
            </span>
            <hr/>
            <div align="right">
            <button type="button" class="btn btn-outline-secondary" @click="changeReplica" >스케일</button>
            <button type="button" class="btn btn-secondary ms-lg-3" @click="onclose">취소</button>
            </div>
        </div>
    </div>
</template>
<script>
    import {ref} from "vue";
    export default {
        props:['currentdeployment','currentreplica','currentnamespace'],
        emits:['changereplica'],
        setup(props,{emit}){
            const setreplica = ref(props.currentreplica);

            const changeReplica = ()=>{
                emit('changeReplica',setreplica.value);
            };
            const onclose = () =>{
                emit('close');
            }
            return{
                changeReplica,setreplica,onclose
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