<template>
  <div class="body" style="clear: both">
    <el-carousel :interval="5000">
      <el-carousel-item v-for="item in dataList" :key="item">
        <el-image :src="formatUrl(item.picture)" style="width: 100%;height: 100%;"/>
        <div style="height: 100px;background: rgba(76,135,188,0.29);position: relative;top: -30px;padding: 0 10px;font-weight: bold;color: white">
          {{item.content}}
        </div>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>
<script>
  export default {
    data(){
      return{
        dataList:[]
      }
    },
    mounted() {
      this.getList()
    },
    methods:{
      getList() {
        this.$request.doGet('/api/notice/getPage', this.queryForm).then(res => {
          if (res.isOk) {
            this.dataList = res.obj.records;
          }
        })
      }
    }
  };
</script>
