<!--【首页底部区域】：包含底部说明-->
<template>
  <div class="footer">
    <div class="footer-content">
      <div class="tab-link">
        <div class="tab pointer" @click="toPage({name:'frontHome'})">网站首页 <span class="sept">|</span></div>
        <div class="tab pointer" :to="{ path: '/front/home' }">隐私策略<span class="sept">|</span></div>
        <div class="tab pointer" :to="{ path: '/front/home' }">安全中心<span class="sept">|</span></div>
        <div class="tab pointer" :to="{ path: '/front/home' }">关于我们<span class="sept">|</span></div>
        <div class="tab pointer" :to="{ path: '/front/home' }">联系我们<span class="sept"></span></div>
      </div>
    </div>
    <div class="copyright pointer">Copyright © {{new Date().getFullYear()}} by {{projectName}}</div>
  </div>
</template>

<script>
  export default {
    name: "home-footer",
    computed: {
      projectName() {
        return this.$store.getters.config.projectName
      },
    },
  }
</script>
<style scoped lang="scss">
  @import '../../../assets/styles/theme';
  .footer {
    color:$theme-front-font-color;
    background: $theme-front-color;
  }

  .footer-content {
    margin: 0 auto;
    width: 1200px;
  }

  .contact {
    float: right;
    margin-right: 100px;
  }
  .sept {
    padding-left: 15px
  }

  .copyright {
    clear: both;
    padding-bottom: 10px;
    text-align: center;
    font-size: smaller;
  }

  .tab {
    padding: 10px;
    font-size: 15px;
    float: left;
    margin-left: 30px;
  }

  .tab:hover {
    color:whitesmoke;
  }

  .tab-link {
    width: 800px;
    margin: 0 auto;
    padding: 10px
  }

  .phone {
    margin-right: 50px;
    color: #FF6700;
    font-size: 20px;
  }

  .official {
    vertical-align: middle;
    width: 60px;
    height: 60px
  }

</style>
