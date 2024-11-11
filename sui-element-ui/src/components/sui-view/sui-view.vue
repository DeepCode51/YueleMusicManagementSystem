<template>
  <el-dialog :title="title" :visible.sync="open" :append-to-body="true" v-dialogDrag>
    <div style="height: 700px">
      <iframe style="background: lightgray" width="100%" height="100%" frameborder=0 scrolling=auto :src="src"></iframe>
    </div>
  </el-dialog>
</template>
<script>
  //浏览器预览:支持txt和pdf
  import Viewer from 'viewerjs';
  import 'viewerjs/dist/viewer.css';
  export default {
    props: {
      title: {
        type: String,
        default: ''
      },
      src:{
        type: String,
        default: ''
      }
    },
    data(){
      return{
        open:false
      }
    },
    mounted() {
    },
    methods:{
      // pdf:txt预览
      iframeViewer(file) {
        this.open=true;
        this.title=file.name;
        this.src=file.src;
      },

      //图片预览
      imgViewer(file){
        const that = this;
        that.$nextTick(function () {
          let viewer = new Viewer(document.getElementById('file'), {
            title: function (img, obj) {  // 这里实现显示图片名称
              return file.name;
            },
            url:function (img, obj) {  // 这里实现显示图片名称
              return file.url;
            },
            toolbar: {
              zoomIn: 1,
              zoomOut: 1,
              oneToOne: 1,
              reset: 1,
              prev: 0,
              play: 0,
              next: 0,
              rotateLeft: 1,
              rotateRight: 1,
              flipHorizontal: 1,
              flipVertical: 1,
            },
            keyboard: false,
            navbar: false,
            fullscreen: false,
            hide: function () { //在图片消失的时候销毁viewer
              viewer.destroy()
            },
          });
          viewer.view()
        })
      }
    }
  }
</script>
