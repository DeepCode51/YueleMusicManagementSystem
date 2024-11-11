<!-- 【歌曲播放】页面 -->
<template>
  <div class="front-body">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <sui-breadcrumb></sui-breadcrumb>
      </div>
      <el-row>
        <el-col :span="8">
          <el-image v-if="item.picture" style="width: 100%;height: 300px" :src="formatUrl(item.picture)"></el-image>
        </el-col>
        <el-col :span="16" style="padding:0 10px;height: 300px;overflow-y: auto">
          <h1 style="text-align: center;margin: 0" class="font-shadow">{{item.name}}</h1>
          <p class="font-shadow" v-html="item.introduction"></p>
        </el-col>
      </el-row>
      <div class="div-shadow" style="text-align: center;background: #333;padding: 10px;border-radius: 5px">
        <h2 style="color: white">{{item.name}}</h2>
        <lyricLine v-if="currentLyric"></lyricLine>
        <div v-if="currentLyric" ref="lyricLine" v-for="(line,index) in currentLyric.lines">
          <span v-if="index==currentLyricNum" class="font-shadow" style="background: rgba(248,183,17,0.05);color: #ED7B07;padding: 5px;margin: 5px;line-height:50px;font-size: 20px;border-radius: 5px"> {{line.txt}}</span>
          <span v-else style="color: white"> {{line.txt}}</span>
        </div>
      </div>
    </el-card>
    <!--【评价|评论】按钮-->
    <sui-evaluate v-if="item.id" style="text-align: center;padding-top: 10px" :refId="item.id" @success="success" type="评论"/>
    <!--【评价|评论】列表-->
    <user-evaluate style="margin:0 10px" v-if="item.id" ref="userEvaluate" :refId="item.id" type="评论"></user-evaluate>
  </div>
</template>

<script>
  import {mapActions} from "vuex";
  import Lyric from 'lyric-parser'
  export default {
    name: "songDetails",
    data() {
      return {
        currentLyricNum: 0,
        currentLyric: "",
        playingLyric:"",
        songPlayList: [],
        //表单数据
        item: {
          picture: "",
          name: "",
          singerId: "",
          writeTime: "",
          url: "",
          lyric: "",
          introduction: ""
        },
      }
    },
    watch: {
      '$route.query.id'(id) {
        const that = this;
        that.commitPlayList().then(res => {
          let songId = that.$route.query.id;
          let playList = that.$store.getters.playList;
          let list = playList.filter(function (v) {
            return v.songId == songId
          });
          that.listerPlay(list[0]);
        });
      }
    },
    mounted() {
      const that = this;
      that.commitPlayList().then(res=>{
        let songId = that.$route.query.id;
        let playList = that.$store.getters.playList;
        let list = playList.filter(function (v) {
          return v.id == songId
        });
        that.listerPlay(list[0]);
      });
      that.$bus.$on("listerPlay", that.listerPlay);
      that.$bus.$on("timeupdate", that.timeupdate);
      that.scrollTop();
    },
    beforeDestroy() {
      // 3.组件即将销毁时，解绑事件
      this.$bus.$off("listerPlay");
      this.$bus.$off("listerMusic");
    },
    watch: {
      "item.lyric"(lyric){
        if(lyric){
          this.readLyric(lyric);
        }
      }
    },
    methods: {
      ...mapActions(['commitPlayList']),
      listerPlay(obj) {
        this.item = obj;
      },
      timeupdate(durationNum, percentNum) {
        this.onPercentBarChange(durationNum, percentNum)
      },
      readLyric(lyric) {
        if(this.currentLyric){
          this.currentLyric.stop();
        }
        this.currentLyric = new Lyric(lyric, this.lyricHandle);
        this.currentLyric.play();
      },
      // 歌词回调
      lyricHandle({lineNum, txt}) {
        console.log(lineNum)
        if (!this.$refs.lyricLine) {
          return
        }
        this.currentLyricNum = lineNum
        if (lineNum > 10) {
          let lineEl = this.$refs.lyricLine[lineNum - 10]
          if (this.$refs.lyricList) {
            this.$nextTick(() => {
              this.$refs.lyricList.scrollToElement(lineEl, 1000)
            })
          }
        } else {
          if (this.$refs.lyricList) {
            this.$nextTick(() => {
              this.$refs.lyricList.scrollTo(0, 0, 1000)
            })
          }
        }
        this.playingLyric = txt;
      },
      // 进度条拖动改变播放进度
      onPercentBarChange(durationNum, percentNum) {
        const currentTime = durationNum * percentNum;
        if (this.currentLyric) {
          this.currentLyric.seek(currentTime * 1000)
        }
      },
      success() {
        this.$refs.userEvaluate.getList();
      },
    }
  }
</script>
