<!-- 【用户信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="600px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
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
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        roleList: [],
        checkRoleList: ["普通用户"],
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        user: {
          loginName:this.$store.getters.userInfo.loginName,
          oldPassword: "",
          newPassword: "",
          prePassword: ""
        },
        //表单校验
        rules: {
          oldPassword: [{required: true, message: '请输入旧密码', trigger: 'blur'}],
          newPassword: [{required: true, message: '请输入新密码', trigger: 'blur'}],
          prePassword: [{required: true, message: '请输入确认密码', trigger: 'blur'}]
        }
      }
    },
    mounted() {
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
      submitForm: function () {
        const that = this;
        that.$refs.passwordForm.validate((valid) => {
          if (valid) {//表达校验
            if(this.user.newPassword!=this.user.prePassword){
              this.showWarn("两次密码不一致");
              return
            }
            that.$request.doPost('/admin/sysUser/modifyPsd', that.user).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                //that.showSuccess(res.info);
                that.$emit('success');
              }
            });
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
