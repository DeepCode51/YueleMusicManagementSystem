<template>
  <div>
    <img src="/static/images/admin/message/bba_thumb.gif" @click="onclick" />
    <transition name="el-zoom-in-top">
      <ul v-if="show" :class="['face pointer',{'aside-hidden': show}]">
        <li v-for="item in gifList"><img class="pointer" @click="selectFace(item)" :src=getGifUrl(item.gif) :title="item.title"></li>
      </ul>
    </transition>
  </div>
</template>
<script>
  export default{
    props: {
      show: {
        type: Boolean,
        default: false
      }
    },
    data(){
      return{
        gifList: [
          {gif: "smilea_thumb.gif", title: "[呵呵]"},
          {gif: "tootha_thumb.gif", title: "[嘻嘻]"},
          {gif: "laugh.gif", title: "[哈哈]"},
          {gif: "tza_thumb.gif", title: "[可爱]"},
          {gif: "kl_thumb.gif", title: "[可怜]"},
          {gif: "kbsa_thumb.gif", title: "[挖鼻屎]"},
          {gif: "cj_thumb.gif", title: "[吃惊]"},
          {gif: "shamea_thumb.gif", title: "[害羞]"},
          {gif: "zy_thumb.gif", title: "[挤眼]"},
          {gif: "bz_thumb.gif", title: "[闭嘴]"},
          {gif: "bs2_thumb.gif", title: "[鄙视]"},
          {gif: "lovea_thumb.gif", title: "[爱你]"},
          {gif: "sada_thumb.gif", title: "[泪]"},
          {gif: "heia_thumb.gif", title: "[偷笑]"},
          {gif: "qq_thumb.gif", title: "[亲亲]"},
          {gif: "sb_thumb.gif", title: "[生病]"},
          {gif: "mb_thumb.gif", title: "[太开心]"},
          {gif: "ldln_thumb.gif", title: "[懒得理你]"},
          {gif: "yhh_thumb.gif", title: "[右哼哼]"},
          {gif: "zhh_thumb.gif", title: "[左哼哼]"},
          {gif: "x_thumb.gif", title: "[嘘]"},
          {gif: "cry.gif", title: "[衰]"},
          {gif: "wq_thumb.gif", title: "[委屈]"},
          {gif: "t_thumb.gif", title: "[吐]"},
          {gif: "k_thumb.gif", title: "[打哈气]"},
          {gif: "bba_thumb.gif", title: "[抱抱]"},
          {gif: "angrya_thumb.gif", title: "[怒]"},
          {gif: "yw_thumb.gif", title: "[疑问]"},
          {gif: "cza_thumb.gif", title: "[馋嘴]"},
          {gif: "88_thumb.gif", title: "[拜拜]"},
          {gif: "sk_thumb.gif", title: "[思考]"},
          {gif: "sweata_thumb.gif", title: "[汗]"},
          {gif: "sleepya_thumb.gif", title: "[困]"},
          {gif: "sleepa_thumb.gif", title: "[睡觉]"},
          {gif: "money_thumb.gif", title: "[钱]"},
          {gif: "sw_thumb.gif", title: "[失望]"},
          {gif: "cool_thumb.gif", title: "[酷]"},
          {gif: "hsa_thumb.gif", title: "[花心]"},
          {gif: "hatea_thumb.gif", title: "[哼]"},
          {gif: "gza_thumb.gif", title: "[鼓掌]"},
          {gif: "dizzya_thumb.gif", title: "[晕]"},
          {gif: "bs_thumb.gif", title: "[悲伤]"}
        ],
      }
    },
    methods:{
      onclick(){
        this.show=!this.show
      },
      getGifUrl(url){
        return '/static/images/admin/message/'+url
      },
      selectFace(item){
        let img = "<img style='vertical-align:middle' src='/static/images/admin/message/"+item.gif+"' title='["+item.title+"]'>"
        this.$emit('selectFace',img)
      }
    }
  }
</script>
<style scoped>
  .face{
    border-radius: 10px;
    z-index: 10;
    padding: 10px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    background: rgba(0, 0, 0, 0.51);
    /*position: relative;*/
    width: 500px;
    height: 120px;
    list-style-type: none;
  }
  .face li{
    padding: 5px;
    float: left;
  }
</style>
