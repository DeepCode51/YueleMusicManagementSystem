<!-- 【角色信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="600px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysRole" ref="sysRoleForm" :rules="rules" label-width="100px">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysRole.id"/>
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="sysRole.roleCode" placeholder="请输入角色编码" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="sysRole.roleName" placeholder="请输入角色名称" :disabled="dialog.disabled" style="width: 217px"></el-input>
        </el-form-item>
        <el-form-item label="备注信息" prop="remarks">
          <el-input type="textarea" v-model="sysRole.remarks" placeholder="请输入备注信息" :disabled="dialog.disabled"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm('sysRoleForm')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "sysRoleForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysRole: {
          roleCode: "",
          roleName: "",
          remarks: ""
        },
        //表单校验
        rules: {
          roleCode: [{required: true, message: '请输入角色编码', trigger: 'blur'},{validator: this.$validator.unique, form: this, tableName: 'sys_role', column: 'role_code', model: 'sysRole', message: '角色编码已存在', trigger: 'blur'}],
          roleName: [{required: true, message: '请输入角色名称', trigger: 'blur'},{validator: this.$validator.unique, form: this, tableName: 'sys_role', column: 'role_name', model: 'sysRole', message: '角色名称已存在', trigger: 'blur'}],
          remarks: [{required: true, message: '请输入备注信息', trigger: 'blur'}]
        }
      }
    },
    computed: {
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSysRole(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //根据id获取数据
      getSysRole(id) {
        this.$request.doGet("/admin/sysRole/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysRole = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            that.$request.doPost('/admin/sysRole/sub', that.sysRole).then(res => {
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
        this.resetForm(this.$refs.sysRoleForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
    }
  }
</script>

<style scoped>
</style>
