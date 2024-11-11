<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="file" tagdir="/WEB-INF/tags/file" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<%@ attribute name="refId" type="java.lang.String" required="true" %>
<%@ attribute name="attachType" type="java.lang.String" required="true" %>
<%@ attribute name="disabled" type="java.lang.Boolean" required="false" %>
<%--说明：该标签需要放到form标签里面，因为有表单字段--%>

<div class="layui-upload" style="overflow:auto;">
    <%--这个标签需要放到form表单内--%>
    <input type="hidden" name="fileFlag" value="true">
    <div class="layui-upload-list">
        <table class="layui-table">
            <thead>
            <tr>
                <th style="text-align: center">文件</th>
                <th style="width: 100px;text-align: center">文件名</th>
                <th style="width: 100px;text-align: center">大小</th>
                <th style="min-width: 100px;text-align: center">状态</th>
                <th style="min-width: 150px;text-align: center">操作</th>
            </tr>
            </thead>
            <tbody id="fileHistory"></tbody>
            <tbody id="fileList"></tbody>
        </table>
    </div>
    <c:if test="${!disabled}">
        <div style="margin: 5px;">
            <button type="button" class="layui-btn layui-btn-xs layui-btn-normal" style="margin-left: 5px;" id="chooseFile">选择文件</button>
                <%--<button type="button" class="layui-btn layui-btn-xs" id="chooseFileAction">开始上传</button>--%>
        </div>
    </c:if>
</div>

