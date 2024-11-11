<!-- 【用户信息】弹窗页面 -->
<template>
  <el-dialog width="420px" class="front-dialog" center :title="dialog.title" :close-on-click-modal="false" :append-to-body="true" v-dialog-drag :visible.sync="dialog.open">
    <el-form :model="register" ref="registerForm" :rules="rules" label-width="100px">
      <el-form-item label="用户头像" prop="loginName">
        <sui-photo type="user" v-model="register.photo" :disabled="dialog.disabled" :url="register.photo"></sui-photo>
      </el-form-item>
      <el-form-item label="登录名" prop="loginName">
        <el-input v-model="register.loginName" placeholder="请输入登录名" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="register.userName" placeholder="请输入姓名" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="register.phone" placeholder="请输入手机号" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="register.email" placeholder="请输入邮箱" :disabled="dialog.disabled" style="width: 217px"></el-input>
      </el-form-item>
    </el-form>
    <div class="dialog-footer">
      <el-button class="sub-btn" @click="submitForm('registerForm')">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {mapActions} from "vuex";
  export default {
    name: "userInfo",
    data() {
      return {
        register: {
          photo: "",
          loginName: "",
          userName: "",
          email: "",
          phone: "",
        },
        roleList: [],
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单校验
        rules: {
          photo: [{required: true, message: '请输入用户头像', trigger: 'blur'}],
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'},{validator: this.$validator.unique, form: this, tableName: 'register', column: 'login_name', model: 'register', message: '登录名已存在', trigger: 'blur'}],
          email: [{type: 'email', message: '请输入正确的邮箱', trigger:'blur'},{validator:this.$validator.unique,form:this,tableName:'register',column:'email',model:'register',message: '邮箱已存在',trigger: 'blur'}],
          phone: [{validator: this.$validator.phone, message: '请输入11位手机号', trigger: 'blur'},{validator: this.$validator.unique, form: this,model: 'register', tableName: 'register', column: 'phone',  message: '手机号已存在', trigger: 'blur'}],
          idCard: [{validator:this.$validator.idCard, message: '请输入正确的身份证号', trigger: 'blur'},{validator: this.$validator.unique, form: this,model: 'register', tableName: 'register', column: 'id_card',  message: '身份证号已存在', trigger: 'blur'}]
        }
      }
    },
    methods: {
      ...mapActions(['commitFrontUserInfo']),
      //打开弹窗
      open(option) {
        this.showDialog(option.title, false);
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
            this.register = res.data.userInfo;
          }
        });
      },
      //提交表单
      submitForm: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            that.commitFrontUserInfo(that.register).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                that.showSuccess("保存成功");
              }
            })
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
