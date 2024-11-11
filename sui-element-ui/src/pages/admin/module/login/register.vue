<!-- 【后台注册】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog class="register-dialog" :title="dialog.title" :close-on-click-modal="false" center style="margin-top: 200px;" width="500px" :visible.sync="dialog.open">
      <el-form style="margin-left: 50px" :model="sysUser" ref="registerForm" :rules="rules" label-width="100px">
        <el-form-item label="登录账号" prop="loginName">
          <el-input v-model="sysUser.loginName" placeholder="请输入登录账号" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="sysUser.password" type="password" placeholder="请输入登录密码" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="prePassword">
          <el-input v-model="sysUser.prePassword" type="password" placeholder="请输入确认密码" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitRegister('registerForm')">提交注册</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  export default {
    name: "registerForm",
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
          loginName: [{required: true, message: '请输入登录账号', trigger: 'blur'}],
          password: [{required: true, message: '请输入登录密码', trigger: 'blur'}],
          prePassword: [{required: true, message: '请输入确认密码', trigger: 'blur'}],
        }
      }
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
      submitRegister: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            if (that.sysUser.password != that.sysUser.prePassword) {
              this.showWarn("两次密码不一致!")
              return false
            }
            that.$request.doPost('/admin/login/register', that.sysUser).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                that.showSuccess("注册成功");
                that.$emit('reloadList');
              }
            });
          }
        });
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.registerForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
    }
  }
</script>

<style scoped lang="scss">
  @import '../../../../assets/styles/theme';
  .register-dialog /deep/ .el-dialog__headerbtn .el-dialog__close{
    color: white!important;
  }
  .register-dialog /deep/ .el-dialog__header  {
    background: #409EFF;
  }
  .register-dialog /deep/ .el-dialog__title  {
    font-weight: bold;
    color: white!important;
  }
</style>
