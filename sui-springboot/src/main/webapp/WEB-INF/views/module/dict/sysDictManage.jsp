<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <title>数据字典管理</title>
    <link href="${ctxStatic}/common/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.css" rel="stylesheet" media="all">
    <style type="text/css">
        .ztree {padding: 5px 0;}
        .ztree * {font-size: 12px;}
        .ztree li ul{ margin:0; padding:0;}
        .ztree li {line-height:30px;position: relative;}
        .ztree li a {width:100%;height:30px;border-bottom: 1px dashed #ccc;cursor: default;padding: 0 5px;box-sizing: border-box;}
        .ztree li a:hover {text-decoration:none; background-color: #E7E7E7;}
        .ztree li span.button.noline_docu{display:none}
        .ztree li a.curSelectedNode {background-color:#D4D4D4;border:0;height:30px;border-bottom: 1px dashed #ccc;}
        .ztree li span {line-height:30px;}
        .ztree li span.node_name{display: inline-block;width:200px;max-width: 200px;overflow: hidden;white-space: nowrap;text-overflow:ellipsis;}
        .ztree li span.diybtn {position: absolute;top:0;right: 0;}
        .ztree li span.diybtn i {font-size: 16px;cursor: pointer;margin:0 2px;display: inline-block;}
        .ztree li span.diybtn i:hover {color: red;}
    </style>

    <script type="application/javascript" src="${ctxStatic}/common/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.js"></script>
    <script type="application/javascript" src="${ctxStatic}/common/jquery-ztree/3.5.12/js/jquery.ztree.exedit-3.5.js"></script>
</head>
<body style="padding: 10px">
<div style="position: relative;display: flex;">
    <div style="width:300px;min-width: 300px;margin-right: 10px;border: 1px solid #ddd;border-top:none;box-sizing: border-box;position: relative;max-height: calc(100vh - 32px);height: calc(100vh - 32px);">
        <blockquote class="layui-elem-quote" style="padding: 5px 10px;margin-bottom: 0;">
            分类
        </blockquote>
        <button id="addDictTypeBtn" class="layui-btn layui-btn-sm layui-btn-normal layui-icon layui-icon-add-1"
                style="position: absolute;top: 0;right: 0;"></button>
        <div style="height: calc(100% - 32px);overflow-y: scroll;">
            <ul id="dictTree" class="ztree"></ul>
        </div>
    </div>
    <div style="flex: 1;border: 1px solid #ddd;border-top:none;min-width: 200px;">
        <blockquote id="itemTitle" class="layui-elem-quote" style="padding: 5px 10px;margin-bottom: 0;">
            字典项列表
        </blockquote>
        <div style="padding: 5px 10px 0px 10px">
            <div class="layui-row">
                <form id="queryForm" class="layui-form" action="">
                    <div class="layui-form-item">
                        <div class="layui-inline">
                            <label class="layui-form-label">标签名</label>
                            <div class="layui-input-inline">
                                <input type="text" name="label" lay-filter="label" autocomplete="off"
                                       placeholder="请输入标签名"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <label class="layui-form-label">数据值</label>
                            <div class="layui-input-inline">
                                <input type="text" name="value" lay-filter="value" autocomplete="off"
                                       placeholder="请输入数据值"
                                       class="layui-input">
                            </div>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn" lay-submit lay-filter="search">搜索</button>
                            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="layui-btn-group">
                <button id="addDictCodeBtn" class="layui-btn layui-btn-sm">新增</button>
            </div>
            <table class="layui-table" lay-filter="dataList">
                <thead>
                <tr>
                    <th lay-data="{type:'checkbox', fixed: 'left'}"></th>
                    <th lay-data="{field: 'id',hide:true, title: '编号', sort: true}"></th>
                    <th lay-data="{field: 'label', title: '标签名', sort: true}"></th>
                    <th lay-data="{field: 'value', title: '数据值', sort: true}"></th>
                    <%--<th lay-data="{field: 'type', title: '类型', sort: true}"></th>--%>
                    <th lay-data="{field: 'sort', title: '排序', sort: true}"></th>
                    <th lay-data="{field: 'style', title: '样式', sort: true}"></th>
                    <th lay-data="{fixed: 'right', width:178, align:'center', toolbar: '#barDemo'}"></th>
                </tr>
                </thead>
            </table>
            <script type="text/html" id="barDemo">
                <a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="detail">查看</a>
                <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit">编辑</a>
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
            </script>
        </div>
    </div>
</div>
</body>
<script>
    var $, table, element, form, zTree;
    var selectTeeItem = {id: '', name: ''};
    layui.use(['jquery', 'form', 'table', 'element'], function () {
        $ = layui.$;
        table = layui.table; //表格
        element = layui.element;
        form = layui.form;
        //监听工具条
        table.on('tools(dataList)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值
            if (layEvent === 'detail') {
                open.view('字典编码查看','${ctx}/admin/sys/dict/view?id=' + data.id,"600px","350px");
            } else if (layEvent === 'edit') {
                open.save('字典编码编辑','${ctx}/admin/sys/dict/edit?id=' + data.id,"600px","350px");
            } else if (layEvent === 'del') {
                open.del('字典编码删除','${ctx}/admin/sys/dict/delete?id=' + data.id);
            }
        });
        //右侧添加数据字典
        $("#addDictCodeBtn").bind("click", function () {
            if (selectTeeItem.id == '') {
                top.layer.msg('请选择分类');
                return;
            }
            open.save('字典编码新增','${ctx}/admin/sys/dict/add/?pid=' + selectTeeItem.id,"600px","350px");
        });

        //转换静态表格
        table.init('dataList', {
            id: 'dataList',//生成 Layui table 的标识 id，必须提供，用于后文刷新操作
            method: 'POST',
            url: '${ctx}/admin/sys/dict/getList',
            where: {type: 'dictCode'},
            page: true,
            height: 'full-180' //设置高度
        });

        //监听提交
        form.on('submit(search)', function (data) {
            reloadList(data.field);
            return false;
        });

    });

    //左侧添加数据字典
    $("#addDictTypeBtn").bind("click", function () {
        open.save('数据字典新增','${ctx}/admin/sys/dict/dictTypeAdd?pid=0',"600px","350px");
    });
    //左侧查看
    function viewType(id, name) {
        open.view('数据字典查看','${ctx}/admin/sys/dict/view?id=' + id,"600px","350px");
    }
    //左侧编辑
    function editType(id, name) {
        open.save('数据字典编辑', '${ctx}/admin/sys/dict/dictTypeEdit?id=' + id,"600px","350px");
    }
    //左侧删除
    function delType(id, name) {
        open.del('数据字典删除','${ctx}/admin/sys/dict/delete?id=' + id,function() {
            initTree();
        });
    }

    $(document).ready(function () {
        initTree();
    });

    var setting = {
        data: {
            key: {
                name: "name"  //显示的字段名称
            }
        },
        view: {
            showLine: false,
            showIcon: false,
            selectedMulti: false,
            dblClickExpand: false,
            addDiyDom: addOpDom
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                selectTeeItem.id = treeNode.id;
                selectTeeItem.name = treeNode.name;
                $("#queryForm")[0].reset();
                $("#itemTitle").text("字典项列表 - " + treeNode.name);
                reloadList({pid: treeNode.id});
            }
        }
    };

    function addOpDom(treeId, treeNode) {
        if (treeNode.parentNode && treeNode.parentNode.id != 1) return;
        var aObj = $("#" + treeNode.tId + "_a");
        var editStr = [
            "<span class='diybtn'>",
            "<i  id='viewType_" + treeNode.id + "' lay-tips='查看' class='layui-icon info select'>&#xe60b;</i>",
            "<i  id='editType_" + treeNode.id + "' lay-tips='编辑' class='layui-icon edit select'>&#xe642;</i>",
            "<i  id='delType_" + treeNode.id + "' lay-tips='删除' class='layui-icon del select'>&#xe640;</i>",
            "</span>"
        ];
        aObj.after(editStr.join(""));
        var viewBtn = $("#viewType_" + treeNode.id);
        if (viewBtn) viewBtn.bind("click", function () {
            viewType(treeNode.id, treeNode.name)
        });
        var editBtn = $("#editType_" + treeNode.id);
        if (editBtn) editBtn.bind("click", function () {
            editType(treeNode.id, treeNode.name)
        });
        var delBtn = $("#delType_" + treeNode.id);
        if (delBtn) delBtn.bind("click", function () {
            delType(treeNode.id, treeNode.name)
        });
        //Tips
        $('*[lay-tips]').bind('mouseover', function () {
            console.log('lay-tips');
            var content = $(this).attr('lay-tips');
            this.index = layer.tips(content, this, {
                time: -1
                , maxWidth: 280
                , tips: [2, '#3595CC']
            });
        }).bind('mouseout', function () {
            layer.close(this.index);
        });
    }

    var initTree = function () {
        var restData;
        request.doAjaxPostSynch("${ctx}/admin/sys/dict/getDictTree?type=dict", null,
            function success(data) {
                if (data.isOk) {
                    if (data.list instanceof Array) {
                        restData = data.list;
                    }
                }
            })
        zTree = jQuery.fn.zTree.init($("#dictTree"), setting, restData);
    }

    var reloadList = function (data) {
        if (data) {
            table.reload("dataList", {
                page: {curr: 1},
                where: {pid:selectTeeItem.id,type:"dictCode",label:data.label,value:data.value}
            });
        } else {
            table.reload("dataList", {page: {curr: 1}});
        }
    }

</script>


</html>
