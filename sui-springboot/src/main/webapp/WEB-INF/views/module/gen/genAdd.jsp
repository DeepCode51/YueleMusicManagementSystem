<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<html>
<head>
    <title>创建表对象</title>
</head>
<body class="layui-body main-content">
<style>
    .checkbox{
        height: 15px;
        width: 15px;
    }
</style>
<script id="columOptionTpl" type="text/html">
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">orderNum、<input type="hidden" name="id" ></td>
        <td><input type="checkbox" class="checkbox" name="opert"></td>
        <td><input type="text" autocomplete="off" lay-verify="required" name="columnName" class="layui-input"></td>
        <td><input type="text" autocomplete="off" lay-verify="required" name="columnDesc" class="layui-input"></td>
        <td>
        <select name="inputType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
            <option value="text" selected="">单行文本</option>
            <option value="select" >下拉选择</option>
            <option value="date" >日期控件</option>
            <option value="picture">图片控件</option>
            <option value="associate">联想控件</option>
            <option value="textarea">多行文本</option>
            <option value="editor" >富文本框</option>
            <option value="office">机构控件</option>
        </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like" selected="">模糊查询</option>
                <option value="equal" >精确查询</option>
                <option value="between" >范围查询</option>
            </select>
        </td>
        <td><input type="text" name="dictType" readonly="true" style="background: gainsboro" class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" checked="checked"></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
</script>

<script type="text/html" id="sorts">
    {{d.LAY_TABLE_INDEX+1}}
