<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <title>字典编码新增</title>
</head>
<body >
<form class="layui-form layui-form-pane" role="form" action="" method="post">
    <input id="dataForm" type="hidden" lay-submit lay-filter="dataForm">
    <input type="hidden" name="pid" value="${sysDict.pid}" class="layui-input">
    <input type="hidden" name="id" value="${sysDict.id}" class="layui-input">
    <input type="hidden" name="type" value="dictCode" class="layui-input">
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="requireField">*</span>标签名</label>
        <div class="layui-input-block">
            <input type="text" autocomplete="off" lay-verify="required|labelUnique" name="label" value="${sysDict.label}" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="requireField">*</span>数据值</label>
        <div class="layui-input-block">
            <input type="text" autocomplete="off" lay-verify="required|valueUnique|validFieldCode" name="value" value="${sysDict.value}" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label"><span class="requireField">*</span>排序</label>
        <div class="layui-input-block">
            <input type="number" autocomplete="off" lay-verify="required|number" name="sort" value="${sysDict.sort}" class="layui-input">
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

        form.verify({
            labelUnique : function(value) {
                var id ="${sysDict.id}";
                var tableName ="sys_dict";
                var params ={"label": value,"pid":'${sysDict.pid}',"type":"dictCode"};
                var validResult = validForm.validFieldUnique(params,tableName,id);
                return validResult;
            },
            valueUnique: function (value) {
                var id = "${sysDict.id}";
                var tableName = "sys_dict";
                var params = {"value": value,"pid":'${sysDict.pid}', "type": "dictCode"};
                var validResult = validForm.validFieldUnique(params, tableName, id);
                return validResult;
            },
            validFieldCode:function (value) {
                var validResult = validForm.validFieldCode(value);
                return validResult;
            }
        });

        form.on('submit(dataForm)', function (formObj) {
            request.doAjaxPost("${ctx}/admin/sys/dict/save", formObj.field,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();//刷新列表页面数据
                        top.initTree();
                    }
            })
        })
    });
    //点击保存时表单提交
    function subForm() {
        $("#dataForm").click();
        return false;
    }
</script>

</html>
