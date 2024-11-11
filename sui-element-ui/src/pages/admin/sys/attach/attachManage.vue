<!-- 【附件信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysAttach-form ref="sysAttachDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="文件名称" prop="fileName">
        <el-input v-model="queryForm.fileName" placeholder="请输入文件名称" size="small" clearable/>
      </el-form-item>
      <el-form-item label="文件模块" prop="fileModule">
        <el-input v-model="queryForm.fileModule" placeholder="请输入文件模块" size="small" clearable/>
      </el-form-item>
      <el-form-item label="关联id" prop="refId">
        <el-input v-model="queryForm.refId" placeholder="请输入关联id" size="small" clearable/>
      </el-form-item>
      <el-form-item label="文件备注" prop="remarks">
        <el-input v-model="queryForm.remarks" placeholder="请输入文件备注" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增附件信息'})" v-hasPermi="['sysAttach:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysAttach:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="文件名称" prop="fileName" show-overflow-tooltip sortable/>
      <el-table-column label="文件大小" prop="fileSize" sortable/>
      <el-table-column label="文件模块" prop="fileModule" sortable/>
      <el-table-column label="上传类型" prop="fileType" sortable/>
      <el-table-column label="文件后缀" prop="suffix" sortable/>
      <el-table-column label="保存路径" prop="savePath" show-overflow-tooltip sortable/>
      <el-table-column label="关联id" prop="refId" show-overflow-tooltip sortable/>
      <el-table-column label="文件备注" prop="remarks" show-overflow-tooltip sortable/>
      <el-table-column label="操作" align="center"  width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看附件信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysAttach:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改附件信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysAttach:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysAttach:delete']">删除</el-button>
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
  import sysAttachForm from './attachForm'
  export default {
    components: {sysAttachForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 10,
          fileName: "",
          fileSize: "",
          fileModule: "",
          fileType: "",
          suffix: "",
          savePath: "",
          refId: "",
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
        this.$request.doGet('/admin/sysAttach/getPage', this.queryForm).then(res => {
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
        this.$refs.sysAttachDialog.open(option);
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
            that.$request.doGet('/admin/sysAttach/delAll', {ids: ids}).then(res => {
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
          that.$request.doGet('/admin/sysAttach/delete?id=' + id).then(res => {
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
