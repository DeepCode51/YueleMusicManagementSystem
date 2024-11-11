<!-- 【评价|建议】列表 -->
<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane :label="'全部('+dataList.length+')'" name="全部">
      <div v-if="dataList.length>0" style="min-height: 500px">
         <span v-for="item in dataList">
          <el-card class="box-card div-shadow">
            <el-button v-if="item.userId==userId" @click="delClick(item.id)" class="float-r" title="删除" size="mini" icon="el-icon-delete" circle></el-button>
            <el-button v-if="type!='评价'&&item.userId!=userId" @click="replyClick(item)" class="float-r" title="回复" size="mini" icon="el-icon-edit" circle></el-button>
            <el-avatar style="vertical-align: middle" :src="formatUrl(item.photo)"></el-avatar>
              <span style="color: grey;">{{item.loginName}}</span>
              <span style="color: grey;font-size: small"><i class="el-icon-time"></i>{{item.createDate}}</span>
            <div style="font-family: cursive;padding: 5px">{{item.content}}</div>
            <div v-if="item.children.length>0" v-for="reply in item.children" style="font-family: cursive;padding: 5px"><span style="color: #4c87bc">回复:</span>
              <span class="reply" >{{reply.content}}</span>
              <span v-if="type!='评价'&&reply.userId==userId" class="float-r el-icon-delete del-reply" @click="delClick(reply.id)"></span>
              <span class="float-r" style="color: grey; font-size: small;">{{reply.createDate}}</span>
            </div>
          </el-card>
        </span>
      </div>
      <el-empty v-else description="暂无评价数据"></el-empty>
      <div class="pagination" style="text-align: center">
        <el-pagination
          @current-change="handleCurrentChange"
          :page-size="queryForm.size"
          layout="total, prev, pager, next"
          :total="total">
        </el-pagination>
      </div>
    </el-tab-pane>
    <el-tab-pane v-if="type=='评价'" :label="'好评('+dataList1.length+')'" name="好评">
      <div v-if="dataList1.length>0" style="min-height: 500px">
        <span v-for="item in dataList1">
        <el-card class="box-card div-shadow">
          <el-button v-if="item.userId==userId" @click="delClick(item.id)" class="float-r" size="mini" icon="el-icon-delete" circle></el-button>
          <el-avatar style="vertical-align: middle" :src="formatUrl(item.photo)"></el-avatar>
            <span style="color: grey;">{{item.loginName}}</span>
            <span style="color: grey;font-size: small"><i class="el-icon-time"></i>{{item.createDate}}</span>
          <div style="font-family: cursive;padding: 5px">{{item.content}}</div>
          <div v-if="item.children.length>0" v-for="reply in item.children" style="font-family: cursive;padding: 5px"><span style="color: #4c87bc">回复:</span>
            <span class="reply" >{{reply.content}}</span>
            <span style="float: right;color: grey; font-size: small;">{{reply.createDate}}</span>
          </div>
        </el-card>
      </span>
      </div>
      <el-empty v-else description="暂无好评数据"></el-empty>
    </el-tab-pane>
    <el-tab-pane v-if="type=='评价'" :label="'中评('+dataList2.length+')'" name="中评">
      <div v-if="dataList2.length>0" style="min-height: 500px">
        <span v-for="item in dataList2">
        <el-card class="box-card div-shadow">
          <el-button v-if="item.userId==userId" @click="delClick(item.id)" class="float-r" size="mini" icon="el-icon-delete" circle></el-button>
          <el-avatar style="vertical-align: middle" :src="formatUrl(item.photo)"></el-avatar>
            <span style="color: grey;">{{item.loginName}}</span>
            <span style="color: grey;font-size: small"><i class="el-icon-time"></i>{{item.createDate}}</span>
          <div style="font-family: cursive;padding: 5px">{{item.content}}</div>
          <div v-if="item.children.length>0" v-for="reply in item.children" style="font-family: cursive;padding: 5px"><span style="color: #4c87bc">回复:</span>
            <span class="reply" >{{reply.content}}</span>
            <span style="float: right;color: grey; font-size: small;">{{reply.createDate}}</span>
          </div>
        </el-card>
      </span>
      </div>
      <el-empty v-else description="暂无中评数据"></el-empty>
    </el-tab-pane>
    <el-tab-pane v-if="type=='评价'" :label="'差评('+dataList3.length+')'" name="差评">
      <div v-if="dataList3.length>0" style="min-height: 500px">
        <span v-for="item in dataList3">
        <el-card class="box-card div-shadow">
          <el-button v-if="item.userId==userId" @click="delClick(item.id)" class="float-r" size="mini" icon="el-icon-delete" circle></el-button>
          <el-avatar style="vertical-align: middle" :src="formatUrl(item.photo)"></el-avatar>
            <span style="color: grey;">{{item.loginName}}</span>
            <span style="color: grey;font-size: small"><i class="el-icon-time"></i>{{item.createDate}}</span>
          <div style="font-family: cursive;padding: 5px">{{item.content}}</div>
          <div v-if="item.children.length>0" v-for="reply in item.children" style="font-family: cursive;padding: 5px"><span style="color: #4c87bc">回复:</span>
            <span class="reply" >{{reply.content}}</span>
            <span style="float: right;color: grey; font-size: small;">{{reply.createDate}}</span>
          </div>
        </el-card>
      </span>
      </div>
      <el-empty v-else description="暂无差评数据"></el-empty>
    </el-tab-pane>
  </el-tabs>
