<!-- 【授权信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="author" ref="authorForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="author.id"/>
        </el-form-item>
        <el-form-item label="头像" prop="avatarUrl">
          <sui-photo type="author_avatarUrl" v-model="author.avatarUrl" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="author.nickName" placeholder="请输入昵称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="国家" prop="country">
          <el-input v-model="author.country" placeholder="请输入国家" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="author.province" placeholder="请输入省份" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="author.city" placeholder="请输入城市" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="用户标识" prop="openid">
          <el-input v-model="author.openid" placeholder="请输入用户标识" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="唯一标识" prop="unionid">
          <el-input v-model="author.unionid" placeholder="请输入唯一标识" :disabled="dialog.disabled" class="sui-input"/>
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
    name: "authorForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        author: {
          avatarUrl: "",
          nickName: "",
          country: "",
          province: "",
          city: "",
          openid: "",
          unionid: ""
        },
        //表单校验
        rules: {
          avatarUrl: [{required: true, message: '请选择头像', trigger: 'blur'}],
          nickName: [{required: true, message: '请输入昵称', trigger: 'blur'}],
          country: [{required: true, message: '请输入国家', trigger: 'blur'}],
          province: [{required: true, message: '请输入省份', trigger: 'blur'}],
          city: [{required: true, message: '请输入城市', trigger: 'blur'}],
          openid: [{required: true, message: '请输入用户标识', trigger: 'blur'}],
          unionid: [{required: true, message: '请输入唯一标识', trigger: 'blur'}]
        }
      }
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getauthor(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getauthor(id) {
        this.$request.doGet("/admin/sysAuthor/get?id=" + id).then(res => {
          if (res.isOk) {
            this.author = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.authorForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysAuthor/sub', that.author).then(res => {
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
        this.resetForm(this.$refs.authorForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
