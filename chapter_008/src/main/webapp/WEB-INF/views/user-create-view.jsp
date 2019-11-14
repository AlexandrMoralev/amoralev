<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create user</title>
</head>
<body>
<h1>Users web-app</h1>
<br>
<h2>Create user</h2>
<br>
<form action="${pageContext.servletContext.contextPath}/users" method="post">
    Name : <input type='text' name='name'/><br>
    Login : <input type='text' name='login'/><br>
    e-mail : <input type='text' name='email'/><br>
    Password : <input type='password' name='password'/><br>
    Role : <select name="role" title="choose role">
            <option value="admin">admin</option>
            <option value="manager">manager</option>
            <option value="user">user</option>
            <option value="maintenance">maintenance</option>
            <option value="guest">guest</option>
        </select>
    <br>
    <input type="submit" name="action" value="create">
</form>
</body>
</html>
