<template>
  <login-form>
    <form @submit.prevent="join">
      <img class="mb-4" :src="require('@/assets/images/kubeLogo.png')" width="200" height="170">
      <h1 class="h3 mb-3 mt-2 fw-normal">Create account</h1>

      <div class="col-md-12 mt-1 text-start">
        <label for="userId" class="form-label">아이디</label>
        <input type="text" class="form-control" name="userId" :value="userId" @keyup="idCheck" @keydown.space.prevent required>
        <div v-if="userId !== '' && !isFinish"><font id="feedback" size="2">{{ validCheck }}</font></div>
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
import { ref, watch,computed } from "vue";
import LoginForm from "@/components/user/LoginForm.vue";
import axios from "axios";
import store from "@/store/store";
import router from "@/router";

export default {
    name: "Join.vue",
    components: { LoginForm },

    setup() {
        const userId = ref("");
        const pwd = ref("");
        const isValid = ref(false);
        const isFinish = ref(false);
        const isBlank = ref(true);
        const isBlank2 = ref(true);

        const idCheck = (e) => {
            userId.value = e.target.value.trim();
            e.target.value = userId.value;

            const inputId = userId.value;

            // 아이디 중복 확인
            axios.post(store.getters.uri + 'user-service/idCheck', {
                "userId": inputId,
            }).then((response) => {
                isValid.value = (response.data !== 1)
            });
        };

        const join = () => {
            if (isBlank.value) {
                alert("아이디를 입력해주세요.");
                return;
            }
            if (isBlank2.value) {
                alert("비밀번호를 입력해주세요.");
                return;
            }
            if (!isValid.value) {
                alert("이미 사용중인 아이디입니다.");
                return;
            }

            axios.post(store.getters.uri + 'user-service/join', {
                "userId": userId.value,
                "pwd": pwd.value
            }).then(() => {
                isFinish.value = true;
                alert("회원가입이 완료되었습니다.");
                router.replace('/login');
            });
        };

        const moveLink = () => {
            router.push("/login")
        };

        // 아이디 사용 가능 여부 안내
        const validCheck = computed(() => {
            return isValid.value ? '사용 가능한 아이디입니다.' : '이미 사용중인 아이디입니다.';
        });

        // 입력값이 빈 문자열인지 여부 확인
        watch(userId.value, (newVal) => {
            isBlank.value = newVal === '';
        });

        watch(pwd.value, (newVal) => {
            isBlank2.value = newVal === '';
        });

        return { userId, pwd, isValid, isFinish, isBlank, isBlank2, idCheck, join, moveLink, validCheck };
    },
};
</script>

<style>

</style>
