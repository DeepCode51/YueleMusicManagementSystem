<template>
  <div class="body">
    <el-card class="box-card" style="height: 350px">
      <div slot="header" class="clearfix">
        <span>邮箱设置</span>
        <el-button style="float: right;" size="mini" @click="subConfigs" type="primary">保存配置</el-button>
      </div>
      <div class="text item">
        <el-form label-width="100px" class="edit-form">
          <el-form-item label="横向显示" class="border">
            <el-switch v-model="configs.isHorizontal" inactive-value="0" active-value="1"></el-switch>
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
