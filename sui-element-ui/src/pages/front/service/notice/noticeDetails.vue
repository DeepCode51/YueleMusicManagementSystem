<!-- 【通知详情】页面 -->
<template>
  <div class="front-body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <div content="article">
        <h1 class="font-shadow article-title">{{notice.title}}</h1>
        <span class="article-type" :style="dict.style" v-for="dict in noticeTypeDict" v-if="dict.dictValue==notice.noticeType">{{dict.dictLabel}}</span>
        <el-image v-if="notice.picture" class="article-image" :src="formatUrl(notice.picture)"></el-image>
        <div v-html="notice.content" style="white-space: pre-wrap;"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "notice",
    data() {
      return {
        //表单数据
        notice: {
          picture: "",
          title: "",
          noticeType: "",
          publishTime: "",
          content: ""
        },
      }
    },
    mounted(){
      let params = this.$route.query;
      this.getNotice(params.id);
    },
    watch: {
      '$route.query.id'(id) {
        if(id){
          this.getNotice(id);
        }
      }
    },
    computed: {
      //【通知分类】
      noticeTypeDict() {
        return this.$store.getters.getDictArray('notice_type');
      },
    },
    methods: {
      //根据id获取数据
      getNotice(id) {
        this.$request.doGet("/api/notice/get?id=" + id).then(res => {
          if (res.isOk) {
            this.notice = res.obj;
          }
        });
      },
    }
  }
</script>
<style scoped>
  .box-card {
    padding-bottom: 20px;
    min-height: 1000px
  }

  .article-title {
    font-size: 22px;
    margin-top: 20px;
    text-align: center
  }

  .article-type {
    font-size: 22px;
    display: block;
    font-size: small;
    margin-top: 20px;
    text-align: center;
    color: #999
  }

  .article-image {
    padding: 10px;
    border-radius: 10px;
    width: 100%;
    height: 350px;
  }
</style>
