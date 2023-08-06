<template>
    <!--페이지네이션-->
    <nav aria-label="Page navigaion example">
        <ul class="pagination justify-content-center">
            <li v-if="currentPage !== 1" class="page-item">
                <a class="page-link" @click="getList(currentPage -1)" style="cursor:pointer">&lt;</a></li>
            <li v-for="page in numberOfPages" :key="page" class="page-item"
                :class="currentPage === page ? 'active' : ''">
                <a class="page-link" @click="getList(page)" style="cursor:pointer">{{ page }}</a></li>
            <li class="page-item" v-if="numberOfPages !== currentPage">
                <a class="page-link" @click="getList(currentPage +1)" style="cursor:pointer">&gt;</a></li>
        </ul>
    </nav>
</template>
<script>
    import { inject } from "vue";

    export default {
        props:['numberOfPages','currentPage'],
        emits:['getList'],
        setup(props,{emit}){
            const getList = (page)=>{
                setLoading()
                emit('getList',page);
            };
            const setLoading = inject('setLoading');
            return{
                getList, setLoading
            };
        }
    }
</script>

<style scoped>
    .active>.page-link {
        background-color: #BDBDBD;
        border-color: #E6E6E6;
        z-index:0;
    }
    .pagination a {
        color: #6E6E6E;
    }
    .pagination {
        margin-top: 30px;
    }
</style>