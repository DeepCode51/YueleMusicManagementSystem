<!-- 【任务信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysQuartz" ref="sysQuartzForm" :rules="rules" label-width="120px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="sysQuartz.id"/>
        </el-form-item>
        <el-form-item label="名称" prop="jobName">
          <el-input v-model="sysQuartz.jobName" placeholder="请输入名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="类型" prop="jobType">
          <el-select v-model="sysQuartz.jobType" placeholder="请选择类型" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in jobTypeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue" class="sui-input"/>
          </el-select>
        </el-form-item>
        <el-form-item label="表达式" prop="cronExpression">
          <el-input v-model="sysQuartz.cronExpression" placeholder="请输入表达式" :disabled="dialog.disabled" class="sui-input"/><a class="el-button" href="https://cron.qqe2.com/" target="view_window">cron表达式</a>
        </el-form-item>
        <el-form-item label="目标对象" prop="beanName">
          <el-input v-model="sysQuartz.beanName" placeholder="请输入目标对象(beanName)" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="目标方法" prop="methodName">
          <el-input v-model="sysQuartz.methodName" placeholder="请输入目标方法" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="是否有参数" prop="hasParams">
          <el-select v-model="sysQuartz.hasParams" placeholder="请选择是否有参数" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in hasParamsDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item v-if="sysQuartz.hasParams==1" label="参数" prop="params">
          <el-input v-model="sysQuartz.params" placeholder="请输入参数" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="开启|关闭" prop="isExec">
          <el-select v-model="sysQuartz.isExec" placeholder="请选择是否执行" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in isExecDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态" prop="jobStatus">
          <el-select v-model="sysQuartz.jobStatus" placeholder="请选择状态" :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="dict in jobStatusDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>
          </el-select>
        </el-form-item>
        <el-form-item label="最后执行时间" prop="lastExecTime">
          <el-date-picker v-model="sysQuartz.lastExecTime" placeholder="请选择最后执行时间" :disabled="dialog.disabled" type="date" value-format="yyyy-MM-dd" class="sui-input"/>
        </el-form-item>
        <el-form-item label="备注信息" prop="remarks">
          <el-input type="textarea" v-model="sysQuartz.remarks" placeholder="请输入备注信息" :disabled="dialog.disabled" class="sui-textarea"/>
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
    name: "sysQuartzForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysQuartz: {
          jobName: "",
          beanName: "",
          params: "",
          cronExpression: "",
          jobType: "",
          hasParams: "",
          methodName: "",
          jobStatus: "",
          isExec: "",
          isBootExec: "",
          lastExecTime: "",
          remarks: ""
        },
        //表单校验
        rules: {
          jobName: [{required: true, message: '请输入名称', trigger: 'blur'}],
          beanName: [{required: true, message: '请输入目标对象', trigger: 'blur'}],
          params: [{required: true, message: '请输入参数', trigger: 'blur'}],
          cronExpression: [{required: true, message: '请输入表达式', trigger: 'blur'}],
          jobType: [{required: true, message: '请选择类型', trigger: 'blur'}],
          hasParams: [{required: true, message: '请选择是否有参数', trigger: 'blur'}],
          methodName: [{required: true, message: '请输入目标方法', trigger: 'blur'}],
          jobStatus: [{required: true, message: '请选择状态', trigger: 'blur'}],
          isExec: [{required: true, message: '请选择是否执行', trigger: 'blur'}],
          isBootExec: [{required: true, message: '请选择启动时执行', trigger: 'blur'}],
          lastExecTime: [{required: true, message: '请选择最后执行时间', trigger: 'blur'}],
          remarks: [{required: true, message: '请输入备注信息', trigger: 'blur'}]
        }
      }
    },
    computed: {
      //【类型】
      jobTypeDict() {
        return this.$store.getters.getDictArray('job_type');
      },
      //【是否有参数】
      hasParamsDict() {
        return this.$store.getters.getDictArray('sys_yes_no');
      },
      //【状态】
      jobStatusDict() {
        return this.$store.getters.getDictArray('job_status');
      },
      //【开启|关闭】
      isExecDict() {
        return this.$store.getters.getDictArray('is_exec');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSysQuartz(option.id);
        }
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSysQuartz(id) {
        this.$request.doGet("/admin/sysQuartz/get?id=" + id).then(res => {
          if (res.isOk) {
            this.sysQuartz = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.sysQuartzForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/sysQuartz/sub', that.sysQuartz).then(res => {
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
        this.resetForm(this.$refs.sysQuartzForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>

</style>
