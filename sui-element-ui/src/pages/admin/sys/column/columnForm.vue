<!-- 【字段信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysColumn" ref="sysColumnForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysColumn.id"/>
        </el-form-item>
        <el-form-item label="表名称" prop="tableName">
          <el-input v-model="sysColumn.tableName" placeholder="请输入表名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="表描述" prop="tableDesc">
          <el-input v-model="sysColumn.tableDesc" placeholder="请输入表描述" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="列名称" prop="columnName">
          <el-input v-model="sysColumn.columnName" placeholder="请输入列名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="列描述" prop="columnDesc">
          <el-input v-model="sysColumn.columnDesc" placeholder="请输入列描述" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="控件类型" prop="inputType">
          <el-input v-model="sysColumn.inputType" placeholder="请输入控件类型" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="查询类型" prop="queryType">
          <el-input v-model="sysColumn.queryType" placeholder="请输入查询类型" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="字典类型" prop="dictType">
          <el-input v-model="sysColumn.dictType" placeholder="请输入字典类型" :disabled="dialog.disabled" class="sui-input"/>
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
    name: "sysColumnForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysColumn: {
          tableName: "",
          tableDesc: "",
          columnName: "",
          columnDesc: "",
          inputType: "",
          queryType: "",
          dictType: "",
          associateType: "",
          isRequired: "",
          isUnique: "",
          isPk: "",
          isShow: "",
          sort: "",
          remarks: ""
        },
        //表单校验
        rules: {
          tableName: [{required: true, message: '请输入表名称', trigger: 'blur'}],
          tableDesc: [{required: true, message: '请输入表描述', trigger: 'blur'}],
          columnName: [{required: true, message: '请输入列名称', trigger: 'blur'}],
          columnDesc: [{required: true, message: '请输入列描述', trigger: 'blur'}],
          inputType: [{required: true, message: '请输入控件类型', trigger: 'blur'}],
          queryType: [{required: true, message: '请输入查询类型', trigger: 'blur'}],
          dictType: [{required: true, message: '请输入字典类型', trigger: 'blur'}]
        }
      }
    },
    computed: {
      //【是否必填】
      isRequiredDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
      //【是否唯一】
      isUniqueDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
      //【是否主键】
      isPkDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
      //【是否显示】
      isShowDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSysColumn(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSysColumn(id) {
        this.$request.doGet("/admin/sysColumn/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysColumn = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysColumnForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysColumn/sub', that.sysColumn).then(res => {
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
        this.resetForm(this.$refs.sysColumnForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
