<%--
  Created by IntelliJ IDEA.
  User: Александр
  Date: 28.08.2019
  Time: 23:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User App login page</title>
</head>
<c:if test="${error ne ''}">
    <div style="background-color: red">
        <c:out value="${error}"/>
    </div>
    <br/>
</c:if>
<body>
    <form action="${pageContext.servletContext.contextPath}/login" method="post">
        Login : <input type="text" name="login"><br/>
        Password : <input type="password" name="password"><br/>
        <input type="submit">
    </form>
</body>
</html>
