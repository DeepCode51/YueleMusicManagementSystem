<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="title" type="java.lang.String" required="true"%>
<button class="layui-btn layui-btn-sm " id="btnExport"><i class="fa fa-file-excel-o"></i> ${title}</button>
<script type="text/javascript">
$(document).ready(function() {
	$("#btnExport").click(function(){
		top.layer.confirm('确认要导出Excel吗?', {icon: 3, title:'系统提示'}, function(index){
            location.href="${url}";
		    top.layer.close(index);
		});
	});
});

</script>