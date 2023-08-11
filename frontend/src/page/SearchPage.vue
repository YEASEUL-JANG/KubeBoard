<template>
  <div>
  <SearchDepl />
  <SearchPod />
  <SearchService />
  </div>
</template>

<script>
import SearchService from '@/components/service/SearchService.vue';
import SearchPod from '@/components/pod/SearchPod.vue';
import SearchDepl from '@/components/deployment/SearchDepl.vue';

export default {
    components:{
        SearchService,
        SearchPod,
        SearchDepl,
    },
    name:"SearchPage",
}
</script>

<style>

</style>