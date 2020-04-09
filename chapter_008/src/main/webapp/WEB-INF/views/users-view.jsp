<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All users</title>
</head>
<body>
<h1>Users web-app</h1>
<br>
<h2>All users</h2>
<br>
<table style="width:auto; height:auto;" border="2">
    <tr>
        <th>id</th>
        <th>name</th>
        <th>login</th>
        <th>created</th>
        <th>role</th>
        <th>country</th>
        <th>city</th>
        <th>actions</th>
    </tr>
    <c:forEach items="${requestScope.userList}" var="user">
        <tr>
            <td> ${user.id} </td>
            <td> ${user.name}</td>
            <td> ${user.login}</td>
            <td> ${user.created}</td>
            <td> ${user.role.description}</td>
            <td> ${user.address.country}</td>
            <td> ${user.address.city}</td>
            <td>
                <form>
                    <button formaction="${pageContext.servletContext.contextPath}/update-user" formmethod="get" name="userId" value="${user.id}">
                        Update
                    </button>
                    <button formaction="${pageContext.servletContext.contextPath}/users?action=delete&userId=${user.id}" formmethod="post">
                        Delete
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<form>
    <button formaction="${pageContext.servletContext.contextPath}/create-user" formmethod="get">Create new user</button>
</form>
</body>
</html>
