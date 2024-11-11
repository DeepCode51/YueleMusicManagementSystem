<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <title>创建表对象</title>
</head>
<body class="layui-body main-content">
<div class="layui-row">
    <form id="queryForm" name="queryForm" class="layui-form" action="">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">表名称</label>
                <div class="layui-input-inline">
                    <input type="text" name="tableName" placeholder="请输入表名称" class="layui-input">
                </div>
            </div>
            <div class="layui-inline">
                <label class="layui-form-label">表描述</label>
                <div class="layui-input-inline">
                    <input type="text" name="comments" placeholder="请输入表描述" class="layui-input">
                </div>
            </div>
            <div style="text-align: center">
                <div class="layui-inline">
                    <button id="search" class="layui-btn" lay-submit lay-filter="search">搜索</button>
                    <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                </div>
            </div>
        </div>
    </form>
</div>
<div class="layui-btn-group">
    <button class="layui-btn layui-btn-sm" onclick="createTable()"><i class="fa fa-plus"></i> 创建表</button>
    <button class="layui-btn layui-btn-sm" onclick="genMenu()"><i class="fa fa-caret-square-o-down"></i> 创建菜单</button>
    <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="genCodeJspAndJava()"><i class="fa fa-files-o"></i>
        生成(前台+后台)代码
    </button>
    <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="genCodeJsp()"><i class="fa fa-html5"></i> 生成后台页面</button>
    <button class="layui-btn layui-btn-sm layui-btn-normal" onclick="genCodeJava()"><i class="fa fa-fire"></i> 生成后台代码</button>
    <button class="layui-btn layui-btn-warm layui-btn-sm" onclick="genCodeApplet()"><i class="fa fa-cubes"></i> 生成微信页面</button>
    <%--<button class="layui-btn layui-btn-warm layui-btn-sm" onclick="genCodeApi()"><i class="fa fa-cubes"></i> 生成接口代码</button>--%>
    <button style="position: fixed;right: 50px" class="layui-btn layui-btn-sm"><a href="/admin/sys/dict/manage" class="fa fa-cubes" style="color: white" target="view_window"> 数据字典</a></button>
</div>
<table class="layui-table" lay-filter="dataList">
    <thead>
    <tr>
        <th lay-data="{type:'checkbox', fixed: 'left'}"></th>
        <th lay-data="{field:'sorts', width:80, title: '排序',fixed: 'left',templet:'#sorts'}"></th>
        <th lay-data="{field: 'tableName', title: '表名', sort: true}"></th>
        <th lay-data="{field: 'comments', title: '表描述', sort: true}"></th>
        <th lay-data="{fixed: 'right', width:200, align:'center', toolbar: '#barDemo'}"></th>
    </tr>
    </thead>
</table>
<script type="text/html" id="sorts">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<script type="text/html" id="isShowTmpl">
    {{#  if(d.isShow ==1){ }}
    <span style="color: blue;">是</span>
    {{#  } else { }}
    <span style="color: red;">否</span>
    {{#  } }}
</script>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="view">查看</a>
    <a class="layui-btn layui-btn-xs" lay-event="edit">字段配置</a>
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
</body>
<script>
    var $, table, element, form, layer;
    layui.use(['jquery', 'form', 'table', 'element', 'layer'], function () {
        $ = layui.$;
        table = layui.table; //表格
        element = layui.element;
        form = layui.form;
        layer = layui.layer;
        //监听工具条
        table.on('tools(dataList)', function (obj) { //注：tools是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'view') {
                open.view('代码生成查看', '${ctx}/admin/sys/gen/view?id=' + data.id+"&comments="+data.comments, '1600px', '800px');
            } else if (layEvent === 'edit') {
                open.save('代码生成编辑', '${ctx}/admin/sys/gen/edit?id=' + data.id+"&comments="+data.comments, '1600px', '800px');
            } else if (layEvent === 'del') {
                open.del("代码生成删除", "${ctx}/admin/sys/gen/delete?id=" + data.id);
            }
        });
        //转换静态表格
        table.init('dataList', {
            id: 'dataList',//生成 Layui table 的标识 id，必须提供，用于后文刷新操作
            method: 'POST',
            url: '${ctx}/admin/sys/gen/getList',
            where: {},
            page: true,
            height: 'full-300' //设置高度
        });

        //监听提交
        form.on('submit(search)', function (data) {
            reloadList(data.field);
            return false;
        });

    });

    var reloadList = function (data) {
        if (data) {
            table.reload("dataList", {
                page: {curr: 1},
                where: data
            });
        } else {
            table.reload("dataList", {page: {curr: 1}});
        }
    }
    /**
     * 生成java代码
     */
    function genCodeJava() {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length != 1) {
            top.layer.msg("请选择一条记录生成代码!");
        } else {
            open.save('生成java代码', '${ctx}/admin/sys/gen/config?flag=true&id=' + rows[0].id + "&comments=" + rows[0].comments, '800px', '600px', 'java');
        }
    }

    /**
     * 生成java代码
     */
    function genCodeJsp() {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length != 1) {
            top.layer.msg("请选择一条记录生成代码!");
        } else {
            open.save('生成jsp代码', '${ctx}/admin/sys/gen/config?flag=true&id=' + rows[0].id + "&comments=" + rows[0].comments, '800px', '600px', 'jsp');
        }
    }

    /**
     * 生成小程序代码
     */
    function genCodeApplet() {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length != 1) {
            top.layer.msg("请选择一条记录生成代码!");
        } else {
            open.save('生成jsp代码', '${ctx}/admin/sys/gen/config?flag=true&id=' + rows[0].id + "&comments=" + rows[0].comments, '800px', '600px', 'vue');
        }
    }
    /**
     * 生成小程序代码
     */
    function genCodeApi() {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length != 1) {
            top.layer.msg("请选择一条记录生成代码!");
        } else {
            open.save('生成jsp代码', '${ctx}/admin/sys/gen/config?flag=true&id=' + rows[0].id + "&comments=" + rows[0].comments, '800px', '600px', 'api');
        }
    }

    /**
     * 生成java代码
     */
    function genCodeJspAndJava() {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length != 1) {
            top.layer.msg("请选择一条记录生成代码!");
        } else {
            open.save('生成jsp代码', '${ctx}/admin/sys/gen/config?flag=true&id=' + rows[0].id + "&comments=" + rows[0].comments, '800px', '600px', 'jspAndJava');
        }
    }

    /**
     * 创建表
     */
    function createTable() {
        open.save('创建表对象', '${ctx}/admin/sys/gen/createTable', '1600px', '800px');
    }

    /**
     * 生成菜单数据
     */
    function genMenu() {
        var checkStatus = table.checkStatus('dataList');
        var rows = checkStatus.data;
        if (rows.length != 1) {
            top.layer.msg("请选择一条记录生成代码!");
        }else {
            var params={
                id:rows[0].id,comments:rows[0].comments
            }
            request.doAjaxPost('${ctx}/admin/sys/gen/genMenu',params,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok(data.info);
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        }
    }
</script>
</html>
