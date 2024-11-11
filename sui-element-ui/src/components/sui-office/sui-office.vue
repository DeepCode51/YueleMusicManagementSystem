<template>
    <el-cascader
      class="el-cascader"
      :clearable="clearable"
      :size="size"
      :value="officeId"
      ref="officeCascader"
      :disabled="disabled"
      :placeholder="placeholder"
      @change="change"
      :options="options"
      :props="{checkStrictly: true,expandTrigger: 'hover',label:'name',value:'id',children:'children'}"
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
      prop:'officeId',
      event:'officeIdChange'
    },
    props: {
      size: {
        type: String,
        default: 'medium'
      },
      officeId: {
        type: String,
        default: ''
      },
      type: {
        type: String,
        default: '1'
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
      officeId(value){
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
          let pid = this.$refs.officeCascader.getCheckedNodes()[0].value;
          this.$emit('officeIdChange', pid);
        }else{
          this.$emit('officeIdChange', '');
        }
      },
      getOfficeTree() {
        this.$request.doGet('/admin/sysOffice/getOfficeTree?type=' + this.type).then(res => {
          if (res.isOk) {
            this.options = res.list;
          }
        });
      }
    }
  };
</script>

