<!-- 【歌手信息】弹窗页面 -->
<template>
  <div class="front-body">
    <el-card class="front-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <el-row>
        <el-col :span="8">
          <el-image v-if="singer.picture" style="width: 100%;height: 300px" :src="formatUrl(singer.picture)"></el-image>
        </el-col>
        <el-col :span="16" style="padding:0 10px;height: 300px;overflow-y: auto">
          <h1 style="text-align: center;margin: 0;" class="font-shadow">{{singer.name}}</h1>
          <p class="font-shadow" v-html="singer.introduction"></p>
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
        <!-- 分页区域 -->
        <div class="pagination">
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :background="true"
            :current-page="queryForm.current"
            :page-sizes="[5,10,20,30,50]"
            :page-size="queryForm.size"
            layout="total, sizes, prev, pager, next, jumper"
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
    name: "singerForm",
    data() {
      return {
        total: 0, //记录数
        dataList: [],
        singerDict: [],
        queryForm: {
          current: 1,
          size: 5,
          singerId: ""
        },
        //表单数据
        singer: {
          picture: "",
          name: "",
          sex: "",
          regionArea: "",
          introduction: "",
        },
        //表单校验
        rules: {
          picture: [{required: true, message: '请选择头像', trigger: 'change'}],
          name: [{required: true, message: '请输入姓名', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'singer',tableName:'singer',column:'name',message:'姓名已存在',trigger:'blur'}],
          sex: [{required: true, message: '请选择性别', trigger: 'change'}],
          regionArea: [{required: true, message: '请选择地区', trigger: 'change'}],
          introduction: [{required: true, message: '请输入简介', trigger: 'blur'}],
        }
      }
    },
    computed: {
      hasLogin() {
        return this.$store.getters.hasLogin;
      },
      //【性别】
      sexDict() {
        return this.$store.getters.getDictArray('sys_sex');
      },
      //【地区】
      regionAreaDict() {
        return this.$store.getters.getDictArray('region_area');
      },
    },
    mounted() {
      let singerId = this.$route.query.id;
      this.queryForm.singerId = singerId;
      //获取歌曲列表
      this.getList()
      //获取歌手信息
      this.getSinger(singerId);
      //初始化数据
      this.listSong();
      //初始化【所属歌手】
      this.getSingerDict();

    },
    methods: {
      ...mapActions(['commitPlayList']),
      //根据id获取数据
      getSinger(id) {
        this.$request.doGet("/api/singer/get?id=" + id).then(res => {
          if (res.isOk) {
            this.singer = res.obj;
          }
        });
      },
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
        this.toPage({name:'songDetails',query:{id:item.id}})
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
        this.$refs.userEvaluate.listSong();
      },
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        this.getList();
      },
      //查询列表数据
      getList() {
        const that = this;
        this.$request.doGet('/api/song/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.obj.records;
            this.total = res.obj.total;
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
      //选择页数
      handleSizeChange: function (size) {
        this.queryForm.size = size;
        this.getList();
      },
    }
  }
</script>

<style scoped>

</style>
