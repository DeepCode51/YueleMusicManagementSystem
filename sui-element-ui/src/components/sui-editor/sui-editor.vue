<template>
  <div style="background: white;border-radius: 4px;">
    <el-upload
      :disabled="this.disabled"
      style="display: none"
      class="avatar-uploader"
      action="/admin/sysAttach/uploadFile/image/editor"
      :show-file-list="false"
      :on-success="handleAvatarSuccess"
      :before-upload="beforeAvatarUpload">
      <el-button size="small" style="display: none" class="upload-btn" type="primary">文件上传</el-button>
    </el-upload>
    <quill-editor
      @blur="handleBlur"
      @change="handleChange"
      :style="'height:'+height+'px;width:100%'"
      v-model="content"
      :options="editorOption"
      ref="quillEditor">
    </quill-editor>
  </div>
</template>
<script>
  // 工具栏配置
  const toolbarOptions = [
    ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
    // ['blockquote', 'code-block'],
    [{'header': 1}, {'header': 2}],               // custom button values
    [{'list': 'ordered'}, {'list': 'bullet'}],
    [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
    [{'indent': '-1'}, {'indent': '+1'}],          // outdent/indent
    // [{'direction': 'rtl'}],                         // text direction
    // [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
    // [{'header': [1, 2, 3, 4, 5, 6, false]}],
    // [{'color': []}],
    // [{'background': []}],          // dropdown with defaults from theme
    // [{'font': []}],
    // [{'align': []}],
    ['image'],
    ['clean'],
  ]
  // 标题
  const titleConfig = {
    'ql-bold':'加粗',
    'ql-color':'颜色',
    'ql-font':'字体',
    'ql-code':'插入代码',
    'ql-italic':'斜体',
    'ql-link':'添加链接',
    'ql-background':'背景颜色',
    'ql-size':'字体大小',
    'ql-strike':'删除线',
    'ql-script':'上标/下标',
    'ql-underline':'下划线',
    'ql-blockquote':'引用',
    'ql-header':'标题',
    'ql-indent':'缩进',
    'ql-list':'列表',
    'ql-align':'文本对齐',
    'ql-direction':'文本方向',
    'ql-code-block':'代码块',
    'ql-formula':'公式',
    'ql-image':'图片',
    'ql-video':'视频',
    'ql-clean':'清除字体样式',
    'ql-upload':'文件'
  };
  export default {
    model:{
      prop:'content',
      event:'contentChange'
    },
    props: {
      validateEvent: {
        type: Boolean,
        default: true
      },
      height: {
        type: Number,
        default:300
      },
      content: {
        type: String,
        default: ''
      },
      placeholder: {
        type: String,
        default: '请输入内容......'
      },
      disabled: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        editorOption: {
          placeholder: this.placeholder,
          readOnly: this.disabled,
          modules: {
            toolbar: {
              container: toolbarOptions,  // 工具栏
              handlers: {
                'image': function (value) {
                  if (value) {
                    // 调用iview图片上传
                    document.querySelector('.upload-btn').click()
                  } else {
                    this.quill.format('image', false);
                  }
                }
              }
            }
          }
        }
      }
    },
    mounted () {
      this.addQuillTitle();
      this.setEditor();
    },
    methods: {
      setEditor(){
        document.querySelector('.quill-editor').disabled=this.disabled;
        this.$refs.quillEditor.quill.enable(!this.disabled);
        if(this.disabled){
          document.querySelector('.quill-editor').style.background="#F5F7FA";
          document.querySelector('.quill-editor').style.color="#C0C4CC";
          document.querySelector('.ql-editor').style.background="#F5F7FA";
        }else{
          document.querySelector('.quill-editor').style.background="white";
          document.querySelector('.quill-editor').style.color="#606266";
          document.querySelector('.ql-editor').style.background="white";
        }
      },
      handleChange(e){
        const that = this;
        that.$nextTick(() => {
          that.setEditor();
          that.$emit('contentChange',e.html);
        });
      },
      //图片显示全进行简单处理:提供给外部调用。
      getFormatContent(){
        return this.content.replace(/\<img/gi, '<img style=max-width:100%;height:auto');
      },
      handleBlur(event) {
        this.$nextTick(() => {
          if(this.content){
            let prop = this.$parent._props.prop;
            if(prop){
              this.$parent.elForm.clearValidate(prop)
            }
          }
        });
      },
      addQuillTitle() {
        const oToolBar = document.querySelector('.ql-toolbar'),
          aButton = oToolBar.querySelectorAll('button'),
          aSelect = oToolBar.querySelectorAll('select');
        aButton.forEach(function (item) {
          if (item.className === 'ql-script') {
            item.value === 'sub' ? item.title = '下标' : item.title = '上标';
          } else if (item.className === 'ql-indent') {
            item.value === '+1' ? item.title = '向右缩进' : item.title = '向左缩进';
          } else {
            item.title = titleConfig[item.classList[0]];
          }
        });
        aSelect.forEach(function(item){
          item.parentNode.title = titleConfig[item.classList[0]];
        });
      },
      handleAvatarSuccess(res, file) {
        if (res.isOk) {
          let quill = this.$refs.quillEditor.quill
          // 获取光标所在位置
          let length = quill.getSelection().index;
          // 插入图片，res为服务器返回的图片链接地址
          quill.insertEmbed(length, 'image', this.formatUrl(res.obj.savePath))
          // 调整光标到最后
          quill.setSelection(length + 1)
        } else {
          // 提示信息，需引入Message
          this.showWarn("图片上传失败")
        }
      },
      beforeAvatarUpload(file) {
        if(this.type==''){
          console.error("请设置附件类型")
        }
        const isWebp = file.type === 'image/webp';
        const isJPG = file.type === 'image/jpeg';
        const isPng = file.type === 'image/png';
        const isLt5M = file.size / 1024 / 1024 < 5;
        if (!(isJPG||isPng||isWebp)) {
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

<style scoped>
  .quill-editor{
    white-space: pre-wrap;
  }
  .quill-editor /deep/ .ql-toolbar.ql-snow{
    margin-top: -40px!important;
    border-radius: 4px 4px 0 0;
  }
  .quill-editor /deep/ .ql-snow *{
    border-radius: 0 0 4px 4px!important;
  }
  .quill-editor /deep/ .ql-container.ql-snow {
    border-radius: 0 0 4px 4px;
  }
</style>

