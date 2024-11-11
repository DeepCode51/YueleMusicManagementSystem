<!-- 【机构信息】管理页面 -->
<template>
  <div class="body">
    <el-row class="div-shadow" style="border: solid 1px rgba(128,128,128,0.41);height:750px;border-radius: 10px">
      <el-col :span="4">
        <office-tree ref="officeTree" code="root" :total="total" @treeSelected="treeSelected" style="padding: 10px;height: 710px;overflow-y: auto"></office-tree>
      </el-col>
      <el-col :span="20" style="border-left: solid 1px rgba(128,128,128,0.41);height:750px;padding: 20px">
        <!-- 表单弹窗 -->
        <sysOffice-form ref="sysOfficeDialog" @reloadList="getList"/>
        <!-- 查询条件 -->
        <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="100px">
          <el-form-item label="名称" prop="name">
            <el-input v-model="queryForm.name" placeholder="请输入名称" size="small" clearable/>
          </el-form-item>
          <el-form-item label="类型" prop="type">
            <el-select v-model="queryForm.type" placeholder="请选择类型" size="small" clearable>
              <el-option v-for="dict in typeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
          </el-form-item>
        </el-form>
        <!-- 查询按钮 -->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增机构信息'})" v-hasPermi="['sysOffice:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" size="mini" icon="el-icon-sort" @click="toggleRowExpansion">全部{{ isExpansion ? "收缩" : "展开" }}</el-button>
          </el-col>
          <right-toolbar @queryTable="getList"></right-toolbar>
          <!-- 表格区域 -->
          <div style="color: #808080;float: right;position: relative;top: 0">
            <span v-if="selectedName">【{{selectedName}}】</span>
          </div>
        </el-row>
        <el-table border row-key="id" ref="dataTreeList" height="600" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
          <el-table-column label="id"  prop="id" v-if="false"/>
          <el-table-column label="名称" prop="name" sortable/>
          <el-table-column label="类型" prop="type" sortable>
            <template slot-scope="scope">
              <span v-for="dict in typeDict" v-if="dict.dictValue==scope.row.type">{{dict.dictLabel}}</span>
            </template>
          </el-table-column>
          <el-table-column label="编码" prop="code" sortable/>
          <el-table-column label="操作" align="left" width="380" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看机构信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysOffice:view']">查看</el-button>
              <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改机构信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysOffice:edit']">修改</el-button>
              <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'添加下级',pageFrom:'add',id:scope.row.id})" v-hasPermi="['sysOffice:edit']">添加下级</el-button>
              <el-button size="mini" type="danger" plain icon="el-icon-delete" v-if="scope.row.code != 'root'" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysOffice:delete']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import sysOfficeForm from './officeForm'
  import officeTree from './officeTree'
  export default {
    components: {sysOfficeForm,officeTree},
    data() {
      return {
        isExpansion:true,
        selectedName:"",
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 100,
          pid: "0",
          name: "",
          code: "",
          type: "",
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
      //【类型】
      typeDict() {
        return this.$store.getters.getDictArray('office_type');
      },
    },
    methods: {
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        if(this.queryForm.name==''&&this.queryForm.type==''){
          return
        }
        this.getQuery();
      },
      toggleRowExpansion() {
        this.isExpansion = !this.isExpansion;
        this.toggleRowExpansionAll(this.dataList, this.isExpansion);
      },
      toggleRowExpansionAll(data, isExpansion) {
        data.forEach((item) => {
          this.$refs.dataTreeList.toggleRowExpansion(item, isExpansion);
          if (item.children !== undefined && item.children !== null) {
            this.toggleRowExpansionAll(item.children, isExpansion);
          }
        });
      },
      treeSelected(data){
        if(data.children==null){
          return
        }
        this.queryForm.pid=data.id;
        this.selectedName = data.name;
        this.getList();
      },
      //查询列表数据
      getList() {
        this.$refs.officeTree.getOfficeTree();
        this.$request.doGet('/admin/sysOffice/getTreePage', this.queryForm).then(res => {
          if (res.isOk) {
            this.dataList = res.obj.records;
            this.total = res.obj.total;
            const that = this;
            setTimeout(function () {
              that.isExpansion = true;
              that.toggleRowExpansionAll(that.dataList, true);
              that.loading = false;
            },100)
          }
        })
      },
      //查询列表数据
      getQuery() {
        this.queryForm.pid = "";
        this.$request.doGet('/admin/sysOffice/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        })
      },
      //重置查询
      resetQuery() {
        this.$refs['queryForm'].resetFields();
        this.queryForm.pid = "0";
        this.getList();
      },
      //打开弹窗
      openDialog(option) {
        this.$refs.sysOfficeDialog.open(option);
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
            that.$request.doGet('/admin/sysOffice/delAll', {ids: ids}).then(res => {
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
          that.$request.doGet('/admin/sysOffice/delete?id=' + id).then(res => {
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
