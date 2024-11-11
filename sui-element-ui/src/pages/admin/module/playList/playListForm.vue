<!-- 【播放清单信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="playList" ref="playListForm" :rules="rules" label-width="120px" class="edit-form">
        <el-form-item label="主键id" class="hidden" prop="id">
          <el-input type="hidden" v-model="playList.id"/>
        </el-form-item>
        <el-form-item label="所属用户" prop="userId">
          <el-select v-model="playList.userId" placeholder="请选择所属用户" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="register in registerDict" :key="register.id" :value="register.id" :label="register.loginName"/>
          </el-select>
        </el-form-item>
        <el-form-item label="所属音乐" prop="songId">
          <el-select v-model="playList.songId" placeholder="请选择所属音乐" filterable :disabled="dialog.disabled" class="sui-input">
            <el-option v-for="song in songDict" :key="song.id" :value="song.id" :label="song.name"/>
          </el-select>
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
    name: "playListForm",
    data() {
      return {
        registerDict:[],
        songDict:[],
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        playList: {
          id: "",
          userId: "",
          songId: "",
        },
        //表单校验
        rules: {
          userId: [{required: true, message: '请选择所属用户', trigger: 'change'}],
          songId: [{required: true, message: '请选择所属音乐', trigger: 'change'}],
        }
      }
    },
    computed: {
    },
    methods: {
      //【所属用户】
      getRegisterDict() {
        this.$request.doGet('/admin/register/getList').then(res => {
          if (res.isOk) {
            this.registerDict = res.list;
          }
        })
      },
      //【所属音乐】
      getSongDict() {
        this.$request.doGet('/admin/song/getList').then(res => {
          if (res.isOk) {
            this.songDict = res.list;
          }
        })
      },
      //打开弹窗
      open(option) {
        const pageFrom = option.pageFrom;
        const disabled = pageFrom == 'view' ? true : false;
        this.showDialog(option.title, disabled);
        this.getRegisterDict();//加载【所属用户】
        this.getSongDict();//加载【所属音乐】
        if (pageFrom == 'edit' || pageFrom == 'view') {
          this.getPlayList(option.id);
        }
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.playListForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      //根据id获取数据
      getPlayList(id) {
        this.$request.doGet("/admin/playList/get?id=" + id).then(res => {
          if (res.isOk) {
            this.playList = res.obj;
          }
        });
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.playListForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/playList/sub', that.playList).then(res => {
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
