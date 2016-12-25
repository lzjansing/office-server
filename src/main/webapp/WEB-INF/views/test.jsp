<%--
  Created by IntelliJ IDEA.
  User: jansing
  Date: 16-12-20
  Time: 下午3:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>WEB OFFICE</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="google" content="notranslate">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <%@ include file="/WEB-INF/views/include/head.jsp"%>
    <%--<link rel="stylesheet" href="${ctxFront}/plugins/pdf/viewer.css">--%>
    <%--<script src="${ctxFront}/plugins/pdf/compatibility.js"></script>--%>

    <%--<!-- This snippet is used in production (included from viewer.html) -->--%>
    <%--<link rel="resource" type="application/l10n" href="${ctxFront}/plugins/pdf/viewer.properties">--%>
    <%--<script src="${ctxFront}/plugins/pdf/build/pdf.js"></script>--%>



    <%--<script src="${ctxFront}/plugins/pdf/debugger.js"></script>--%>
    <script type="application/javascript">
        <%--//TODO 在viewer.js中用到--%>
        var file = ctx+'${file eq null or file eq ''?'/index.pdf':file}';
        console.log(file);
    </script>
    <%--<script src="${ctxFront}/plugins/pdf/viewer.js"></script>--%>
</head>
<body>
${file}
</body>
</html>
