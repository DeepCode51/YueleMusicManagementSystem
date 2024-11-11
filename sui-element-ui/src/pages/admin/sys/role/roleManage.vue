<!-- 【角色信息】管理页面 -->
<template>
  <div class="app-container">
    <!-- 表单弹窗 -->
    <sysRole-form ref="sysRoleDialog" @reloadList="getList"/>
    <sysRole-tree ref="sysRoleTreeDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="角色编码" prop="roleCode">
        <el-input v-model="queryForm.roleCode" placeholder="请输入角色编码" size="small" clearable/>
      </el-form-item>
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="queryForm.roleName" placeholder="请输入角色名称" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增角色信息'})" v-hasPermi="['sysRole:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysRole:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id"  prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" width="50"/>
      <el-table-column label="角色编码" prop="roleCode" sortable/>
      <el-table-column label="角色名称" prop="roleName" sortable/>
      <el-table-column label="备注信息" prop="remarks" sortable/>
      <el-table-column label="操作" align="center"  width="450" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看角色信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysRole:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改角色信息',pageFrom:'edit',id:scope.row.id})"v-hasPermi="['sysRole:edit']">修改</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="handleRoleMenu(scope.row)" v-hasPermi="['sysRole:edit']">设置权限</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)" v-hasPermi="['sysRole:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页区域 -->
    <div class="pagination">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :background="true"
        :current-page="queryForm.pageNum"
        :page-sizes="[5,10,20,30,50]"
        :page-size="queryForm.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
  import sysRoleForm from './roleForm'
  import sysRoleTree from "./roleTree";

  export default {
    components: {sysRoleTree, sysRoleForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 10,
          roleCode: "",
          roleName: "",
          remarks: ""
        }
      };
    },
    mounted() {
      //初始化数据
      this.getList();
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
        this.$request.doGet('/admin/sysRole/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        })
      },
      //重置查询
      resetQuery() {
        this.$refs['queryForm'].resetFields();
        this.handleQuery();
      },
      //打开弹窗
      openDialog(option) {
        this.$refs.sysRoleDialog.open(option);
      },
      //权限设置
      handleRoleMenu(row) {
        this.$refs.sysRoleTreeDialog.open({title:'角色权限',id:row.id});
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
      //选中行删除
      handleDelete(id) {
        const that = this;
        that.showConfirm('确定删除选中的数据吗?', function () {
          that.$request.doGet('/admin/sysRole/delete?id=' + id).then(res => {
            if (res.isOk) {
              that.showSuccess("删除成功");
              that.getList();
            }
          });
        });
      },
      //批量删除
      handleDeleteAll() {
        const that = this;
        that.showConfirm('确定删除选中的数据吗?', function () {
          if (that.selectedData.length > 0) {
            let ids = that.selectedData.map(function (v) {return v.id});
            that.$request.doGet('/admin/sysRole/delAll', {ids: ids}).then(res => {
              if (res.isOk) {
                that.showSuccess("删除成功");
                that.getList();
              }
            });
          } else {
            that.showWarn("至少选择一条数据！");
          }
        });
      }
    }
  };
</script>
