<!-- 【前台首页】 -->
<template>
  <div class="front-body">
    <el-card class="box-card">
    <div slot="header" class="clearfix">
      <sui-breadcrumb></sui-breadcrumb>
    </div>
    <div class="search">
      <el-input class="search-btn" v-model="queryForm.name" clearable placeholder="请输入歌曲名称"/><el-button type="primary" class="el-icon-search" @click="search"> 搜 索</el-button>
      <div class="category">
        <el-link class="category-link" v-for="item in categoryList" @click="toPage({name:'categoryList',query:{id:item.id}})" :key="item.id" type="primary">{{item.name}}</el-link>
      </div>
    </div>
    <!--轮播组件-->
    <sui-wheel :showNotice="false"></sui-wheel>
    <div style="margin: 10px auto;">
      <el-card class="box-card" style="padding-bottom: 15px">
        <div slot="header" class="clearfix">
          <span>最近歌单</span>
        </div>
        <div v-if="dataList.length>0">n
          <sui-info v-for="item in dataList" :key="item.id"
                    @click.native="toPage({name:'listSongList',query:{songSheetId:item.id}})"
                    :title="item.title"
                    :imgsrc="item.picture"
                    :content="item.introduction"
                    :date="item.createDate">
          </sui-info>
        </div>
        <el-empty v-else class="sui-empty" description="暂无数据"></el-empty>
      </el-card>
    </div>
    <div class="pagination" style="text-align: center">
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
    data() {
      return {
        categoryDict: [],
        dataList: [],
        total:0,
        queryForm: {
          current: 1,
          size: 8,
          id: "",
          picture: "",
          title: "",
          name: "",
          categoryId: "",
          content: ""
        },
        categoryList:[],
      }
    },
    computed: {},
    mounted(){
      //初始化数据
      this.getList(this.queryForm);
      //初始化【所属分类】
      this.getCategoryList();
    },
    methods: {
      //查询分类数据
      getCategoryList() {
        this.$request.doGet('/api/category/getList').then(res => {
          if (res.isOk) {
            this.categoryList = res.list;
          }
        })
      },
      //点击搜索
      search(){
        const that = this;
        that.$request.doPost('/api/song/getList', {name: this.queryForm.name}).then(res => {
          if (res.isOk) {
            if (res.list.length==0) {
              that.showWarn("抱歉，没有搜索到相关歌曲。");
            } else {
              this.toPage("/front/searchSongList?name="+this.queryForm.name)
            }
          };
        });
      },
      //查询列表数据
      getList(queryForm) {
        this.$request.doGet('/api/songList/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        });
      },
      //重置查询
      resetQuery() {
        this.$refs['queryForm'].resetFields();
        this.handleQuery();
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
  .search {
    padding-top: 50px;
    border-radius: 5px;
    text-align: center;
    background: white;
    height: 150px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }

  .search-btn {
    width: 500px
  }

  .category {
    padding: 10px
  }

  .category-link {
    padding: 5px 20px
  }
</style>