<script>

    $(function () {
        getFileHisstory();
        if (${!disabled}) {
            clearFileCache();
        }
    });

    //清除附件缓存
    function clearFileCache() {
        request.doAjaxPost("${ctx}/sysAttach/clearFileCache", null, function (data) {
        })
    }

    function iniHistory(data) {
        if (data.length > 0) {
            for (var i = 0; i < data.length; i++) {
                var tr = $(['<tr id="upload-' + data[i].id + '">'
                    , '<td style="text-align: center"><img style="min-width: 100px; min-height: 100px;max-width: 100px; max-height: 100px" id="img-' + data[i].id + '" src=' + getFileInco(data[i]) + '></td>'
                    , '<td style="text-align: center">' + data[i].fileName + '</td>'
                    , '<td style="text-align: center">' + data[i].fileSize + 'kb</td>'
                    , '<td style="text-align: center">已保存</td>'
                    , '<td style="text-align: center">'
                    , '<input type="button" class="layui-btn layui-btn-xs disabled" value="下载" fileId=' + data[i].id + '  onclick="downFile($(this))">'
                    , '<input type="button" class="layui-btn layui-btn-xs disabled" value="预览" fileId=' + data[i].id + '  onclick="viewFile($(this))">'
                    <c:if test="${!disabled}">
                    , '<input type="button" class="layui-btn layui-btn-xs layui-btn-danger" value="删除"  fileId=' + data[i].id + '  onclick="deleteFile($(this))">'
                    </c:if>
                    , '</td>'
                    , '</tr>'].join(''));
                $("#fileHistory").append(tr);
            }
        }
    }

    function getFileHisstory() {
        $.ajax({
            type: 'post',
            url: "${ctx}/sysAttach/getFileHistory?refId=${refId}",
            async: false,//关闭异步
            dataType: 'json',//后台需要返回json字符串
            success: function (data) {
                if (data.isOk) {
                    iniHistory(data.list)
                }
            }
        })
    }

    function downFile(obj) {
        var fileId = obj.attr("fileId");
        location.href = "${ctx}/sysAttach/downFile?id=" + fileId;
    }

    function viewFile(obj) {
        var fileId = obj.attr("fileId");
        $('#img-' + fileId).click();
    }

    function deleteFile(obj) {
        var fileListView = $('#fileList');
        var length = fileListView.find('img').length;
        if (length > 0) {
            resultInfo.warn("您有临时文件未保存,请先进行保存。");
            return false;
        }
        var fileId = obj.attr("fileId");
        $.ajax({
            type: 'post',
            url: "${ctx}/sysAttach/deleteFile?id=" + fileId,
            async: false,//关闭异步
            dataType: 'json',//后台需要返回json字符串
            success: function (data) {
                if (data.isOk) {
                    obj.parent().parent().remove();
                    resultInfo.ok(data.info);
                    //location.reload();
                } else {
                    resultInfo.error(data.info);
                }
            }
        })
    }

    function removeFile(obj) {
        var fileId = obj.attr("fileId");
        $.ajax({
            type: 'post',
            url: "${ctx}/sysAttach/clearReferCache?attachId=" + fileId,
            async: false,//关闭异步
            dataType: 'json',//后台需要返回json字符串
            success: function (data) {
                if (data.isOk) {
                    obj.parent().parent().remove();
                }
            }
        })
    }

    layui.use('upload', function () {
        var $ = layui.jquery, upload = layui.upload;
        //多文件列表示例
        var fileListView = $('#fileList')
            , uploadListIns = upload.render({
            elem: '#chooseFile'
            , url: '${ctx}/sysAttach/uploadFile/file/${attachType}'
            , accept: 'file' //普通文件
            , exts: '${fns:getConst('upload.file.limitType')}' //只允许上传压缩文件
            , multiple: true
            , auto: true
            , bindAction: '#chooseFileAction'
            , choose: function (obj) {
                var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                //读取本地文件
                obj.preview(function (index, file, result) {
                    var tr = $(['<tr id="upload-' + index + '">'
                        , '<td style="text-align: center"><img style="min-width: 100px;max-height: 100px; min-height: 100px;"></td>'
                        , '<td style="text-align: center">' + file.name + '</td>'
                        , '<td style="text-align: center">' + (file.size / 1024).toFixed(2) + 'kb</td>'
                        , '<td style="text-align: center">上传中...</td>'
                        , '<td style="text-align: center">'
                        // , '<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                        , '<button class="layui-btn layui-btn-xs layui-btn-warm demo-delete">移除</button>'
                        , '</td>'
                        , '</tr>'].join(''));

                    //单个重传
                    tr.find('.demo-reload').on('click', function () {
                        obj.upload(index, file);
                    });

                    //删除
                    tr.find('.demo-delete').on('click', function () {
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    });
                    fileListView.append(tr);
                });
            }
            , done: function (data, index, upload) {
                if (data.isOk) { //上传成功
                    var file = data.obj;
                    var tr = fileListView.find('tr#upload-' + index), tds = tr.children();
                    tds.eq(0).html("<img style='min-width: 100px; min-height: 100px;max-width: 100px; max-height: 100px' src=" + getFileInco(file) + ">");
                    tds.eq(1).html("<span style='color: #5FB878;'>" + file.fileName + "</span>");
                    tds.eq(2).html("<span style='color: #5FB878;'>" + file.fileSize + "kb</span>");
                    tds.eq(3).html("<span style='color: #5FB878;'>已上传/待保存</span>"); //清空操作
                    tds.eq(4).html("<input type='button' class='layui-btn layui-btn-xs layui-btn-warm' value='移除' fileId='" + file.id + "' onclick='removeFile($(this))'>"); //清空操作
                    return delete this.files[index]; //删除文件队列已经上传成功的文件
                }
                this.error(data, index, upload);
            }
            , error: function (data, index, upload) {
                var tr = fileListView.find('tr#upload-' + index)
                    , tds = tr.children();
                tds.eq(1).css("color", "#FF5722");
                tds.eq(2).html('<span style="color: #FF5722;">' + data.info + '</span>');
                tds.eq(3).html('<span style="color: #FF5722;">上传失败</span>');
                // tds.eq(4).find('.demo-reload').removeClass('layui-hide'); //显示重传
            }
        });

    });

    function getFileInco(file) {
        var fileName = file.fileName;
        var name = fileName.toLowerCase();
        var inco = "${ctxStatic}/images/config/inco/other.png";
        if (/.(gif|jpg|jpeg|png)$/.test(name)) {
            inco = '${fns:getConst("upload.basePath")}/' + file.savePath;
        } else if (/.(xlsx|xls)$/.test(name)) {
            inco = "${ctxStatic}/images/config/inco/xls.png";
        } else if (/.(docx|doc|rtf)$/.test(name)) {
            inco = "${ctxStatic}/images/config/inco/word.png";
        } else if (/.(txt)$/.test(name)) {
            inco = "${ctxStatic}/images/config/inco/txt.png";
        } else if (/.(zip)$/.test(name)) {
            inco = "${ctxStatic}/images/config/inco/zip.png";
        } else if (/.(pdf)$/.test(name)) {
            inco = "${ctxStatic}/images/config/inco/pdf.png";
        } else {
            inco = "${ctxStatic}/images/config/inco/other.png";
        }
        return inco;
    }

</script>
<file:picViewer title="图片预览" viewerId="fileHistory"></file:picViewer>