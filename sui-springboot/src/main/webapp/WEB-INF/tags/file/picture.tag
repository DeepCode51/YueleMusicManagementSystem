<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="file" tagdir="/WEB-INF/tags/file" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="value" type="java.lang.String" required="true" %>
<%@ attribute name="verify" type="java.lang.String" required="false" %>
<%@ attribute name="width" type="java.lang.Integer" required="false" %>
<%@ attribute name="height" type="java.lang.Integer" required="false" %>
<%@ attribute name="attachType" type="java.lang.String" required="true" %>
<%@ attribute name="disabled" type="java.lang.Boolean" required="false" %>
<%-- fileFlag 不进行第三张表关联--%>
<input type="hidden" name="fileFlag" value="false">
<input type="hidden" lay-verify="${verify}" id="pic" name="${name}" value="${value}" class="layui-input">
<div class="file-picture-input">
    <img id="img" style="display:none;min-width: ${width==null?100:width}px; min-height: ${height==null?100:height}px;max-width: ${width==null?100:width}px; max-height: ${height==null?100:height}px; margin-left: 10px;float: left; border-radius: 5px">
    <c:if test="${!disabled}">
        <input type="button" id="upload"
               style="margin-left: 10px;float: left;width: 120px;height: 100px;border: none;background: #fbfbfb;color: #757575;"
               value="<c:if test="${not empty value}">点击修改图片 </c:if><c:if test="${empty value}">点击上传图片 </c:if>">
    </c:if>
</div>
<input style="display: none" value="${value}" type="file" accept="image/jpeg,image/png" id="file">
<file:picViewer title="图片预览" viewerId="img"></file:picViewer>
<script>
    $(function () {
        var $img = $("#img");
        $img.parent().parent().parent().find('label').css("line-height", "80px");
        $img.parent().parent().parent().find('label').css("height", "102px");
        if ('${value}' != "") {
            var imgSrc ='${fns:getConst("upload.basePath")}/${value}'
            if('${value}'.indexOf('http')>-1){
                imgSrc ='${value}'
            }
            $("#img").attr("src", imgSrc);
            $img.css("display", "block");
        }
    })
    $('#file').change(function () {
        upload();
    });
    $('#upload').click(function () {
        $("#file").trigger("click");
    });

    function upload() {
        var formFile = $('#file')[0].files[0];
        var formData = new FormData();
        formData.append("file", formFile);
        $.ajax({
            url: '${ctx}/sysAttach/uploadFile/image/${attachType}',
            dataType: 'json',
            type: 'POST',
            async: false,
            data: formData,
            processData: false, // 使数据不做处理
            contentType: false, // 不要设置Content-Type请求头
            success: function (data) {
                if (data.isOk) {
                    $("#pic").val(data.obj.savePath);
                    $("#img").css("display", "block");
                    $("#img").attr("src", "${fns:getConst("upload.basePath")}/" + data.obj.savePath)
                } else {
                    resultInfo.error(data.info);
                }
            }
        });
    }
</script>