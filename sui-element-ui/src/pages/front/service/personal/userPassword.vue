<!-- 【修改密码】弹窗页面 -->
<template>
  <el-dialog width="420px" class="front-dialog" center :title="dialog.title" :close-on-click-modal="false" :append-to-body="true" v-dialog-drag :visible.sync="dialog.open">
    <el-form :model="user" ref="passwordForm" :rules="rules" label-width="100px">
      <el-form-item label="登录名" prop="loginName">
        <el-input v-model="user.loginName" disabled placeholder="请输入登录名" style="width: 217px"></el-input>
      </el-form-item>
      <el-form-item label="原始密码" prop="oldPassword">
        <el-input v-model="user.oldPassword" type="password" placeholder="请输入原始密码" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="user.newPassword" type="password" placeholder="请输入新密码" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="prePassword">
        <el-input v-model="user.prePassword" type="password" placeholder="请输入确认密码" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialog-footer">
      <el-button class="sub-btn" @click="submitForm('passwordForm')">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "userPassword",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        user: {
          loginName:"",
          oldPassword: "",
          newPassword: "",
          prePassword: ""
        },
        //表单校验
        rules: {
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'}],
          oldPassword: [{required: true, message: '请输入旧密码', trigger: 'blur'}],
          newPassword: [{required: true, message: '请输入新密码', trigger: 'blur'}],
          prePassword: [{required: true, message: '请输入确认密码', trigger: 'blur'}]
        }
      }
    },
    methods: {
     //打开弹窗
     open(option) {
       const pageFrom = option.pageFrom;
       const disabled = pageFrom == 'view' ? true : false;
       this.showDialog(option.title, disabled);
       this.getLoginInfo();
     },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //获取当前用户信息
      getLoginInfo() {
        this.$request.doGet("/api/register/getLoginInfo").then(res => {
          if (res.isOk) {
            this.user = res.data.userInfo;
          }
        });
      },
      //提交表单
      submitForm: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            if(this.user.newPassword!=this.user.prePassword){
              this.showWarn("两次密码不一致");
              return
            }
            that.$request.doPost('/api/register/modifyPsd', that.user).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                that.showSuccess(res.info);
                that.$emit('reloadList');
              }
            });
          }
        });
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.passwordForm);//重置表单
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
</style>
