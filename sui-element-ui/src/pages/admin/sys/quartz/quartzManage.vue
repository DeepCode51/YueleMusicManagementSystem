<!-- 【任务信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <sysQuartz-form ref="sysQuartzDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="名称" prop="jobName">
        <el-input v-model="queryForm.jobName" placeholder="请输入名称" size="small" clearable/>
      </el-form-item>
      <el-form-item label="目标对象" prop="beanName">
        <el-input v-model="queryForm.beanName" placeholder="请输入目标对象" size="small" clearable/>
      </el-form-item>
      <el-form-item label="类型" prop="jobType">
        <el-select v-model="queryForm.jobType" placeholder="请选择类型" size="small" clearable>
          <el-option v-for="dict in jobTypeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      <el-form-item label="目标方法" prop="methodName">
        <el-input v-model="queryForm.methodName" placeholder="请输入目标方法" size="small" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 查询按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增任务信息'})" v-hasPermi="['sysQuartz:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['sysQuartz:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="名称" prop="jobName" width="150" sortable/>
      <el-table-column label="类型" prop="jobType" sortable>
        <template slot-scope="scope">
          <span v-for="dict in jobTypeDict" v-if="dict.dictValue==scope.row.jobType">{{dict.dictLabel}}</span>
        </template>
      </el-table-column>
      <el-table-column label="表达式" prop="cronExpression" sortable/>
      <el-table-column label="目标对象" prop="beanName" sortable/>
      <el-table-column label="目标方法" prop="methodName" show-overflow-tooltip sortable>
        <template slot-scope="scope">
          <span>{{scope.row.methodName}}({{scope.row.params}})</span>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否有参数" prop="hasParams" sortable>-->
<!--        <template slot-scope="scope">-->
<!--          <span :style="dict.style" v-for="dict in hasParamsDict" v-if="dict.dictValue==scope.row.hasParams">{{dict.dictLabel}}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="参数" prop="params" sortable/>-->
      <el-table-column label="开启|关闭" prop="isExec" sortable>
        <template slot-scope="scope">
          <span :style="dict.style" v-for="dict in isExecDict" v-if="dict.dictValue==scope.row.isExec">{{dict.dictLabel}}</span>
        </template>
      </el-table-column>
      <el-table-column label="执行状态" prop="jobStatus" sortable>
        <template slot-scope="scope">
          <span :style="dict.style" v-for="dict in jobStatusDict" v-if="dict.dictValue==scope.row.jobStatus">{{dict.dictLabel}}</span>
        </template>
      </el-table-column>
      <el-table-column label="最后执行时间" prop="lastExecTime" show-overflow-tooltip sortable/>
<!--      <el-table-column label="备注信息" prop="remarks" show-overflow-tooltip sortable/>-->
      <el-table-column label="操作" align="center"  width="380" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" v-if="scope.row.isExec==0" title="开启" type="success" icon="el-icon-caret-right" circle @click="start(scope.row)" v-hasPermi="['sysQuartz:edit']"></el-button>
          <el-button size="mini" v-if="scope.row.isExec==1" title="关闭" type="warning" icon="el-icon-switch-button" circle @click="stop(scope.row)" v-hasPermi="['sysQuartz:edit']"></el-button>
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看任务信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['sysQuartz:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改任务信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['sysQuartz:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)"  v-hasPermi="['sysQuartz:delete']">删除</el-button>
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
  import sysQuartzForm from './quartzForm'
  export default {
    components: {sysQuartzForm},
    data() {
      return {
        loading: true,//是否加载完成
        selectedData: [],//勾选数据
        total: 0, //记录数
        dataList: [],
        queryForm: {
          current: 1,
          size: 10,
          jobName: "",
          beanName: "",
          params: "",
          cronExpression: "",
          jobType: "",
          hasParams: "",
          methodName: "",
          jobStatus: "",
          isExec: "",
          lastExecTime: "",
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
      jobTypeDict() {
        return this.$store.getters.getDictArray('job_type');
      },
      //【是否有参数】
      hasParamsDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
      //【状态】
      jobStatusDict() {
        return this.$store.getters.getDictArray('job_status');
      },
      //【开启|关闭】
      isExecDict() {
        return this.$store.getters.getDictArray('is_exec');
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
        this.$request.doGet('/admin/sysQuartz/getPage', this.queryForm).then(res => {
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
        this.$refs.sysQuartzDialog.open(option);
      },
      start(row) {
       const that = this;
        that.showConfirm("确认开启定时任务吗?", function () {
          row.isExec = 1;
          that.$request.doPost('/admin/sysQuartz/updateSchedule', row).then(res => {
            if (res.isOk) {
              that.showSuccess("操作成功");
            }
            that.getList();
          });
        })
      },
      stop(row) {
        const that = this;
        that.showConfirm("确认关闭定时任务吗?", function () {
          row.isExec = 0;
          that.$request.doPost('/admin/sysQuartz/updateSchedule', row).then(res => {
            if (res.isOk) {
              that.showSuccess("操作成功");
            }
            that.getList();
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
      //批量删除
      handleDeleteAll() {
        const that = this;
        that.showConfirm('确定删除选中的数据吗?', function () {
          if (that.selectedData.length > 0) {
            let ids = that.selectedData.map(function (v) {return v.id});
            that.$request.doGet('/admin/sysQuartz/delAll', {ids: ids}).then(res => {
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
          that.$request.doGet('/admin/sysQuartz/delete?id=' + id).then(res => {
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
