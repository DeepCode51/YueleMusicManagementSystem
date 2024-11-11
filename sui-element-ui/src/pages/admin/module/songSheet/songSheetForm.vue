<!-- 【歌单信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="songSheet" ref="songSheetForm" :rules="rules" label-width="120px" class="edit-form">
        <el-form-item label="主键" class="hidden" prop="id">
          <el-input type="hidden" v-model="songSheet.id"/>
        </el-form-item>
        <el-form-item label="歌单图片" prop="picture">
          <sui-photo type="songSheet" v-model="songSheet.picture" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="歌单标题" prop="title">
          <el-input v-model="songSheet.title" placeholder="请输入歌单标题" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="歌单分类" prop="categoryId">
          <el-select v-model="songSheet.categoryId" placeholder="请选择歌单分类" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="category in categoryDict" :key="category.id" :value="category.id" :label="category.name"/>
          </el-select>
        </el-form-item>
        <el-form-item label="歌单简介" prop="introduction" class="sui-editor-item">
          <sui-editor v-model="songSheet.introduction" placeholder="请输入歌单简介" :disabled="dialog.disabled"/>
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
    name: "songSheetForm",
    data() {
      return {
        categoryDict:[],
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        songSheet: {
          picture: "",
          title: "",
          categoryId: "",
          introduction: "",
        },
        //表单校验
        rules: {
          picture: [{required: true, message: '请选择歌单图片', trigger: 'change'}],
          title: [{required: true, message: '请输入歌单标题', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'songSheet',tableName:'song_sheet',column:'title',message:'歌单标题已存在',trigger:'blur'}],
          categoryId: [{required: true, message: '请选择歌单分类', trigger: 'change'}],
          introduction: [{required: true, message: '请输入歌单简介', trigger: 'blur'}],
        }
      }
    },
    computed: {
    },
    methods: {
      //【歌单分类】
      getCategoryDict() {
        this.$request.doGet('/admin/category/getList').then(res => {
          if (res.isOk) {
            this.categoryDict = res.list;
          }
        })
      },
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        this.getCategoryDict();//加载【歌单分类】
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSongSheet(option.id);
        }
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.songSheetForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSongSheet(id) {
        this.$request.doGet("/admin/songSheet/get?id=" + id).then(res => {
          if (res.isOk) {
            this.songSheet = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.songSheetForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/songSheet/sub', that.songSheet).then(res => {
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
