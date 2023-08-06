<template>
  <span v-for="(value, key) in labels" :key="key" class="badge rounded-pill bg-secondary">{{ key + " = " + value }}</span>
</template>

<script>
export default {
  props: ['labels']
}
</script>

<style scoped>
span {
  margin: 0 2px;
}
</style>