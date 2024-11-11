<template>
  <div class="body">
    <h1 style="text-align: center;font-size: 30px;margin-top: 0px;font-family: cursive" class="font-shadow">{{projectName}}</h1>
    <div class="home" style="background-image: url('/static/login/images/login.jpg');"></div>
  </div>
</template>
<script>
  export default {
    name: "adminIndex",
    computed: {
      projectName() {
        return this.$store.getters.config.projectName;
      }
    }
  }
</script>

<style scoped>
  .home {
    margin: 0 auto;
    border-radius: 100px;
    border: saddlebrown;
    box-shadow: 0px 0px 50px 0px rgba(121, 179, 185, 0.34);
    width: 85%;
    height: 680px;
    background-size: 100% 100%
  }
</style>
