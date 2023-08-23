<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary fixed-top" >
  <div class="container-fluid">
    <a class="navbar-brand" href="/">
      <img :src="require('@/assets/images/STRATO.png')" alt="Bootstrap"  height="40">
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <form class="mainSearch" v-on:submit="search">
    <div class="input-group">
      <span class="input-group-text" id="basic-addon1"><button><img :src="require('@/assets/images/search.png')" style="height:20px"/></button></span>
      <input type="text" class="form-control" placeholder="검색어를 입력하세요." aria-label="Username" aria-describedby="basic-addon1" v-model="searchInput">
    </div>
      </form>
    </ul>
    </div>

    <div >
<!--    <li class="nav-item dropdown" style="list-style: none; margin-right: 20px;">-->
<!--          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">-->
<!--            <img src="@/assets/images/person-square.svg" style="height:20px"/>-->
<!--          </a>-->
<!--          <div>-->
<!--          <ul class="dropdown-menu" style="z-index: 1;">-->
<!--            <li><a class="dropdown-item" style="cursor:pointer" @click="logout">로그아웃</a></li>-->
<!--          </ul>-->
<!--        </div>-->
<!--        </li>-->
      </div>
  </div>



</nav>
</template>

<script>
import {onMounted, ref} from 'vue';
import {useRoute, useRouter} from "vue-router";

export default {
    name: 'NavBar',

    setup() {
        const searchInput = ref('');
        const route = useRoute();
        const router = useRouter();
        const search = () => {
            if(searchInput.value !== '') {
                router.push({
                    path: "/search/"+searchInput.value,
                });
                console.log(searchInput.value);
            } else {
                alert('검색어를 입력해주세요.');
            }
        };
        onMounted(()=>{
            if (route.query.q) {
                searchInput.value = route.query.q;
                console.log(searchInput.value);
            }
        })
        return {
            searchInput,
            search,
        };
    }
}
</script>


<style scoped>
.navbar{
  background: white;
}
.navbar-brand{
  margin-left: 25px;
  text-align: left;
}
.navbar-nav{
  margin-left: 100px;
  text-align: left;
}
.mainSearch{
  width: 500px;
  margin-left: 40px;
}

</style>