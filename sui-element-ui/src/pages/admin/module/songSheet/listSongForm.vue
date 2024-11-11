<!-- 【歌单歌曲信息】弹窗页面 -->
<template>
  <div class="body">
    <el-dialog width="800px" :title="dialog.title" :close-on-click-modal="false" :visible.sync="dialog.open">
      <el-form :model="listSong" ref="listSongForm" :rules="rules" label-width="120px" class="edit-form">
        <el-transfer
          :titles="['未选歌曲', '已选歌曲']"
          :props="{
            key: 'id',
            label: 'name'
          }"
          v-model="listSong.songIds"
          :data="songDict">
        </el-transfer>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="cancel">取 消</el-button>
        <el-button size="small" type="primary" @click="submitForm()">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "listSongForm",
    data() {
      return {
        songDict:[],
        //弹窗设置
        dialog: {
          title: "",
          open: false,
          disabled: false
        },
        //表单数据
        listSong: {
          songIds: [],
          songSheetId: "",
        },
      }
    },
    computed: {
    },
    methods: {
      //【所属歌曲】
      getSongDict() {
        this.$request.doGet('/admin/song/getList').then(res => {
          if (res.isOk) {
            this.songDict = res.list;
          }
        })
      },
      //打开弹窗
      open(option) {
        this.showDialog(option.title, true);
        this.listSong.songSheetId = option.songSheetId;
        this.getDefaultList();//加载【初始歌单】
        this.getSongDict();//加载【所属歌曲】
      },
      //显示弹窗
      showDialog(title, disabled) {
        this.resetForm(this.$refs.listSongForm);//重置表单
        this.dialog.open = true;//打开弹窗
        this.dialog.title = title;//设置标题
        this.dialog.disabled = disabled;//是否可编辑
      },
      //取消弹窗
      cancel: function () {
        this.dialog.open = false;
      },
      getDefaultList() {
        this.$request.doGet('/admin/listSong/getList',{songSheetId:this.listSong.songSheetId}).then(res => {
          if (res.isOk) {
            this.listSong.songIds= res.list.map(function (v) {
              return v.songId
            });
          }
        })
      },
      //提交表单
      submitForm: function () {
        const that = this;
        that.$refs.listSongForm.validate((valid) => {
          if (valid) {//表单校验
            that.$request.doPost('/admin/listSong/sub', that.listSong).then(res => {
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
