<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <title>日志监控</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <!-- 注意：如果你直接复制所有代码到本地，上述css路径需要改成你本地的 -->
</head>
<script>
    layui.use(['table', 'laydate', 'element', 'layer'], function () {
    });
</script>

<body class="layui-body main-content">
<div class="layui-tab-content" style="height:auto;">
    <div class="layui-tab-item layui-show">
        <div class="layui-collapse" lay-accordion="">
            <div class="layui-colla-item">
                <div class="layui-collapse" lay-accordion="">
                    <div class="layui-colla-item">
                        <div class="layui-colla-content layui-show">
                            <table class="layui-table"
                                   lay-data="{height:'auto', url:'${pageContext.request.contextPath}/admin/sys/gen/getCloums?tableName=${param.id}', page:false, id:'idQuartz'}"
                                   lay-filter="quartz">
                                <thead>
                                <blockquote id="itemTitle" class="layui-elem-quote" style="padding: 5px 10px;margin-bottom: 0;">
                                    表名 : 【${param.id}】   &nbsp;&nbsp;&nbsp;&nbsp;  表描述 : 【${param.comments}】
                                </blockquote>
                                <tr>
                                    <th lay-data="{field: 'columnName', title: '列名称', sort: true}"></th>
                                    <th lay-data="{field: 'columnType', title: '列类型', sort: true}"></th>
                                    <th lay-data="{field: 'columnLength', title: '列长度', sort: true}"></th>
                                    <th lay-data="{field: 'columnDescribe', title: '列描述', sort: true}"></th>
                                </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<br>
</body>
</html>