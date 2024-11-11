<template>
  <div class="body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>客户端</span>
        <el-button style="float: right" size="mini" type="primary" @click="subConfigs">保存配置</el-button>
      </div>
      <div class="text item">
        <el-form label-width="100px" class="edit-form">
          <el-form-item label="主题" class="border">
            <el-input  placeholder="请输入客户端配置"></el-input>
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
