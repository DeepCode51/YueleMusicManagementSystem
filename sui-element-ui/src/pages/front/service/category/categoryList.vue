<!-- 【分类列表】页面 -->
<template>
  <div class="front-body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <div v-if="dataList.length>0">
        <sui-card v-for="item in dataList" :key="item.id"
                  @click.native="toPage({name:'listSongList',query:{songSheetId:item.id}})"
                  :title="item.title"
                  :imgsrc="item.picture"
                  :content="item.introduction"
                  :date="item.createDate">
        </sui-card>
      </div>
      <el-empty v-else class="sui-empty" description="暂无数据"></el-empty>
    </el-card>
    <div class="pagination" style="text-align: center">
      <el-pagination
        @current-change="handleCurrentChange"
        :page-size="queryForm.size"
        layout="total, prev, pager, next"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
    export default {
      data() {
        return {
          total: 0, //记录数
          dataList: [],
          queryForm: {
            current: 1,
            size: 8,
            id: "",
            picture: "",
            title: "",
            name: "",
            categoryId: "",
            content: ""
          }
        };
      },
      mounted() {
        let params = this.$route.query;
        this.queryForm.categoryId = params.id;
        //初始化数据
        this.getList();
      },
      methods: {
        //点击搜索
        handleQuery() {
          this.queryForm.current = 1;
          this.getList();
        },
        //查询列表数据
        getList() {
          this.$request.doGet('/api/songList/getPage', this.queryForm).then(res => {
            if (res.isOk) {
              this.loading = false;
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
      }
    };
</script>
<style scoped>
  .box-card {
    min-height: 800px
  }
</style>