</script>
<script id="baseTempt" type="text/html">
    <%--id--%>
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">选项1、<input type="hidden" name="id" > </td>
        <td><input type="checkbox" class="checkbox" name="opert" ></td>
        <td><input type="text"  name="columnName" value="id" class="layui-input"></td>
        <td><input type="text"  name="columnDesc"  value="主键id"  class="layui-input"></td>
        <td>
            <select name="inputType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="hidden" selected="">隐藏域</option>
                <option value="text">单行文本</option>
                <option value="select" >下拉选择</option>
                <option value="date" >日期控件</option>
                <option value="picture">图片控件</option>
                <option value="associate">联想控件</option>
                <option value="textarea">多行文本</option>
                <option value="editor" >富文本框</option>
                <option value="office">机构控件</option>
            </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like">模糊查询</option>
                <option value="equal" selected="">精确查询</option>
                <option value="between" >范围查询</option>
            </select>
        </td>
        <td><input type="text"  name="dictType" value="" readonly="true" style="background: gainsboro" class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" checked="checked"></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"checked="checked"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
    <%--创建人 create_by--%>
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">选项2、<input type="hidden" name="id" > </td>
        <td><input type="checkbox" class="checkbox" name="opert" ></td>
        <td><input type="text"  name="columnName" value="create_by" class="layui-input"></td>
        <td><input type="text"  name="columnDesc"  value="创建人" class="layui-input"></td>
        <td>
            <select name="inputType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="text" >单行文本</option>
                <option value="select" >下拉选择</option>
                <option value="date">日期控件</option>
                <option value="picture">图片控件</option>
                <option value="associate">联想控件</option>
                <option value="textarea">多行文本</option>
                <option value="editor" >富文本框</option>
                <option value="office">机构控件</option>
            </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like">模糊查询</option>
                <option value="equal" selected="">精确查询</option>
                <option value="between" >范围查询</option>
            </select>
        </td>
        <td><input type="text"  name="dictType" readonly="true" style="background: gainsboro" class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" ></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
    <%--创建时间 create_date--%>
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">选项3、<input type="hidden" name="id" > </td>
        <td><input type="checkbox" class="checkbox" name="opert" ></td>
        <td><input type="text"  name="columnName" value="create_date" class="layui-input"></td>
        <td><input type="text"  name="columnDesc"  value="创建时间"  class="layui-input"></td>
        <td>
            <select name="inputType" type="input" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="text" selected="">单行文本</option>
                <option value="select" >下拉选择</option>
                <option value="date" selected>日期控件</option>
                <option value="picture">图片控件</option>
                <option value="associate">联想控件</option>
                <option value="textarea">多行文本</option>
                <option value="editor" >富文本框</option>
                <option value="office">机构控件</option>
            </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like" >模糊查询</option>
                <option value="equal" selected="">精确查询</option>
                <option value="between">范围查询</option>
            </select>
        </td>
        <td><input type="text" name="dictType" readonly="true" style="background: gainsboro" class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" ></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
    <%--更新人 update_by--%>
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">选项4、<input type="hidden" name="id" > </td>
        <td><input type="checkbox" class="checkbox" name="opert"></td>
        <td><input type="text"  name="columnName" value="update_by" class="layui-input"></td>
        <td><input type="text"  name="columnDesc"  value="更新人" class="layui-input"></td>
        <td>
            <select name="inputType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="text" selected="">单行文本</option>
                <option value="select" >下拉选择</option>
                <option value="date" >日期控件</option>
                <option value="picture">图片控件</option>
                <option value="associate">联想控件</option>
                <option value="textarea">多行文本</option>
                <option value="editor" >富文本框</option>
                <option value="office">机构控件</option>
            </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like">模糊查询</option>
                <option value="equal" selected="">精确查询</option>
                <option value="between" >范围查询</option>
            </select>
        </td>
        <td><input type="text"  name="dictType" readonly="true" style="background: gainsboro" class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" ></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
    <%--更新时间 update_date--%>
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">选项5、<input type="hidden" name="id"> </td>
        <td><input type="checkbox" class="checkbox" name="opert" ></td>
        <td><input type="text"  name="columnName" value="update_date" class="layui-input"></td>
        <td><input type="text"  name="columnDesc"  value="更新时间"  class="layui-input"></td>
        <td>
            <select name="inputType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="text" selected="">单行文本</option>
                <option value="select" >下拉选择</option>
                <option value="date" selected>日期控件</option>
                <option value="picture">图片控件</option>
                <option value="associate">联想控件</option>
                <option value="textarea">多行文本</option>
                <option value="editor" >富文本框</option>
                <option value="office">机构控件</option>
            </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like" >模糊查询</option>
                <option value="equal" selected="">精确查询</option>
                <option value="between" >范围查询</option>
            </select>
        </td>
        <td><input type="text"  name="dictType" readonly="true" style="background: gainsboro"  class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" ></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
    </tr>
    <%--删除标识 del_flag--%>
    <tr class="layui-form-hand">
        <td class="layui-form-drag" style="cursor:pointer;">选项6、<input type="hidden" name="id" > </td>
        <td><input type="checkbox" class="checkbox" name="opert"></td>
        <td><input type="text"  name="columnName" value="del_flag" class="layui-input"></td>
        <td><input type="text"  name="columnDesc"  value="删除标识"  class="layui-input"></td>
        <td>
            <select name="inputType" lay-verify="required" onchange="changeInputType($(this))" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="text" selected="">单行文本</option>
                <option value="select" >下拉选择</option>
                <option value="date" >日期控件</option>
                <option value="picture">图片控件</option>
                <option value="associate">联想控件</option>
                <option value="textarea">多行文本</option>
                <option value="editor" >富文本框</option>
                <option value="office">机构控件</option>
            </select>
        </td>
        <td>
            <select name="queryType" lay-verify="required" style="border-color: #e6e6e6;border-radius: 2px;width: 180px;height: 38px;">
                <option value="like" >模糊查询</option>
                <option value="equal" selected="">精确查询</option>
                <option value="between" >范围查询</option>
            </select>
        </td>
        <td><input type="text"  name="dictType" readonly="true" style="background: gainsboro" class="layui-input"></td>
        <td><input type="checkbox" class="checkbox" name="isShow" ></td>
        <td><input type="checkbox" class="checkbox" name="isHidden"></td>
        <td><input type="checkbox" class="checkbox" name="isUnique"></td>
    </tr>
