<template>
  <div class="body">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="服务端" name="first">
        <base-setting :configs="configs"></base-setting>
      </el-tab-pane>
      <el-tab-pane label="公众号" name="second">
        <offical-setting :configs="configs"></offical-setting>
      </el-tab-pane>
      <el-tab-pane label="小程序" name="third">
        <applet-setting :configs="configs"></applet-setting>
      </el-tab-pane>
      <el-tab-pane label="客户端" name="fourth">
        <client-setting :configs="configs"></client-setting>
      </el-tab-pane>
      <el-tab-pane label="邮箱设置" name="fifth">
        <email-setting :configs="configs"></email-setting>
      </el-tab-pane>
      <el-tab-pane label="主题设置" name="sixth">
        <theme-setting :configs="configs"></theme-setting>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import baseSetting from './form/base-setting'
  import officalSetting from './form/offical-setting'
  import appletSetting from './form/applet-setting'
  import clientSetting from './form/client-setting'
  import emailSetting from './form/email-setting'
  import themeSetting from './form/theme-setting'
  export default {
    components:{baseSetting,officalSetting,appletSetting,clientSetting,emailSetting,themeSetting},
    data() {
      return {
        changeMap:new Map(),
        changeList:[],
        configs:{},
        activeName: 'first'
      };
    },
    mounted() {
      this.getList()
    },

    computed: {
      newConfigs () {
        return JSON.parse(JSON.stringify(this.configs))
      }
    },
    methods: {
      subConfigs() {
        const that = this;
        Object.keys(that.configs).forEach(function (key) {
          that.changeList.push({code: key, val: that.configs[key]})
        });
        if (that.changeList.length == 0) {
          that.showWarn("您没有修改数据,无需保存");
          return
        }
        that.showConfirm("确认保存配置吗?", function () {
          that.$request.doPost('/admin/sysConfig/editSub', {configs: that.changeList}).then(res => {
            if (res.isOk) {
              that.showSuccess("操作成功");
              that.getList();
              that.changeList = []
            };
          });
        })
      },
      getList(){
        this.$request.doGet('/admin/sysConfig/getConfigMap').then(res => {
          if (res.isOk) {
            this.configs = res.data
            console.log('configs:',this.configs)
          };
        });
      },
      handleClick(tab, event) {
        console.log(tab, event);
      }
    }
  };
</script>
