<template>
  <section>
    <base-card>
      <h2>Page not found</h2>
      <p>존재하지 않는 페이지입니다. 홈으로 <router-link to="/">이동</router-link></p>
    </base-card>
  </section>
</template>

<script>
import BaseCard from "@/ui/BaseCard.vue";
export default {
  components: {
    BaseCard
  }
}
</script>