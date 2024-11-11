<!-- 【消息信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysMessage-form ref="sysMessageDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="消息类型" prop="type">
        <el-input v-model="queryForm.type" placeholder="请输入消息类型" size="small" clearable/>
      </el-form-item>
      <el-form-item label="用户名称" prop="userId">
        <el-select v-model="queryForm.userId" placeholder="请选择用户名称" size="small" filterable clearable>
          <el-option v-for="register in registerDict" :key="register.id" :value="register.id" :label="register.loginName"/>
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
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysMessage:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border row-key="id" :header-cell-style="{background:'#f4f4f5'}" :tree-props="{children: 'children', hasChildren: 'hasChildren'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="消息类型" prop="type" sortable/>
      <el-table-column label="用户名称" prop="loginName" sortable/>
      <el-table-column label="消息内容" prop="content" show-overflow-tooltip sortable>
        <template slot-scope="scope">
          <span v-html="scope.row.content"></span>
        </template>
      </el-table-column>
      <el-table-column label="操作时间" prop="createDate" sortable/>
      <el-table-column label="操作" align="center" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" v-if="!scope.row.children" plain icon="el-icon-edit" @click="openDialog({title:'修改消息信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysMessage:edit']">修改</el-button>
          <el-button size="mini" type="success" v-if="scope.row.children" plain icon="el-icon-edit" @click="reply(scope.row)">回复</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysMessage:delete']">删除</el-button>
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
  import sysMessageForm from './messageForm'
  export default {
    components: {sysMessageForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        registerDict: [],
        sysMessage: {
          type: "",
          pid: "",
          userId: "",
          refId: "",
          content: ""
        },
        queryForm: {
          current: 1,
          size: 10,
          type: "",
          pid: "",
          userId: "",
          refId: "",
          content: ""
        }
      };
    },
    mounted() {
      //初始化数据
      this.getList();
      //初始化【用户名称】
      this.getRegisterDict();
    },
    methods: {
      //点击搜索
      handleQuery() {
        this.queryForm.current = 1;
        this.getList();
      },
      //查询列表数据
      getList() {
        this.$request.doGet('/admin/sysMessage/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.loading = false;
            this.dataList = res.obj.records;
            this.total = res.obj.total;
          }
        });
      },
      //【用户名称】
      getRegisterDict() {
        this.$request.doGet('/admin/register/getList').then(res => {
          if (res.isOk) {
            this.registerDict = res.list;
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
        this.$refs.sysMessageDialog.open(option);
      },
      //选择页数
      handleSizeChange: function (size) {
        this.queryForm.size = size;
        this.getList();
      },
      //回复信息
      reply: function (row) {
        const that = this;
        that.$prompt('请输入回复内容', '温馨提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
        }).then(({value}) => {
          if(value==null){
            that.showWarn("请输入内容");
            return;
          }
          that.sysMessage.type = "回复";
          that.sysMessage.pid = row.id;
          that.sysMessage.content = value;
          that.$request.doPost('/admin/sysMessage/sub', that.sysMessage).then(res => {
            if (res.isOk) {
              that.sysMessage.content ="";
              that.showSuccess("回复成功");
              that.getList();
            }
          });
        })
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
            that.$request.doGet('/admin/sysMessage/delAll', {ids: ids}).then(res => {
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
          that.$request.doGet('/admin/sysMessage/delete?id=' + id).then(res => {
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
