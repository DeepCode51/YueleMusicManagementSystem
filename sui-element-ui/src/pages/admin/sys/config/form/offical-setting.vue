<template>
  <div class="body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>公众号</span>
        <el-button style="float: right" size="mini" type="primary" @click="subConfigs">保存配置</el-button>
      </div>
      <div class="text item">
        <div class="text item">
          <el-form label-width="100px" class="edit-form">
            <el-form-item label="appid" class="border">
              <el-input v-model="configs.officialAppId" placeholder="请输入公众号appid"></el-input>
            </el-form-item>
            <el-form-item label="appSecret" class="border">
              <el-input v-model="configs.officialAppSecret" placeholder="请输入公众号appSecret"></el-input>
            </el-form-item>
            <el-form-item label="token" class="border">
              <el-input v-model="configs.officialToken" placeholder="请输入公众号token"></el-input>
            </el-form-item>
            <el-form-item label="正式环境" class="border">
              <el-switch  v-model="configs.isProdEnvt" inactive-value="0" active-value="1"></el-switch>
            </el-form-item>
            <el-button style="position: relative;left: 0;top: 0" @click="updateMenu">同步菜单</el-button>
            <el-form-item label="菜单栏"  class="border">
              <el-input type="textarea" rows="15" v-model="configs.officialMenus"  placeholder="请输入公众号菜单"/>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>
<script>
  export default {
    name: "offical-setting",
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
    computed:{
    },
    watch: {
      "configs.officialMenus"(value) {
        this.configs.officialMenus = unescape(this.configs.officialMenus);
      }
    },
    methods: {
      subConfigs(){
        this.configs.officialMenus = escape(this.configs.officialMenus);
        this.$parent.$parent.$parent.subConfigs()
      },
      updateMenu(){
        const that = this;
        that.showConfirm("确认同步公众号菜单吗?", function () {
          let s = that.configs.officialMenus.replace(/[\f\n\r\t]/g, '');
           that.$request.doPost('/admin/setting/updateMenu', {"menuJson": s}).then(res => {
              if (res.isOk) {
                that.showSuccess("操作成功");
             };
          });
        })
      }
    }
  };
</script>
