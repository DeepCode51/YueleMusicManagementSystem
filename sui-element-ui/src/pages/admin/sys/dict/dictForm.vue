<!-- 【字典信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="600px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysDict" ref="sysDictForm" :rules="rules" label-width="100px" style="height: 250px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysDict.id"/>
        </el-form-item>
        <el-form-item label="标签名" prop="label">
          <el-input v-model="sysDict.label" placeholder="请输入标签名" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="数据值" prop="value">
          <el-input v-model="sysDict.value" placeholder="请输入数据值" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input v-model="sysDict.sort" placeholder="请输入排序" :disabled="dialog.disabled" class="sui-input"/>
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
    name: "sysDictForm",
    data() {
      return {
        pageFrom:"",
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysDict: {
          pid: "",
          label: "",
          value: "",
          type: "",
          style: "",
          sort: 1,
          remarks: ""
        },
        //表单校验
        rules: {
          pid: [{required: true, message: '请输入父级编号', trigger: 'blur'}],
          label: [{required: true, message: '请输入标签名', trigger: 'blur'}],
          value: [{required: true, message: '请输入数据值', trigger: 'blur'}],
          type: [{required: true, message: '请输入类型', trigger: 'blur'}],
          style: [{required: true, message: '请输入样式', trigger: 'blur'}],
          sort: [{required: true, message: '请输入排序', trigger: 'blur'}],
          remarks: [{required: true, message: '请输入备注信息', trigger: 'blur'}]
        }
      }
    },
    computed: {
    },
    methods: {
      //打开弹窗
      open(option) {
        this.pageFrom = option.pageFrom;
        const disabled = this.pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (this.pageFrom == 'add') {
          this.sysDict.type='dict'
          this.sysDict.pid= '0'
        }else if(this.pageFrom == 'edit'){
          this.getSysDict(option.id);
        }else if(this.pageFrom == 'view'){
          this.getSysDict(option.id);
        }else if(this.pageFrom == 'dictCode'){
          this.sysDict.type='dictCode'
          this.sysDict.pid= option.id
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSysDict(id) {
        this.$request.doGet("/admin/sysDict/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysDict = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysDictForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysDict/sub', that.sysDict).then(res => {
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
        this.resetForm(this.$refs.sysDictForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
