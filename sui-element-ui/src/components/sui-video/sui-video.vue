<template>
    <el-dialog width="600px" center :title="file.name" :visible.sync="open" :append-to-body="true" v-dialogDrag>
      <video-player
        :title="file.src"
        v-if="open"
        class="video-player vjs-custom-skin"
        ref="videoPlayer"
        :playsinline="true"
        :options="playerOptions"
      >
      </video-player>
    </el-dialog>
</template>
<script>
  //视频预览
  export default {
    props: {
      title: {
        type: String,
        default: ''
      },
      src: {
        type: String,
        default: ''
      },
    },
    data(){
      return{
        file:{
          name:"",
          src:""
        },
        //弹窗设置
        open: false,
        playerOptions: {
          playbackRates: [0.5, 1.0, 1.5, 2.0],//倍速控制
          autoplay: false,//是否自动播放
          muted: false,//是否静音播放
          loop: false,//是否循环播放
          preload: "auto",
          language: "zh-CN", //语言，还要引入对应的文件
          aspectRatio: "16:9",//比例
          fluid: true,
          sources: [
            {
              type: "",
              src: "", //url地址
            },
          ],
          poster: "", //你的封面地址
          notSupportedMessage: "此视频暂无法播放，请稍后再试",
          controlBar: {
            timeDivider: true,
            durationDisplay: true,//剩余时间是否显示
            remainingTimeDisplay: false,//剩余时间是否显示，有一个即可
            fullscreenToggle: true,//全屏按钮
          },
        },
      }
    },
    mounted() {
    },
    methods:{
      play(file) {
        this.open=true;
        this.file=file;
        this.playerOptions.sources[0].src=file.src;
      },
    }
  }
</script>
