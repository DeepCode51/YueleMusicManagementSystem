<!-- 【配置信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="800px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form style="height: auto" :model="sysConfig" ref="sysConfigForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysConfig.id"/>
        </el-form-item>
        <el-form-item label="配置名称" prop="name">
          <el-input v-model="sysConfig.name" placeholder="请输入配置名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="配置编码" prop="code">
          <el-input v-model="sysConfig.code" placeholder="请输入配置编码" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="配置取值" prop="val">
          <el-input type="textarea" v-model="sysConfig.val" placeholder="请输入配置取值" :disabled="dialog.disabled" class="sui-textarea"/>
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
    name: "sysConfigForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysConfig: {
          name: "",
          code: "",
          val: ""
        },
        //表单校验
        rules: {
          name: [{required: true, message: '请输入配置名称', trigger: 'blur'}],
          code: [{required: true, message: '请输入配置编码', trigger: 'blur'}],
          val: [{required: true, message: '请输入配置取值', trigger: 'blur'}]
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
          this.getSysConfig(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSysConfig(id) {
        this.$request.doGet("/admin/sysConfig/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysConfig = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysConfigForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysConfig/sub', that.sysConfig).then(res => {
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
        this.resetForm(this.$refs.sysConfigForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
