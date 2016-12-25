<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8" />
    <title> WEB OFFICE - 系统错误！</title>
    <%@ include file="/WEB-INF/views/include/head.jsp"%>
    <!-- BEGIN THEME STYLES -->
    <link rel="stylesheet" type="text/css" href="${ctxFront}/css/pages/error.css"/>
    <!-- END THEME STYLES -->
</head>
<!-- END HEAD -->
<!-- BEGIN BODY -->
<body class="page-404-full-page">
<div class="row">
    <div class="col-md-12 page-404">
        <div class="number">
            404
        </div>
        <div class="details">
            <h3>-_-#！程序员猝死了吗</h3>
            <p>
                <%--todo 意见反馈--%>
                不能忍！趁着白天，<a href="${ctx}/report">给他领导打个报告</a>
            </p>
        </div>
    </div>
</div>
<script>
    jQuery(document).ready(function() {
        App.init();
    });
</script>
</body>
<!-- END BODY -->
</html>