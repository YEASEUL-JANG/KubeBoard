<template>
  <div>
  <SearchDepl />
  <SearchPod />
  <SearchService />
  </div>
</template>

<script>
import SearchService from '@/components/SearchService.vue';
import SearchPod from '@/components/SearchPod.vue';
import SearchDepl from '@/components/SearchDepl.vue';

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