import { createWebHistory, createRouter } from "vue-router";
import ServiceList from "./components/ServiceList.vue";
import ServiceView from "./components/ServiceView.vue";
import Introduction from "./components/Introduction.vue";
import Deployment from "./components/Deployment.vue";
import DeploymentDetail from "./components/DeploymentDetail.vue";
import Pod from "./components/Pod.vue";
import PodDetail from "./components/PodDetail.vue";
import MainPage from "./components/MainPage.vue";
import LoginView from "@/components/LoginView.vue";
import JoinView from "@/components/JoinView.vue";
//import store from "@/store/store";
import NotFound from "@/components/NotFound.vue";
import SearchPage from "./components/SearchPage.vue";

const routes = [
    { path: "/", name: 'mainPage', component: MainPage, meta: {auth: true} },
    { path: '/service', name: 'serviceList', component: ServiceList, meta: {auth: true} },
    { path: '/service/:name', name: 'serviceView', component: ServiceView, meta: {auth: true} },
    { path: '/introduction', name: 'introduction', component: Introduction, meta: {auth: true} },
    { path: '/deployment', name: 'Deployment', component: Deployment, meta: {auth: true} },
    { path: '/deployment/:name', name: 'deploymentDetail', component: DeploymentDetail, props: true, meta: {auth: true} },
    { path: '/pod', name: 'Pod', props: true, component: Pod, meta: {auth: true} },
    { path: '/pod/:name', name: 'PodDetail', props: true, component: PodDetail, meta: {auth: true} },
    { path: '/login', name: 'Login', component: LoginView, meta: {unauth: true} },
    { path: '/join', name: 'Join', component: JoinView, meta: {unauth: true} },
    { path: '/logout', name: 'Logout', component: LoginView, meta: {unauth: true} },
    { path: '/search/:searchInput', name: 'SearchPage', component: SearchPage, meta: {auth: true}},
    { path: '/:anything(.*)*', component: NotFound },
];

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes,
    linkActiveClass: 'active',
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition;
        }
        return {left: 0, top: 0};
    },
});

//네비게이션 가드
/*
to: 이동할 url 정보가 담긴 라우터 객체
from: 현재 url 정보가 담긴 라우터 객체
next: to에서 지정한 url로 이동하기 위해 호출하는 함수
beforeEach() 호출하면 모든 라이팅이 대기 상태가 됨
해당 url로 라우팅하기 위해서는 next()를 호출해야함
*/
// router.beforeEach(function (to, from, next) {
//     //access 토큰이 없는 경우, 로그인 화면 이동
//     if (to.meta.auth && !store.getters.getLogin) {
//         next('/login');
//     }
//     //이미 로그인 된 상태이면 로그인, 회원가입 페이지 이동 불가
//     else if (to.meta.unauth && store.getters.getLogin) {
//         next('/');
//     }
//     //그 외 access 토큰이 있는 경우, to에서 지정한 url 이동
//     else {
//         next();
//     }
// });

export default router;
