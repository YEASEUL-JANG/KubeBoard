<template>
  <login-form>
    <form @submit.prevent="login">
      <img class="mb-4" :src="require('@/assets/images/kubeLogo.png')" width="200" height="170">
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
      <p class="mt-5 mb-3 text-muted">&copy; Copyrightⓒ KubeBoard. ALL Right Reserved.</p>
    </form>
  </login-form>
</template>

<script>
import { ref, watch } from "vue";
import LoginForm from "@/components/user/LoginForm.vue";
import store from "@/store/store";
import router from "@/router";
import axios from "axios";
export default {
    name: "Login.vue",
    components: { LoginForm },

    setup() {
        const loginSuccess = ref(false);
        const id = ref("");
        const pwd = ref("");

        // 로그인 성공 시 해당 페이지로 이동
        watch(loginSuccess => {
            if (loginSuccess) {
                axios.defaults.headers.common["X-AUTH-TOKEN"]=store.getters.getToken();
                router.replace("/");
            }
        });

        // 로그인 함수
        async function login() {
            // store actions 내의 auth 함수 실행, 전달인자 id, pwd
            await store.dispatch("auth", {
                id: id.value,
                pwd: pwd.value,
            });

            // store 아이디, 토큰 정보 여부 확인
            loginSuccess.value = store.getters.getLogin ? true : false;
            if (!loginSuccess.value) alert("아이디 또는 비밀번호가 틀렸습니다.");
        }

        // 회원가입 페이지 이동
        function moveLink() {
            router.push('/join')
        }

        return { loginSuccess, id, pwd, login, moveLink };
    },
};
</script>

<style>

</style>
