<!-- 【附件信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysAttach" ref="sysAttachForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysAttach.id"/>
        </el-form-item>
        <el-form-item label="文件名称" prop="fileName">
          <el-input v-model="sysAttach.fileName" placeholder="请输入文件名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="文件大小" prop="fileSize">
          <el-input v-model="sysAttach.fileSize" placeholder="请输入文件大小" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="文件模块" prop="fileModule">
          <el-input v-model="sysAttach.fileModule" placeholder="请输入文件模块" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="上传类型" prop="fileType">
          <el-input v-model="sysAttach.fileType" placeholder="请输入上传类型" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="文件后缀" prop="suffix">
          <el-input v-model="sysAttach.suffix" placeholder="请输入文件后缀" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="保存路径" prop="savePath">
          <el-input v-model="sysAttach.savePath" placeholder="请输入保存路径" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="关联id" prop="refId">
          <el-input v-model="sysAttach.refId" placeholder="请输入关联id" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="文件备注" prop="remarks">
          <el-input v-model="sysAttach.remarks" placeholder="请输入文件备注" :disabled="dialog.disabled" class="sui-input"/>
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
    name: "sysAttachForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysAttach: {
          fileName: "",
          fileSize: "",
          fileModule: "",
          fileType: "",
          suffix: "",
          savePath: "",
          refId: "",
          remarks: ""
        },
        //表单校验
        rules: {
          fileName: [{required: true, message: '请输入文件名称', trigger: 'blur'}],
          fileSize: [{required: true, message: '请输入文件大小', trigger: 'blur'}],
          fileModule: [{required: true, message: '请输入文件模块', trigger: 'blur'}],
          fileType: [{required: true, message: '请输入上传类型', trigger: 'blur'}],
          suffix: [{required: true, message: '请输入文件后缀', trigger: 'blur'}],
          savePath: [{required: true, message: '请输入保存路径', trigger: 'blur'}],
          refId: [{required: true, message: '请输入关联id', trigger: 'blur'}],
          remarks: [{required: true, message: '请输入文件备注', trigger: 'blur'}]
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
          this.getSysAttach(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSysAttach(id) {
        this.$request.doGet("/admin/sysAttach/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysAttach = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysAttachForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysAttach/sub', that.sysAttach).then(res => {
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
        this.resetForm(this.$refs.sysAttachForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
