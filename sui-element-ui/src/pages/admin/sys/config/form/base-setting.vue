<template>
  <div class="body">
    <el-card class="box-card" style="height: 400px">
      <div slot="header" class="clearfix">
        <span>基础配置</span>
        <el-button style="float: right" size="mini" type="primary" @click="subConfigs">保存配置</el-button>
      </div>
      <div class="text item">
        <el-form label-width="100px" class="edit-form">
          <el-form-item label="项目名称" class="border">
            <el-input v-model="configs.projectName"></el-input>
          </el-form-item>
          <el-form-item label="项目域名" class="border">
            <el-input v-model="configs.projectDomain"></el-input>
          </el-form-item>
          <el-form-item label="显示注册" class="border">
            <el-switch v-model="configs.showRegister" inactive-value="0" active-value="1"></el-switch>
          </el-form-item>
          <el-form-item label="显示角色" class="border">
            <el-switch v-model="configs.showRoles" inactive-value="0" active-value="1"></el-switch>
          </el-form-item>
          <el-form-item label="登录验证码" class="border">
            <el-switch v-model="configs.showValidCode" inactive-value="0" active-value="1"></el-switch>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="box-card" style="height: 350px">
      <div slot="header" class="clearfix">
        <span>高级配置</span>
        <el-button style="float: right" size="mini" type="primary" @click="subConfigs">保存配置</el-button>
      </div>
      <div class="text item">
        <el-form label-width="100px" class="edit-form">
          <el-form-item label="定时任务" class="border">
            <el-switch v-model="configs.isScheduleOpen" inactive-value="0" active-value="1"></el-switch>
          </el-form-item>
          <el-form-item label="首页轮播" class="border">
            <el-switch v-model="configs.isNeedWheel" inactive-value="0" active-value="1"></el-switch>
          </el-form-item>
          <el-form-item label="记录日志" class="border">
            <el-switch v-model="configs.isOpenLog" inactive-value="0" active-value="1"></el-switch>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<style>
  .border{
    border: 1px solid #EBEEF5
  }
</style>
<script>
  export default {
    name: "base-setting",
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
