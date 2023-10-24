import {createStore} from 'vuex'
import axios from "axios";

const store = createStore({
        //데이터 상태 관리
        state: {
            id: localStorage.getItem('userId'),
            token: localStorage.getItem('token'),
            uri: process.env.VUE_APP_URI
        },

        //
        getters: {
            //아이디, 토큰 받아오는 용도
            getLogin(state) {
                return (state.id, state.token);
            },

            //유효성 검사 용도
            isLogin(state) {
                console.log(state.id, state.token)
                return state.id !== null && state.token !== null
            },

            //아이디만 받아오는 용도
            getId(state) {
                return state.id;
            },

            //토큰만 받아오는 용도
            getToken(state) {
                return state.token;
            },

            uri(state) {
                return state.uri;
            }
        },

        //state를 변경시키는 역할
        mutations: {
            setData(state, fetchedData) {
                state.id = fetchedData.id;
                state.token = fetchedData.token;
            },
        },

        //mutations를 실행시키는 역할
        actions: {
            //로그인 시 아이디, 토큰을 local storage 및 store 저장
            async auth(context, payload) {
                return axios.post(store.getters.uri + 'user-service/login', {
                    id: payload.id,
                    pwd: payload.pwd
                })
                    .then(response => {
                        if (response.status < 400) {
                            return response.data;
                        } else {
                            throw new Error();
                        }
                    })
                    .then((data) => {
                        localStorage.setItem('id', payload.id);
                        localStorage.setItem('token', data);
                        context.commit('setData', {
                            id: payload.id,
                            token: data
                        })
                    })
                    .catch((error) => {
                        return error.message;
                    });
            },

//로그아웃
            logout(context) {
                localStorage.removeItem('id');
                localStorage.removeItem('token');

                context.commit('setData', {
                    id: null,
                    token: null
                });

                return axios.post(store.getters.uri + 'user-service/logout', {}
                )
            }
            ,

        }
    })
;

export default store;
