<!-- 【${comments}】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <${className?uncap_first}-form ref="${className?uncap_first}Dialog" @reloadList="getList"/>
    <!-- 查询条件 -->
    <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="90px">
    <#list attrs as attr>
    <#if attr.isPk != 1 && attr.isShow==1 && attr.inputType != 'picture'&& attr.inputType != 'textarea'>
      <#if attr.inputType == 'text'>
      <el-form-item label="${attr.columnDesc}" prop="${attr.columnFieldName}">
        <el-input v-model="queryForm.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}" size="small" clearable/>
      </el-form-item>
      </#if>
    <#if attr.inputType == 'date'>
      <#if attr.queryType == 'equal'>
      <el-form-item label="${attr.columnDesc}" prop="${attr.columnFieldName}">
        <el-date-picker v-model="queryForm.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" type="date" value-format="yyyy-MM-dd" size="small" clearable/>
      </el-form-item>
      </#if>
      <#if attr.queryType == 'between'>
      <el-form-item label="${attr.columnDesc}" prop="${attr.columnFieldName}Range">
        <el-date-picker v-model="queryForm.${attr.columnFieldName}Range" type="daterange" value-format="yyyy-MM-dd" size="small" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" clearable/>
      </el-form-item>
      </#if>
    </#if>
      <#if attr.inputType == 'select'>
      <el-form-item label="${attr.columnDesc}" prop="${attr.columnFieldName}Range">
        <el-select v-model="queryForm.${attr.columnFieldName}Range" placeholder="请选择${attr.columnDesc}" size="small" multiple collapse-tags filterable clearable>
          <el-option v-for="dict in ${attr.columnFieldName}Dict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
        </el-select>
      </el-form-item>
      </#if>
      <#if attr.inputType == 'office'>
      <el-form-item label="${attr.columnDesc}" prop="${attr.columnFieldName}">
        <sui-office v-model="queryForm.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" size="small" clearable/>
      </el-form-item>
      </#if>
      <#if attr.inputType == 'associate'>
      <el-form-item label="${attr.columnDesc}" prop="${attr.columnFieldName}">
        <el-select v-model="queryForm.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" size="small" filterable clearable>
          <el-option v-for="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list> in <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict" :key="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.id" :value="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.id" :label="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 1>${codename}</#if></#list>"/>
        </el-select>
      </el-form-item>
      </#if>
    </#if>
    </#list>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery()">重置</el-button>
      </el-form-item>
    </el-form>
    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="openDialog({pageFrom:'add',title:'新增${comments}'})" v-hasPermi="['${className?uncap_first}:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" icon="el-icon-delete" size="mini"  @click="handleDeleteAll" v-hasPermi="['${className?uncap_first}:delete']">批量删除</el-button>
      </el-col>
    <#if isPort=='true'>
      <el-col :span="1.5">
        <el-export type="primary" size="mini" action="/admin/${className?uncap_first}/export" title="导出数据" v-hasPermi="['${className?uncap_first}:export']"/>
      </el-col>
      <el-col :span="1.5">
        <el-import type="primary" size="mini" action="/admin/${className?uncap_first}/import" title="导入数据" @success="getList" v-hasPermi="['${className?uncap_first}:import']"/>
      </el-col>
    </#if>
      <right-toolbar @queryTable="getList"></right-toolbar>
    </el-row>
    <!-- 表格区域 -->
    <el-table border :header-cell-style="{background:'#f4f4f5'}" :height="600" v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="50"/>
      <el-table-column label="id" prop="id" v-if="false"/>
      <el-table-column label="序号" type="index" width="50"/>
