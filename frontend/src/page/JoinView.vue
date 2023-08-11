<template>
  <login-form>
    <form @submit.prevent="join">
      <img class="mb-4" :src="require('@/assets/images/strato2.png')" width="200" height="50">
      <h1 class="h3 mb-3 mt-2 fw-normal">Create account</h1>

      <div class="col-md-12 mt-1 text-start">
        <label for="id" class="form-label">아이디</label>
        <input type="text" class="form-control" name="id" :value="id" @keyup="idCheck" @keydown.space.prevent required>
        <div v-if="id !== '' && !isFinish"><font id="feedback" size="2">{{ validCheck }}</font></div>
      </div>

      <div class="col-md-12 mt-2 text-start">
        <label for="pwd" class="form-label">비밀번호</label>
        <input type="password" class="form-control" name="pwd" v-model="pwd" @keydown.space.prevent required>
      </div>

      <div class="mb-2 mt-2">
        <div type="button" class="w-100 btn btn-lg btn-primary" @click="join">가입하기</div>
      </div>

      <p class="mb-3 text-muted" style="cursor:pointer" @click="moveLink">뒤로가기</p>

      <p class="mt-5 mb-3 text-muted">&copy; Copyrightⓒ STRATO. ALL Right Reserved.</p>
    </form>
  </login-form>
</template>

<script>
import LoginForm from "@/layout/LoginForm.vue";
import axios from "axios";

export default {
  name: "Join.vue",
  components: { LoginForm },

  //객체 혹은 함수의 형태로 vue 인스턴스가 사용할 정보를 가지고 있는 속성
  data() {
    return {
      id: "",
      pwd: "",
      isValid: false,
      isFinish: false,
      isBlank: true,
      isBlank2: true
    }
  },

  //vue 인스턴스가 사용할 함수를 저장하고 있는 속성
  methods: {
    idCheck(e) {
      this.id = e.target.value.trim();
      e.target.value = this.id;

      const inputId = this.id;

      //아이디 중복 확인
      axios.post(this.$store.getters.uri + 'idCheck.do', {
        "id": inputId,
      }).then((response) => {
        this.isValid = (response.data === 1) ? false : true
      })
    },

    //회원가입
    join() {
      if (this.isBlank) {
        alert("아이디를 입력해주세요.");
        return
      }
      if (this.isBlank2) {
        alert("비밀번호를 입력해주세요.");
        return
      }
      if (!this.isValid) {
        alert("이미 사용중인 아이디입니다.");
        return;
      }

      axios.post(this.$store.getters.uri + 'join', {
        "id": this.id,
        "pwd": this.pwd
      }).then(() => {
        this.isFinish = true
        alert("회원가입이 완료되었습니다.");
        this.$router.replace('./login');
        // this.result = response;
      })
    },

    //뒤로가기 이동
    moveLink() {
      location.href = '/login';
    },
  },

  //아이디 사용 가능 여부 안내
  computed: {
    validCheck() {
      return this.isValid ? '사용 가능한 아이디입니다.' : '이미 사용중인 아이디입니다.';
    },
  },

  watch: {
    id() {
      this.isBlank = (this.id === '') ? true : false;
    },

    pwd() {
      this.isBlank2 = (this.pwd === '') ? true : false;
    }
  }
}
</script>

<style>

</style>
