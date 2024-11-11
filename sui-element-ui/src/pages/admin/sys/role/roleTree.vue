<!-- 【角色信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <div style="height: 50vh;overflow: auto;">
      <el-tree
        :data="treeData"
        show-checkbox
        node-key="id"
        :default-expanded-keys="['239acec3d2354b4dae5b042dbb5e25e2']"
        ref="tree"
        highlight-current
        :props="defaultProps">
      </el-tree>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="getCheckedNodes">保 存</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
  export default {
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        sysRole: {},
        treeData: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        }
      };
    },
    methods: {
      getMenuRoleTree(roleId) {
        const that = this;
        this.$request.doGet('/admin/sysRole/getMenuRoleTree?roleId=' + roleId).then(res => {
          if (res.isOk) {
            this.treeData = res.list;
            setTimeout(function () {
              that.setCheckedNodes(that.treeData);
            });
            console.log(res.list)
          }
        });
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //打开编辑页面
      openDialogMenuEdit(title, url) {
        this.$request.doGet(url).then(res => {
          if (res.isOk) {
            this.showDialog(title, false);
            this.sysRole = res.obj;

          }
        });
      },
      open(option) {
        this.showDialog(option.title,false);
        this.getSysRole(option.id);
      },
      //根据id获取数据
      getSysRole(id) {
        this.$request.doGet("/admin/sysRole/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysRole = res.obj;
            this.getMenuRoleTree(res.obj.id)
          }
        });
      },
      getCheckedNodes() {
        //获取勾选节点以及勾选节点的父节点
        const checkedNodes =  this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
        this.$request.doPost('/admin/sysRole/updateMenuRoleTree', {
          roleId: this.sysRole.id,
          menuIds: checkedNodes
        }).then(res => {
          if (res.isOk) {
            this.showSuccess("保存成功");
            this.dialog.open = false;
          }
        });
      },
      getNodes(checkList, treeData) {
        treeData.filter(v => {
          if (v.children) {
            let itmes = v.children;
            itmes.forEach(node => {
              if (node.children) {
                this.getNodes(checkList, node.children)
              } else {
                if (node.checked) {
                  checkList.push(node)
                }
              }
            })
          } else {
            if (v.checked) {
              checkList.push(v)
            }
          }
        });
      },
      //设置勾选
      setCheckedNodes(treeData) {
        let checkList = [];
        this.getNodes(checkList, treeData);
        this.$refs.tree.setCheckedNodes(checkList);
      },
    },
  };
</script>

<style scoped>
</style>
