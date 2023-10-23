<template>
    <div class="wrap">
        <NavBar v-if="isLogined" />
        <SideBar v-if="isLogined"/>
        <div class="content">
            <router-view v-slot="routerView">
                <transition name="route" mode="out-in">
                    <component :is="routerView.Component"></component>
                </transition>
            </router-view>
        </div>
        <Footer v-if="isLogined"></Footer>
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
