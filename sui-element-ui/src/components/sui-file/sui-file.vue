<template>
  <div id="file" class="body">
    <el-upload
      list-type="picture-card"
      :class="{ 'hide-upload-btn': hideUpload||disabled }"
      :limit="limit"
      :on-preview="handlePreview"
      :multiple="true"
      :disabled="disabled"
      :action="'/admin/sysAttach/uploadFile/file/'+type"
      :before-remove="beforeRemove"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload"
      :file-list="fileList"
      :handleRemove="handleRemove"
      :on-change="onChange"
    >
      <!--txt:pdf:img-->
      <sui-view ref="view"></sui-view>
      <!--视频-->
      <sui-video ref="video"></sui-video>
      <!--音频-->
      <sui-audio ref="audio"></sui-audio>

      <i slot="default" class="el-icon-plus"></i>
      <div slot="file" style="height: 100%" :title="file.name" slot-scope="{file}">
        <el-image class="el-upload-list__item-thumbnail" :src="file.url" title="上传" alt="上传">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline" style="font-size: 150px"></i>
          </div>
        </el-image>
        <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePreview(file)" title="查看" alt="查看">
              <i class="el-icon-video-play"></i>
            </span>
            <span class="el-upload-list__item-delete" @click="handleDownload(file)" title="下载" alt="下载">
              <i class="el-icon-download"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="beforeRemove(file)" title="删除" alt="删除">
            <i class="el-icon-delete"></i>
            </span>
          </span>
      </div>
    </el-upload>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        fileList: [],//不能进行修改，否则报错Cannot set property 'status' of null
        hideUpload: false,
      }
    },
    model:{
      prop:'file',
      event:'fileChange'
    },
    props: {
      file: {
        type: Array,
        default: []
      },
      placeholder: {
        type: String,
        default: '只能上传jpg/png文件，且不超过500kb'
      },
      type: {
        type: String,
        default: ''
      },
      refId: {
        type: String,
        default: ''
      },
      disabled: {
        type: Boolean,
        default: false
      },
      limit: {
        type: Number,
        default: 10
      }
    },
    watch: {
      refId(refId) {
        if (this.refId != '') {
          this.getFileList(this.refId)
        } else {
          this.fileList = []
        }
      }
    },
    mounted() {
      this.getFileList(this.refId)
    },
    methods: {
      onChange(file, fileList) {
        this.hideUpload = fileList.length >= this.limit;
      },
      handleRemove(file, fileList) {
        this.hideUpload = fileList.length >= this.limit;
      },
      handleDownload(file) {
        // 生成一个 a 标签
        const a = document.createElement('a');
        // 创建一个点击事件
        const event = new MouseEvent('click');
        // 将 a 的 download 属性设置为我们想要下载的图片的名称，若 name 不存在则使用'图片'作为默认名称
        a.download = file.name || '文件';
        // 将生成的 URL 设置为 a.href 属性
        a.href = "/admin/sysAttach/downFile?id="+file.id;
        // 触发 a 的点击事件
        a.dispatchEvent(event);
        const loading = this.$loading({
          lock: false,
          text: '正在下载,请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(232,244,255,0.25)'
        });
        setTimeout(function () {
          loading.close();
        },1500)
      },
      //预览文件
      handlePreview(file) {
        file.src = file.response ? this.formatUrl(file.response.obj.savePath) : file.src;
        const name = file.name.toLowerCase();
        if (/.(gif|jpg|jpeg|png|webp)$/.test(name)) {
          this.$refs.view.imgViewer(file)
        }else if(/.(mp3|wav|wma)$/.test(name)){
          this.$refs.audio.play(file);
        }else if(/.(mp4|wmv|rmvb|3gp|avi|mkv)$/.test(name)){
          this.$refs.video.play(file);
        }else if(/.(txt|pdf)$/.test(name)){
          this.$refs.view.iframeViewer(file);
        }else {
          this.showWarn("该文件类型需要下载预览");
        }
      },
      //查询文件
      getFileList(refId) {
        const that = this;
        this.$request.getFileHistory({refId: refId}).then(res => {
          if (res.isOk) {
            that.fileList = res.list.map(function (v) {
              let url = that.getFilePicture(v.savePath);
              return {id: v.id, name: v.remarks, url:url,src:that.formatUrl(v.savePath)}
            });
            that.hideUpload = that.fileList.length >= that.limit;
            let fileIds = that.fileList.map(function (v) {
              return v.id
            });
            that.$emit('fileChange', fileIds);
          }
        })
      },
      //上传成功
      handleAvatarSuccess(res,file,fileList) {
        if(!res.isOk){
          this.deleteFile(file);
          this.showWarn(res.info);
          return
        }
        file.url = this.getFilePicture(file.response.obj.savePath);
        let files = fileList.filter(function (v) {
          if(v.status=='success'){
            return v
          }
        });
        let fileIds = files.map(function (v) {
          if(v.id){
            return v.id
          }else{
            return v.response.obj.id
          }
        });
        this.fileList = fileList;
        this.$emit('fileChange', fileIds);
      },
      beforeAvatarUpload(file) {
        if(this.type==''){
          console.error("请设置附件类型");
          return
        }
      },
      //移除文件
      beforeRemove(file, fileList) {
        const that = this;
        that.showConfirm("确认删除文件吗?", function () {
          that.deleteFile(file);
        });
        return false; // 这是重点,不管上面的操作结果如何都返回false
      },
      //删除方法
      deleteFile(file) {
        const that = this;
        that.$request.deleteFile({id: file.id}).then(res => {
          if (res.isOk) {
            that.fileList = that.fileList.filter(function (v) {
              if (v.uid !== file.uid) {
                return v
              }
            });
            that.hideUpload = that.fileList.length >= that.limit;
          }
        })
      },
      //获取文件图片
      getFilePicture(savePath){
        let name = savePath.toLowerCase();
        let inco = "/static/images/admin/config/inco/other.png";
        if (/.(gif|jpg|jpeg|png|webp)$/.test(name)) {
          inco =  this.formatUrl(savePath)
        } else if (/.(xlsx|xls)$/.test(name)) {
          inco = "/static/images/admin/config/inco/xls.png";
        } else if (/.(docx|doc|rtf)$/.test(name)) {
          inco = "/static/images/admin/config/inco/word.png";
        } else if (/.(txt)$/.test(name)) {
          inco = "/static/images/admin/config/inco/txt.png";
        } else if (/.(zip)$/.test(name)) {
          inco = "/static/images/admin/config/inco/zip.png";
        } else if (/.(pdf)$/.test(name)) {
          inco = "/static/images/admin/config/inco/pdf.png";
        } else if (/.(ppt)$/.test(name)) {
          inco = "/static/images/admin/config/inco/ppt.png";
        } else if (/.(txt)$/.test(name)) {
          inco = "/static/images/admin/config/inco/txt.png";
        } else if (/.(mp3|wav|wma)$/.test(name)) {
          inco = "/static/images/admin/config/inco/audio.png";
        } else if (/.(mp4|wmv|rmvb|3gp|avi|mkv)$/.test(name)) {
          inco = "/static/images/admin/config/inco/video.png";
        }else {
          inco = "/static/images/admin/config/inco/other.png";
        }
        return inco;
      },
    },
  }
</script>
<style scoped>
  .hide-upload-btn /deep/ .el-upload--picture-card {
    display: none;
  }
</style>
