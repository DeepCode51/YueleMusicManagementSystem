<!-- 【用户注册】弹窗页面 -->
<template>
  <el-dialog width="420px" class="front-dialog" :title="dialog.title" :close-on-click-modal="false" center :append-to-body="true" v-dialog-drag :visible.sync="dialog.open">
    <el-form :model="sysUser" ref="sysUserForm" :rules="rules">
      <div class="front-input">
        <span class="fa fa-user-o" aria-hidden="true"></span>
        <el-input v-model="sysUser.loginName" placeholder="请输入登录名"></el-input>
      </div>
      <div class="front-input">
        <span class="fa fa-key" aria-hidden="true"></span>
        <el-input v-model="sysUser.password" type="password" placeholder="请输入密码"></el-input>
      </div>
      <div class="front-input">
        <span class="fa fa-key" aria-hidden="true"></span>
        <el-input v-model="sysUser.prePassword" type="password" placeholder="请输入确认密码"></el-input>
      </div>
    </el-form>
    <div class="dialog-footer">
      <el-button class="sub-btn" @click="register">注 册</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {mapActions} from "vuex";
  import axios from "axios";
  import md5 from 'js-md5';
  export default {
    name: "userRegister",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysUser: {
          photo: "",
          loginName: "",
          prePassword:"",
          password: ""
        },
        //表单校验
        rules: {
          photo: [{required: true, message: '请输入用户头像', trigger: 'blur'}],
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}],
          prePassword: [{required: true, message: '请输入确认密码', trigger: 'blur'}],
        }
      }
    },
    mounted(){
    },
    methods: {
      //打开弹窗
      open(option) {
        this.showDialog(option.title, false);
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //提交表单
      register: function () {
        const that = this;
        if (that.sysUser.password == "") {
          this.showWarn("密码不能为空!");
          return false
        }
        if (that.sysUser.prePassword == "") {
          this.showWarn("确认密码不能为空!");
          return false
        }
        if (that.sysUser.password != that.sysUser.prePassword) {
          this.showWarn("两次密码不一致!");
          return false
        }
        that.$request.doPost('/api/login/register', that.sysUser).then(res => {
          if (res.isOk) {
            that.dialog.open = false;
            that.showSuccess("注册成功");
            that.$emit('reloadList');
          }
        });
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.sysUserForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
    }
  }
</script>
<style scoped lang="scss">
  @import '../../../../assets/styles/theme';
  .front-dialog /deep/ .el-dialog__headerbtn .el-dialog__close{
    color: white!important;
  }
  .front-dialog /deep/ .el-dialog__header  {
    background: $theme-front-color;
  }
  .front-dialog /deep/ .el-dialog__title  {
    font-weight: bold;
    color: white!important;
  }

  .sub-btn  {
    margin: 20px 0 10px;
    width: 100%;
    background: $theme-front-color;
    color: white;
    height: 45px
  }
  .front-input {
    margin: 20px 0;
    padding: 3px 15px;
    border: 1px solid #eee;
    background:white;
    border-radius: 5px;
    display: -webkit-box;
    display: -ms-flexbox;
    display: flex;
    -webkit-box-align: center;
    -ms-flex-align: center;
    align-items: center;
  }
  .front-input span   {
    font-size: 15px;
    color: #464646;
    margin-right: 10px;
  }
</style>
