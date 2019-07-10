<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>Result</title>
    <meta http-equiv='refresh' content='2; url= <%= request.getContextPath() %>/list'>
</head>
<body>
    <% if ("1".equals(request.getParameter("result"))) {%>
    <b>Action done</b>
    <% } else { %>
    <b>Action error</b>
    <% } %>
</body>
</html>
