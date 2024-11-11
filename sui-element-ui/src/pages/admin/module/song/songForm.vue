<!-- 【歌曲信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="song" ref="songForm" :rules="rules" label-width="120px" class="edit-form">
        <el-form-item label="主键" class="hidden" prop="id">
          <el-input type="hidden" v-model="song.id"/>
        </el-form-item>
        <el-form-item label="歌曲图片" prop="picture">
          <sui-photo type="song" v-model="song.picture" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="歌曲名称" prop="name">
          <el-input v-model="song.name" placeholder="请输入歌曲名称" :disabled="dialog.disabled" class="sui-input"/>
        </el-form-item>
        <el-form-item label="歌手姓名" prop="singerId">
          <el-select v-model="song.singerId" placeholder="请选择歌手姓名" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="singer in singerDict" :key="singer.id" :value="singer.id" :label="singer.name"/>
          </el-select>
        </el-form-item>
        <el-form-item label="歌曲歌词" prop="lyric">
          <el-input type="textarea" v-model="song.lyric" placeholder="请输入歌曲歌词" :disabled="dialog.disabled" class="sui-textarea"/>
        </el-form-item>
        <el-form-item label="歌曲简介" prop="introduction" class="sui-editor-item">
          <sui-editor v-model="song.introduction" placeholder="请输入歌曲简介" :disabled="dialog.disabled"/>
        </el-form-item>
        <el-form-item label="歌曲文件" prop="fileList">
          <sui-file type="song" :limit="1" :refId="song.id" v-model="song.fileList" v-if="dialog.open" :disabled="dialog.disabled"/>
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
    name: "songForm",
    data() {
      return {
        singerDict:[],
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        song: {
          picture: "",
          name: "",
          singerId: "",
          lyric: "",
          introduction: "",
        },
        //表单校验
        rules: {
          picture: [{required: true, message: '请选择歌曲图片', trigger: 'change'}],
          name: [{required: true, message: '请输入歌曲名称', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'song',tableName:'song',column:'name',message:'歌曲名称已存在',trigger:'blur'}],
          singerId: [{required: true, message: '请选择歌手姓名', trigger: 'change'}],
          lyric: [{required: true, message: '请输入歌曲歌词', trigger: 'blur'}],
          introduction: [{required: true, message: '请输入歌曲简介', trigger: 'blur'}],
          fileList: [{required: true,type:'array', message: '请选择文件上传', trigger: 'blur'}],
        }
      }
    },
    computed: {
    },
    methods: {
      //【歌手姓名】
      getSingerDict() {
        this.$request.doGet('/admin/singer/getList').then(res => {
          if (res.isOk) {
            this.singerDict = res.list;
          }
        })
      },
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        this.getSingerDict();//加载【歌手姓名】
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSong(option.id);
        }
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.songForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getSong(id) {
        this.$request.doGet("/admin/song/get?id=" + id).then(res => {
          if (res.isOk) {
            this.song = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.songForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/song/sub', that.song).then(res => {
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
