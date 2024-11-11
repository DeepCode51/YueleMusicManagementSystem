<!-- 【留言板】页面 -->
<template>
  <div class="front-body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <div class="message">
        <i class="el-icon-position count font-shadow"> 累计 {{total}} 条</i>
        <h1 class="font-shadow msg-title">交流平台</h1>
        <div contenteditable="true" id="message" ref="message" class="message-content" v-html="message.content"></div>
        <sui-face @selectFace="selectFace" class="msg-face pointer" :show="false"></sui-face>
        <div class="clearfix"></div>
        <el-button @click="subMsg" size="small" type="primary" class="el-icon-edit msg-btn" plain>发布留言</el-button>
        <div v-if="dataList.length>0" class="comment-list">
          <div class="comment-info" v-for="item in dataList">
            <header><el-image class="msg-header pointer" :src="formatUrl(item.photo)"/></header>
            <div class="comment-right">
              <h3>{{item.loginName}}</h3>
              <div class="content" v-html="item.content">
              </div>
              <div class="comment-content-header">
                <span class="msg-time"><i class="el-icon-time"></i> {{item.createDate}}</span>
              </div>
              <div class="comment-content-footer">
                <span class="float-l pointer reply-span" @click="reply(item)">回复</span>
                <span class="float-r pointer del-span" v-if="userInfo && (item.userId == userInfo.id)"  @click="del(item)">删除</span>
              </div>
              <div class="clearfix"></div>
              <div class="reply-list">
                <div class="reply" v-for="reply in item.children">
                  <el-image class="reply-header pointer" :src="formatUrl(reply.photo)"/>
                  <span>{{reply.loginName}} : <i style="color: grey">@{{item.loginName}} </i> </span><span style="font-family: cursive">{{reply.content}}</span>
                  <div>
                    <span class="msg-time"><i class="el-icon-time"></i> {{reply.createDate}}</span>
                    <span class="float-r pointer del-span" style="font-size: small" v-if="userInfo && (reply.userId == userInfo.id)"  @click="del(reply)">删除</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <el-empty v-else class="sui-empty" description="还没人发言,赶快抢个沙发吧!"></el-empty>
      </div>
      <div v-if="dataList.length>0" class="pagination" style="text-align: center">
        <el-pagination
          @current-change="handleCurrentChange"
          :page-size="queryForm.size"
          layout="total, prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>
