<!-- 【歌曲信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="song" ref="songForm" :rules="rules" label-width="120px"  style="height: auto">
          <el-input :rows="30" type="textarea" v-model="song.lyric" placeholder="请输入歌词"/>
      </el-form>
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
          writeTime: "",
          url: "",
          lyric: "",
          introduction: ""
        },
        //表单校验
        rules: {
          picture: [{required: true, message: '请选择歌曲图片', trigger: 'blur'}],
          name: [{required: true, message: '请输入歌曲名称', trigger: 'blur'},{validator:this.$validator.unique,form:this,model:'song',tableName:'song',column:'name',message:'歌曲名称已存在',trigger:'blur'}],
          singerId: [{required: true, message: '请选择所属歌手', trigger: 'blur'}],
          writeTime: [{required: true, message: '请选择创作时间', trigger: 'blur'}],
          url: [{required: true, message: '请输入歌曲地址', trigger: 'blur'}],
          lyric: [{required: true, message: '请输入歌词', trigger: 'blur'}],
          introduction: [{required: true, message: '请输入简介', trigger: 'blur'}],
        }
      }
    },
    computed: {
    },
    methods: {
      //【所属歌手】
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
        this.getSingerDict();//加载【所属歌手】
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getSong(option.id);
        }
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
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.songForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      }
    }
  }
</script>

<style scoped lang="scss">
  .input /deep/ .el-input__inner {
    height: 360px;
  }
</style>
