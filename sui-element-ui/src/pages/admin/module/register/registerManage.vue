<!-- 【前台用户】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <register-form ref="registerDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="登录名" prop="loginName">
        <el-input v-model="queryForm.loginName" placeholder="请输入登录名" size="small" clearable/>
      </el-form-item>
      <el-form-item label="姓名" prop="userName">
        <el-input v-model="queryForm.userName" placeholder="请输入姓名" size="small" clearable/>
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input v-model="queryForm.phone" placeholder="请输入手机号" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['register:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="头像" prop="photo" sortable>
        <template slot-scope="scope">
          <sui-image :src="scope.row.photo"></sui-image>
        </template>
      </el-table-column>
      <el-table-column label="登录名" prop="loginName" sortable/>
      <el-table-column label="姓名" prop="userName" sortable/>
      <el-table-column label="手机号" prop="phone" sortable/>
      <el-table-column label="邮箱" prop="email" sortable/>
      <el-table-column label="操作" align="center"  width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看注册信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['register:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改注册信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['register:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['register:delete']">删除</el-button>
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
  import registerForm from './registerForm'
  export default {
    components: {registerForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 10,
          photo: "",
          loginName: "",
          password: "",
          userName: "",
          sex: "",
          userNo: "",
          phone: "",
          idCard: "",
          email: "",
          userType: "",
          userAuthorId: "",
          address: ""
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
      //【用户类型】
      userTypeDict() {
        return this.$store.getters.getDictArray('user_type');
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
        this.$request.doGet('/admin/register/getPage', this.queryForm).then(res => {
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
        this.$refs.registerDialog.open(option);
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
            that.$request.doGet('/admin/register/delAll', {ids: ids}).then(res => {
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
          that.$request.doGet('/admin/register/delete?id=' + id).then(res => {
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
