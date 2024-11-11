<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<%@ attribute name="url" type="java.lang.String" required="true" %>
<%@ attribute name="title" type="java.lang.String" required="true" %>
<button id="btnImport" class="layui-btn layui-btn-sm "><i class="fa fa-folder-open-o"></i> ${title}</button>
<div id="importBox" style="display:none;">
    <form id="importForm" method="post" enctype="multipart/form-data" style="padding-left:20px;text-align:center;"><br/>
        <input style="float: left" id="uploadFile" name="file" type="file"/><br/>
        <span style="float: left;color: #da0a0a">导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！</span><br/>　　
    </form>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#btnImport").click(function () {
            top.layer.open({
                type: 1,
                area: [500, 300],
                title: "导入数据",
                content: $("#importBox").html(),
                btn: ['下载模板', '确定', '关闭'],
                btn1: function (index, layero) {
                    window.location.href = '${url}/template';
                },
                btn2: function (index, layero) {
                    var inputForm = new FormData(top.$("#importForm")[0]);
                    $.ajax({
                        url: "${url}",
                        type: "post",
                        data: inputForm,
                        cache: false,
                        processData: false,
                        contentType: false,
                        dataType: 'json',//后台需要返回json字符串
                        success: function (data) {
                            if (data.isOk) {
                                resultInfo.ok(data.info);
                                var indexFrame = parent.layer.getFrameIndex(window.name); //获取窗口索引
                                top["mainFrame"].reloadList();
                            } else {
                                resultInfo.error(data.info);
                            }
                        },
                        error: function (e) {
                            resultInfo.error("网络错误，请重试！！");
                        }
                    });
                    top.layer.close(index);
                },

                btn3: function (index) {
                    top.layer.close(index);
                }
            });
        });

    });

</script>