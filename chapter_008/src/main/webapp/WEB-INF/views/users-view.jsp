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
<h1>All users</h1>
<br>
<table style="width:auto; height:auto;" border="2">
    <tr>
        <th>id</th>
        <th>name</th>
        <th>login</th>
        <th>email</th>
        <th>created</th>
        <th>actions</th>
    </tr>
    <c:forEach items="${requestScope.userList}" var="user">
        <tr>
            <td> ${user.id} </td>
            <td> ${user.name}</td>
            <td> ${user.login}</td>
            <td> ${user.email}</td>
            <td> ${user.created}</td>
            <td>
                <form>
                    <button formaction='${requestScope.contextPath}/update-user' formmethod='get' name='id' value='${user.id}'>
                        Update
                    </button>
                    <button formaction='${requestScope.contextPath}/?action=delete&id=${user.id}' formmethod='post'>
                        Delete
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
<br>
<form>
    <button formaction='${requestScope.contextPath}/create-user' formmethod='get'>Create user</button>
</form>
</body>
</html>
