<!-- 【歌单信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <songSheet-form ref="songSheetDialog" @reloadList="getList"/>
    <listSong-form ref="listSongDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="90px">
      <el-form-item label="歌单标题" prop="title">
        <el-input v-model="queryForm.title" placeholder="请输入歌单标题" size="small" clearable/>
      </el-form-item>
      <el-form-item label="歌单分类" prop="categoryId">
        <el-select v-model="queryForm.categoryId" placeholder="请选择歌单分类" size="small" filterable clearable>
          <el-option v-for="category in categoryDict" :key="category.id" :value="category.id" :label="category.name"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增歌单信息'})" v-hasPermi="['songSheet:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['songSheet:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="歌单图片" prop="picture" sortable>
        <template slot-scope="scope">
          <sui-image :src="scope.row.picture"></sui-image>
        </template>
      </el-table-column>
      <el-table-column label="歌单标题" prop="title" sortable/>
      <el-table-column label="歌单分类" prop="categoryId" sortable>
        <template slot-scope="scope">
          <span v-for="category in categoryDict" v-if="category.id==scope.row.categoryId">{{category.name}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="400" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看歌单信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['songSheet:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改歌单信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['songSheet:edit']">修改</el-button>
          <el-button size="mini" type="success" plain icon="el-icon-edit" @click="openSongDialog({title:'歌曲管理',pageFrom:'edit',songSheetId:scope.row.id})" v-hasPermi="['songSheet:edit']">歌曲管理</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)" v-hasPermi="['songSheet:delete']">删除</el-button>
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
</template>

<script>
  import songSheetForm from './songSheetForm'
  import listSongForm from './listSongForm'
  export default {
    components: {songSheetForm,listSongForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        categoryDict: [],
        dataList: [],
        queryForm: {
          current: 1,
          size: 10,
          picture: "",
          title: "",
          categoryId: "",
          introduction: "",
        }
      };
    },
    mounted() {
      //初始化数据
      this.getList();
      //初始化【歌单分类】
      this.getCategoryDict();
    },
    computed: {
    },
    methods: {
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        this.getList();
      },
      //查询列表数据
      getList() {
        this.$request.doGet('/admin/songSheet/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        });
      },
      //【歌单分类】
      getCategoryDict() {
        this.$request.doGet('/admin/category/getList').then(res => {
          if (res.isOk) {
            this.categoryDict = res.list;
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
        this.$refs.songSheetDialog.open(option);
      },
      //打开弹窗
      openSongDialog(option) {
        this.$refs.listSongDialog.open(option);
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
            that.$request.doGet('/admin/songSheet/delAll', {ids: ids}).then(res => {
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
          that.$request.doGet('/admin/songSheet/delete?id=' + id).then(res => {
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
