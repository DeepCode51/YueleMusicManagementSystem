<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>代码生成配置页面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
</head>
<body class="layui-body main-content">
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>代码生成配置页面</legend>
</fieldset>
<form class="layui-form" id="edit_form">
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">表名:</label>
            <input type="hidden" name="isPort" value="${isPort}">
            <input type="hidden" name="isFileList" value="${isFileList}">
            <div class="layui-input-inline">
                <input type="text" class="layui-input" readonly="true" style="background: #a9a9a936;width: 500px" name="tableName"
                       value="${tableName}"/>
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">表名描述:</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" readonly="true" style="background: #a9a9a936;width: 500px" name="comments"
                       value="${comments}"/>
            </div>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">保存路径:</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" readonly="true" style="background: #a9a9a936;width: 500px" name="path"
                       value="${path}"/>
            </div>
        </div>
    </div>

    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">模块位置:</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" readonly="true" style="background: #a9a9a936;width: 500px" name="prefixPath"
                       value="${prefixPath}"/>
            </div>
        </div>
    </div>
    <style>
    </style>
    <div class="layui-form-item">
        <div class="layui-inline">
            <label class="layui-form-label">模块名称:</label>
            <div class="layui-input-inline">
                <input type="text" class="layui-input" readonly="true" style="background: #a9a9a936;width: 500px" name="modelName"
                       value="${modelName}"/>
            </div>
        </div>
    </div>
</form>

<script>
    function subForm(flag) {
        if (flag == 'java') {
            var params = $("#edit_form").serializeJSON();
            request.doAjaxPost("${ctx}/admin/sys/gen/genCodeJava", params,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();//刷新列表页面数据
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        } else if (flag == 'jsp') {
            var params = $("#edit_form").serializeJSON();
            request.doAjaxPost("${ctx}/admin/sys/gen/genCodeWeb", params,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();//刷新列表页面数据
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        } else if (flag == 'jspAndJava') {
            var params = $("#edit_form").serializeJSON();
            request.doAjaxPost("${ctx}/admin/sys/gen/genCodeWebAndJava", params,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();//刷新列表页面数据
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        } else if (flag == 'vue') {
            var params = $("#edit_form").serializeJSON();
            request.doAjaxPost("${ctx}/admin/sys/gen/genCodeWX", params,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();//刷新列表页面数据
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        }else if (flag == 'api') {
            var params = $("#edit_form").serializeJSON();
            request.doAjaxPost("${ctx}/admin/sys/gen/genCodeApi", params,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();//刷新列表页面数据
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        }
    }
</script>
</body>
</html>