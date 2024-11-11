<!--【首页头部区域】：包含登录|注册-->
<template>
  <div class="main-header">
    <user-login ref="userLoginDialog"></user-login>
    <user-register ref="userRegisterDialog"></user-register>
    <user-info ref="userInfoDialog"/>
    <user-password ref="passwordDialog"/>
    <div class="header">
      <div class="header-content">
        <div class="header-banner">
          <el-menu router :default-active="$route.path" style="width: 100%;height: 61px" class="el-menu-demo" mode="horizontal" background-color="#4288B0" text-color="#fff" active-text-color="#ffd04b">
            <router-link :to="{ path: '/front/home' }" class="float-l pointer">
              <el-avatar class="header-logo" src="/static/images/front/logo/logo.jpg"></el-avatar>
              <span class="projectName">{{projectName}}</span>
            </router-link>
            <el-menu-item index="/front/home">发现音乐</el-menu-item>
            <el-menu-item index="/front/singerList">热门歌手</el-menu-item>
            <el-menu-item index="/front/recommendList">推荐歌曲</el-menu-item>
            <el-menu-item v-if="hasLogin" index="/front/userSongList">我的音乐</el-menu-item>
            <div v-if="hasLogin">
              <el-dropdown class="pointer float-r">
             <span class="el-dropdown-link">
               <el-avatar size="small" class="middle" :src="formatUrl(userInfo.photo)"></el-avatar>
               <span class="loginName">{{userInfo.loginName}}</span>
               <i class="el-icon-arrow-down el-icon--right"></i>
             </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item icon="el-icon-user" @click.native="updateUserInfo">用户信息</el-dropdown-item>
                  <el-dropdown-item icon="el-icon-key" @click.native="updatePassword">修改密码</el-dropdown-item>
                  <el-dropdown-item divided icon="el-icon-switch-button" @click.native="loginOut">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div v-else class="float-r">
              <span class="pointer login" @click="userLogin">登 录</span>
              <span class="pointer register" @click="userRegister">注 册</span>
            </div>
          </el-menu>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  import {mapActions} from "vuex";
  export default {
    name: "home-header",
    data(){
      return {
        //路由的key
        rootKey: new Date().getTime(),
      }
    },
    computed:{
      userInfo(){
        return this.$store.getters.userInfo;
      },
      hasLogin(){
        return this.$store.getters.hasLogin;
      },
      projectName(){
        return this.$store.getters.config.projectName;
      }
    },
    mounted() {
    },
    methods: {
      ...mapActions(['commitFrontLoginOut']),
      loginOut(){
        const that = this;
        that.showConfirm('确认退出吗?',function (){
          that.commitFrontLoginOut().then(res=>{
            if(res.isOk){
              that.$router.push("/front/home");
              that.showToast("退出成功");
            }
          })
        })
      },
      userLogin() {
        this.$refs.userLoginDialog.open({title:'账号登录'});
      },
      userRegister() {
        this.$refs.userRegisterDialog.open({title:'用户注册'});
      },
      updateUserInfo() {
        this.$refs.userInfoDialog.open({title:'用户信息'});
      },
      updatePassword() {
        this.$refs.passwordDialog.open({title:'修改密码'});
      },
    }
  }
</script>
<style scoped lang="scss" scoped>
  @import '../../../assets/styles/theme';
  .header {
    height: 60px;
    z-index: 1000;
    color:$theme-front-font-color;
    background: $theme-front-color;
    position: fixed;
    width: 100%
  }
  .el-menu-item {
    background: $theme-front-color !important;
  }
  .el-menu-demo{
    background-color:$theme-front-color !important;;
  }
  .projectName {
    width: 200px;
    vertical-align: middle;
    font-size: 20px;
    font-weight: bold;
    font-family: "Helvetica Neue"
  }

  .el-dropdown-link {
    color: whitesmoke;
    font-size: 16px;
  }
  .el-icon-arrow-down {
    font-size: 16px;
  }

  .tab {
    line-height: 65px;
    font-size: 15px;
    float: left;
    margin-left: 30px;
  }

  .tab:hover {
    color:whitesmoke;
  }

  .loginName {
    font-weight: bold;
    font-size: medium
  }

  .middle {
    vertical-align: middle
  }

  .header-content {
    line-height: 60px;
    width: 1200px;
    margin: 0 auto;
  }

  .header-logo {
    vertical-align: middle;
    width: 45px;
    height: 45px;
  }

  .header-banner {
    vertical-align: middle;
  }

  .register:hover, .login:hover{
    color:whitesmoke;
  }
  .login,.register{
    margin-left: 10px;
    font-size: 12px;
  }

</style>

