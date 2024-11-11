<!-- 【通知信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <notice-form ref="noticeDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
      <el-form-item label="消息标题" prop="title">
        <el-input v-model="queryForm.title" placeholder="请输入消息标题" size="small" clearable/>
      </el-form-item>
      <el-form-item label="通知类型" prop="noticeTypeRange">
        <el-select v-model="queryForm.noticeTypeRange" placeholder="请选择通知类型" size="small" multiple collapse-tags filterable clearable>
          <el-option v-for="dict in noticeTypeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="发布时间" prop="publishTimeRange">
        <el-date-picker v-model="queryForm.publishTimeRange" type="daterange" value-format="yyyy-MM-dd" size="small" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增通知信息'})" v-hasPermi="['notice:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['notice:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="标题图片" prop="picture" sortable>
        <template slot-scope="scope">
          <sui-image :src="scope.row.picture"></sui-image>
        </template>
      </el-table-column>
      <el-table-column label="消息标题" prop="title" sortable/>
      <el-table-column label="通知类型" prop="noticeType" sortable>
        <template slot-scope="scope">
          <span :style="dict.style" v-for="dict in noticeTypeDict" v-if="dict.dictValue==scope.row.noticeType">{{dict.dictLabel}}</span>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" prop="publishTime" sortable/>
      <el-table-column label="操作" align="center" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看通知信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['notice:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改通知信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['notice:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)" v-hasPermi="['notice:delete']">删除</el-button>
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
  import noticeForm from './noticeForm'
    export default {
      components: {noticeForm},
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
            title: "",
            noticeTypeRange: [],
            noticeType: "",
            publishTimeRange: [],
            publishTime: "",
            content: ""
          }
        };
      },
      mounted() {
        //初始化数据
        this.getList();
      },
      computed: {
        //【通知类型】
        noticeTypeDict() {
          return this.$store.getters.getDictArray('notice_type');
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
          this.$request.doGet('/admin/notice/getPage', this.queryForm).then(res => {
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
          this.$refs.noticeDialog.open(option);
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
              that.$request.doGet('/admin/notice/delAll', {ids: ids}).then(res => {
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
            that.$request.doGet('/admin/notice/delete?id=' + id).then(res => {
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
