<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@ page import="com.jansing.office.utils.Exceptions" %>
<%@ page import="com.jansing.office.utils.StringUtil" %>
<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ include file="/WEB-INF/views/include/head.jsp"%>
<%
    response.setStatus(500);

    // 获取异常类
    Throwable ex = Exceptions.getThrowable(request);
    if (ex != null) {
        LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
        Exception myException = new Exception();
        myException.addSuppressed(ex);
    }

    // 编译错误信息
    StringBuilder sb = new StringBuilder("详细信息：\n");
    if (ex != null) {
        sb.append(Exceptions.getStackTraceAsString(ex));
    } else {
        sb.append("未知错误...\n\n");
    }


    // 输出异常信息页面
%>
<!DOCTYPE html>
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8" />
    <title> WEB OFFICE - 过分了啊！</title>
    <link rel="stylesheet" type="text/css" href="${ctxFront}/css/pages/error.css"/>
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-500-full-page">
<div class="row">
    <div class="col-md-12 page-500">
        <div class=" number">
            500
        </div>
        <div class=" details">
            <h3>我擦！过分了啊，居然出错！</h3>
            <p>
                请不要担心，我们正在解决<br />
                还是请移步<a href="${ctx}">首页吧</a><br /><br />
            </p>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-12" style="padding: 20px; padding-left: 50px;">
        <p>
            错误信息：<%=ex == null ? "未知错误..." : StringUtil.toHtml(ex.getMessage())%>
        </p>
        <p>
            <%=StringUtil.toHtml(sb.toString())%>
        </p>
    </div>
</div>
<script>
    jQuery(document).ready(function() {
        App.init();
    });
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>
<%
    out = pageContext.pushBody();
%>