<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8" />
    <title> WEB OFFICE - 页面居然不存在！</title>
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
            <h3>我了个去！页面居然不存在</h3>
            <p>
                哎，算了，我们还是<a href="${ctx}">回首页吧</a>
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