</script>

<form role="form" action="" method="post">
    <input id="addForm" type="hidden" lay-submit lay-filter="addForm">
    <blockquote class="layui-elem-quote" style="padding: 5px 10px;margin-bottom: 0;">
        代码配置 <span id="addRowBtn" class="layui-btn layui-btn-sm" style="margin-left: 10px"><i
            class="layui-icon">&#xe61f;</i></span>
        <div class="layui-inline">
            <label class="layui-form-label"><span style="color: red">*</span>表名</label>
            <div class="layui-input-inline">
                <input type="text" autocomplete="off" lay-verify="required" id="tableName" name="tableName" placeholder="请输入表名" class="layui-input">
            </div>
        </div>
        <div class="layui-inline">
            <label class="layui-form-label"><span style="color: red">*</span>表描述</label>
            <div class="layui-input-inline">
                <input type="text" autocomplete="off" lay-verify="required" id="tableDesc" name="tableDesc" placeholder="请输入表描述" class="layui-input">
            </div>
        </div>
    </blockquote>
    <div class="layui-btn-group"style="margin: 10px 10px 0px 10px">
        <button type="button" class="layui-btn layui-btn-primary layui-btn-sm" onclick="removeRow()">删除选项</button>
    </div>

    <div class="layui-form layui-form-pane" style="float: right;margin-top: 5px">
        <div class="layui-input-inline" style="width: 250px">
            <label class="layui-form-label">导入导出</label>
            <input type="checkbox" name="isPort" id="isPort" lay-filter="switchIsPort" lay-skin="switch" lay-text="启用|关闭">
        </div>
        <div class="layui-input-inline" style="width: 250px">
            <label class="layui-form-label">附件列表</label>
            <input type="checkbox" name="isFileList" id="isFileList" lay-filter="switchIsFileList" lay-skin="switch" lay-text="启用|关闭">
        </div>
        <div class="layui-input-inline" style="width: 250px">
            <label class="layui-form-label">开启锁定</label>
            <input type="checkbox" name="isLock" id="isLock" lay-filter="switchIsLock" lay-skin="switch" lay-text="启用|关闭">
        </div>
    </div>

    <table class="layui-table" lay-filter="dataList">
        <thead>
        <tr>
            <th>序号</th>
            <th>操作</th>
            <th>列名</th>
            <th>说明</th>
            <th>控件类型</th>
            <th>查询方式</th>
            <th>字典类型</th>
            <th>列表显示</th>
            <th>表单隐藏</th>
            <th>是否唯一</th>
        </tr>
        </thead>
        <tbody id="optionList">
        </tbody>
    </table>
</form>

<script type="text/javascript" src="${ctxStatic}/common/sortable/Sortable.js"></script>

