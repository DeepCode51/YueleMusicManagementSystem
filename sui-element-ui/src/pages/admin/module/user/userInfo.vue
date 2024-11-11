<!-- 【用户信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="600px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysUser" ref="sysUserForm" :rules="rules" label-width="100px">
        <el-form-item label="用户头像" prop="loginName">
          <sui-photo type="user" v-model="sysUser.photo" :disabled="dialog.disabled" :url="sysUser.photo"></sui-photo>
        </el-form-item>
        <el-form-item label="登录名" prop="loginName">
          <el-input v-model="sysUser.loginName"  placeholder="请输入登录名" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="sysUser.userName" placeholder="请输入姓名" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="sysUser.phone" placeholder="请输入手机号" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="sysUser.email" placeholder="请输入邮箱" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {mapActions} from "vuex";
  export default {
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
          userName: "",
          userNo: "",
          email: "",
          phone: "",
          officeId: ""
        },
        //表单校验
        rules: {
          photo: [{required: true, message: '请输入用户头像', trigger: 'blur'}],
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'}],
          userName: [{required: true, message: '请输入姓名', trigger: 'blur'}],
          email: [{type: 'email', message: '请输入正确的邮箱', trigger:'blur'},{validator:this.$validator.unique,form:this,model:'sysUser',tableName:'sys_user',column:'email',message: '邮箱已存在',trigger: 'blur'}],
          phone: [{validator: this.$validator.phone, message: '请输入11位手机号', trigger: 'blur'},{validator: this.$validator.unique, form: this,model: 'sysUser', tableName: 'sys_user', column: 'phone',  message: '手机号已存在', trigger: 'blur'}],
        }
      }
    },
    computed:{
      userInfo(){
        return this.$store.getters.userInfo;
      }
    },
    methods: {
      ...mapActions(['commitUpdateUserInfo']),
      //打开弹窗
      open(option) {
        const that = this;
        const user = JSON.parse(JSON.stringify(this.userInfo))
        that.sysUser = user;
        that.showDialog(option.title, false);
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysUserForm.validate((valid) => {
          if (valid) {//表达校验
            that.commitUpdateUserInfo(that.sysUser).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                that.showSuccess("提交成功");
                that.$emit('reload');
              }
            })
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

<style scoped>
</style>
