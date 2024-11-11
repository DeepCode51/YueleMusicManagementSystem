<!-- 【评价|建议】填写 -->
<template>
  <div>
    <el-button :class="icon" size="mini" type="warning" @click.native="open" :title="showTitle">{{showTitle}}</el-button>
    <el-dialog width="420px" class="front-dialog" center :title="type" :close-on-click-modal="false" :append-to-body="true" v-dialog-drag :visible.sync="dialog.open">
      <!--星级-->
      <el-rate v-if="type=='评价'" :texts="texts" v-model="stars" show-text></el-rate>
      <el-input type="textarea" v-model="content" placeholder="请输入内容" :disabled="dialog.disabled"/>
      <div class="dialog-footer">
        <el-button class="sub-btn" @click="sub">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    name:"sui-evaluate",
    props: {
      type: {
        type: String,
        default: ''
      },
      refId: {
        type: String,
        default: ''
      },
    },
    data(){
      return{
        title:this.type,
        //弹窗设置
        dialog: {
          title: this.type,
          open: false,
          disabled: false
        },
        texts:['极差', '差评','一般', '满意', '非常满意'],
        stars: 5,
        content:"",
        icon:"el-icon-edit-outline",
        total:0
      }
    },
    computed: {
      hasLogin() {
        return this.$store.getters.hasLogin;
      },
      showTitle() {
        return this.title;
      },
    },
    methods: {
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      open(option) {
        const that = this;
        if(!that.hasLogin){
          that.showWarn("请先进行登录");
          return
        }
        if(that.type==''){
          that.showWarn("请设置消息类型");
          return
        }
        if(that.refId==''){
          that.showWarn("请设置关联id");
          return
        }
        this.showDialog(option.title, false);
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.sysUserForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      sub() {
        const that = this;
        if(that.content.trim()==''){
          that.showWarn("请填写内容！");
          return
        }
        that.$request.doPost('/api/message/sub',{type:that.type,content:that.content,stars:that.stars,refId:that.refId}).then(res => {
          if (res.isOk) {
            that.content="";
            that.showSuccess(res.info);
            that.cancel();
            that.$emit('success')
          }
        });
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
