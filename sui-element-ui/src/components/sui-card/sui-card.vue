<template>
<!-- 要 picture、name(或者title)、id-->
    <div :style="'width:'+(100/column).toFixed(2)+'%'" class="card-body">
      <el-card shadow="hover" class="pointer">
        <div style="height: 210px">
          <el-image class="card-img" :src="formatUrl(imgsrc)"></el-image>
          <b class="font-overflow card-title">{{title}}</b>
          <div v-html="delHtmlTag(content)" class="content"></div>
        </div>
        <div class="card-footer">
          <span class="footer-time"><i class="el-icon-time"></i> {{date.split(' ')[0]}}</span>
          <el-button class="footer-btn" type="text" size="mini">查看详情</el-button>
        </div>
      </el-card>
    </div>
</template>
<script>
    export default {
      props: {
        title: {
          type: String,
          default: ""
        },
        imgsrc: {
          type: String,
          default: ""
        },
        content: {
          type: String,
          default: ""
        },
        date: {
          type: String,
          default: ""
        },
        column: {
          type: Number,
          default: 4
        },
      },
      methods: {
        delHtmlTag(str){
          return str.replace(/<[^>]+>/g,"");  //正则去掉所有的html标记
        }
      }
    };
</script>
<style scoped>
  .card-body {
    float: left;
    padding: 5px;
  }
  .content {
    font-size: 14px;
    color: #999;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .card-img {
    border-radius: 5px;
    height: 150px;
    width: 100%
  }

  .card-title {
    display: block;
    font-family: cursive
  }

  .footer-time {
    float: left;
    font-size: 14px;
    color: #999;
    margin-top: 5px;
  }

  .footer-btn {
    float: right;
    margin-bottom: 5px
  }
</style>
