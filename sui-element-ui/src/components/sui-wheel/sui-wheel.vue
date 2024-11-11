<template>
    <el-row class="wheel">
      <el-col :span="spanLeft">
        <el-carousel height="300px" style="border-radius: 5px">
          <el-carousel-item  v-for="item in wheelList" :key="item.id">
            <el-image @click="toPage({name:'noticeDetails',query:{id:item.id}})" class="wheel-picture pointer" :src="formatUrl(item.picture)"></el-image>
          </el-carousel-item>
        </el-carousel>
      </el-col>
      <el-col :span="spanRight" class="msg">
        <div class="noticeItem">
          <ul class="noticeList">
            <li class="pointer" @click="toPage({name:'noticeDetails',query:{id:item.id}})" v-for="(item,index) in wheelList" :key="item.id"><i style="color: white">{{index+1}}</i><a>{{item.title}}</a>
              <p>{{getContent(item.content)}}</p>
            </li>
          </ul>
        </div>
      </el-col>
    </el-row>
</template>
<script>
    export default {
      props: {
        showNotice: {
          type: Boolean,
          default:true
        },
      },
      data(){
        return{
          dataList:[],
        }
      },
      mounted(){
        this.getList();
      },
      computed:{
        spanLeft(){
          return this.showNotice ? 16:24
        },
        spanRight(){
           return this.showNotice ? 8:0
        },
        wheelList(){
          return this.dataList;
        },
      },
      methods: {
        getContent(str) {
          return str.replace(/<[^>]+>/g, "");  //正则去掉所有的html标记
        },
        //查询列表数据
        getList() {
          this.$request.doGet('/api/notice/getList', {}).then(res => {
            if (res.isOk) {
              this.dataList = res.list;
            }
          })
        },
      }
    };
</script>
<style scoped>
   ul,li{ padding:0;margin:0;list-style:none;font-size: 15px}
  .noticeList { width: 100%;padding: 10px;background:white;}
  .noticeList li { overflow: hidden; line-height: 32px;padding: 0 5px}
  .noticeList li a { height: 32px; }
  .noticeList li img { width: 100% }
  .noticeList li { height: 28px; }
  .noticeList li:hover { height: 100%; background: #f7f7f7 }
  .noticeList li:hover a { color: #000; font-weight: bold }
  .noticeList li:hover i { background: #222; }
  .noticeList li:nth-child(1) { height: 100%; background: #f7f7f7 }
  .noticeList li:nth-child(1) a { color: #000; font-weight: bold }
  .noticeList li:nth-child(1) a { color: #000; font-weight: bold }
  .noticeList li:nth-child(1) i { background: #9a9a9a; }
  .noticeList:hover li:nth-child(1) { height: 28px; background: none; }
  .noticeList:hover li:nth-child(1) a { color: #333; font-weight: normal }
  .noticeList:hover li:nth-child(1) i { background: #9a9a9a; }
  .noticeList:hover li:nth-child(1):hover { height: 100%; background: #f7f7f7; }
  .noticeList:hover li:nth-child(1):hover a { color: #000; font-weight: bold }
  .noticeList:hover li:nth-child(1):hover i { background: #222; }
  .noticeList p { line-height: 24px; font-size: 14px; overflow: hidden; text-overflow: ellipsis; -webkit-box-orient: vertical; display: -webkit-box; -webkit-line-clamp: 2; height: 48px; }
  .noticeItem{height: 250px;overflow-y: auto;border-radius: 5px}
  .noticeItem ul li i { line-height: 20px;padding-left: 5px; display: block; width: 20px; height: 20px; background: #9a9a9a; float: left; margin-top: 6px; margin-right: 20px; position: relative; font-style: normal }
  .noticeItem ul li i::before { position: absolute; left: 0; top: 0; font-size: 9px; color: #fff; line-height: 20px; width: 20px; text-align: center }
  .noticeItem ul li:first-child i { background: #222; }
  .wheel {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    background: rgba(255, 255, 255, 0.62)
  }
  .wheel-picture {
    height: 100%;
    width: 100%;
    border-radius: 5px;
  }
  .msg {
    padding-left: 10px;
  }
</style>
