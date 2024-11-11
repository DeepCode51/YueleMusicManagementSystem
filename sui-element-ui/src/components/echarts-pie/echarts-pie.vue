<!--饼状图-->
<template>
  <div :id="id" :style="style"></div>
</template>
<script>
  export default {
    name: 'echarts-pie',
    props: {
      id: {
        type: String,
        default: ""
      },
      style: {
        type: Object,
        default: {width: '100%', height: '500px'}
      },
      title: {
        type: String,
        default: "饼状图"
      },
      data: {//[{ name:'Search Engine',value: 1048 },{ name: 'Direct',value: 735}]
        type: Array,
        default: []
      }
    },
    data() {
      return {}
    },
    mounted() {
      this.drawPie(this.data);
    },
    methods: {
      drawPie(data) {
        // 基于准备好的dom，初始化echarts实例
        let pieChart = this.$echarts.init(document.getElementById(this.id))
        // 绘制图表
        let option = {
          title: {
            text: this.title,
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [
            {
              type: 'pie',
              radius: '50%',
              data: data,
              label: {            //饼图图形上的文本标签
                normal: {
                  show: true,
                  formatter: '{b} : {d}%'
                }
              },
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        pieChart.setOption(option);
      },
    }
  }
</script>
