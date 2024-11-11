<template>
<div class="body">
  <div class="title">
    <div class="title-content">
      <uni-icons class="image left" custom-prefix="iconfont" :color="color" type="icon-label" size="45"></uni-icons>
      <span class="title-span">${comments}详情</span>
      <uni-icons v-if="${className?uncap_first}.id&&!disabled" class="image right" custom-prefix="iconfont" :color="color" type="icon-del" size="45" @click="del${className}(${className?uncap_first}.id)"></uni-icons>
    </div>
  </div>
  <div class="list-body">
    <uni-forms :rules="rules" :value="${className?uncap_first}" ref="form" validate-trigger="bind" err-show-type="undertext">
    <#list attrs as attr>
    <#if attr.isPk != 1 &&attr.isShow==1>
    <#if attr.inputType == 'picture'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <sui-photo type="${className?uncap_first}" :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}"/>
      </uni-forms-item>
    </#if>
    <#if attr.inputType == 'text'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <uni-easyinput type="text" :inputBorder="false" :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}"/>
      </uni-forms-item>
    </#if>
    <#if attr.inputType == 'textarea'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <uni-easyinput type="textarea" :inputBorder="false" :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}"/>
      </uni-forms-item>
    </#if>
    <#if attr.inputType == 'editor'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <uni-editor :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}" placeholder="请输入${attr.columnDesc}"></uni-editor>
      </uni-forms-item>
    </#if>
    <#if attr.inputType == 'select'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <uni-select-picker :inputBorder="false" :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}" :dictList="${attr.columnFieldName}Dict" code="dictValue" label="dictLabel" placeholder="请选择${attr.columnDesc}"/>
      </uni-forms-item>
    </#if>
    <#if attr.inputType == 'associate'||attr.inputType == 'office'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <uni-select-picker :inputBorder="false" :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}" :dictList="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict" code="id" label="<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 1>${codename}</#if></#list>" placeholder="请选择${attr.columnDesc}"/>
      </uni-forms-item>
    </#if>
    <#if attr.inputType == 'date'>
      <uni-forms-item name="${attr.columnFieldName}" label="${attr.columnDesc}">
        <uni-datetime-picker :border="false" :disabled="disabled" v-model="${className?uncap_first}.${attr.columnFieldName}" type="date" :start="getDate().fullDate" placeholder="请选择${attr.columnDesc}"/>
      </uni-forms-item>
    </#if>
    </#if>
    </#list>
      <#if isFileList=='true'>
      <uni-forms-item name="fileList" label="附件信息">
        <sui-file type="${className?uncap_first}" :refId="${className?uncap_first}.id" :disabled="disabled" v-model="${className?uncap_first}.fileList"/>
      </uni-forms-item>
      </#if>
    </uni-forms>
    <div style="margin-bottom: 100upx">
      <button v-if="!disabled" class="sub-btn" @click="submitForm('form')">提交</button>
    </div>
  </div>
</div>
</template>

<script>
  export default {
    data() {
      return {
        color:this.$theme,
        disabled:false,
      <#list attrs as attr>
      <#if attr.inputType == 'associate'||attr.inputType == 'office'>
        <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict:[],
      </#if>
      </#list>
        ${className?uncap_first}: {
          <#list attrs as attr>
          <#if attr.isShow==1>
          ${attr.columnFieldName}: "",
          </#if>
          </#list>
        <#if isFileList=='true'>
          fileList: []
        </#if>
        },
        rules: {
        <#list attrs as attr>
        <#if attr.isShow==1&&attr.isPk != 1>
          ${attr.columnFieldName}: {rules: [{required: true, errorMessage: '${attr.columnDesc}不能为空'}]},
        </#if>
        </#list>
        <#if isFileList=='true'>
          fileList: {rules: [{required: true,format:'array', errorMessage: '请选择文件上传'}]},
        </#if>
        },
      };
    },
    onLoad(option) {
      const pageFrom = option.pageFrom;
      this.disabled = pageFrom == 'view' ? true : false;
      <#list attrs as attr>
      <#if attr.inputType == 'associate'||attr.inputType == 'office'>
      this.get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict();//加载【${attr.columnDesc}】
      </#if>
      </#list>
      if (pageFrom == 'edit' || pageFrom == 'view') {
        //获取${comments}数据
        this.get${className}(option.id)
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
    <#if attr.inputType == 'associate'||attr.inputType == 'office'>
      //【${attr.columnDesc}】
      get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict(){
        this.$request.doGet('/api/<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>/getList'<#list "${attr.associateType}"?split(":") as codename><#if codename == 'sysUser'>, {roleCode:'userRole'}</#if><#if codename == 'sysOffice'>, {type:'2'}</#if></#list>).then(res => {
          if (res.isOk) {
            this.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict=res.list;
          }
        })
      },
    </#if>
    </#list>
      //获取${comments}数据
      get${className}(id){
        uni.showLoading({title: "加载中"});
        this.$request.doGet('/api/${className?uncap_first}/get?id='+id).then(res=>{
          if(res.isOk){
            uni.hideLoading();
            this.${className?uncap_first} = res.obj;
          }
        })
      },
      //删除${comments}数据
      del${className}(id){
        const that = this;
        this.showConfirm("确认删除吗?",function () {
          that.$request.doGet("/api/${className?uncap_first}/delete?id="+id).then(res=>{
            if(res.isOk){
              that.showSuccess("删除成功！");
              that.backPage();
            }
          })
        })
      },
      //提交${comments}数据
      submitForm(form) {
        const that = this;
        this.$refs[form].submit().then(res => {
          this.showConfirm("确认提交吗?", function () {
            that.$request.doPost("/api/${className?uncap_first}/sub", that.${className?uncap_first}).then(res => {
              if (res.isOk) {
                  that.showSuccess("提交成功！");
                  that.backPage();
              }
            })
          })
        }).catch(errors => {
          console.error('验证失败：', errors)
        })
      }
    }
  }
</script>

<style scoped lang="scss">
  .sub-btn{
    background: $theme;
  }
</style>
