<!-- 【我的音乐】页面 -->
<template>
  <div class="front-body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-export type="primary" size="mini" :action="'/api/playList/export?userId='+userId" title="导出歌单"/>
        </el-col>
        <el-col :span="1.5">
          <el-import type="primary" size="mini" :action="'/api/playList/import?userId='+userId" title="导入歌单" @success="getList"/>
        </el-col>
      </el-row>
      <el-tabs type="border-card" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="播放歌单" name="playList"/>
        <el-tab-pane label="我的收藏" name="collect"/>
      </el-tabs>
      <div v-if="dataList.length>0">
        <el-table border :header-cell-style="{background:'#e8f4ff'}" :height="720" v-loading="loading" :data="dataList">
          <el-table-column label="id" prop="id" v-if="false"/>
          <el-table-column label="" type="index" fixed width="50"/>
          <el-table-column label="歌曲图片" prop="picture">
            <template slot-scope="scope">
              <el-image :src="formatUrl(scope.row.picture)" class="column-image"></el-image>
            </template>
          </el-table-column>
          <el-table-column label="歌曲名称" prop="songName"/>
          <el-table-column label="歌手" prop="singerName"/>
          <el-table-column label="操作" align="center" width="250" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="success" title="播放" circle icon="el-icon-video-play" @click="play(scope.row)"></el-button>
              <el-button size="mini" type="primary" v-if="activeName=='playList'" title="收藏" @click="collect(scope.row)" circle plain icon="el-icon-folder-add"></el-button>
              <el-button size="mini" type="info" title="下载" @click="downLoad(scope.row)" circle plain icon="el-icon-download"></el-button>
              <el-button size="mini" type="info" v-if="activeName=='playList'" title="删除" @click="del(scope.row)" circle plain icon="el-icon-delete"></el-button>
              <el-button size="mini" type="info" v-if="activeName=='collect'" title="取消收藏" @click="cancel(scope.row)" circle plain icon="el-icon-delete"></el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination" style="text-align: center">
          <el-pagination
            @current-change="handleCurrentChange"
            :page-size="queryForm.size"
            layout="total"
            :total="total">
          </el-pagination>
        </div>
      </div>
      <el-empty v-else class="sui-empty" description="暂无数据"></el-empty>
    </el-card>
  </div>
</template>

<script>
  import {mapActions} from "vuex";

  export default {
    data() {
      return {
        activeName:"playList",
        loading: false,
        total: 0, //记录数
        dataList: [],
        singerDict: [],
        songPlayList: [],
        singer: {},
        queryForm: {
          searchType:"playList",
          current: 1,
          size: 1000,
          songListId: "",
          songId: ""
        }
      };
    },
    computed: {
      hasLogin() {
        return this.$store.getters.hasLogin;
      },
      userId(){
        return this.$store.getters.userInfo.id;
      }
    },
    mounted() {
      let params = this.$route.query;
      this.queryForm.singerId = params.singerId;
      //初始化数据
      this.getPage();
      //初始化【所属歌手】
      this.getSingerDict();
      //查询歌手基础信息
      this.getSinger()
    },
    methods: {
      ...mapActions(['commitPlayList']),
      handleClick(tab, event) {
        this.queryForm.searchType = tab.name;
        if(this.queryForm.searchType=='playList'){
          this.getPage()
        }else{
          this.getList()
        }
      },
      //查询播放歌单
      getPage() {
        const that = this;
        that.$request.doGet('/api/playList/getPage',this.queryForm).then(res => {
          if (res.isOk) {
            that.loading = false;
            that.dataList = res.obj.records;
            that.total = res.obj.total;
          }
        });
      },
      //查询收藏歌单
      getList() {
        const that = this;
        that.$request.doGet('/api/playList/getList',this.queryForm).then(res => {
          if (res.isOk) {
            that.loading = false;
            that.dataList = res.list;
            that.total = res.list.length;
          }
        });
      },
      //歌曲收藏
      collect(item) {
        this.$request.doPost('/api/message/subCollect', {type: "收藏", content: item.songName, refId: item.songId}).then(res => {
          if (res.isOk) {
            this.showSuccess(res.info);
          }
        });
      },
      //删除歌曲
      del(item) {
        const that = this;
        that.showConfirm("确认删除歌曲吗?", function () {
          that.$request.doPost('/api/playList/delete', {id:item.id}).then(res => {
            if (res.isOk) {
              that.getPage();
              that.commitPlayList();
              that.showSuccess("删除成功！");
            }
          });
        })
      },
      //删除歌曲
      cancel(item) {
        const that = this;
        that.showConfirm("确认取消收藏歌曲吗?", function () {
          that.$request.doPost('/api/message/cancel', {refId:item.songId}).then(res => {
            if (res.isOk) {
              that.getList();
              that.showSuccess("取消成功");
            }
          })
        })
      },
      downLoad(file) {
        // 生成一个 a 标签
        const a = document.createElement('a');
        // 创建一个点击事件
        const event = new MouseEvent('click');
        // 将 a 的 download 属性设置为我们想要下载的图片的名称，若 name 不存在则使用'图片'作为默认名称
        a.download = file.name || '文件';
        // 将生成的 URL 设置为 a.href 属性
        a.href = "/api/sysAttach/downFile?refId=" + file.songId;
        // 触发 a 的点击事件
        a.dispatchEvent(event);
        const loading = this.$loading({
          lock: false,
          text: '正在下载,请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(232,244,255,0.25)'
        });
        setTimeout(function () {
          loading.close();
        }, 1500)
      },
      success() {
        this.$refs.userEvaluate.getList();
      },
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        this.getList();
      },
      //播放音乐
      play(item){
        if(!this.hasLogin){
          this.showWarn("请先进行登录");
          return;
        }
        const that = this;
        that.$request.doPost('/api/playList/sub', {songId: item.songId}).then(res => {
          if (res.isOk) {
            that.commitPlayList();
            that.toPage({name:'songDetails',query:{id:item.songId}})
          };
        });
      },
      //查询歌手基础信息
      getSinger() {
        this.$request.doGet('/api/singer/get?id=' + this.queryForm.singerId).then(res => {
          if (res.isOk) {
            this.singer = res.obj;
          }
        });
      },
      //【所属歌手】
      getSingerDict() {
        this.$request.doGet('/api/singer/getList').then(res => {
          if (res.isOk) {
            this.singerDict = res.list;
          }
        });
      },
      //选择页码
      handleCurrentChange: function (currentPage) {
        this.queryForm.current = currentPage;
        this.getList();
      },
    }
  };
</script>
<style scoped>
  .box-card {
    min-height: 900px
  }

  .aplayer-list {
    height: 300px !important;
  }

  /deep/ .el-tabs--border-card > .el-tabs__content {
    padding: 0px;
  }

  /deep/ .el-tabs__nav .el-tabs__item:nth-child(1) span {
    display: none;
  }

  /deep/ .menu-tabs .el-tabs__item {
    border: solid 1px #dfe6ec !important;
  }
</style>
