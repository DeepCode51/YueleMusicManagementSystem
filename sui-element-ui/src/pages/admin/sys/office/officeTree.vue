<template>
  <div class="body">
    <el-tree
      :data="treeData"
      default-expand-all
      @node-click="handleNodeClick"
      node-key="id"
      ref="tree"
      highlight-current
      :props="defaultProps">
    </el-tree>
  </div>
</template>
<script>
  export default {
    props: {
      code: {
        type: String,
        default: 'root'
      },
      total: {
        type: String,
        default: ''
      }
    },
    data() {
      return {
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'name'
        }
      };
    },
    watch: {
      total(value){
        this.getOfficeTree()
      }
    },
    mounted() {
      this.getOfficeTree()
    },
    methods: {
      getOfficeTree() {
        this.$request.doGet('/admin/sysOffice/getOfficeTree?code='+this.code).then(res => {
          if (res.isOk) {
            this.treeData = res.list;
            console.log(res.list)
          }
        });
      },
      // 点击节点名称触发的事件
      handleNodeClick: function (data) {
        this.$emit('treeSelected',data)
      },
      getCheckedNodes() {
        console.log(this.$refs.tree.getCheckedNodes());
      },
      getCheckedKeys() {
        console.log(this.$refs.tree.getCheckedKeys());
      },
      setCheckedNodes() {
        this.$refs.tree.setCheckedNodes([{
          id: 5,
          label: '二级 2-1'
        }, {
          id: 9,
          label: '三级 1-1-1'
        }]);
      },
      setCheckedKeys() {
        this.$refs.tree.setCheckedKeys([3]);
      },
      resetChecked() {
        this.$refs.tree.setCheckedKeys([]);
      }
    },
  };
</script>
