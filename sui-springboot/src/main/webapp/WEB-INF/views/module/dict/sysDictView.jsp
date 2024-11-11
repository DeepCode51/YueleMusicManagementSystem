<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <title>字典信息查看</title>
</head>
<body >
<form class="layui-form layui-form-pane" role="form" action="" method="post">
    <input id="dataForm" type="hidden" lay-submit lay-filter="dataForm">
    <input type="hidden" name="id" value="${sysDict.id}" class="layui-input">
    <div class="layui-form-item">
        <label class="layui-form-label">数据值</label>
        <div class="layui-input-block">
            <input type="text" name="value" disabled="true" value="${sysDict.value}" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">标签名</label>
        <div class="layui-input-block">
            <input type="text" name="label" disabled="true" value="${sysDict.label}" class="layui-input">
        </div>
    </div>
    <c:if test="${sysDict.type eq 'dictCode'}">
        <div class="layui-form-item">
            <label class="layui-form-label">样式</label>
            <div class="layui-input-block">
                <input type="text" autocomplete="off" name="style" disabled="true" value="${sysDict.style}" class="layui-input">
            </div>
        </div>
    </c:if>
    <div class="layui-form-item">
        <label class="layui-form-label">排序</label>
        <div class="layui-input-block">
            <input type="text" name="sort" disabled="true" value="${sysDict.sort}" class="layui-input">
        </div>
    </div>
</form>
</body>

<script>
    var $,element,form,layer,laytpl,laydate;
    layui.use(['jquery','form','laydate', 'layer', 'laytpl', 'element'], function(){
        $ = layui.$;
        element = layui.element;
        form = layui.form;
        layer = layui.layer;
        laydate = layui.laydate;
    });

</script>

</html>
