<template>
  <div>
  <el-dialog width="600px" center :title="file.name" :visible.sync="open" @close='close' :append-to-body="true" v-dialogDrag>
    <el-image :title="file.src" src="/static/images/admin/config/audio/audio.gif" style="width: 100%"/>
  </el-dialog>
  </div>
</template>
<script>
  export default {
    props: {
      title: {
        type: String,
        default: ''
      },
    },
    data(){
      return{
        open:false,
        myAudio:{},
        file:{
          name:"",
          src:""
        }
      }
    },
    mounted() {
     this.myAudio = new Audio();
    },
    methods:{
      close(){
        this.open=false;
        let myAudio = this.myAudio;
        if (myAudio.play) {
          myAudio.pause();
        }
      },
      //导出
      play(file) {
        this.open = true;
        let myAudio = this.myAudio;
        myAudio.src = file.src;
        this.file=file
        if (myAudio.paused) {
          myAudio.play();
        }
      },
    }
  }
</script>