<script>
  export default {
    name: "sui-discuss",
    data() {
      return {
        dataList: [],
        total:0,
        message: {
          type:"讨论",
          pid: "",
          userId: "",
          refId: "",
          content: ""
        },
        queryForm: {
          current: 1,
          size: 5,
          type:"讨论",
          pid: "0",
          userId: "",
          refId: "",
          content: ""
        }
      }
    },
    computed: {
      userInfo() {
        return this.$store.getters.userInfo
      },
      hasLogin() {
        return this.$store.getters.hasLogin;
      }
    },
    mounted() {
      this.focus();
      this.getList();
    },
    methods: {
      focus(){
        this.$nextTick((x) => {//焦点聚焦
          this.$refs.message.focus();
        })
      },
      //查询商品分类数据
      selectFace(face) {
        const that = this;
        if (!this.hasLogin) {
          that.showWarn("请先进行登录");
          that.focus();
          return
        }
        let innerHTML = document.getElementById("message").innerHTML;
        that.message.content = innerHTML + face
      },
      getList() {
        this.$request.doGet('/api/message/getTreePage',this.queryForm).then(res => {
          if (res.isOk) {
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        });
      },
      //选择页码
      handleCurrentChange: function (currentPage) {
        this.queryForm.current = currentPage;
        this.getList();
      },
      //提交表单
      subMsg: function () {
        const that = this;
        if (!this.hasLogin) {
          that.showWarn("请先进行登录");
          that.focus();
          return
        }
        if (document.getElementById("message").innerHTML=='') {
          that.showWarn("内容不能为空");
          that.focus();
          return
        }
        let innerHTML = "<p>"+document.getElementById("message").innerHTML+"</p>";
        that.message.pid = "";
        that.message.content = innerHTML;
        that.$request.doPost('/api/message/sub', that.message).then(res => {
          if (res.isOk) {
            that.message.content = "";
            that.showSuccess("发布成功");
            that.getList();
            that.focus();
          }
        });
      },
      //回复
      reply(item) {
        const that = this;
        if (!that.hasLogin) {
          that.showWarn("请先进行登录");
          that.focus();
          return
        }
        if (item.userId == that.userInfo.id) {
          that.showWarn("自己不能回复自己哦");
          return
        }
        this.$prompt('请输入回复内容', '温馨提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          if(value==null){
            that.showWarn("请输入内容");
            return;
          }
          that.message.type = "回复";
          that.message.pid = item.id;
          that.message.content = value;
          that.$request.doPost('/api/message/sub', that.message).then(res => {
            if (res.isOk) {
              that.message.content ="";
              that.showSuccess("回复成功");
              that.getList();
            }
          });
        })
      },
      //删除评论
      del(item){
        const that = this;
        that.showConfirm("确认删除该条留言吗?", function () {
          that.$request.doPost('/api/message/delete?id='+item.id).then(res => {
            if (res.isOk) {
              that.showSuccess("删除成功");
              that.queryForm.current = 1;
              that.getList();
            };
          });
        })
      }
    }
  }
</script>

<style scoped>
  .content /deep/ p{
    margin: 0!important;
  }
  .count{
    float: left;
    margin-left: 10px;
    color: grey
  }
  .msg-btn {
    float: right;
    margin-right: 160px
  }
  .message-content{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
    background: rgba(76, 135, 188, 0.21);
    margin: 10px auto;
    width: 900px;
    height: 200px;
    font-family: cursive;
    color: black;
    font-weight: bold;
    border: solid 1px #cccccc;
    border-radius: 10px
  }
  .comment-list{
    width: 900px;
    margin: 20px auto;
    clear: both;
    padding-top: 20px;
  }
  .comment-list .comment-info{
    position: relative;
    margin-bottom: 20px;
    border-bottom: 1px solid #ccc;
  }
  .comment-list .comment-info header{
    width: 10%;
    position: absolute;
  }
  .comment-list .comment-info header img{
    width: 100%;
    border-radius: 50%;
    padding: 5px;
  }
  .comment-list .comment-info .comment-right{
    padding:5px 0px 5px 11%;
  }
  .comment-list .comment-info .comment-right h3{
    margin: 0px;
  }
  .comment-list .comment-info .comment-right .comment-content-header span,.comment-list .comment-info .comment-right .comment-content-footer{
    cursor: pointer;
  }
  .comment-list .comment-info .comment-right .reply-list {
    border-left: 3px solid #ccc;
    padding-left: 7px;
  }
  .comment-list .comment-info .comment-right .reply-list .reply{
    padding: 5px;
    border-bottom: 1px dashed #ccc;
  }
  .msg-face {
    float: left;
    margin-left: 185px
  }
  .content{
    font-family: cursive;
    margin: 5px 0;
  }
  .msg-header {
    margin-left: 20px;
    width: 50px;
    height: 50px;
    border-radius: 50%
  }
  .reply-header {
    vertical-align: middle;
    width: 30px;
    height: 30px;
    border-radius: 50%
  }
  .msg-time {
    color: grey;
    font-size: small
  }
  .message {
    margin-top: 10px;
    min-height: 800px
  }
  .msg-title {
    text-align: center;font-family: cursive
  }
  .reply-span {
    margin-bottom: 5px;
    color: #4c87bc!important;
  }
  .del-span {
    padding-right: 8px;
    color: #E65D6E!important;
  }
</style>
