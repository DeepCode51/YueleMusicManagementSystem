<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="value" type="java.lang.String" required="false"%>
<%@ attribute name="verify" type="java.lang.String" required="false"%>
<%@ attribute name="belongName" type="java.lang.String" required="true"%>
<%@ attribute name="belongValue" type="java.lang.String" required="false"%>
<%@ attribute name="where" type="java.lang.String" required="false"%>
<%@ attribute name="belongInstance" type="java.lang.String" required="true"%>
<%@ attribute name="disabled" type="java.lang.Boolean" required="false"%>

<select id="${id}" name="${name}" lay-verify="${verify}" <c:if test="${disabled}">disabled="true"</c:if><c:if test="${!disabled}">lay-search=""</c:if>>
    <option value=""></option>
</select>
<script type="text/javascript">
    $.ajax({
        type: 'post',
        url: "${ctx}/${belongInstance}/getList?${where}",
        dataType: 'json',//后台需要返回json字符串
        async: false,//关闭异步:开启异步导致多个下拉加载数据失败
        success: function (result) {
            if (result.code=="00000") {
                var list = result.data;
                for (var i = 0; i < list.length; i++) {
                    $("#${id}").append("<option value='" + list[i].${belongValue} + "'>" + list[i].${belongName} + "</option>")
                }
            }
        }
    });
</script>
<script>
      <c:if test="${value!=null&&value!=''}">
      $("#${id}").val('${value}');
          setTimeout(function () {
              form.render('select');
          },1000)
      </c:if>
</script>