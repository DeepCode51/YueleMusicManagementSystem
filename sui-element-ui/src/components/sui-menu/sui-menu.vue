<template>
    <el-cascader
      class="el-cascader"
      :clearable="clearable"
      :size="size"
      :value="menuId"
      ref="menuCascader"
      :disabled="disabled"
      :placeholder="placeholder"
      @change="change"
      :options="options"
      :props="{ checkStrictly: true,expandTrigger: 'hover',label:'name',value:'id',children:'children' }"
      >
    </el-cascader>
</template>
<script>
  export default {
    data(){
      return{
        options:[]
      }
    },
    model:{
      prop:'menuId',
      event:'menuIdChange'
    },
    props: {
      size: {
        type: String,
        default: 'medium'
      },
      menuId: {
        type: String,
        default: ''
      },
      pid: {
        type: String,
        default: 'root'
      },
      placeholder:{
        type: String,
        default: ''
      },
      disabled: {
        type: Boolean,
        default: false
      },
      clearable: {
        type: Boolean,
        default: false
      }
    },
    watch: {
      menuId(value){
        this.getOfficeTree();
      }
    },
    mounted() {
      this.getOfficeTree()
    },
    computed:{
    },
    methods:{
      change(array){
        if(array.length>0){
          let pid = this.$refs.menuCascader.getCheckedNodes()[0].value;
          this.$emit('menuIdChange', pid);
        }else{
          this.$emit('menuIdChange', '');
        }
      },
      getOfficeTree() {
        this.$request.doGet('/admin/sysMenu/getMenuTree?pid='+this.pid).then(res => {
          if (res.isOk) {
            this.options =res.list[0];
          }
        });
      },
    }
  };
</script>

