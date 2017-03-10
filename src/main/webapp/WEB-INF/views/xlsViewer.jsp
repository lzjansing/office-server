<%--
  Created by IntelliJ IDEA.
  User: jansing
  Date: 17-3-9
  Time: 上午9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${fileName}</title>
    <%@ include file="/WEB-INF/views/include/head.jsp"%>
</head>
<body>
<script type="application/javascript">
    window.location.href="${fn:startsWith(file, 'http://')?file:ctx.concat(file)}";
</script>
</body>
</html>
