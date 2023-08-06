<template>
  <footer class="footer text-center" >
    <a href="https://strato.co.kr/bbs/page.php?hid=m05_02"><img :src="require('@/assets/images/STRATO.png')" class="FooterLogo"/></a>
    2기 온보딩 프로젝트
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