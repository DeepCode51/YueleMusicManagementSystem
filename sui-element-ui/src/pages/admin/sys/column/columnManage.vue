<!-- 【字段信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysColumn-form ref="sysColumnDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="表名称" prop="tableName">
        <el-input v-model="queryForm.tableName" placeholder="请输入表名称" size="small" clearable/>
      </el-form-item>
      <el-form-item label="表描述" prop="tableDesc">
        <el-input v-model="queryForm.tableDesc" placeholder="请输入表描述" size="small" clearable/>
      </el-form-item>
      <el-form-item label="列名称" prop="columnName">
        <el-input v-model="queryForm.columnName" placeholder="请输入列名称" size="small" clearable/>
      </el-form-item>
      <el-form-item label="列描述" prop="columnDesc">
        <el-input v-model="queryForm.columnDesc" placeholder="请输入列描述" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增字段信息'})" v-hasPermi="['sysColumn:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysColumn:delete']">批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-export type="primary" size="mini" action="/admin/sysColumn/export" title="导出数据" v-hasPermi="['sysColumn:export']"/>
      </el-col>
      <el-col :span="1.5">
        <el-import type="primary" size="mini" action="/admin/sysColumn/import" title="导入数据" v-hasPermi="['sysColumn:import']"/>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="表名称" prop="tableName" sortable/>
      <el-table-column label="表描述" prop="tableDesc" sortable/>
      <el-table-column label="列名称" prop="columnName" sortable/>
      <el-table-column label="列描述" prop="columnDesc" sortable/>
      <el-table-column label="控件类型" prop="inputType" sortable/>
      <el-table-column label="查询类型" prop="queryType" sortable/>
      <el-table-column label="字典类型" prop="dictType" sortable/>
      <el-table-column label="操作" align="center"  width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看字段信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysColumn:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改字段信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysColumn:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysColumn:delete']">删除</el-button>
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
  import sysColumnForm from './columnForm'
    export default {
      components: {sysColumnForm},
      data() {
        return {
          loading: true,//是否加载完成
          selectedData: [],//勾选数据
          total: 0, //记录数
          dataList: [],
          queryForm: {
            current: 1,
            size: 10,
            tableName: "",
            tableDesc: "",
            columnName: "",
            columnDesc: "",
            inputType: "",
            queryType: "",
            dictType: "",
            associateType: "",
            isRequired: "",
            isUnique: "",
            isPk: "",
            isShow: "",
            sort: "",
            remarks: ""
          }
        };
      },
      mounted() {
        //初始化数据
        this.getList();
      },
      computed: {
        //【是否必填】
        isRequiredDict() {
          return this.$store.getters.getDictArray('sys_yes_no');
        },
        //【是否唯一】
        isUniqueDict() {
          return this.$store.getters.getDictArray('sys_yes_no');
        },
        //【是否主键】
        isPkDict() {
          return this.$store.getters.getDictArray('sys_yes_no');
        },
        //【是否显示】
        isShowDict() {
          return this.$store.getters.getDictArray('sys_yes_no');
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
          this.$request.doGet('/admin/sysColumn/getPage', this.queryForm).then(res => {
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
          this.$refs.sysColumnDialog.open(option);
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
              that.$request.doGet('/admin/sysColumn/delAll', {ids: ids}).then(res => {
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
            that.$request.doGet('/admin/sysColumn/delete?id=' + id).then(res => {
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
