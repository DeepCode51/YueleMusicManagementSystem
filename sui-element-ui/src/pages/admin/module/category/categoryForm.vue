<!-- 【类别信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="800px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form style="height: auto" :model="category" ref="categoryForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="category.id"/>
        </el-form-item>
        <el-form-item  label="类别名称" prop="name">
          <el-input v-model="category.name" placeholder="请输入类别名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item  label="类别描述" prop="content">
          <el-input type="textarea" v-model="category.content" placeholder="请输入类别描述" :disabled="dialog.disabled" class="sui-textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "categoryForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        category: {
          name: "",
          content: ""
        },
        //表单校验
        rules: {
          name: [{required: true, message: '请输入类别名称', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'category',tableName:'category',column:'name',message:'类别名称已存在',trigger:'blur'}],
          content: [{required: true, message: '请输入类别描述', trigger: 'blur'}]
        }
      }
    },
    computed: {
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getCategory(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getCategory(id) {
        this.$request.doGet("/admin/category/get?id=" + id).then(res => {
          if (res.isOk) {
            this.category = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.categoryForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/category/sub', that.category).then(res => {
              if (res.isOk) {
                that.cancel();
                that.showSuccess("提交成功");
                that.$emit('reloadList');
              }
            });
          }
        });
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.categoryForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
