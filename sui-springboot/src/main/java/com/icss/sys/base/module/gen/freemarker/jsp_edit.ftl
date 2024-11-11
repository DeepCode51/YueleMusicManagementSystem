<!-- 【${comments}】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="${className?uncap_first}" ref="${className?uncap_first}Form" :rules="rules" label-width="120px" class="edit-form">
    <#list attrs as attr>
      <#if attr.isPk == 1>
        <el-form-item label="${attr.columnDesc}" class="hidden" prop="${attr.columnFieldName}">
          <el-input type="hidden" v-model="${className?uncap_first}.${attr.columnFieldName}"/>
        </el-form-item>
      </#if>
      <#if attr.isShow==1>
      <#if attr.isPk != 1>
      <#if attr.inputType == 'picture'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <sui-photo type="${className?uncap_first}" v-model="${className?uncap_first}.${attr.columnFieldName}" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'text'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <el-input v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'textarea'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <el-input type="textarea" v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}" :disabled="dialog.disabled" class="sui-textarea"/>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'editor'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <sui-editor v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}" :disabled="dialog.disabled"/>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'select'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <el-select v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in ${attr.columnFieldName}Dict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'office'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <sui-office v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'associate'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <el-select v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list> in <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict" :key="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.id" :value="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.id" :label="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 1>${codename}</#if></#list>"/>
          </el-select>
        </el-form-item>
      </#if>
      <#if attr.inputType == 'date'>
        <el-form-item<#if attr.isHidden==1> class="hidden"</#if> label="${attr.columnDesc}" prop="${attr.columnFieldName}">
          <el-date-picker v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请选择${attr.columnDesc}" :disabled="dialog.disabled" type="date" value-format="yyyy-MM-dd" class="sui-input"/>
        </el-form-item>
      </#if>
      </#if>
      </#if>
    </#list>
      <#if isFileList=='true'>
        <el-form-item label="文件列表" prop="fileList">
          <sui-file type="${className?uncap_first}" :refId="${className?uncap_first}.id" v-model="${className?uncap_first}.fileList" v-if="dialog.open" :disabled="dialog.disabled"/>
        </el-form-item>
      </#if>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" v-prevent-re-click @click="submitForm()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "${className?uncap_first}Form",
    data() {
      return {
    <#list attrs as attr>
      <#if attr.inputType == 'associate'>
        <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict:[],
      </#if>
    </#list>
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        ${className?uncap_first}: {
          <#list attrs as attr>
          <#if attr.isShow==1>
          ${attr.columnFieldName}: ""<#if attr_has_next>,</#if>
          </#if>
          </#list>
        },
        //表单校验
        rules: {
          <#list attrs as attr>
          <#if attr.isShow==1&&attr.isPk != 1>
          <#if attr.inputType == 'text'||attr.inputType == 'textarea'||attr.inputType == 'editor'>
          <#if attr.isHidden==1>//</#if>${attr.columnFieldName}: [{required: true, message: '请输入${attr.columnDesc}', trigger: 'blur'}<#if attr.isUnique == 1>,{validator:this.$validator.unique,form:this,model:'${className?uncap_first}',tableName:'${tableName}',column:'${attr.columnName}',message:'${attr.columnDesc}已存在',trigger:'blur'}</#if>],
          </#if>
          <#if attr.inputType == 'select'>
          <#if attr.isHidden==1>//</#if>${attr.columnFieldName}: [{required: true, message: '请选择${attr.columnDesc}', trigger: 'change'}],
          </#if>
          <#if attr.inputType == 'date'>
          <#if attr.isHidden==1>//</#if>${attr.columnFieldName}: [{required: true, message: '请选择${attr.columnDesc}', trigger: 'change'}],
          </#if>
          <#if attr.inputType == 'office'>
          <#if attr.isHidden==1>//</#if>${attr.columnFieldName}: [{required: true, message: '请选择${attr.columnDesc}', trigger: 'change'}],
          </#if>
          <#if attr.inputType == 'picture'>
          <#if attr.isHidden==1>//</#if>${attr.columnFieldName}: [{required: true, message: '请选择${attr.columnDesc}', trigger: 'change'}],
          </#if>
          <#if attr.inputType == 'associate'>
          <#if attr.isHidden==1>//</#if>${attr.columnFieldName}: [{required: true, message: '请选择${attr.columnDesc}', trigger: 'change'}],
          </#if>
          </#if>
          </#list>
          <#if isFileList=='true'>
          fileList: [{required: true,type:'array', message: '请选择文件上传', trigger: 'blur'}],
          </#if>
        }
      }
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
      <#list attrs as attr>
      <#if attr.inputType == 'associate'>
      //【${attr.columnDesc}】
      get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict() {
        this.$request.doGet('/admin/<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>/getList'<#list "${attr.associateType}"?split(":") as codename><#if codename == 'sysUser'>, {roleCode:'userRole'}</#if></#list>).then(res => {
          if (res.isOk) {
            this.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict = res.list;
          }
        })
      },
        </#if>
        </#list>
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        <#list attrs as attr>
        <#if attr.inputType == 'associate'>
        this.get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict();//加载【${attr.columnDesc}】
        </#if>
        </#list>
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.get${className}(option.id);
        }
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.${className?uncap_first}Form);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      get${className}(id) {
        this.$request.doGet("/admin/${className?uncap_first}/get?id=" + id).then(res => {
          if (res.isOk) {
            this.${className?uncap_first} = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.${className?uncap_first}Form.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/${className?uncap_first}/sub', that.${className?uncap_first}).then(res => {
              if (res.isOk) {
                that.cancel();
                that.showSuccess("提交成功");
                that.$emit('reloadList');
              }
            });
          }
        });
      }

    }
  }
</script>

<style scoped>

</style>
