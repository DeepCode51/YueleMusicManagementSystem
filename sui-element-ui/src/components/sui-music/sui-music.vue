<template>
<!-- 准备一个容器用来存放音乐播放器 -->
  <aplayer
    v-if="playList.length>0"
    @playing="playing"
    @play="play"
    @ended="ended"
    @paused="paused"
    :listFolder="true"
    :music="playList[0]"
    :list="playList"
    :showlrc="true"
    float
    ref="aplayer"
    :autoplay="true"
    mode="circulation"
    repeat="repeat-all"
    style="position:absolute;top: 60px;right:30px;width: 500px;z-index: 1000"
  ></aplayer>
</template>
<script>
  import aplayer from "vue-aplayer";
  export default {
    components: {aplayer},
    data() {
      return {
        timer:""
      };
    },
    mounted() {
    },
    computed: {
      playList() {
        const that = this;
        let list = this.$store.getters.playList;
        let playList = [];
        if(list!=null && list.length>0){
          playList = list.clone().map(function (v) {
            return {
              title: v.name,
              artist: v.singerName,
              src: that.formatUrl(v.url),
              pic: that.formatUrl(v.picture),
              lrc: v.lyric,
              theme: "rgb(241,141,68)",
            }
          });
        }
        return playList;
      },
    },
    watch: {
      playList(playList){
        const that = this;
        if(playList){
          setTimeout(function () {
            that.$nextTick(() => {
              let elementsByClassName = document.getElementsByClassName("aplayer-icon aplayer-icon-play");
              elementsByClassName[0].click()
            });
          },1000)
        }
      }
    },
    methods: {
      play(audio){
        const that = this;
        let currentSrc = audio.currentTarget.currentSrc;
        let list = that.$store.getters.playList.filter(function (v) {
          return that.formatUrl(v.url) == currentSrc
        });
        let music = list[0];
        this.$bus.$emit("listerPlay",music);
      },
      playing(){
        this.timeupdate();
      },
      ended(){
      },
      paused(){
      },
      timeupdate(){
        let elementsByClassName = document.getElementsByClassName("aplayer-dtime");
        let duration = elementsByClassName[0].textContent;//音乐总长度
        let percentClassName = document.getElementsByClassName("aplayer-played");
        let percent = percentClassName[0].style.width;//音乐总长度
        let durationNum =  parseInt(duration.split(":")[0])*60+ parseInt(duration.split(":")[1]);
        let percentNum =  percent.replace("%","")/ 100;
        this.$bus.$emit("timeupdate", durationNum,percentNum)
      },
    }
  };
</script>
