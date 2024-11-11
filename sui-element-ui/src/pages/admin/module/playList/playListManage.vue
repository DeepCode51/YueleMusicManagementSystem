<!-- 【播放清单信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <playList-form ref="playListDialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="90px">
      <el-form-item label="所属用户" prop="userId">
        <el-select v-model="queryForm.userId" placeholder="请选择所属用户" size="small" filterable clearable>
          <el-option v-for="register in registerDict" :key="register.id" :value="register.id" :label="register.loginName"/>
        </el-select>
      </el-form-item>
      <el-form-item label="所属音乐" prop="songId">
        <el-select v-model="queryForm.songId" placeholder="请选择所属音乐" size="small" filterable clearable>
          <el-option v-for="song in songDict" :key="song.id" :value="song.id" :label="song.name"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增播放清单信息'})" v-hasPermi="['playList:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['playList:delete']">批量删除</el-button>
      </el-col>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" fixed width="50"/>
      <el-table-column label="所属用户" prop="userId" sortable>
        <template slot-scope="scope">
          <span v-for="register in registerDict" v-if="register.id==scope.row.userId">{{register.loginName}}</span>
        </template>
      </el-table-column>
      <el-table-column label="所属音乐" prop="songId" sortable>
        <template slot-scope="scope">
          <span v-for="song in songDict" v-if="song.id==scope.row.songId">{{song.name}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看播放清单信息',pageFrom:'view',id:scope.row.id})" v-hasPermi="['playList:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改播放清单信息',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['playList:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)" v-hasPermi="['playList:delete']">删除</el-button>
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
  import playListForm from './playListForm'
    export default {
      components: {playListForm},
      data() {
        return {
          loading: true,//是否加载完成
          selectedData: [],//勾选数据
          total: 0, //记录数
          registerDict: [],
          songDict: [],
          dataList: [],
          queryForm: {
            current: 1,
            size: 10,
            id: "",
            userId: "",
            songId: "",
          }
        };
      },
      mounted() {
        //初始化数据
        this.getList();
        //初始化【所属用户】
        this.getRegisterDict();
        //初始化【所属音乐】
        this.getSongDict();
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
          this.$request.doGet('/admin/playList/getPage', this.queryForm).then(res => {
            if (res.isOk) {
              this.loading = false;
              this.dataList = res.obj.records;
              this.total = res.obj.total;
            }
          });
        },
        //【所属用户】
        getRegisterDict() {
          this.$request.doGet('/admin/register/getList').then(res => {
            if (res.isOk) {
              this.registerDict = res.list;
            }
          });
        },
        //【所属音乐】
        getSongDict() {
          this.$request.doGet('/admin/song/getList').then(res => {
            if (res.isOk) {
              this.songDict = res.list;
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
          this.$refs.playListDialog.open(option);
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
              that.$request.doGet('/admin/playList/delAll', {ids: ids}).then(res => {
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
            that.$request.doGet('/admin/playList/delete?id=' + id).then(res => {
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
