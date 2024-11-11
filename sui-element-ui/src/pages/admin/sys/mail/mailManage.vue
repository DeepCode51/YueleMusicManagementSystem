<template>
  <el-card class="box-card" style="height: 350px">
    <div slot="header" class="clearfix">
      <span>邮件服务</span>
      <el-button style="float: right;" size="mini" @click="send" type="primary">发送邮件</el-button>
    </div>
    <div class="text item">
      <el-form :model="mail" ref="mailForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="收件人邮箱" prop="mailUser" class="border">
          <el-input v-model="mail.mailUser" placeholder="请输入收件人邮箱"></el-input>
        </el-form-item>
        <el-form-item label="邮件标题" prop="mailTitle" class="border">
          <el-input v-model="mail.mailTitle" placeholder="请输入邮件标题"></el-input>
        </el-form-item>
        <el-form-item label="发送内容" prop="mailContent" class="border">
          <el-input type="textarea" v-model="mail.mailContent" placeholder="请输入发送内容"></el-input>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
</template>
<script>
  export default {
    name: "base-setting",
    data() {
      return {
        mail: {
          mailUser: "",
          mailTitle: "",
          mailContent: "",
        },
        //表单校验
        rules: {
          mailUser: [{required: true, message: '请输入收件人邮箱', trigger: 'blur'},{type: 'email', message: '请输入正确的邮箱', trigger: ['blur', 'change']}],
          mailTitle: [{required: true, message: '请输入邮件标题', trigger: 'blur'}],
          mailContent: [{required: true, message: '请输入发送内容', trigger: 'blur'}]
        }
      };
    },
    methods:{
      send(){
        const that = this;
        that.$refs.mailForm.validate((valid) => {
          if (valid) {//表达校验
            that.showConfirm("确认发送邮件吗?", function () {
              that.$request.doPost('/admin/sys/mail/send', that.mail).then(res => {
                if (res.isOk) {
                  that.showSuccess("发送成功");
                };
              });
            })
          }
        });
      }
    }
  };
</script>
