<!-- 【菜单信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysMenu" ref="sysMenuForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysMenu.id" />
        </el-form-item>
        <el-form-item label="父级编号" prop="pid">
          <sui-menu v-model="sysMenu.pid" placeholder="请选择父级菜单" size="medium" class="sui-input"/>
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="sysMenu.name" placeholder="请输入名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="是否显示" prop="isShow">
          <el-select v-model="sysMenu.isShow" placeholder="请选择是否显示" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in isShowDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="sysMenu.isShow==1" label="排序" prop="sort">
          <el-input v-model="sysMenu.sort" placeholder="请输入排序" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item v-if="sysMenu.isShow==1" label="组件" prop="component">
          <el-input v-model="sysMenu.component" placeholder="请输入组件" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item v-if="sysMenu.isShow==1" label="链接" prop="href">
          <el-input v-model="sysMenu.href" placeholder="请输入链接" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item v-if="sysMenu.isShow==0" label="权限标识" prop="permission">
          <el-input v-model="sysMenu.permission" placeholder="请输入权限标识" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item v-if="sysMenu.isShow==1" label="图标" prop="icon">
          <el-input v-model="sysMenu.icon" placeholder="请输入图标" :disabled="dialog.disabled" class="sui-input"/><a class="el-button" href="http://www.fontawesome.com.cn/faicons/" target="view_window"> 选择图标</a>
        </el-form-item>
        <el-form-item label="备注信息" prop="remarks">
          <el-input type="textarea" v-model="sysMenu.remarks" placeholder="输入备注信息" :disabled="dialog.disabled" class="sui-textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm('sysMenuForm')">保 存</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
  export default {
    components: {

    },
    name: "sysMenuForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysMenu: {
          name: "",
          component: "",
          href: "",
          permission: "",
          sort: "2000",
          pid: "",
          icon: "fa fa-envira",
          remarks: ""
        },
        //表单校验
        rules: {
          name: [{required: true, message: '请输入名称', trigger: 'blur'}],
          isShow: [{required: true, message: '请选择是否显示', trigger: 'blur'}],
          href: [{validator:this.$validator.unique,form:this,tableName:'sys_menu',column:'href',model:'sysMenu',message:'链接已存在',trigger:'blur'}],
          sort: [{required: true, message: '请输入排序', trigger: 'blur'}],
          pid: [{required: true, message: '请输入父级编号', trigger: 'blur'}],
        }
      }
    },
    computed: {
      //【是否显示】
      isShowDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        this.sysMenu.icon = 'fa fa-envira';
        this.sysMenu.sort = '2000';
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSysMenu(option.id)
        }
      },
      //根据id获取数据
      getSysMenu(id) {
        this.$request.doGet("/admin/sysMenu/get?id="+id).then(res => {
          if (res.isOk) {
            this.sysMenu = res.obj;
          }
        });
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false
      },
      //提交表单
      submitForm: function (formName) {
        const that = this;
        that.$refs[formName].validate((valid) => {
          if (valid) {//表达校验
            that.$request.doPost('/admin/sysMenu/sub', that.sysMenu).then(res => {
              if (res.isOk) {
                that.dialog.open = false;
                that.showSuccess("提交成功");
                that.$emit('reloadList');
              }
            });
          }
        });
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.sysMenuForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>
</style>
