<template>
  <nav class="navbar bg-body-tertiary fixed-top" style="z-index: 2">
    <div class="container-fluid">
      <div class="toggleButton">
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="offcanvas"
          data-bs-target="#offcanvasNavbar"
          aria-controls="offcanvasNavbar"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <b class="toggleName"> {{ watchRoute }} </b>
        <b class="toggleName"> {{ watchRoute2 }} </b>
      </div>
      <div
        class="offcanvas offcanvas-start"
        tabindex="-1"
        id="offcanvasNavbar"
        aria-labelledby="offcanvasNavbarLabel"
        style="width: 350px"
      >
        <div class="offcanvas-header">
          <h5 class="offcanvas-title" id="offcanvasNavbarLabel">
            Kubernetes Dashboard
          </h5>
          <button
            type="button"
            class="btn-close"
            data-bs-dismiss="offcanvas"
            aria-label="Close"
          ></button>
        </div>
        <div class="offcanvas-body">
          <ul class="navbar-nav justify-content-end flex-grow-1 pe-3">
            <li v-for="a in list" :key="a" class="nav-item" data-bs-dismiss="offcanvas">
              <router-link
                @click="select(a.title)"
                class="nav-link"
                :to="{ path: `${a.address}` }" >
                {{ a.title }}
              </router-link>
            </li>
            <hr />
            <li class="nav-item" data-bs-dismiss="offcanvas">
            <router-link @click="select(title='소개')" class="nav-link" to="/introduction">
            소개
            </router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  </nav>
</template>

<script>
import sideBarList from '../assets/sideBarList';

export default {
  name: 'SideBar',
  isblue: false,

  data() {
    return {
      list: sideBarList,
      selected: '메인페이지',
    };
  },
  methods: {
    select: function (title) {
      this.selected = title;
    },

    setName() {
      if (this.$route.params != undefined && this.$route.params != null) {
        this.pageDetail = this.$route.params.name;
        console.log('pageDetail=' + this.pageDetail);
      }
      this.pageName = this.$route.name;
      console.log('pagename=' + this.pageName);
    },
  },

  computed: {
    watchRoute() {
      const path = this.$route.path;
      if (path.includes('/pod')) {
        return '파드';
      } else if (path.includes('/service')) {
        return '서비스';
      } else if (path.includes('/deployment')) {
        return '디플로이먼트';
      } else if (path.includes('/introduction')) {
        return '소개';
      } else if (path.includes('/search')){
        return '검색 결과'
      } else {
        return '메인페이지';
      }
    },
    watchRoute2() {
      return this.$route.params.name ? this.$route.params.name : ' ';
    },
  },
};
</script>

<style scoped>
.navbar {
  background: linear-gradient(to right, #90278e, #1b75bb, #4e5158);
  margin-top: 60px;
}
.toggleButton {
  color: white;
  margin-left: 20px;
}
.navbar-toggler {
  margin-top: 5px;
  background-color: white;
}
.navbar-toggler-icon {
  border-color: white;
}
.toggleName {
  margin-left: 30px;
  font-size: 120%;
}
.toggleName2 {
  margin-left: 30px;
  font-size: 100%;
  color: white;
}
.offcanvas {
  margin-top: 121px;
}
.offcanvas-body {
  text-align: left;
  margin-left: 20px;
}
.nav-item {
  font-size: 110%;
  margin: 15px;
  height: 35px;
}
.nav-link:hover {
  background-color: #e7e4e8;
  color: rgb(83, 105, 232);
}
.router-link-active {
  font-size: 115%;
  color: rgb(83, 105, 232) !important;
}

hr {
  /* position: absolute;
  bottom: 20px; */
  height: 3px !important;
  background-color: #8fa1af !important;
  margin-top: 300px;
}

</style>
