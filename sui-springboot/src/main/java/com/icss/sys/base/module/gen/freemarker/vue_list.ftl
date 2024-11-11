<template>
  <div class="body">
    <div class="title">
      <div class="title-content">
        <uni-icons class="image left" custom-prefix="iconfont" :color="color" type="icon-list" size="45"></uni-icons>
        <span class="title-span">${comments}列表</span>
        <uni-icons class="image right" custom-prefix="iconfont" :color="color" type="icon-add" size="45" @click="toPage('/pages/module/${className?uncap_first}/${className?uncap_first}Details?pageFrom=add')"></uni-icons>
      </div>
    </div>
    <div class="list-body">
      <uni-list v-if="${className?uncap_first}List.length>0">
        <uni-list-chat v-for="${className?uncap_first} in ${className?uncap_first}List" :key="${className?uncap_first}.id" clickable @click="toPage('/pages/module/${className?uncap_first}/${className?uncap_first}Details?pageFrom=edit&id='+${className?uncap_first}.id)"
             :title="111" :note="222" :time="333" :badge-text="444">
        </uni-list-chat>
      </uni-list>
      <sui-empty v-else class="sui-empty" description="暂无数据"></sui-empty>
      <view class="loadMore" v-if="showLoadMore">{{loadMoreText}}</view>
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        color:this.$theme,
        ${className?uncap_first}List: [],
      <#list attrs as attr>
      <#if attr.inputType == 'associate'||attr.inputType == 'office'>
        <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict: [],
      </#if>
      </#list>
        loadMoreText: "加载中...",
        showLoadMore: false,
        limit:15,
        page: 0,
        total: 0,
        max: 0
      }
    },
    computed: {
      <#list attrs as attr>
      <#if attr.isShow==1&&attr.inputType == 'select'>
      //【${attr.columnDesc}】数组
      ${attr.columnFieldName}Array: function() {
        return this.$store.getters.getDictArray('${attr.dictType}');
      }<#if attr_has_next>,</#if>
      </#if>
      </#list>
    },
    onLoad() {
      this.reset();
      //初始化数据
      this.getList();
      <#list attrs as attr>
      <#if attr.inputType == 'associate'||attr.inputType == 'office'>
      //初始化【${attr.columnDesc}】
      this.get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict();
      </#if>
      </#list>
    },
    onReachBottom() {
      console.log("onReachBottom");
      if (this.max >= this.total) {
        this.showLoadMore = true;
        this.loadMoreText = "没有更多数据了!";
        return;
      }
      setTimeout(() => {
        this.getList();
      }, 300);
    },
    methods: {
      //重置数据
      reset(){
        this.${className?uncap_first}List=[],
        this.max=0,
        this.page=0,
        this.showLoadMore = false;
        this.loadMoreText="加载中...";
      },
    <#list attrs as attr>
      <#if (attr.inputType == 'select')&&attr.isShow==1>
      //获取【${attr.columnDesc}】字典文本
      ${attr.columnFieldName}Name(value) {
        return this.getArrayName(this.${attr.columnFieldName}Array,value);
      },
      </#if>
      <#if attr.inputType == 'associate'||attr.inputType == 'office'>
      //【${attr.columnDesc}】名称
      <#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Name(id) {
        if(this.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict.length>0){
          return this.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict.find(function (v) {
            return v.id == id
          }).<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 1>${codename}</#if></#list><#list "${attr.associateType}"?split(":") as codename></#list>;
        }
      },
      //【${attr.columnDesc}】列表
      get<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename?cap_first}</#if></#list>Dict() {
        this.$request.doGet('/api/<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>/getList'<#list "${attr.associateType}"?split(":") as codename><#if codename == 'sysUser'>, {roleCode:'userRole'}</#if></#list>).then(res => {
          if (res.isOk) {
            this.<#list "${attr.associateType}"?split(":") as codename><#if codename_index == 0>${codename}</#if></#list>Dict = res.list;
          }
        });
      },
      </#if>
      </#list>
      //获取数据列表
      getList() {
        this.page += 1;
        this.max += this.limit;
        uni.showLoading({title:"加载中"});
        let that = this;
        this.$request.doGet('/api/${className?uncap_first}/getPage',{page:that.page,limit:that.limit}).then(res=>{
          uni.hideLoading();
          that.${className?uncap_first}List = that.${className?uncap_first}List.concat(res.obj.records);
          that.total=res.count;
        })
      }
    }
  }
</script>

<style scoped>

</style>
