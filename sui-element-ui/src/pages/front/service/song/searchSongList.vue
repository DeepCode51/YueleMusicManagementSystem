<!-- 【歌曲搜索结果】页面 -->
<template>
  <div class="front-page">
    <home-header></home-header>
    <div class="front-body">
      <div >
        <el-card style="height: auto">
          <div slot="header" class="clearfix">
            <el-breadcrumb separator-class="el-icon-arrow-right">
              <el-breadcrumb-item :to="{ path: '/front' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>搜索结果</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <!--歌单信息-->
          <div v-if="dataList.length>0">
            <!-- 表格区域 -->
            <el-table border :header-cell-style="{background:'#e8f4ff'}" v-loading="loading" :data="dataList">
              <el-table-column label="id" prop="id" v-if="false"/>
              <el-table-column label="" type="index" fixed width="50"/>
              <el-table-column label="歌曲图片" prop="picture">
                <template slot-scope="scope">
                  <el-image :src="formatUrl(scope.row.picture)" class="column-image"></el-image>
                </template>
              </el-table-column>
              <el-table-column label="歌曲标题" prop="name"/>
              <el-table-column label="歌手" prop="singerId">
                <template slot-scope="scope">
                  <span v-for="singer in singerDict" v-if="singer.id==scope.row.singerId">{{singer.name}}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" width="250" fixed="right">
                <template slot-scope="scope">
                  <el-button size="mini" type="success" title="播放" circle icon="el-icon-video-play" @click="play(scope.row)"></el-button>
                  <el-button size="mini" type="primary" title="添加列表" @click="addPlay(scope.row)" circle plain icon="el-icon-plus"></el-button>
                  <el-button size="mini" type="primary" title="收藏" @click="collect(scope.row)" circle plain icon="el-icon-folder-add"></el-button>
                  <el-button size="mini" type="info" title="下载" @click="downLoad(scope.row)" circle plain icon="el-icon-download"></el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <el-empty v-else class="sui-empty" description="暂无数据"></el-empty>
        </el-card>
      </div>
    </div>
    <home-footer></home-footer>
  </div>

</template>

<script>
  import {mapActions} from "vuex";

  export default {
    data() {
      return {
        total: 0, //记录数
        dataList: [],
        singerDict: [],
        songPlayList:[],
        songList:{},
        queryForm: {
          current: 1,
          size: 100,
          name:""
        }
      };
    },
    mounted() {
      let params = this.$route.query;
      this.queryForm.name = params.name;
      //初始化数据
      this.getList();
      //初始化【所属歌手】
      this.getSingerDict();
      //查询歌单基础信息
      this.getSongList()
    },
    computed: {
      hasLogin() {
        return this.$store.getters.hasLogin;
      },
    },
    methods: {
      ...mapActions(['commitPlayList']),
      //添加播放列表
      addPlay(item) {
        if(!this.hasLogin){
          this.showWarn("请先进行登录");
          return;
        }
        const that = this;
        that.$request.doPost('/api/playList/sub', {songId: item.id}).then(res => {
          if (res.isOk) {
            that.commitPlayList();
            that.showSuccess("添加歌单成功");
          };
        });
      },
      //播放音乐
      play(item){
        if(!this.hasLogin){
          this.showWarn("请先进行登录");
          return;
        }
        const that = this;
        that.$request.doPost('/api/playList/sub', {songId: item.id}).then(res => {
          if (res.isOk) {
            that.commitPlayList();
            that.toPage({name:'songDetails',query:{id:item.id}})
          };
        });
      },
      //歌曲收藏
      collect(item){
        this.$request.doPost('/api/message/subCollect',{type:"收藏",content:item.name,refId:item.id}).then(res => {
          if (res.isOk) {
            this.showSuccess(res.info);
          }
        });
      },
      downLoad(file) {
        // 生成一个 a 标签
        const a = document.createElement('a');
        // 创建一个点击事件
        const event = new MouseEvent('click');
        // 将 a 的 download 属性设置为我们想要下载的图片的名称，若 name 不存在则使用'图片'作为默认名称
        a.download = file.name || '文件';
        // 将生成的 URL 设置为 a.href 属性
        a.href = "/api/sysAttach/downFile?refId="+file.id;
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
        },1500)
      },
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        this.getList();
      },
      //查询列表数据
      getList() {
        const that = this;
        this.$request.doGet('/api/song/getList', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.list;
            this.total = res.list.length;
          }
        });
      },
      //查询歌单基础信息
      getSongList() {
        this.$request.doGet('/api/songList/get?id='+this.queryForm.songListId).then(res => {
          if (res.isOk) {
            this.songList = res.obj;
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
    }
  };
</script>
<style scoped>
  .box-card {
    min-height: 800px
  }
</style>
