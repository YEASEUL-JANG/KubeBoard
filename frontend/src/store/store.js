import {createStore} from 'vuex'
import axios from "axios";

const store = createStore({
        //데이터 상태 관리
        state: {
            userId: localStorage.getItem('userId'),
            token: localStorage.getItem('token'),
            uri: process.env.VUE_APP_URI
        },

        //
        getters: {
            //아이디, 토큰 받아오는 용도
            getLogin(state) {
                return (state.userId, state.token);
            },

            //유효성 검사 용도
            isLogin(state) {
                console.log(state.userId, state.token)
                return state.userId !== null && state.token !== null
            },

            //아이디만 받아오는 용도
            getId(state) {
                return state.userId;
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
                state.userId = fetchedData.userId;
                state.token = fetchedData.token;
            },
        },

        //mutations를 실행시키는 역할
        actions: {
            //로그인 시 아이디, 토큰을 local storage 및 store 저장
            async auth(context, payload) {
                return axios.post('/user-service/login', {
                    userId: payload.userId,
                    pwd: payload.pwd
                })
                    .then(response => {
                        if (response.status < 400) {
                            console.log(response)
                            const token = response.headers['token'];

                            if (token) {
                                localStorage.setItem('userId', payload.userId);
                                localStorage.setItem('token', token);
                                context.commit('setData', {
                                    userId: payload.userId,
                                    token: token
                                });
                                return response.data;
                            } else {
                                throw new Error("Token not found in response headers.");
                            }
                        } else {
                            throw new Error("Invalid response status.");
                        }
                    })
                    .catch((error) => {
                        console.error(error);
                        return error.message;
                    });
            },

//로그아웃
            logout(context) {
                localStorage.removeItem('userId');
                localStorage.removeItem('token');

                context.commit('setData', {
                    userId: null,
                    token: null
                });

                return axios.post('/user-service/logout', {}
                )
            }
            ,

        }
    })
;

export default store;
