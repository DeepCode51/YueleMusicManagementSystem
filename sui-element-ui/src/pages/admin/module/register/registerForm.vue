<!-- 【前台用户】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="register" ref="registerForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="register.id"/>
        </el-form-item>
        <el-form-item label="头像" prop="photo">
          <sui-photo type="register_photo" v-model="register.photo" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="登录名" prop="loginName">
          <el-input v-model="register.loginName" placeholder="请输入登录名" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="register.userName" placeholder="请输入姓名" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="register.phone" placeholder="请输入手机号" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="register.email" placeholder="请输入邮箱" :disabled="dialog.disabled" class="sui-input"/>
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
        register: {
          photo: "",
          loginName: "",
          password: "",
          userName: "",
          sex: "",
          userNo: "",
          phone: "",
          idCard: "",
          email: "",
          userType: "",
          userAuthorId: "",
          address: ""
        },
        //表单校验
        rules: {
          photo: [{required: true, message: '请输入头像', trigger: 'blur'}],
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'},{validator: this.$validator.unique, form: this, tableName: 'register', column: 'login_name', model: 'register', message: '登录名已存在', trigger: 'blur'}],
          email: [{type: 'email', message: '请输入正确的邮箱', trigger:'blur'},{validator:this.$validator.unique,form:this,tableName:'register',column:'email',model:'register',message: '邮箱已存在',trigger: 'blur'}],
          phone: [{validator: this.$validator.phone, message: '请输入11位手机号', trigger: 'blur'},{validator: this.$validator.unique, form: this,model: 'register', tableName: 'register', column: 'phone',  message: '手机号已存在', trigger: 'blur'}],
          idCard: [{validator:this.$validator.idCard, message: '请输入正确的身份证号', trigger: 'blur'},{validator: this.$validator.unique, form: this,model: 'register', tableName: 'register', column: 'id_card',  message: '身份证号已存在', trigger: 'blur'}],
          userName: [{required: true, message: '请输入姓名', trigger: 'blur'}],
          sex: [{required: true, message: '请选择性别', trigger: 'blur'}],
          userNo: [{required: true, message: '请输入工号', trigger: 'blur'}],
          userType: [{required: true, message: '请选择用户类型', trigger: 'blur'}],
          address: [{required: false, message: '请输入详细地址', trigger: 'blur'}]
        }
      }
    },
    computed: {
      //【性别】
      sexDict() {
        return this.$store.getters.getDictArray('sys_sex');
      },
      //【用户类型】
      userTypeDict() {
        return this.$store.getters.getDictArray('user_type');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getRegister(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getRegister(id) {
        this.$request.doGet("/admin/register/get?id=" + id).then(res => {
          if (res.isOk) {
            this.register = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.registerForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/register/sub', that.register).then(res => {
              if (res.isOk) {
                that.cancel();
                that.showSuccess("提交成功");
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
      }
    }
  }
</script>

<style scoped>

</style>
