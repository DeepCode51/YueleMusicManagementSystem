<!-- 【审核日志信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="500px" class="front-dialog" center :title="dialog.title" :close-on-click-modal="false" :append-to-body="true" v-dialog-drag :visible.sync="dialog.open">
      <el-form :model="audit" ref="auditForm" :rules="rules" label-width="100px">
        <el-descriptions class="margin-top" :column="1" direction="vertical">
          <el-descriptions-item label="请选择">
            <el-radio-group v-model="audit.status" size="small" style="text-align: center">
              <el-radio :label=true border>通过</el-radio>
              <el-radio :label=false border>不通过</el-radio>
            </el-radio-group>
          </el-descriptions-item>
          <el-descriptions-item label="备注内容">
            <el-input type="textarea" v-model="audit.remarks" placeholder="请输入备注内容"/>
          </el-descriptions-item>
        </el-descriptions>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm()">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "auditForm",
    data() {
      return {
        status: '1',
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        audit: {
          row:{},
          status:true,
          remarks: ""
        },
        //表单校验
        rules: {
          status: [{required: true, message: '请选择审核状态', trigger: 'blur'}],
          remarks: [{required: true, message: '请输入审核备注', trigger: 'blur'}]
        }
      }
    },
    methods: {
      //打开弹窗
      open(option) {
        this.showDialog(option.title,option.row);
        this.audit.status = true;
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //提交表单
      submitForm: function () {
        if(this.audit.remarks==''){
          this.showWarn("请填写备注内容。")
        }else {
          this.$emit('auditResponse',this.audit)
        }
      },
      //显示弹窗
      showDialog(title,row) {
        this.resetForm(this.$refs.auditForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.audit.row = row;//对象数据
      }
    }
  }
</script>

