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
        <!-- <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            default
          </a>
          <ul class="dropdown-menu" style="z-index: 1">
            <li><a class="dropdown-item" href="#">모든 네임스페이스</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item disabled" >네임스페이스</a></li>
            <li><a class="dropdown-item" href="#">kube-node-lease</a></li>
            <li><a class="dropdown-item" href="#">kube-public</a></li>
            <li><a class="dropdown-item" href="#">kube-system</a></li>
            <li><a class="dropdown-item" href="#">kubenetes-dashboard</a></li>
          </ul>
        </li> -->
        <form class="mainSearch" v-on:submit="search">
    <div class="input-group">
      <span class="input-group-text" id="basic-addon1"><button><img :src="require('@/assets/images/search.png')" style="height:20px"/></button></span>
      <input type="text" class="form-control" placeholder="검색어를 입력하세요." aria-label="Username" aria-describedby="basic-addon1" v-model="searchInput">
    </div>
      </form>
    </ul>
    </div>

    <div >
    <li class="nav-item dropdown" style="list-style: none; margin-right: 20px;">
          <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <img src="@/assets/images/person-square.svg" style="height:20px"/>
            {{ idCheck }}
          </a>
          <div>
          <ul class="dropdown-menu" style="z-index: 1;">
            <li><a class="dropdown-item" style="cursor:pointer" @click="logout">로그아웃</a></li>
          </ul>
        </div>
        </li>
      </div>
  </div>



</nav>
</template>

<script>
import store from "@/store/store";

export default {
  name :'NavBar',

    data(){
      return {
        searchInput:''
      }
    },
    methods: {
      search(){
        const vm = this;
        if(vm.searchInput != ''){
        this.$router.push({
        path: "/search/" + vm.searchInput,
                })
          console.log(vm.searchInput);
      }else{
        alert('검색어를 입력해주세요.')
      }},
      logout() {
      this.$store.dispatch('logout');
      location.href = '/login';
    },

  },

  computed: {
    idCheck() {
      return store.getters.getId;
    }
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