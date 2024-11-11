<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/include/taglib.jsp" %>
<%@ attribute name="printContentId" type="java.lang.String" required="true"%>
<%@ attribute name="title" type="java.lang.String" required="true"%>
<%@ attribute name="printTop" type="java.lang.String" required="true"%>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js">
<!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>打印</title>
    <meta name="description" content="jQuery.print is a plugin for printing specific parts of a page">
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="${ctxStatic}/common/jQueryPrint/css/normalize.min.css">
    <style type='text/css'>
        .a {
            background: black;
            color: white;
        }
        .b {
            color: #aaa;
        }
    </style>
    <!--[if lt IE 9]>
    <script src="${ctxStatic}/common/jQueryPrint/js/vendor/html5-3.6-respond-1.1.0.min.js"></script>
    <![endif]-->
</head>
<body class="layui-body main-content">
<!--[if lt IE 9]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
<![endif]-->
<button id="print" class="layui-btn layui-btn-sm"><i class="fa fa-print"></i> ${title}</button>
<script src="${ctxStatic}/common/jQueryPrint/jQuery.print.js"></script>
<script type='text/javascript'>
    //<![CDATA[
    jQuery(function($) { 'use strict';
        $("#print").on('click', function() {
            //Print ele4 with custom options
            $("#${printContentId}").print({
                //Use Global styles
                globalStyles : false,
                //Add link with attrbute media=print
                mediaPrint : false,
                //Print in a hidden iframe
                iframe : true,
                //Don't print this
                noPrintSelector : ".avoid-this",
                //Add this at top
                prepend : "<center>${printTop}</center><br/>",
                //Add this on bottom
                append : "<br/>Buh Bye!",
                //Log to console when printing is done via a deffered callback
                deferred: $.Deferred().done(function() { console.log('Printing done', arguments); })
            });
        });
    });
    //]]>
</script>
</body>
</html>
