<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="id" type="java.lang.String" required="true"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="value" type="java.lang.String" required="false"%>
<%@ attribute name="verify" type="java.lang.String" required="false"%>
<%@ attribute name="dictType" type="java.lang.String" required="true"%>
<%@ attribute name="disabled" type="java.lang.String" required="false"%>

<select id="${id}" name="${name}" lay-verify="${verify}" <c:if test="${disabled}">disabled="true" </c:if><c:if test="${!disabled}">lay-search=""</c:if> >
    <option value=""></option>
    <c:forEach items="${fns:getDictList(dictType)}" var="dict">
        <option value="${dict.value}">${dict.label}</option>
    </c:forEach>
</select>
<script>
    <c:if test="${value!=null&&value!=''}">
    $("#${id}").val('${value}');
        setTimeout(function () {
            form.render('select');
        },1000)
    </c:if>
</script>
