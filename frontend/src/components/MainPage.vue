<template>
  <table-slot>
    <MainWorkload />
    <MainService />
  </table-slot>
</template>

<script>
import TableSlot from '@/layout/TableSlot.vue'
import MainWorkload from './MainWorkload.vue'
import MainService from './MainService.vue'

export default {
  components: { TableSlot, MainWorkload, MainService },

}
</script>

<style>

</style>
