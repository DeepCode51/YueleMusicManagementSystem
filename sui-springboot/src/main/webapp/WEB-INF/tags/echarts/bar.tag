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
    var xData = data.map(function (v) {
        return v.name;
    });
    var yData = data.map(function (v) {
        return v.value;
    });
    option = {
        title:{text: '${title}',x:'center',y: 'top'},
        xAxis: {
            type: 'category',
            data: xData
        },
        yAxis: {
            type: 'value'
        },
        series: [{
            data: yData,
            type: 'bar',
            itemStyle: {
                normal: {
                    label: {
                        show: true,
                        position: 'top',
                        textStyle: {
                            color: '#802a0e'
                        }
                    }
                }
            }
        }]
    };
    option && myChart.setOption(option);

</script>