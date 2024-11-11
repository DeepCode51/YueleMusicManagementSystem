<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<%@ attribute name="title" type="java.lang.String" required="true"%>
<%@ attribute name="viewerId" type="java.lang.String" required="true"%>
<link rel="stylesheet" href="${ctxStatic}/common/viewer/css/viewer.min.css">
<script src="${ctxStatic}/common/viewer/js/viewer.min.js"></script>
<script src="${ctxStatic}/common/viewer/js/viewer-jquery.min.js"></script>
<script>
    $(function() {
        $('#${viewerId}').viewer();
    });
</script>


