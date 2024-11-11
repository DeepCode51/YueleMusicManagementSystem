<!-- 【歌手信息】管理页面 -->
<template>
  <div class="front-body">
    <el-card class="api-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <el-card v-for="regionArea in regionAreaDict" :key="regionArea.id" class="box-card">
        <div slot="header" class="clearfix">
          {{regionArea.dictLabel}}
        </div>
        <div v-if="dataList.length>0" class="body">
          <sui-card v-for="item in dataList.filter(function(v) {return v.regionArea ==regionArea.dictValue})" :key="item.id"
                    @click.native="toPage({name:'singerDetails',query:{id:item.id}})"
                    :title="item.name"
                    :imgsrc="item.picture"
                    :content="item.introduction"
                    :date="item.createDate">
          </sui-card>
        </div>
        <el-empty v-else class="sui-empty" description="暂无数据"></el-empty>
      </el-card>
    </el-card>
  </div>
</template>>
<script>
  import singerForm from './singerDetails'
    export default {
      components: {singerForm},
      data() {
        return {
          loading: true,//是否加载完成
          selectedData: [],//勾选数据
          total: 0, //记录数
          dataList: [],
          queryForm: {
            current: 1,
            size: 10,
            picture: "",
            name: "",
            sexRange: [],
            sex: "",
            regionAreaRange: [],
            regionArea: "",
            introduction: "",
          }
        };
      },
      mounted() {
        //初始化数据
        this.getList();
      },
      computed: {
        //【性别】
        sexDict() {
          return this.$store.getters.getDictArray('sys_sex');
        },
        //【地区】
        regionAreaDict() {
          return this.$store.getters.getDictArray('region_area');
        },
      },
      methods: {
        //点击搜索
        handleQuery() {
          this.queryForm.current = 1;
          this.getList();
        },
        //查询列表数据
        getList() {
          this.$request.doGet('/api/singer/getPage', this.queryForm).then(res => {
            if (res.isOk) {
              this.loading = false;
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
        //打开弹窗
        openDialog(option) {
          this.$refs.singerDialog.open(option);
        },
        //选择页数
        handleSizeChange: function (size) {
          this.queryForm.size = size;
          this.getList();
        },
        //选择页码
        handleCurrentChange: function (currentPage) {
          this.queryForm.current = currentPage;
          this.getList();
        },
        //获取勾选数据
        handleSelectionChange(data) {
          this.selectedData = data;
        },
        //批量删除
        handleDeleteAll() {
          const that = this;
          that.showConfirm('确定删除选中的数据吗?', function () {
            if (that.selectedData.length > 0) {
              let ids = that.selectedData.map(function (v) {return v.id});
              that.$request.doGet('/api/singer/delAll', {ids: ids}).then(res => {
                if (res.isOk) {
                  that.showSuccess("删除成功");
                  that.getList();
                }
              });
            } else {
              that.showWarn("至少选择一条数据！");
            }
          });
        },
        //选中行删除
        handleDelete(id) {
          const that = this;
          that.showConfirm('确定删除选中的数据吗?', function () {
            that.$request.doGet('/api/singer/delete?id=' + id).then(res => {
              if (res.isOk) {
                that.showSuccess("删除成功");
                that.getList();
              }
            });
          });
        }
      }
    };
</script>
