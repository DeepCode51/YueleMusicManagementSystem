<!-- 【后台登录】 页面-->
<template>
  <div class="home">
    <title>登录页面</title>
    <register ref="registerDialog"></register>
      <div class="content">
        <div class="login-form" :style="'height:'+formHeight+'px'">
          <div class="project-name" style="text-shadow: 1px 1px 3px rgba(43,74,120,0.52);">{{projectName}}</div>
          <el-form :model="sysUser" status-icon ref="sysUserForm" label-width="100px">
            <div class="login-input">
              <el-input v-model="sysUser.loginName" placeholder="请输入登录名"></el-input>
            </div>
            <div class="login-input">
              <el-input type="password" v-model="sysUser.password" placeholder="请输入密码" autocomplete="off"></el-input>
            </div>
            <div v-if="showRoles" class="login-input"  style="text-align: center">
              <el-radio-group v-model="sysUser.roleCode">
                <el-radio v-for="role in allRole" :key="role.roleCode" :label="role.roleCode">{{role.roleName}}</el-radio>
              </el-radio-group>
            </div>
            <validCode :show="isValid" @success="validSuccess"/>
            <div class="login-input">
                <span v-if="showRegister" class="register-span">
                    <small>没有账号?&nbsp;&nbsp;</small><a class="register-a" @click="register()">立即注册</a>
                </span>
              <el-button style="width: 100%;" :class="[{'btn-top': !(showRegister||showRoles)}]" type="primary" @click="login">登录</el-button>
            </div>
          </el-form>
        </div>
    </div>
  </div>
</template>
<script>
  import {mapActions} from "vuex";
  import validCode from "vue-puzzle-vcode";
  import register from './register'
  export default {
    components:{register,validCode},
    data() {
      return {
        isValid: false, // 用来控制显示是否显示图片滑动验证框
        sysUser: {
          roleCode:'',
          loginName: '',
          password: ''
        }
      };
    },
    computed:{
      projectName(){
        return this.$store.getters.config.projectName
      },
      allRole(){
        return this.$store.getters.config.allRole;
      },
      showRoles(){
        return this.$store.getters.config.showRoles;
      },
      showRegister(){
        return this.$store.getters.config.showRegister
      },
      showValidCode(){
        return this.$store.getters.config.showValidCode
      },
      formHeight(){
        return this.showRoles?350:320
      }
    },
    mounted() {
      this.initData();
    },
    methods: {
      ...mapActions(['commitConfig']),
      //打开弹窗
      register(){
        this.$refs.registerDialog.open({title:'用户注册'});
      },
      validSuccess(){
        this.isValid = false; // 通过验证后，进行登录
        this.subLogin();
      },
      initData() {
        //加载配置数据
        this.commitConfig();
      },

      login() {
        this.showValidCode ? this.isValid = true : this.subLogin()
      },
      subLogin() {
        const that = this;
        const loading = that.$loading({
          lock: true,
          text: '请稍后,登录中...',
          spinner: 'el-icon-loading',
          background: 'rgba(0,0,0,0.25)'
        });
        if (this.showRoles && that.sysUser.roleCode == '') {
          that.showWarn("请选择角色！");
          loading.close();
          return
        }
        that.$request.doPost("/admin/login/userLogin", that.sysUser).then(res => {
          if (res.isOk) {
            loading.close();
            that.$router.push('/main/home');
            that.showSuccess("登录成功！");
          } else {
            loading.close();
          }
        });
      }
    }
  }
</script>
<style scoped>
  .btn-top{
    margin-top: 20px
  }
  .home{
    width: 100%;
    height: 100vh;
    background: url("/static/login/images/login.jpg") center center no-repeat;
    background-size: 100% 100%;
  }
  .content {
    width: 350px;
    margin: 0 auto; /*水平居中*/
    position: relative;
    top: 50%; /*偏移*/
    transform: translateY(-50%);
  }
  .title{
    padding: 20px;
    text-align: center;
    color: #6a6c6f;
  }
  .login-form{
    border-radius: 10px;
    width: 350px;
    background: rgba(255, 255, 255, 0.85);
  }
  .login-input{
    margin: 25px;
  }
  .register-span{
    display: block;
    padding-right: 3px;
    padding-bottom: 5px;
    float: right
  }
  .register-a{
    cursor: pointer;
  }

  .project-name{
    padding:10px;
    font-size: 30px;
    color: #324262;
    font-weight: bold;
    text-align: center;
    margin-bottom: 10px;
    /*margin: 0 auto; !*水平居中*!*/
  }
</style>
