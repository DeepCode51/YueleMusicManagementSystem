<template>
  <div class="body">
    <el-card class="box-card" style="height: 350px">
      <div slot="header" class="clearfix">
        <span>小程序</span>
        <el-button style="float: right;" size="mini" @click="updateSetting" type="primary">保存配置</el-button>
      </div>
      <div class="text item">
        <el-form label-width="100px" class="edit-form">
          <el-form-item label="appid" class="border">
            <el-input v-model="configs.appletAppId" placeholder="请输入小程序appid"></el-input>
          </el-form-item>
          <el-form-item label="appSecret" class="border">
            <el-input v-model="configs.appletAppSecret" placeholder="请输入小程序appSecret"></el-input>
          </el-form-item>
          <el-form-item label="登录校验" class="border">
            <el-switch  v-model="configs.isCheckLogin" inactive-value="0" active-value="1"></el-switch>
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
