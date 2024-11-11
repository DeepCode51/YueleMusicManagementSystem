<!-- 【点赞|收藏】列表 -->
<template>
  <el-dialog width="420px" class="front-dialog" center :title="dialog.title" :close-on-click-modal="false" :append-to-body="true" v-dialog-drag :visible.sync="dialog.open">
    <div v-if="dataList.length>0">
      <el-card  v-for="(item,index) in dataList" :key="item.id" class="box-card">
        <div class="el-icon-star-on">{{item.content}}</div>
        <div class="float-r" style="padding-bottom: 5px">
          <el-button  @click="view(item)" size="mini" title="查看" icon="el-icon-link" circle></el-button>
          <el-button  @click="del(item)" size="mini" title="删除" icon="el-icon-delete" circle style="margin-left:5px"></el-button>
        </div>
      </el-card>
    </div>
    <el-empty v-else description="暂无数据"></el-empty>
    <div class="dialog-footer">
      <el-button class="sub-btn" @click="cancel">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  export default {
    name: "user-evaluate",
    props: {
      type: {
        type: String,
        default: ''
      },
      path: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        //弹窗设置
        dataList:[],
        total:0,
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
      }
    },
    mounted() {
      if(this.type==''){
        this.showWarn("请设置消息类型");
      }
    },
    methods: {
      view(item){
        this.cancel();
        this.$router.push({name:this.path,query:{id:item.refId}})
      },
      del(item) {
        const that = this;
        that.$request.doPost('/api/message/cancel', {id:item.id}).then(res => {
          if (res.isOk) {
            that.showSuccess("取消成功");
            that.getList();
          }
        })
      },
      //打开弹窗
      open(option) {
        this.getList();
        this.showDialog(option.title, false);
        this.register=this.userInfo;
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
     //查询列表数据
      getList() {
        const that = this;
        that.$request.doGet('/api/message/getList', {type:this.type,userId:this.$store.getters.userInfo.id}).then(res => {
          if (res.isOk) {
            that.dataList = res.list;
            that.total = res.list.length;
          }
        })
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.sysUserForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
    }
  }
</script>
<style scoped lang="scss">
  @import '../../assets/styles/theme';
  .front-dialog /deep/ .el-dialog__headerbtn .el-dialog__close{
    color: white!important;
  }
  .front-dialog /deep/ .el-dialog__header  {
    background: $theme-front-color;
  }
  .front-dialog /deep/ .el-dialog__title  {
    font-weight: bold;
    color: white!important;
  }
  .sub-btn  {
    margin: 20px 0 10px;
    width: 100%;
    background: $theme-front-color;
    color: white;
    height: 45px
  }
</style>
