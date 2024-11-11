<!-- 【用户信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="600px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysUser" ref="sysUserForm" :rules="rules" label-width="100px">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysUser.id"/>
        </el-form-item>
        <el-form-item label="登录名" prop="loginName">
          <el-input v-model="sysUser.loginName" placeholder="请输入登录名" :disabled="dialog.disabled" class="sui-input"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="userName">
          <el-input v-model="sysUser.userName" placeholder="请输入姓名" :disabled="dialog.disabled" class="sui-input"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="sysUser.sex" placeholder="选择性别" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in sexDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="sysUser.phone" placeholder="请输入手机号" :disabled="dialog.disabled" class="sui-input"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="sysUser.email" placeholder="请输入邮箱" :disabled="dialog.disabled" class="sui-input"></el-input>
        </el-form-item>
        <el-form-item label="所属角色" v-if="!sysUser.id" prop="roleList" >
          <el-radio-group v-model="sysUser.roleList[0]">
            <el-radio v-for="role in allRole" :key="role.roleCode" :label="role" border :disabled="!showRole">{{role.roleName}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm('sysUserForm')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    components:{
    },
    name: "userForm",
    data() {
      return {
        showRole:false,
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        allRole:[],
        //表单数据
        sysUser: {
          roleList:[{roleCode:'userRole'}],//保存角色
          photo: "",
          loginName: "",
          password: "",
          userName: "",
          office:{},
          userNo: "",
          email: "",
          phone: "",
          officeId: ""
        },
        //表单校验
        rules: {
          photo: [{required: true, message: '请输入用户头像', trigger: 'blur'}],
          loginName: [{required: true, message: '请输入登录名', trigger: 'blur'},{validator: this.$validator.unique, form: this, model: 'sysUser', tableName: 'sys_user', column: 'login_name', message: '登录名已存在', trigger: 'blur'}],
          password: [{required: true, message: '请输入密码', trigger: 'blur'}],
          userName: [{required: true, message: '请输入姓名', trigger: 'blur'}],
          sex: [{required: true, message: '请选择性别', trigger: 'blur'}],
          userNo: [{required: true, message: '请输入用户编号', trigger: 'blur'}],
          email: [{type: 'email', message: '请输入正确的邮箱', trigger: ['blur', 'change']},{validator: this.$validator.unique, form: this,model: 'sysUser', tableName: 'sys_user', column: 'email',  message: '邮箱已存在', trigger: 'blur'}],
          phone: [{validator: this.$validator.phone, message: '请输入11位手机号', trigger: 'blur'},
                 {validator: this.$validator.unique, form: this,model: 'sysUser', tableName: 'sys_user', column: 'phone',  message: '手机号已存在', trigger: 'blur'}],
          roleList: [{required: true, message: '请至少选择一个角色', trigger: 'blur'}],
          officeId: [{required: true, message: '请输入所属机构', trigger: ['blur', 'change']}]
        }
      }
    },
    computed: {
      sexDict() {
        return this.$store.getters.getDictArray('sys_sex');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const that = this;
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        that.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.showRole = false
          that.getUser(option.id);
        }else{
          that.getRoleList();//新增初始化角色
          that.showRole = true
        }
      },
      //初始化角色
      getRoleList(userId){
        const that = this;
        that.$request.doGet('/admin/sysUser/findCheckedRolesByUserId?userId=' + userId).then(res => {
          if (res.isOk) {
            that.allRole = res.list.filter(function (v) {
              return v.roleCode != 'adminRole'
            });
            that.sysUser.roleList = that.allRole.filter(function (v) {
              return v.checked
            })
          }
        });
      },
      //根据id获取数据
      getUser(id) {
        const that = this;
        this.$request.doGet("/admin/sysUser/get?id="+id).then(res => {
          if (res.isOk) {
            that.sysUser = res.obj;
            that.getRoleList(id);//编辑初始化角色
          }
        });
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //提交表单
      submitForm: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            // that.sysUser.roleList = new Array(that.sysUser.roleList);
            that.$request.doPost('/admin/sysUser/sub', that.sysUser).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                that.showSuccess("提交成功");
                that.$emit('reloadList');
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
  [v-cloak]{
    display: none;
  }
</style>
