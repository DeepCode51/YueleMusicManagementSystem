<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" %>
<%@ attribute name="data" type="java.lang.String" required="true" %>
<script src="${ctxStatic}/common/echarts-v3.0/echarts.js"></script>
<script>
    var chartDom = document.getElementById('${id}');
    var myChart = echarts.init(chartDom);
    var data = ${data}
    option = {
        title: {
            text: '${title}',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
        },
        series : [{
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:data,
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                },
                normal:{
                    label:{
                        show: true,
                        formatter: '{b} : {c} ({d}%)'
                    },
                    labelLine :{show:true}
                }
            }
        }
        ]
    };
    option && myChart.setOption(option);

</script>