<template>
  <el-button :type="type" :icon="icon" :disabled="disabled" :size="size" @click="exportData">{{title}}</el-button>
</template>
<script>
  import qs from 'qs'
  export default {
    props: {
      title: {
        type: String,
        default: ''
      },
      action: {
        type: String,
        default: ''
      },
      disabled: {
        type: Boolean,
        default: false
      },
      dataList: {
        type: Array,
        default: []
      },
      type: {
        type: String,
        default: 'primary'
      },
      icon: {
        type: String,
        default: 'el-icon-download'
      },
      size: {
        type: String,
        default: 'mini'
      }
    },
    mounted() {
    },
    methods:{
      //导出
      exportData() {
        const that = this;
        let dataList = this.dataList;
        if(dataList.length==0 && that.$parent.$parent.$parent.dataList && that.$parent.$parent.$parent.dataList.length==0){
          that.showWarn("没有数据，不能导出！");
          return;
        }
        // let queryForm = that.$parent.$parent.$parent.queryForm;
        // let paramsStr = qs.stringify(queryForm,{allowDots : true,skipNulls: true});//post接受需要qs加持：不然后端得用注解接收
        that.showConfirm('确定要导出数据吗?', function () {
          // 生成一个 a 标签
          const a = document.createElement('a');
          // 创建一个点击事件
          const event = new MouseEvent('click');
          // 将生成的 URL 设置为 a.href 属性
          a.href = that.action;
          // 触发 a 的点击事件
          a.dispatchEvent(event);
          //location.href = that.action;
          const loading = that.$loading({
            lock: false,
            text: '正在导出,请稍等...',
            spinner: 'el-icon-loading',
            background: 'rgba(232,244,255,0.25)'
          });
          setTimeout(function () {
            loading.close();
          },1500)
        });
      },
    }
  }
</script>
