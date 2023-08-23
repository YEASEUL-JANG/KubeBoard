<template>
  <footer class="footer text-center" >
      <img :src="require('@/assets/images/kubeLogo.png')" alt="Bootstrap"  height="40">
      <span style="font-size: 25px; font-weight: bold;" class="ms-2">KubeBoard</span>
  </footer>
</template>

<script>
export default {
  name: "Footer",
};
</script>

<style>
.FooterLogo{
  height : 2vw;
  margin-bottom: 5px;
  margin-right: 10px;
}
.footer{
  font-size: 1vw;
  height: 70px;
  width: 100%;
}
</style>