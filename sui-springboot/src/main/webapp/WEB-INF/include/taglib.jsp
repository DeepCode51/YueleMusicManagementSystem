<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags/input" %>
<%@ taglib prefix="table" tagdir="/WEB-INF/tags/table" %>
<%@ taglib prefix="echarts" tagdir="/WEB-INF/tags/echarts" %>
<%@ taglib prefix="button" tagdir="/WEB-INF/tags/button" %>
<%@ taglib prefix="file" tagdir="/WEB-INF/tags/file" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<link rel="stylesheet" href="${ctxStatic}/common/layui-v2.5.4/layui/lay/extend/css/layuiExtend.css">
<link rel="stylesheet" href="${ctxStatic}/common/layui-v2.5.4/layui/css/layui.css"  media="all">
<link rel="stylesheet" href="${ctxStatic}/common/font-awesome-4.7.0/css/font-awesome.min.css">
<script src="${ctxStatic}/common/jquery-v3.3.1/jquery-3.3.1.min.js"></script>
<script src="${ctxStatic}/common/jquery-v3.3.1/jquery.serializejson.min.js"></script>
<script src="${ctxStatic}/common/utils/stringUtils.js?_v=${version}"></script>
<script src="${ctxStatic}/common/layui-v2.5.4/layui/layui.js"></script>
<script src="${ctxStatic}/common/utils/common.js?_v=${version}"></script>

<script>ctx = '${ctx}'</script>






