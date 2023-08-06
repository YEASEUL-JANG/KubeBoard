<template>
  <login-form>
    <form @submit.prevent="login">
      <img class="mb-4" :src="require('@/assets/images/strato1.png')" width="200" height="170">
      <h1 class="h3 mb-3 fw-normal">Please sign in</h1>

      <div class="form-floating">
        <input type="text" class="form-control" name="id" v-model="id">
        <label for="id">아이디</label>
      </div>

      <div class="form-floating">
        <input type="password" class="form-control" name="pwd" v-model="pwd">
        <label for="pwd">비밀번호</label>
      </div>

      <div class="mb-2">
        <button type="submit" class="w-100 btn btn-lg btn-primary">로그인</button>
      </div>

      <p class="mb-3 text-muted" style="cursor:pointer" @click="moveLink">회원가입</p>
      <p class="mt-5 mb-3 text-muted">&copy; Copyrightⓒ STRATO. ALL Right Reserved.</p>
    </form>
  </login-form>
</template>

<script>
import LoginForm from "@/layout/LoginForm.vue";

export default {
  name: "Login.vue",
  components: {LoginForm},

  //객체 혹은 함수의 형태로 vue 인스턴스가 사용할 정보를 가지고 있는 속성
  data() {
    return {
      loginSuccess: false,
      id: "",
      pwd: "",
    }
  },

  //data 속성의 데이터 값이 변경될 때 실행
  watch: {
    //로그인 성공 시 해당 페이지 이동
    loginSuccess() {
      if (this.loginSuccess) {
        this.$axios.defaults.headers.common["X-AUTH-TOKEN"] = this.$store.getters.getToken;
        this.$router.replace('/');
      }
    }
  },

  //vue 인스턴스가 사용할 함수를 저장하고 있는 속성
  methods: {
    //로그인
    //store actions 내에 auth 함수 실행, 전달인자 id, pwd
    async login() {
      await this.$store.dispatch('auth', {
        id: this.id,
        pwd: this.pwd
      });

      //store 아이디, 토큰 정보 여부 확인
      this.loginSuccess = this.$store.getters.getLogin ? true : false;
      if (!this.loginSuccess) alert('아이디 또는 비밀번호가 틀렸습니다.');
    },

    //회원가입 페이지 이동
    moveLink() {
      location.href = '/join';
    },
  }
}
</script>

<style>

</style>
