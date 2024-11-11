<template>
  <div class="body">
    <el-card class="box-card" style="height: 350px">
      <div slot="header" class="clearfix">
        <span>邮箱设置</span>
        <el-button style="float: right;" size="mini" @click="subConfigs" type="primary">保存配置</el-button>
      </div>
      <div class="text item">
        <el-form label-width="100px" class="edit-form">
          <el-form-item label="邮件协议" class="border">
            <el-input v-model="configs.mailSmtp" placeholder="请输入邮件协议"></el-input>
          </el-form-item>
          <el-form-item label="邮件端口" class="border">
            <el-input v-model="configs.mailPort" placeholder="请输入邮件端口"></el-input>
          </el-form-item>
          <el-form-item label="邮件用户" class="border">
            <el-input v-model="configs.mailName" placeholder="请输入邮件用户"></el-input>
          </el-form-item>
          <el-form-item label="邮件密码" class="border">
            <el-input v-model="configs.mailPassword" placeholder="请输入邮件密码"></el-input>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<script>
  export default {
    name: "base-setting",
    data() {
      return {
      };
    },
    props: {
      configs: {
        type: Object,
        default:{}
      }
    },
    methods: {
      subConfigs(){
        this.$parent.$parent.$parent.subConfigs()
      }
    }
  };
</script>
