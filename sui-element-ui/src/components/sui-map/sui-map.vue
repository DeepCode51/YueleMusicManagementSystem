<template>
  <div>
    <div v-if="showLine" style="padding-bottom: 10px">
      <el-input v-model="queryForm.startAddr" placeholder="请输入起始点" size="small" style="width: 300px" clearable/> <i class="el-icon-d-arrow-right"></i>
      <el-input v-model="queryForm.endAddr" placeholder="请输入目的地" size="small" style="width: 300px" clearable/>
      <el-button size="mini" style="margin-left: 5px" type="primary" @click="searchLine()">查询路线</el-button>
    </div>
    <div v-else style="padding-bottom: 10px">
      <el-input v-model="queryForm.addr" placeholder="请输入地点" size="small" style="width: 300px" clearable/> <i class="el-icon-location-information"></i>
      <el-button size="mini" style="margin-left: 5px" type="primary" @click="searchAddr()">查询位置</el-button>
    </div>
    <baidu-map class="addrClass" :class="[{'lineClass':showLine}]"  :center="center" :zoom="zoom" :scroll-wheel-zoom="true">
      <bm-local-search :keyword="showLine?start:keyword" :auto-viewport="true"></bm-local-search>
      <bm-map-type :map-types="['BMAP_NORMAL_MAP', 'BMAP_HYBRID_MAP']" anchor="BMAP_ANCHOR_TOP_LEFT"></bm-map-type>
      <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_RIGHT" :showAddressBar="true" :autoLocation="true"></bm-geolocation>
      <bm-driving
        v-if="showLine"
        class="driving"
        :start="start"
        :end="end"
        :auto-viewport="true"
        :panel="showLine"
        policy="BMAP_DRIVING_POLICY_LEAST_DISTANCE"
      >
      </bm-driving>
    </baidu-map>
  </div>
</template>
<script>
  export default {
    props: {
      showLine: {
        type: Boolean,
        default: false
      },
      center: {
        lng: 116.403326,
        lat: 39.915241
      },
      zoom: {
        type: Number,
        default: 15
      },
      keyword:{
        type: String,
        default: "北京"
      },
      start:{
        type: String,
        default: "天安门"
      },
      end:{
        type: String,
        default: "天坛公园"
      }
    },
    data () {
      return {
        queryForm:{
          addr: this.keyword,
          startAddr: this.start,
          endAddr: this.end
        }
      }
    },
    methods:{
      searchLine(){
        this.start = this.queryForm.startAddr
        this.end = this.queryForm.endAddr
      },
      searchAddr(){
        this.keyword = this.queryForm.addr
      }
    }
  }
</script>
<style scoped>
  .addrClass{
    height: 620px;
    width: 100%;
  }
  .lineClass{
    height: 620px;
    width: 70%;
  }
  .driving{
    position: absolute;
    top: 100px;
    right: 0;
    height: 800px;
    overflow-y: auto;
    width: 500px
  }
</style>
