<!-- 【审核日志信息】弹窗页面 -->
<template>
  <el-card v-if="refId" class="audit-list">
    <div slot="header" class="clearfix">
      <span>审核记录</span>
    </div>
    <el-timeline v-if="dataList.length>0">
      <el-timeline-item
        v-for="(item, index) in dataList"
        :key="index"
        color="#0bbd87"
        :timestamp="'【审核时间】'+item.createDate">
        【审核人】：<span class="font-shadow" v-for="sysUser in sysUserDict" v-if="sysUser.id==item.userId">
                        <span v-for="role in sysUser.roleList">{{role.roleName}}</span>
                        <span>{{sysUser.loginName}}</span>
                   </span>
        【审核结果】：<span class="font-shadow">{{item.content}}</span>
      </el-timeline-item>
    </el-timeline>
    <el-empty v-else description="暂无审核数据"></el-empty>
  </el-card>
</template>

<script>
  export default {
    name: "sui-audit-record",
    props: {
      refId: {
        type: String,
        default: ""
      },
      type: {
        type: String,
        default: "审核"
      },
    },
    data() {
      return {
        dataList: [],
        sysUserDict: [],
      }
    },
    watch: {
      refId(value) {
        this.dataList = [];
        if (value != null) {
          this.getList()
        }
      }
    },
    mounted() {
      this.getList();
      this.getSysUserDict();
    },
    methods: {
      getList() {
        const that = this;
        //设置状态
        that.$request.doPost('/admin/sysMessage/getList', {type: this.type, refId: this.refId}).then(res => {
          if (res.isOk) {
            this.dataList = res.list;
          };
        });
      },
      //审核用户
      getSysUserDict() {
        this.$request.doGet('/admin/sysUser/getList').then(res => {
          if (res.isOk) {
            this.sysUserDict = res.list;
          }
        })
      },
    }
  }
</script>
<style scoped>
  .audit-list {
    margin: 0 10px;
    min-height: 380px;
    height: 380px;
    overflow-y: auto
  }
</style>

