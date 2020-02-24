<%@ page import="ru.job4j.crudservlet.User" %>
<%@ page import="java.util.Collection" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All users</title>
</head>
<body>
<h1>All users</h1>
<br>
<table style="width:auto; height:auto;" border = "2">
    <% Collection<User> users = (Collection<User>) request.getAttribute("userList");%>
        <tr>
            <th>id</th>
            <th>name</th>
            <th>login</th>
            <th>email</th>
            <th>created</th>
            <th>actions</th>
        </tr>
    <% for (User user : users) {%>
        <tr>
            <td> <%=user.getId()%></td>
            <td> <%=user.getName()%></td>
            <td> <%=user.getLogin()%></td>
            <td> <%=user.getEmail()%></td>
            <td> <%=user.getCreated()%></td>
        <td><form>
            <button formaction='<%=request.getContextPath()%>/update' formmethod='get' name='id' value='<%=user.getId()%>'>Update</button>
            <button formaction='<%=request.getContextPath()%>/list?action=delete&id=<%=user.getId()%>' formmethod='post'>Delete</button>
        </form></td>
        </tr>
        <% } %>
</table><br>
<form>
    <button formaction='<%=request.getContextPath()%>/create' formmethod='get'>Create user</button>
</form></body></html>
