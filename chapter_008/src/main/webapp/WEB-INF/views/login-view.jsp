<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User App login page</title>
</head>
<body>
<h1>Users web-app</h1>
<br>
<h2>Login page</h2>
<br>
<c:if test="${'' ne requestScope.error}">
    <div style="background-color: red">
        <c:out value="${requestScope.error}"/>
    </div>
    <br/>
</c:if>
    <form action="${pageContext.servletContext.contextPath}/login-view" method="post">
        Login : <input type="text" name="login"><br/>
        Password : <input type="password" name="password"><br/>
        <input type="submit" name="action" value="Auth">
    </form>
    <form>
        <button formaction="${pageContext.servletContext.contextPath}/create-user" formmethod="get">Registration</button>
    </form>
</body>
</html>
