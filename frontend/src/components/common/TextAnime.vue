<template>
    <div class="TextAnime1">
        <h1
        v-for="(t, index) in text1"
        :key="index"
        class="item"
        :style="{animationDelay: index*100+'ms'}"
        v-text="t"
        />
        <br />
        <h5
        v-for="(t, index) in text2"
        :key="index"
        class="item"
        :style="{animationDelay: index*40+'ms'}"
        v-text="t"
        />
        <br />
        <p
        v-for="(t, index) in text3"
        :key="index"
        class="item"
        :style="{animationDelay: index*180+'ms'}"
        v-text="t"
        />
        <br />
        <p
        v-for="(t, index) in text4"
        :key="index"
        class="item"
        :style="{animationDelay: index*70+'ms'}"
        v-text="t"
        />
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        text1: 'Kubernetes Dashboard',
        text2: 'Kubernetes를 이용한 Deployment, Pod, Service 관리 플랫폼',
        text3: 'by STRATO 2기',
        text4: '김범현, 김원백, 엽은준, 이용찬, 임하영, 장예슬'
      }
    }
  }
  </script>
  
  <style scoped>
  @keyframes text-in {
    0% {
      transform: translate(0, -20px);
      opacity: 0;
    }
  }
  .item {
    display: inline-block;
    min-width: 0.3em;
    animation: text-in .8s cubic-bezier(0.22, 0.15, 0.25, 1.43) 0s backwards;
  }
  </style>