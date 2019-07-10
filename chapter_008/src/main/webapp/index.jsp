<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users App</title>
</head>
<body>
<% String url = request.getContextPath() + "/list"; %>
<% response.sendRedirect(url);%>

</body>
</html>
