<template>
    <div class="wrap">
        <NavBar />
        <SideBar/>
        <div class="content">
            <router-view v-slot="routerView">
                <transition name="route" mode="out-in">
                    <component :is="routerView.Component"></component>
                </transition>
            </router-view>
        </div>
        <Footer></Footer>
    </div>
</template>


<script>
import Footer from './layout/Footer.vue';
import NavBar from './layout/NavBar.vue';
import SideBar from './layout/SideBar.vue';

export default {
    name: 'App',
    components: {
        Footer,
        NavBar,
        SideBar
    },

    //로그인 토큰 여부 확인
    //네브바 출력 여부 결정 시 사용
    computed: {
        isLogined() {
            return this.$store.getters.isLogin;
        }
    },
}
</script>

<style>
.content {
    min-height: 80vh;
}

Footer {
    position: relative;
    bottom: 0;
}

.route-enter-from {
    opacity: 0;
    transform: translateY(-30px);
}

.route.leave-to {
    opacity: 0;
    transform: translateY(30px);
}

.route-enter-active {
    transition: all 0.3s ease-out
}

.route-leave-active {
    transition: all 0.3s ease-in
}

.route-enter-to,
.route-leave-from {
    opacity: 1;
    transform: translateY(0);
}
</style>
