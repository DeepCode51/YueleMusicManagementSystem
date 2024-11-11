<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="dictType" type="java.lang.String" required="true" %>
<%@ attribute name="width" type="java.lang.String" required="false" %>
<%@ attribute name="height" type="java.lang.String" required="false" %>
<%-- 使用方法： 1.将本tag写在查询的form之前；2.传入table的id和controller的url --%>

<script type="text/html" id="${id}">
    <c:choose>
        <c:when test="${dictType=='office'}">
            <c:forEach items="${fns:getOfficeList()}" var="office">
                {{#  if(d.${name}=='${office.id}'){ }}
                ${office.name}
                {{#  } }}
            </c:forEach>
        </c:when>
        <c:when test="${dictType=='picture'}">
            {{#  if(d.${name}.indexOf('http')>-1){ }}
            <img style="height: ${height};width: ${width};border-radius: 5px" src="{{d.${name}}}">
            {{#  } else { }}
            <img style="height: ${height};width: ${width};border-radius: 5px" src="${fns:getConst('upload.basePath')}/{{d.${name}}}">
            {{#  } }}
        </c:when>
        <c:otherwise>
            <c:forEach items="${fns:getDictList(dictType)}" var="dict">
                {{#  if(d.${name}=='${dict.value}'){ }}
                <span style="${dict.style}">${dict.label}</span>
                {{#  } }}
            </c:forEach>
        </c:otherwise>
    </c:choose>
</script>
