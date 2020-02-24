<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Update user</title>
</head>
<body>
<h1>Users web-app</h1>
<br>
<h2>Update user</h2>
<br>
<table style="width:60%; height:auto;" border="1">
    <form action="${pageContext.servletContext.contextPath}/users" method="post">
        <tr>
            <td>Name : <input type='text' name='name' value='${requestScope.user.name}'/></td>
            <td>Login : <input type='text' name='login' value='${requestScope.user.login}'/></td>
            <td>e-mail : <input type='text' name='email' value='${requestScope.user.email}'/></td>
            <td>role : <input type='text' name='role' value='${requestScope.user.role}'/></td>
            <td><input type='hidden' name='id' value='${requestScope.user.id}'></td>
            <td><input type='hidden' name='pwd' value='${requestScope.user.password}'></td>
            <td><input type='submit' name='action' value='update'></td>
        </tr>
    </form>
</table>
<br>
<form>
    <button formaction="${pageContext.servletContext.contextPath}/users" formmethod="get">Cancel</button>
</form>
</body>
</html>
