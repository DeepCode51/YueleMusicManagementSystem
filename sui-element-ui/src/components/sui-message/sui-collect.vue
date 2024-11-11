<!-- 【点赞|收藏】按钮 -->
<template>
    <el-button :class="icon" size="small" type="warning" @click="sub" :title="showTitle"> {{showTitle}}</el-button>
</template>
<script>
  export default {
    name:"sui-collect",
    props: {
      type: {
        type: String,
        default: ''
      },
      refId: {
        type: String,
        default: ''
      },
      refContent: {
        type: String,
        default: ''
      },
    },
    data(){
      return{
        title:this.type,
        icon:"el-icon-star-on"
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
    mounted() {
      this.getMessage()
    },
    methods: {
      sub() {
        const that = this;
        if(!that.hasLogin){
          that.showWarn("请先进行登录");
          return
        }
        if(that.type==''){
          that.showWarn("请设置消息类型");
          return
        }
        if(that.refContent==''){
          that.showWarn("请设置消息内容");
          return
        }
        if(that.refId==''){
          that.showWarn("请设置关联id");
          return
        }
        that.$request.doPost('/api/message/subCollect',{type:that.type,content:that.refContent,refId:that.refId}).then(res => {
          if (res.isOk) {
            that.showSuccess(res.info);
            that.getMessage();
          }
        });
      },
      getMessage(){
        const that = this;
        that.$request.doPost('/api/message/get',{type:that.type,refId:that.refId}).then(res => {
          if (res.isOk) {
            that.title = res.data.flag?"已"+that.type:that.type;
            that.icon = res.data.flag?"el-icon-star-on on":"el-icon-star-off off";
          }
        });
      }
    }
  }
</script>
<style scoped>
  .on{
    color: #E5511D;
  }
  .off{
    color: white;
  }
</style>
