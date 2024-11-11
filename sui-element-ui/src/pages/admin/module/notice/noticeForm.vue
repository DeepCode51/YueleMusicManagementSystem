<!-- 【通知信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="notice" ref="noticeForm" :rules="rules" label-width="120px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="notice.id"/>
        </el-form-item>
        <el-form-item label="标题图片" prop="picture">
          <sui-photo type="notice" v-model="notice.picture" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="消息标题" prop="title">
          <el-input v-model="notice.title" placeholder="请输入消息标题" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="通知类型" prop="noticeType">
          <el-select v-model="notice.noticeType" placeholder="请选择通知类型" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in noticeTypeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item class="hidden" label="发布时间" prop="publishTime">
          <el-date-picker v-model="notice.publishTime" placeholder="请选择发布时间" :disabled="dialog.disabled" type="date" value-format="yyyy-MM-dd" class="sui-input"/>
        </el-form-item>
        <el-form-item v-if="pageFrom=='view'" label="消息内容" prop="content">
          <div class="content" v-html="notice.content"></div>
        </el-form-item>
        <el-form-item v-else label="消息内容" prop="content" class="sui-editor-item">
          <sui-editor v-model="notice.content" placeholder="请输入消息内容" :disabled="dialog.disabled"/>
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
    name: "noticeForm",
    data() {
      return {
        pageFrom:"",
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        notice: {
          picture: "",
          title: "",
          noticeType: "",
          publishTime: "",
          content: ""
        },
        //表单校验
        rules: {
          picture: [{required: true, message: '请选择标题图片', trigger: 'change'}],
          title: [{required: true, message: '请输入消息标题', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'notice',tableName:'notice',column:'title',message:'消息标题已存在',trigger:'blur'}],
          noticeType: [{required: true, message: '请选择通知类型', trigger: 'change'}],
          content: [{required: true, message: '请选输入消息内容', trigger: 'blur'}],
        }
      }
    },
    computed: {
      //【通知类型】
      noticeTypeDict() {
        return this.$store.getters.getDictArray('notice_type');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        this.pageFrom = option.pageFrom
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getNotice(option.id);
        }
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.noticeForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getNotice(id) {
        this.$request.doGet("/admin/notice/get?id=" + id).then(res => {
          if (res.isOk) {
            this.notice = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.noticeForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/notice/sub', that.notice).then(res => {
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
  .content {
    background-color: #F5F7FA;
    border: solid 1px #dfe4ed;
    color: #C0C4CC;
    border-radius: 10px;
    white-space: pre-wrap;
    padding: 0 10px;
    margin-right: 10px;
  }
</style>
