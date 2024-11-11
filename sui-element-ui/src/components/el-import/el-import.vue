<template>
  <div>
    <el-button :type="type" :icon="icon" :disabled="disabled" :size="size" @click="importData">{{title}}</el-button>
    <el-dialog
      style="margin-top: 300px"
      title="温馨提示"
      :visible.sync="dialogVisible"
      width="20%"
      >
      <el-upload
        class="upload-demo"
        ref="upload"
        :action="action"
        :auto-upload="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        :file-list="fileList">
        <el-button size="small">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传xls/xlsx文件，且不超过5MB!</div>
      </el-upload>
      <span slot="footer" class="dialog-footer">
        <el-button type="success" size="mini" @click="template">下载模板</el-button>
        <el-button size="mini" @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" size="mini"  @click="submitUpload">提 交</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
  export default {
    props: {
      title: {
        type: String,
        default: ''
      },
      action: {
        type: String,
        default: ''
      },
      disabled: {
        type: Boolean,
        default: false
      },
      type: {
        type: String,
        default: 'primary'
      },
      icon: {
        type: String,
        default: 'el-icon-upload2'
      },
      size: {
        type: String,
        default: 'mini'
      }
    },
    data(){
      return{
        fileList: [],
        dialogVisible:false,
      }
    },
    methods:{
      template(){
        location.href=this.action+'/template';
      },
      submitUpload() {
        this.$refs.upload.submit();
      },
      //导入
      importData() {
        this.fileList=[];
        this.dialogVisible = true;
      },
      handleAvatarSuccess(res, file) {
        const that = this;
        if(res.isOk){
          that.showSuccess("导入成功");
          setTimeout(function () {
            that.$emit("success");
            that.dialogVisible = false;
          },1000)
        }
      },
      beforeAvatarUpload(file) {
        let name = file.name.substring(file.name.lastIndexOf("."));
        const isEXECL = (name=='.xls'||name=='.xlsx');
        const isLt5M = file.size / 1024 / 1024 < 5;
        if (!isEXECL) {
          this.showWarn('上传文件只能是 EXECL 格式!');
        }
        if (!isLt5M) {
          this.showWarn('上传文件不能超过 5MB!');
        }
        return isEXECL && isLt5M;
      }
    }
  }
</script>
