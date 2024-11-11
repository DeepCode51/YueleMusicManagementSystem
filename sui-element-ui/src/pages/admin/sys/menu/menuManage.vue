<!-- 【菜单信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysMenu-form ref="sysMenuDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="queryForm.name" placeholder="请输入名称" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增菜单'})" v-hasPermi="['sysMenu:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysMenu:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table row-key="id" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id"  prop="id" v-if="false"/>
      <el-table-column label="名称" prop="icon" width="150" sortable>
        <template slot-scope="scope">
          <i :class="'fa '+scope.row.icon"></i>
          <span>{{scope.row.name}}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否显示" prop="isShow" width="100" sortable>
        <template slot-scope="scope" >
          <span :style="dict.style" v-for="dict in isShowDict" v-if="dict.dictValue==scope.row.isShow">{{dict.dictLabel}}</span>
        </template>
      </el-table-column>
      <el-table-column label="链接" prop="href" width="250" sortable/>
      <el-table-column label="组件" prop="component" width="350" sortable/>
      <el-table-column label="权限标识" prop="permission" width="200" sortable/>
      <el-table-column label="排序" prop="sort" width="100" sortable/>
      <el-table-column label="操作" align="center"  width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看菜单信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysMenu:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改菜单信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysMenu:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysMenu:delete']">删除</el-button>
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
  import sysMenuForm from './menuForm'
  export default {
    components: {sysMenuForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 20,
          name: "",
          component: "",
          isShow: "",
          href: "",
          permission: "",
          sort: "",
          pid: "",
          icon: "",
          remarks: ""
        }
      };
    },
    mounted() {
      //初始化数据
      this.getList();
    },
    computed: {
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
        this.$request.doGet('/admin/sysMenu/getTreePage', this.queryForm).then(res => {
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
      openDialog(onLoad) {
        this.$refs.sysMenuDialog.open(onLoad);
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
          that.$request.doGet('/admin/sysMenu/delete?id=' + id).then(res => {
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
            that.$request.doGet('/admin/sysMenu/delAll', {ids: ids}).then(res => {
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

<style type="scss">

</style>
