<!-- 【歌手信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="singer" ref="singerForm" :rules="rules" label-width="120px" class="edit-form">
        <el-form-item label="主键" class="hidden" prop="id">
          <el-input type="hidden" v-model="singer.id"/>
        </el-form-item>
        <el-form-item label="头像" prop="picture">
          <sui-photo type="singer" v-model="singer.picture" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="singer.name" placeholder="请输入姓名" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="singer.sex" placeholder="请选择性别" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in sexDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="地区" prop="regionArea">
          <el-select v-model="singer.regionArea" placeholder="请选择地区" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in regionAreaDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="简介" prop="introduction" class="sui-editor-item">
          <sui-editor v-model="singer.introduction" placeholder="请输入简介" :disabled="dialog.disabled"/>
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
    name: "singerForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        singer: {
          picture: "",
          name: "",
          sex: "",
          regionArea: "",
          introduction: "",
        },
        //表单校验
        rules: {
          picture: [{required: true, message: '请选择头像', trigger: 'change'}],
          name: [{required: true, message: '请输入姓名', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'singer',tableName:'singer',column:'name',message:'姓名已存在',trigger:'blur'}],
          sex: [{required: true, message: '请选择性别', trigger: 'change'}],
          regionArea: [{required: true, message: '请选择地区', trigger: 'change'}],
          introduction: [{required: true, message: '请输入简介', trigger: 'blur'}],
        }
      }
    },
    computed: {
      //【性别】
      sexDict() {
        return this.$store.getters.getDictArray('sys_sex');
      },
      //【地区】
      regionAreaDict() {
        return this.$store.getters.getDictArray('region_area');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSinger(option.id);
        }
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.singerForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSinger(id) {
        this.$request.doGet("/admin/singer/get?id=" + id).then(res => {
          if (res.isOk) {
            this.singer = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.singerForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/singer/sub', that.singer).then(res => {
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
