<!--【首页头部区域】：包含登录|注册-->
<template>
  <div class="front-page">
    <index-header></index-header>
    <router-view :key="rootKey"></router-view>
    <sui-music></sui-music>
    <index-footer></index-footer>
  </div>
</template>
<script>
  export default {
    name: "frontIndex",
    data() {
      return {
        //路由的key
        rootKey: new Date().getTime(),
      }
    },
    mounted() {
      if (this.$route.path == '/front') {//进入首页
        this.$router.push("/front/home")
      }
    }
  }
</script>