<#list attrs as attr>
   <#if attr.isPk != 1 &&attr.isShow==1>
    <#if attr.inputType == 'text'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" sortable/>
    </#if>
    <#if attr.inputType == 'date'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" sortable/>
    </#if>
    <#if attr.inputType == 'textarea'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" show-overflow-tooltip sortable/>
    </#if>
    <#if attr.inputType == 'select'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" sortable>
        <template slot-scope="scope">
          <span :style="dict.style" v-for="dict in ${attr.columnFieldName}Dict" v-if="dict.dictValue==scope.row.${attr.columnFieldName}">{{dict.dictLabel}}</span>
        </template>
      </el-table-column>
    </#if>
    <#if attr.inputType == 'picture'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" sortable>
        <template slot-scope="scope">
          <span-photo :value="scope.row.${attr.columnFieldName}"></span-photo>
        </template>
      </el-table-column>
    </#if>
    <#if attr.inputType == 'editor'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" show-overflow-tooltip sortable>
        <template slot-scope="scope">
          <span v-text="formatText(scope.row.${attr.columnFieldName})"></span>
        </template>
      </el-table-column>
    </#if>
    <#if attr.inputType == 'office'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" sortable>
        <template slot-scope="scope">
          <span-office :value="scope.row.${attr.columnFieldName}"></span-office>
        </template>
      </el-table-column>
    </#if>
    <#if attr.inputType == 'associate'>
      <el-table-column label="${attr.columnDesc}" prop="${attr.columnFieldName}" sortable>
        <template slot-scope="scope">
          <span-label :dict="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict" :value="scope.row.${attr.columnFieldName}" label="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 1>${codename}</#if></#list>"></span-label>
        </template>
      </el-table-column>
    </#if>
  </#if>
</#list>
      <el-table-column label="操作" align="center" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="info" plain icon="el-icon-view" @click="openDialog({title:'查看${comments}',pageFrom:'view',id:scope.row.id})" v-hasPermi="['${className?uncap_first}:view']">查看</el-button>
          <el-button size="mini" type="primary" plain icon="el-icon-edit" @click="openDialog({title:'修改${comments}',pageFrom:'edit',id:scope.row.id})" v-hasPermi="['${className?uncap_first}:edit']">修改</el-button>
          <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="handleDelete(scope.row.id)" v-hasPermi="['${className?uncap_first}:delete']">删除</el-button>
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
  import ${className?uncap_first}Form from './${className?uncap_first}Form'
    export default {
      components: {${className?uncap_first}Form},
      data() {
        return {
          loading: true,//是否加载完成
          selectedData: [],//勾选数据
          total: 0, //记录数
        <#list attrs as attr>
        <#if attr.inputType == 'associate'||attr.inputType == 'office'>
          <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict: [],
        </#if>
        </#list>
          dataList: [],
          queryForm: {
            current: 1,
            size: 10,
        <#list attrs as attr>
        <#if attr.isShow==1>
          <#if attr.inputType == 'select'>
            ${attr.columnFieldName}Range: [],
          </#if>
          <#if attr.inputType == 'date' && attr.queryType == 'between'>
            ${attr.columnFieldName}Range: [],
          </#if>
            ${attr.columnFieldName}: ""<#if attr_has_next>,</#if>
        </#if>
        </#list>
          }
        };
      },
      mounted() {
        //初始化数据
        this.getList();
        <#list attrs as attr>
        <#if attr.inputType == 'associate'||attr.inputType == 'office'>
        //初始化【${attr.columnDesc}】
        this.get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict();
        </#if>
        </#list>
      },
      computed: {
        <#list attrs as attr>
        <#if attr.isShow==1&&attr.inputType == 'select'>
        //【${attr.columnDesc}】
        ${attr.columnFieldName}Dict() {
          return this.$store.getters.getDictArray('${attr.dictType}');
        }<#if attr_has_next>,</#if>
        </#if>
        </#list>
      },
      methods: {
        //点击搜索
        handleQuery() {
          this.queryForm.current = 1;
          this.getList();
        },
        //查询列表数据
        getList() {
          this.$request.doGet('/admin/${className?uncap_first}/getPage', this.queryForm).then(res => {
            if (res.isOk) {
              this.loading = false;
              this.dataList = res.obj.records;
              this.total = res.obj.total;
            }
          });
        },
        <#list attrs as attr>
        <#if attr.inputType == 'associate'||attr.inputType == 'office'>
        //【${attr.columnDesc}】
        get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict() {
          this.$request.doGet('/admin/<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>/getList'<#list "${attr.associateType}"?split(":") as codename><#if codename == 'sysUser'>, {roleCode:'userRole'}</#if></#list>).then(res => {
            if (res.isOk) {
              this.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict = res.list;
            }
          });
        },
        </#if>
        </#list>
        //重置查询
        resetQuery() {
          this.$refs['queryForm'].resetFields();
          this.handleQuery();
        },
        //打开弹窗
        openDialog(option) {
          this.$refs.${className?uncap_first}Dialog.open(option);
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
              that.$request.doGet('/admin/${className?uncap_first}/delAll', {ids: ids}).then(res => {
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
            that.$request.doGet('/admin/${className?uncap_first}/delete?id=' + id).then(res => {
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
