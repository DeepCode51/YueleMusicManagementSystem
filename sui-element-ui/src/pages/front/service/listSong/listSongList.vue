<!-- 【歌单歌曲】页面 -->
<template>
  <div class="front-body">
    <el-card>
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <el-row>
        <el-col :span="8">
          <el-image v-if="songList.picture" style="width: 100%;height: 300px" :src="formatUrl(songList.picture)"></el-image>
        </el-col>
        <el-col :span="16" style="padding:0 10px;height: 300px;overflow-y: auto">
          <h1 style="text-align: center;margin: 0" class="font-shadow">{{songList.title}}</h1>
          <p class="font-shadow" v-html="songList.introduction"></p>
        </el-col>
      </el-row>
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
    <!-- 准备一个容器用来存放音乐播放器 -->
    <div id="aplayer"></div>
    <!--【评价|评论】按钮-->
    <sui-evaluate v-if="queryForm.songSheetId" style="text-align: center;padding-top: 10px" :refId="queryForm.songSheetId" @success="success" type="评论"/>
    <!--【评价|评论】列表-->
    <user-evaluate style="margin:0 10px" v-if="queryForm.songSheetId" ref="userEvaluate" :refId="queryForm.songSheetId" type="评论"></user-evaluate>
  </div>
</template>

<script>
  import {mapActions} from "vuex";
  export default {
    data() {
      return {
        info: {
          fixed: false, // 不开启吸底模式
          listFolded: true, // 折叠歌曲列表
          autoplay: true, // 开启自动播放
          preload: "auto", // 自动预加载歌曲
          loop: "all", // 播放循环模式、all全部循环 one单曲循环 none只播放一次
          order: "list", //  播放模式，list列表播放, random随机播放
        },
        total: 0, //记录数
        dataList: [],
        singerDict: [],
        songPlayList: [],
        songList: {},
        queryForm: {
          current: 1,
          size: 100,
          songSheetId: "",
          songId: ""
        }
      };
    },
    mounted() {
      let params = this.$route.query;
      this.queryForm.songSheetId = params.songSheetId;
      //初始化数据
      this.getList();
      //初始化【所属歌手】
      this.getSingerDict();
      //查询歌单基础信息
      this.getSongList()
      this.scrollTop();
    },
    computed: {
      hasLogin() {
        return this.$store.getters.hasLogin;
      },
    },
    methods: {
      ...mapActions(['commitPlayList']),
      //歌曲收藏
      collect(item) {
        if(!this.hasLogin){
          this.showWarn("请先进行登录");
          return;
        }
        this.$request.doPost('/api/message/subCollect', {type: "收藏", content: item.name, refId: item.id}).then(res => {
          if (res.isOk) {
            this.showSuccess(res.info);
          }
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
      downLoad(file) {
        if(!this.hasLogin){
          this.showWarn("请先进行登录");
          return;
        }
        // 生成一个 a 标签
        const a = document.createElement('a');
        // 创建一个点击事件
        const event = new MouseEvent('click');
        // 将 a 的 download 属性设置为我们想要下载的图片的名称，若 name 不存在则使用'图片'作为默认名称
        a.download = file.name || '文件';
        // 将生成的 URL 设置为 a.href 属性
        a.href = "/api/sysAttach/downFile?refId=" + file.id;
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
      //查询列表数据
      getList() {
        const that = this;
        this.$request.doGet('/api/listSong/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.list;
            // this.$store.commit('PLAY_LIST', this.dataList);
            this.total = res.list.length;
          }
        });
      },
      //查询歌单基础信息
      getSongList() {
        this.$request.doGet('/api/songList/get?id=' + this.queryForm.songSheetId).then(res => {
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
      //选择页码
      handleCurrentChange: function (currentPage) {
        this.queryForm.current = currentPage;
        this.getList();
      },
    }
  };
</script>
