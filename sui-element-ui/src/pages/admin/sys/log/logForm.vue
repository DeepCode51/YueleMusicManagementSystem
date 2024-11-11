<!-- 【日志信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="sysLog" ref="sysLogForm" :rules="rules" label-width="100px" class="edit-form">
        <el-form-item label="用户名称" prop="loginName">
          <el-input v-model="sysLog.loginName" placeholder="请输入用户名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <!--<el-form-item label="用户ID" prop="userId">-->
        <!--  <el-input v-model="sysLog.userId" placeholder="请输入用户ID" :disabled="dialog.disabled" class="sui-input"/>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="系统类型" prop="systemType">-->
        <!--  <el-select v-model="sysLog.systemType" placeholder="选择系统类型" :disabled="dialog.disabled" class="sui-input">-->
        <!--    <el-option v-for="dict in systemTypeDict" :key="dict.dictValue" :label="dict.dictLabel" :value="dict.dictValue"/>-->
        <!--  </el-select>-->
        <!--</el-form-item>-->
        <el-form-item label="模块名称" prop="moduleName">
          <el-input v-model="sysLog.moduleName" placeholder="请输入模块名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="方法类型" prop="method">
          <el-input v-model="sysLog.method" placeholder="请输入方法类型" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="操作日期" prop="operationDate">
          <el-date-picker v-model="sysLog.operationDate" placeholder="选择操作日期" :disabled="dialog.disabled" type="date" value-format="yyyy-MM-dd" class="sui-input"/>
        </el-form-item>
        <el-form-item label="请求URI" prop="requestUri">
          <el-input v-model="sysLog.requestUri" placeholder="请输入请求URI" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="请求端口" prop="remotePort">
          <el-input v-model="sysLog.remotePort" placeholder="请输入请求端口" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="本地主机" prop="localName">
          <el-input v-model="sysLog.localName" placeholder="请输入本地主机" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="本地地址" prop="localAddr">
          <el-input v-model="sysLog.localAddr" placeholder="请输入本地地址" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="远程主机" prop="remoteHost">
          <el-input v-model="sysLog.remoteHost" placeholder="请输入远程主机" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="远程地址" prop="remoteAddr">
          <el-input v-model="sysLog.remoteAddr" placeholder="请输入远程地址" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" v-if="!dialog.disabled" type="primary" @click="submitForm('sysLogForm')">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    components: {

    },
    name: "sysLogForm",
    data() {
      return {
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        sysLog: {
          loginName: "",
          userId: "",
          systemType: "",
          moduleName: "",
          method: "",
          operationDate: "",
          requestUri: "",
          remotePort: "",
          localName: "",
          localAddr: "",
          remoteHost: "",
          remoteAddr: ""
        },
        //表单校验
        rules: {
          loginName: [{required: true, message: '请输入用户名称', trigger: 'blur'}],
          userId: [{required: true, message: '请输入用户ID', trigger: 'blur'}],
          systemType: [{required: true, message: '请选择系统类型', trigger: 'blur'}],
          moduleName: [{required: true, message: '请输入模块名称', trigger: 'blur'}],
          method: [{required: true, message: '请输入方法类型', trigger: 'blur'}],
          operationDate: [{required: true, message: '请选择操作日期', trigger: 'blur'}],
          requestUri: [{required: true, message: '请输入请求URI', trigger: 'blur'}],
          remotePort: [{required: true, message: '请输入请求端口', trigger: 'blur'}],
          localName: [{required: true, message: '请输入本地主机', trigger: 'blur'}],
          localAddr: [{required: true, message: '请输入本地地址', trigger: 'blur'}],
          remoteHost: [{required: true, message: '请输入远程主机', trigger: 'blur'}],
          remoteAddr: [{required: true, message: '请输入远程地址', trigger: 'blur'}]
        }
      }
    },
    computed: {
      //【系统类型】
      systemTypeDict() {
        return this.$store.getters.getDictArray('system_type');
      },
    },
    methods: {
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSysLog(option.id)
        }
      },
      //根据id获取数据
      getSysLog(id) {
        this.$request.doGet("/admin/sysLog/get?id="+id).then(res => {
          if (res.isOk) {
            this.sysLog = res.obj;
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
            that.$request.doPost('/admin/sysLog/sub', that.sysLog).then(res => {
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
        this.resetForm(this.$refs.sysLogForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped>
</style>
