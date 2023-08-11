<template>
  <table-slot>
    <MainWorkload />
    <MainService />
  </table-slot>
</template>

<script>
import TableSlot from '@/layout/TableSlot.vue'
import MainWorkload from '../components/main/MainWorkload.vue'
import MainService from '../components/main/MainService.vue'

export default {
  components: { TableSlot, MainWorkload, MainService },

}
</script>

<style>

</style>
