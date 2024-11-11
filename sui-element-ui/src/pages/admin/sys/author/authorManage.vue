<!-- 【授权信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysAuthor-form ref="sysAuthorDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="昵称" prop="nickName">
        <el-input v-model="queryForm.nickName" placeholder="请输入昵称" size="small" clearable/>
      </el-form-item>
      <el-form-item label="用户标识" prop="openid">
        <el-input v-model="queryForm.openid" placeholder="请输入用户标识" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysAuthor:delete']">批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-export type="primary" size="mini" action="/admin/sysAuthor/export" title="导出数据" v-hasPermi="['sysAuthor:export']"/>
      </el-col>
      <el-col :span="1.5">
        <el-import type="primary" size="mini" action="/admin/sysAuthor/import" title="导入数据" v-hasPermi="['sysAuthor:import']"/>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="头像" prop="avatarUrl" sortable>
        <template slot-scope="scope">
          <sui-image :src="scope.row.avatarUrl"></sui-image>
        </template>
      </el-table-column>
      <el-table-column label="昵称" prop="nickName" sortable/>
      <el-table-column label="省份" prop="province" sortable/>
      <el-table-column label="城市" prop="city" sortable/>
<!--      <el-table-column label="用户标识" prop="openid" sortable/>-->
<!--      <el-table-column label="唯一标识" prop="unionid" sortable/>-->
      <el-table-column label="操作" align="center"  width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看授权信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysAuthor:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改授权信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysAuthor:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysAuthor:delete']">删除</el-button>
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
  import sysAuthorForm from './authorForm'
    export default {
      components: {sysAuthorForm},
      data() {
        return {
          loading: true,//是否加载完成
          selectedData: [],//勾选数据
          total: 0, //记录数
          dataList: [],
          queryForm: {
            current: 1,
            size: 10,
            avatarUrl: "",
            nickName: "",
            country: "",
            province: "",
            city: "",
            openid: "",
            unionid: ""
          }
        };
      },
      mounted() {
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
          this.$request.doGet('/admin/sysAuthor/getPage', this.queryForm).then(res => {
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
          this.$refs.sysAuthorDialog.open(option);
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
              that.$request.doGet('/admin/sysAuthor/delAll', {ids: ids}).then(res => {
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
            that.delete(id);
          });
        },
        //选中行删除
        handleDelete(id) {
          const that = this;
          that.showConfirm('确定删除选中的数据吗?', function () {
            that.$request.doGet('/admin/sysAuthor/delete?id=' + id).then(res => {
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
