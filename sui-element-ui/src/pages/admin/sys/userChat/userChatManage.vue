<!-- 【咨询信息】管理页面 -->
<template>
  <div class="body">
    <!-- 表单弹窗 -->
    <userChat-form ref="chatDialog"/>
    <div>
      <el-button type="primary" icon="el-icon-plus" style="margin: 0 auto;padding: 100px;display: block" @click="openDialog({pageFrom:'add',title:'在线咨询'})">在线解答</el-button>
    </div>
  </div>
</template>

<script>
  import userChatForm from './userChatForm.vue'
  export default {
    components: {userChatForm},
    data() {
      return {
      };
    },
    computed: {
    },
    methods: {
      //打开弹窗
      openDialog(option) {
        this.$refs.chatDialog.open(option);
      },
    }
  };
</script>
