<!--折线图-->
<template>
  <div :id="id" :style="style"></div>
</template>
<script>
  export default {
    name: 'echarts-bend',
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
        default: "折线图"
      },
      xData: {//['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        type: Array,
        default: []
      },
      yData: {//[150, 230, 224, 218, 135, 147, 260]
        type: Array,
        default: []
      },
    },
    data() {
      return {}
    },
    mounted() {
      this.drawBend(this.xData, this.yData);
    },
    methods: {
      drawBend(xData, yData) {
        // 基于准备好的dom，初始化echarts实例
        let bendChart = this.$echarts.init(document.getElementById(this.id));
        // 绘制图表
        let option = {
          title: {text: this.title, left: 'center'},
          tooltip: {
            trigger: 'axis'
          },
          toolbox: {
            show: true,
            feature: {
              magicType: { type: ['line', 'bar'] },
              restore: {},
              saveAsImage: {}
            }
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: xData
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: yData,
              type: 'line',
              smooth: true,
              markPoint: {
                data: [
                  { type: 'max', name: 'Max' },
                  { type: 'min', name: 'Min' }
                ]
              },
              // 区域填充样式
              areaStyle: {
                color: {
                  type: 'linear',
                  x: 0,
                  y: 0,
                  x2: 0,
                  y2: 1,
                  colorStops: [{
                    offset: 0, color: 'rgba(185,51,47,0.5)' // 0% 处的颜色
                  }, {
                    offset: 1, color: 'rgba(185,51,47,0)' // 100% 处的颜色
                  }],
                  global: false // 缺省为 false
                }
              },
              markLine: {
                data: [{ type: 'average', name: 'Avg' }]
              },
              itemStyle: {        //上方显示数值
                normal: {
                  label: {
                    show: true, //开启显示
                    position: 'top', //在上方显示
                    textStyle: { //数值样式
                      color: 'gray',
                      fontSize: 16
                    }
                  }
                }
              }
            }
          ]
        };
        bendChart.setOption(option);
      }
    }
  }
</script>