<script>

    //移除表格行
    function removeRow() {
        var table = document.getElementById("optionList");
        var rows = table.rows;
        var removeRows =[];
        for (var i = 0; i < rows.length; i++) {
         if ($(rows[0].cells[1]).find('input').attr('name')=='opert') {
                var isChecked = $(rows[i].cells[1]).find('[type=checkbox]').is(':checked');
                if(isChecked){
                    removeRows.push(rows[i])
                }
            }
        }
        for(var i = 0; i < removeRows.length; i++){
            removeRows[i].remove();
        }
        if(removeRows.length>0){
            resultInfo.ok("移除"+removeRows.length+"个选项成功!");
        }else{
            resultInfo.warn("未选择任何选项!");
        }
    }

    //下拉联动
    function changeInputType(obj) {
        var $inputType = obj;
        var $queryType= $inputType.parent().parent().children().eq(5).find('select');
        var $dictType= $inputType.parent().parent().children().eq(6).find('input');
        if($inputType.val()=='select'){
            $queryType.val("equal");
            $dictType.css("background","white");
            $dictType.attr('readonly',false);
        }else if($inputType.val()=='date'){
            $queryType.val("between");
            $dictType.css("background","gainsboro");
            $dictType.attr('readonly',true);
        }else if($inputType.val()=='text'){
            $queryType.val("like");
            $dictType.css("background","gainsboro");
            $dictType.attr('readonly',true);
        }else if($inputType.val()=='textarea'){
            $queryType.val("like");
            $dictType.css("background","gainsboro");
            $dictType.attr('readonly',true);
        }else if($inputType.val()=='editor'){
            $queryType.val("like");
            $dictType.css("background","gainsboro");
            $dictType.attr('readonly',true);
        }else if($inputType.val()=='office'){
            $queryType.val("equal");
            $dictType.css("background","gainsboro");
            $dictType.attr('readonly',true);
        }else if($inputType.val()=='associate'){
            $queryType.val("equal");
            var optStr = ""
            $.ajax({
                type: 'post',
                url: "${ctx}/admin/sys/gen/getList",
                dataType: 'json',//后台需要返回json字符串
                async: false,//关闭异步:开启异步导致多个下拉加载数据失败
                success: function (result) {
                    if (result.code=="00000") {
                        var list = result.data;
                        for (var i = 0; i < list.length; i++) {
                            optStr=optStr+"<option style='text-align: left' value='" + list[i].tableName + "'>" + list[i].tableName+' 【'+list[i].comments +'】'+ "</option>";
                        }
                    }
                }
            });
            var content = "<center><apan>关联显示的字段名称</span><br><input id='selText' placeholder='选择表和字段' class='sel-input' type='text'value=''></center>" +
                "<hr><select id='selTable' onchange='selectTable()' class='sel-input'>" +
                "<option value=''></option>"+optStr+"</select>" +
                "<select style='text-align: left' onchange='selectFiled()' id='filed' class='sel-input'></select>"
            layer.open({
                title:"联想控件",
                content: content
                ,area: ['500px','300px']//宽,高
                ,btn: ['确认', '取消']
                ,yes: function(index, layero){
                    if($("#selTable").val()==''){
                        resultInfo.warn("请选择联想表");
                        return;
                    }
                    if($("#filed").val()==''){
                        resultInfo.warn("请选择联想字段");
                        return;
                    }
                    $dictType.val($("#selText").val());
                    layer.close(index);
                },btn2: function(index, layero){
                    return true;//点击后不关闭窗口，需返回false
                }
                ,cancel: function(){
                    //右上角关闭回调
                }
            });
        }else{
            $dictType.val('');
        }
        form.render('select');
    }

    function selectTable() {
        var tableName = $("#selTable").val();
        $("#filed").empty();
        $.ajax({
            type: 'post',
            url: "${ctx}/admin/sys/gen/getCloums?tableName="+tableName,
            dataType: 'json',//后台需要返回json字符串
            async: false,//关闭异步:开启异步导致多个下拉加载数据失败
            success: function (result) {
                if (result.code=="00000") {
                    var list = result.data;
                    $("#filed").append("<option value=''></option>")
                    for (var i = 0; i < list.length; i++) {
                        $("#filed").append("<option value='" + list[i].columnName + "'>" + list[i].columnName+' 【'+list[i].columnDescribe +'】'+ "</option>")
                    }
                }
            }
        });
    }

    function selectFiled(){
        var tableName = $("#selTable").val();
        var filed = $("#filed").val();
        $("#selText").val(tableName+":"+filed);
    }


    //获取表格数据
    function getDayValues() {
        var tableValues = [];
        var table = document.getElementById("optionList");
        var rows = table.rows;
        for (var i = 0; i < rows.length; i++) {
            var rowobj = {};
            for (var j = 0; j < rows[i].cells.length; j++) {
                if ($(rows[0].cells[j]).find('input').attr('name')) {
                    if ($(rows[0].cells[j]).find('input').attr('name')=='columnDesc') {
                        if($(rows[i].cells[j]).find('input').val()==""){
                            $(rows[i].cells[j]).find('input').focus();
                            $(rows[i].cells[j]).find('input').css("border","solid 1px red");
                            return;
                        }
                        rowobj['columnDesc'] = $(rows[i].cells[j]).find('input').val();
                    } else if ($(rows[0].cells[j]).find('input').attr('name')=='columnName') {
                        if($(rows[i].cells[j]).find('input').val()==""){
                            $(rows[i].cells[j]).find('input').focus();
                            $(rows[i].cells[j]).find('input').css("border","solid 1px red");
                            return;
                        }
                        rowobj['columnName'] = $(rows[i].cells[j]).find('input').val();
                    }  else if ($(rows[0].cells[j]).find('input').attr('name')=='dictType') {
                        rowobj['dictType'] = $(rows[i].cells[j]).find('input').val();
                    }else if ($(rows[0].cells[j]).find('input').attr('name')=='id') {
                        rowobj['id'] = $(rows[i].cells[j]).find('input').val();
                    } else if ($(rows[0].cells[j]).find('input').attr('name')=='isShow') {
                        rowobj['isShow'] = $(rows[i].cells[j]).find('[type=checkbox]').is(':checked')==true?1:0;
                    } else if ($(rows[0].cells[j]).find('input').attr('name')=='opert') {
                        rowobj['opert'] = $(rows[i].cells[j]).find('[type=checkbox]').is(':checked')==true?1:0;
                    } else if ($(rows[0].cells[j]).find('input').attr('name')=='isHidden') {
                        rowobj['isHidden'] = $(rows[i].cells[j]).find('[type=checkbox]').is(':checked')==true?1:0;
                    } else if ($(rows[0].cells[j]).find('input').attr('name')=='isUnique') {
                        rowobj['isUnique'] = $(rows[i].cells[j]).find('[type=checkbox]').is(':checked')==true?1:0;
                    }
                }else if($(rows[0].cells[j]).find('select').attr('name')){ //下拉控件:其他标签是input类型
                    if ($(rows[0].cells[j]).find('select').attr('name')=='inputType') {
                        rowobj['inputType'] = $(rows[i].cells[j]).find('select').val();
                    } else if ($(rows[0].cells[j]).find('select').attr('name')=='queryType') {
                        rowobj['queryType'] = $(rows[i].cells[j]).find('select').val();
                    }
                }
                $(rows[i].cells[j]).find('input').css("border","solid 1px #e6e6e6");
            }
            tableValues.push(rowobj);
        }
        return tableValues;
    }
