<template>
  <div>
      <Pod/>
      <Deployment/>
      <Service/>
  </div>
</template>

<script>
import Service from '@/components/service/Service.vue';
import Pod from "@/components/pod/Pod.vue";
import Deployment from "@/components/deployment/Deployment.vue";

export default {
    components:{
        Deployment,
        Pod,
        Service,
    },
    name:"SearchPage",

}
</script>

<style>

</style>