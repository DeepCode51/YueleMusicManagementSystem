<!-- 【消息信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysMessage" ref="sysMessageForm" :rules="rules" label-width="100px" style="height: 300px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysMessage.id"/>
        </el-form-item>
        <el-form-item label="消息类型" prop="type">
          <el-input v-model="sysMessage.type" placeholder="请选输入息类型" :disabled="true" class="sui-input"/>
        </el-form-item>
        <el-form-item label="操作时间" prop="createDate">
          <el-input v-model="sysMessage.createDate" placeholder="请输入操作时间" :disabled="true" class="sui-input"/>
        </el-form-item>
        <el-form-item label="消息内容" prop="content">
          <el-input type="textarea" v-model="sysMessage.content" placeholder="请输入消息内容" :disabled="dialog.disabled" class="sui-textarea"/>
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
    name: "sysMessageForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysMessage: {
          type: "",
          pid: "",
          userId: "",
          refId: "",
          content: ""
        },
        //表单校验
        rules: {
          type: [{required: true, message: '请输入消息类型', trigger: 'blur'}],
          pid: [{required: true, message: '请输入父级id', trigger: 'blur'}],
          userId: [{required: true, message: '请输入用户id', trigger: 'blur'}],
          refId: [{required: true, message: '请输入关联id', trigger: 'blur'}],
          content: [{required: true, message: '请输入消息内容', trigger: 'blur'}]
        }
      }
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSysMessage(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSysMessage(id) {
        this.$request.doGet("/admin/sysMessage/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysMessage = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysMessageForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysMessage/sub', that.sysMessage).then(res => {
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
        this.resetForm(this.$refs.sysMessageForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
