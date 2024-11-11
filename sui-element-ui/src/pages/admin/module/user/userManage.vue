<!-- 【用户信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysUser-form ref="sysUserFormDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="登录名" prop="loginName">
        <el-input v-model="queryForm.loginName" placeholder="请输入登录名" size="small" clearable/>
      </el-form-item>
      <!--<el-form-item label="所属机构" prop="officeId">
        <sui-office v-model="queryForm.officeId" placeholder="请选择所属机构" size="small" clearable/>
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({title:'新增用户',pageFrom:'add'})" v-hasPermi="['sysUser:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysUser:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id"  prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" />
      <el-table-column label="登录名" prop="loginName" sortable />
      <el-table-column label="所属角色" prop="roleId" sortable>
        <template slot-scope="scope">
          <span v-if="role" v-for="role in scope.row.roleList">【{{role.roleName}}】</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="所属机构" prop="officeId" sortable>
        <template slot-scope="scope">
          <span v-for="sysOffice in sysOfficeDict" v-if="sysOffice.id==scope.row.officeId">{{sysOffice.name}}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="手机号" prop="phone" sortable />
      <el-table-column label="邮箱" prop="email" sortable />
      <el-table-column label="操作" align="center" width="500" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看用户',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysUser:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改用户',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysUser:edit']">修改</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="handleReset(scope.row)" v-hasPermi="['sysUser:edit']">重置密码</el-button>
          <el-button v-if="scope.row.disabled==1" size="mini" type="danger" plain icon="el-icon-edit" @click="handleUnLock(scope.row)" v-hasPermi="['sysUser:edit']">释放</el-button>
          <el-button v-else size="mini" type="primary" plain icon="el-icon-edit" @click="handleLock(scope.row)" v-hasPermi="['sysUser:edit']">冻结</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)" v-hasPermi="['sysUser:delete']">删除</el-button>
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
  import sysUserForm from './userForm'

  export default {
    components: {sysUserForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        sysOfficeDict:[],
        sysRoleDict:[],
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 10,
          photo: "",
          roleCode:'userRole',//角色过滤
          roleList:[],
          loginName: "",
          password: "",
          userName: "",
          userNo: "",
          email: "",
          phone: "",
          officeId: ""
        }
      };
    },
    mounted() {
      //初始化数据
      this.getList();
      this.getSysRoleDict();
      this.getSysOfficeDict();
    },
    computed: {
    },
    methods: {
      //打开弹窗
      openDialog(option){
        this.$refs.sysUserFormDialog.open(option);
      },
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        this.getList();
      },
      //查询列表数据
      getList() {
        this.$request.doGet('/admin/sysUser/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        })
      },
      //【所属机构】
      getSysOfficeDict(){
        this.$request.doPost('/admin/sysOffice/getList', {}).then(res => {
          if (res.isOk) {
            this.sysOfficeDict=res.list;
          }
        })
      },
      //【所属角色】
      getSysRoleDict(){
        this.$request.doGet('/admin/sysRole/getList').then(res => {
          if (res.isOk) {
            this.sysRoleDict=res.list;
          }
        })
      },
      //重置查询
      resetQuery() {
        this.$refs['queryForm'].resetFields();
        this.handleQuery();
      },
      //重置密码
      handleReset(row) {
        const that = this;
        that.showConfirm("确认重置该用户的密码吗？",function () {
          that.$request.doGet('/admin/sysUser/resetPassword?userId=' + row.id).then(res => {
            if (res.isOk) {
              that.getList();
              that.showSuccess(res.info);
            };
          });
        })
      },
      //冻结用户
      handleLock(row) {
        const that = this;
        that.showConfirm("确认冻结该用户吗？",function () {
          that.$request.doGet('/admin/sysUser/lockUser?id=' + row.id+"&disabled=1").then(res => {
            if (res.isOk) {
              that.getList();
              that.showSuccess(res.info);
            };
          });
        })
      },
      //释放用户
      handleUnLock(row) {
        const that = this;
        that.showConfirm("确认释放该用户吗？",function () {
          that.$request.doGet('/admin/sysUser/lockUser?id=' + row.id+"&disabled=0").then(res => {
            if (res.isOk) {
              that.getList();
              that.showSuccess(res.info);
            };
          });
        })
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
          that.$request.doGet('/admin/sysUser/delete?id=' + id).then(res => {
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
            that.$request.doGet('/admin/sysUser/delAll', {ids: ids}).then(res => {
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