</script>

<script>
   function setConfig(){
       request.doAjaxPost('${ctx}/admin/sys/gen/setConfig', {
               tableName:$("#tableName").val(),
               tableDesc:$("#tableDesc").val(),
               isPort:$("#isPort").val(),
               isLock:$("#isLock").val(),
               isFileList:$("#isFileList").val()
           },
           function success(data) {//操作成功
               if (data.isOk) {
                   resultInfo.ok(data.info);
               } else {
                   resultInfo.error(data.info);
               }
           }
       );
   }
    var $, element, form, layer, laytpl;
    var parentWin = {index: '', name: ''};
    var options = [];
    var isUseIntroduce = 0;
    layui.use(['jquery', 'form', 'layer', 'laytpl', 'element'], function () {
        $ = layui.$;
        element = layui.element;
        form = layui.form;
        layer = layui.layer;
        laytpl = layui.laytpl;
        //监听指定开关
        form.on('switch(switchIsPort)', function(formObj){
            $("#isPort").val(this.checked);
            setConfig();
        });
        form.on('switch(switchIsLock)', function(formObj){
            $("#isLock").val(this.checked);
            setConfig();
        });
        form.on('switch(switchIsFileList)', function(formObj){
            $("#isFileList").val(this.checked);
            setConfig();
        });
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        //初始化
        var $baseTemptHrml = $('#baseTempt').html();
        $("#optionList").append($baseTemptHrml);
        $("#tableName").focus();
        form.on('submit(addForm)', function (formObj) {
            var options = getDayValues();
            for(var i=0;i<options.length;i++){
                formObj.field["tableOptions[" + i + "].id"] ="";
                formObj.field["tableOptions[" + i + "].sort"] =i;
                formObj.field["tableOptions[" + i + "].inputType"] =options[i].inputType;
                formObj.field["tableOptions[" + i + "].queryType"] =options[i].queryType;
                formObj.field["tableOptions[" + i + "].dictType"] =options[i].dictType;
                formObj.field["tableOptions[" + i + "].columnDesc"] =options[i].columnDesc;
                formObj.field["tableOptions[" + i + "].columnName"] =(options[i].columnName).toLowerCase();
                formObj.field["tableOptions[" + i + "].isShow"] =options[i].isShow;
                formObj.field["tableOptions[" + i + "].isHidden"] =options[i].isHidden;
                formObj.field["tableOptions[" + i + "].isUnique"] =options[i].isUnique;
                formObj.field["tableOptions[" + i + "].opert"] =options[i].opert;
            }
            var tableName = $("#tableName").val();
            var tableDesc = $("#tableDesc").val();
            var isLock = $("#isLock").val();
            var isFileList = $("#isFileList").val();
            var isOffice = $("#isOffice").val();

            formObj.field["tableName"] =tableName;
            formObj.field["tableDesc"] =tableDesc;
            formObj.field["isLock"] =isLock;
            formObj.field["isFileList"] =isFileList;
            formObj.field["isOffice"] =isOffice;
            if(tableName==""){
                $("#tableName").css("border","solid 1px red");
                $("#tableName").focus();
                resultInfo.error("请输入表名！");
                return false;
            }
            $("#tableName").css("border","solid 1px #e6e6e6");
            if(tableDesc==""){
                $("#tableDesc").css("border","solid 1px red");
                $("#tableDesc").focus();
                resultInfo.error("请输入表描述！");
                return false;
            }
            request.doAjaxPost('${ctx}/admin/sys/gen/addSub', formObj.field,
                function success(data) {//操作成功
                    if (data.isOk) {
                        resultInfo.ok("创建成功");
                        page.closeCurrentPage();//关闭当前窗口
                        top.reloadList();
                        // page.reloadListData();//刷新列表页面数据
                    } else {
                        resultInfo.error(data.info);
                    }
                }
            );
        })

        $("#addRowBtn").bind("click", function () {
            var $OptionTpl = $('#columOptionTpl').html();
            var table = document.getElementById("optionList");
            var rows = table.rows;
            var $OptionTpl_new = $OptionTpl.replace("orderNum","选项"+(rows.length+1));
            $("#optionList").append($OptionTpl_new);
            form.render();
        })

        var itemVoteOptionsList = Sortable.create(document.getElementById("optionList"), {
            animation: 150, // ms, animation speed moving items when sorting, `0` — without animation
            handle: ".layui-form-drag", // Restricts sort start click/touch to the specified element
            draggable: ".layui-form-hand", // Specifies which items inside the element should be sortable
            onUpdate: function (evt/**Event*/) {
//                var item = evt.item; // the current dragged HTMLElement
//                var newOrderList = [];
//                $("#optionList").children().each(function (i) {
//                    var orderNum = $(this).find("input[key='orderNum']").val();
//                    var optionData = options[orderNum - 1];
//                    optionData.orderNum = i + 1;
//                    newOrderList.push(optionData);
//                });
//                options = newOrderList;
            }
        });
    });

    function subForm() {
        $("#addForm").click();
        return false;
    }
</script>
</body>
<style>
    .sel-input{
        border-radius: 5px;
        height: 30px;
        width: 220px;
        text-align: center;
    }
</style>
</html>