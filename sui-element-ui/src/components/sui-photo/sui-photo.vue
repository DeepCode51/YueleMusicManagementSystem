<template>
    <el-upload
      :disabled="this.disabled"
      class="avatar-uploader"
      :action="'/admin/sysAttach/uploadFile/image/'+type"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :on-change="handleChange"
    >
    <el-image v-if="imgUrl" :src="imgUrl" class="avatar">
      <div slot="error" class="image-slot">
        <i class="el-icon-picture-outline" style="font-size: 100px"></i>
      </div>
    </el-image>
    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
</template>
<script>
  export default {
    props: {
      value: {
        type: String,
        default: ''
      },
      type: {
        type: String,
        default: ''
      },
      style: {
        type: String,
        default: ''
      },
      disabled: {
        type: Boolean,
        default:false
      }
    },
    data() {
      return {
        imgUrl:"",
      }
    },
    watch: {
      value(value){
        this.imgUrl = this.formatUrl(this.value)
      }
    },
    mounted() {
      this.imgUrl = this.formatUrl(this.value)
    },
    methods: {
      handleChange(event) {
        this.$nextTick(() => {
          if(this.value){
            let prop = this.$parent._props.prop;
            if(prop){
              this.$parent.elForm.clearValidate(prop)
            }
          }
        });
      },
      handleAvatarSuccess(res, file) {
        this.$emit('input', res.obj.savePath);
      },
      beforeAvatarUpload(file) {
        if(this.type==''){
          console.error("请设置附件类型")
          return
        }
        const isImg = (file.type === 'image/jpeg' || file.type === 'image/webp'|| file.type === 'image/png');
        const isLt5M = file.size / 1024 / 1024 < 5;
        if (!isImg) {
          this.$message.error('只能上传 jpg|png|webp 格式的图片！');
          return false
        }
        if (!isLt5M) {
          this.$message.error('上传头像图片大小不能超过 5MB！');
          return false
        }
        return true;
      }
    }
  }
</script>
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
