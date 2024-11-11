<template>
  <div>
    {{date}} {{time}}
  </div>
</template>
<script>
  export default{
    data(){
      return{
        date: '',
        time: '',
      }
    },
    mounted() {
      this.getDate();
      this.timer = setInterval(() => {
        this.getDate();
      }, 1000)
    },
    methods:{
      getDate: function() {
        let weeks = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
        let time = new Date();
        let year = time.getFullYear();
        let month = time.getMonth() + 1;
        let day = time.getDate();
        let week = weeks[time.getDay()];
        this.getTime();
        this.date = year + '/' + month + '/' + day + " " + week;
      },
      getTime: function() {
        let time = new Date();
        let hour = time.getHours();
        let minutes = time.getMinutes();
        if(minutes<10){
          minutes='0'+minutes
        }
        let seconds = time.getSeconds();
        if(seconds<10){
          seconds='0'+seconds
        }
        this.time = hour + ":" + minutes+":"+seconds
      },
    }
  }
</script>
