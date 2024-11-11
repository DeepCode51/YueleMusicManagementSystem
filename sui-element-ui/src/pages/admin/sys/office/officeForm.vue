<!-- 【机构信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="800px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form style="height: auto" :model="sysOffice" ref="sysOfficeForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键" class="hidden" prop="id">
          <el-input v-model="sysOffice.id" :disabled="dialog.disabled"/>
        </el-form-item>
        <el-form-item v-if="sysOffice.pid!='0'" label="父级" prop="pid">
          <sui-office v-model="sysOffice.pid" type="0" placeholder="请输入父级" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="sysOffice.name" placeholder="请输入名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item v-if="sysOffice.pid!='0'" label="类型" prop="type">
          <el-select v-model="sysOffice.type" placeholder="选择类型" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in typeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="sysOffice.pid!='0'" label="编码" prop="code">
          <el-input v-model="sysOffice.code" placeholder="请输入编码" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="sysOffice.leader" placeholder="请输入负责人" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="sysOffice.email" placeholder="请输入邮箱" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="sysOffice.phone" placeholder="请输入联系电话" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="备注信息" prop="remarks">
          <el-input type="textarea" v-model="sysOffice.remarks" placeholder="输入备注信息" :disabled="dialog.disabled"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm('sysOfficeForm')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "sysOfficeForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysOffice: {
          pid: "",
          name: "",
          code: "",
          type: "",
          leader: "",
          email: "",
          phone: "",
          sort: "",
          remarks: ""
        },
        //表单校验
        rules: {
          pid: [{required: true, message: '请输入父级', trigger: 'blur'}],
          name: [{required: true, message: '请输入名称', trigger: 'blur'},{validator: this.$validator.unique, form: this, tableName: 'sys_office', column: 'name', model: 'sysOffice', message: '名称已存在', trigger: 'blur'}],
          type: [{required: true, message: '请选择类型', trigger: 'blur'}],
          code: [{required: true, message: '请输入编码', trigger: 'blur'},{validator: this.$validator.unique, form: this, tableName: 'sys_office', column: 'code', model: 'sysOffice', message: '编码已存在', trigger: 'blur'}],
          email: [{type: 'email', message: '请输入正确的邮箱', trigger:'blur'},{validator:this.$validator.unique,form:this,model:'sysOffice',tableName:'sys_office',column:'email',message: '邮箱已存在',trigger: 'blur'}],
          phone: [{validator: this.$validator.phone, message: '请输入11位手机号', trigger: 'blur'},{validator: this.$validator.unique, form: this,model: 'sysOffice', tableName: 'sys_office', column: 'phone',  message: '手机号已存在', trigger: 'blur'}],
        }
      }
    },
    computed: {
      //【类型】
      typeDict() {
        return this.$store.getters.getDictArray('office_type');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        this.sysOffice.pid=option.id;
        this.sysOffice.name = "";
        this.sysOffice.code = "";
        if (pageFrom == 'edit' || pageFrom == 'view') {
          //获取通知信息数据
          this.getSysOffice(option.id)
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //根据id获取数据
      getSysOffice(id){
        this.$request.doGet("/admin/sysOffice/get?id="+id).then(res => {
          if (res.isOk) {
            this.sysOffice = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            that.$request.doPost('/admin/sysOffice/sub', that.sysOffice).then(res => {
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
        this.resetForm(this.$refs.sysOfficeForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>
</style>