</template>
<script>
  export default {
    name:"user-evaluate",
    props: {
      refId: {
        type: String,
        default: ''
      },
      type: {
        type: String,
        default: ''
      },
    },
    data(){
      return{
        message:{
          pid:"",
          content:"",
          type:""
        },
        dataList:[],
        activeName: '全部',
        icon:"el-icon-star-on",
        flag:false,
        total:0,
        queryForm: {
          current: 1,
          size: 5,
          type:"",
          pid: "",
          userId: "",
          refId: "",
          content: ""
        }
      }
    },
    computed:{
      userId(){
        return this.$store.getters.userInfo==null?"":this.$store.getters.userInfo.id;
      },
      dataList1(){//好评
       return  this.dataList.clone().filter(function (v) {
          return v.stars>3
        });
      },
      dataList2(){//一般
        return this.dataList.filter(function (v) {
          return v.stars==3
        })
      },
      dataList3(){//差评
        return this.dataList.filter(function (v) {
          return v.stars<3
        })
      }
    },
    mounted() {
      this.getList()
    },
    methods: {
      replyClick(item) {
        const that = this;
        this.$prompt('请输入回复内容', '温馨提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          if (value == null) {
            that.showWarn("请输入内容");
            return;
          }
          that.message.type = "回复";
          that.message.pid = item.id;
          that.message.refId = that.refId;
          that.message.content = value;
          that.$request.doPost('/api/message/sub', that.message).then(res => {
            if (res.isOk) {
              that.message.content = "";
              that.showSuccess("回复成功");
              that.getList();
            }
          });
        })
      },
      delClick(id) {
        const that = this;
        that.showConfirm("确认删除吗?", function () {
          that.$request.doPost('/api/message/delete?id='+id).then(res => {
            if (res.isOk) {
              that.showSuccess("删除成功");
              that.getList();
            };
          });
        })
      },
      handleClick(tab, event) {
        console.log(tab, event);
      },
      //查询列表数据
      getList() {
        const that = this;
        if(that.type==''){
          that.showWarn("请设置消息类型");
          return
        }
        that.queryForm.type = this.type;
        that.queryForm.refId = this.refId;
        that.$request.doGet('/api/message/getTreePage',that.queryForm).then(res => {
          if (res.isOk) {
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        })
      },
      //选择页码
      handleCurrentChange: function (currentPage) {
        this.queryForm.current = currentPage;
        this.getList(this.queryForm);
      },
    }
  }
</script>
<style scoped>
  .del-reply {
    color: grey;
    font-size: small;
    margin-left: 5px
  }
  .del-reply:hover {
    color: #88B8FF;
  }
</style>
