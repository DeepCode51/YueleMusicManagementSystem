<template>
  <el-card shadow="hover" class="pointer">
    <el-row>
      <el-col :span="3">
        <el-image class="picture" :src="formatUrl(imgsrc)"></el-image>
      </el-col>
      <el-col :span="21">
        <div>
          <span class="title">{{title}}</span>
          <div class="font-overflow-2" v-html="delHtmlTag(content)"></div>
          <div>
            <span class="el-icon-time time"> {{date}}</span>
            <el-button type="text" class="float-r">查看详情</el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
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
      }
    },
    methods:{
      delHtmlTag(str){
        return str.replace(/<[^>]+>/g,"");  //正则去掉所有的html标记
      }
    }
  };
</script>
<style scoped>
  .picture {
    height: 100px;
    width: 120px;
    border-radius: 5px
  }

  .title {
    text-align: center;
    display: block;
    font-weight: bold
  }

  .time {
    font-size: 13px;
    color: #666;
    margin-top: 20px
  }
</style>
