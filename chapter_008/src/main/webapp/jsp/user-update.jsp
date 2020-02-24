<%@ page import="ru.job4j.crudservlet.User" %>
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Update user</title>
</head>
<body>

<h1>Update user</h1>
<br>
<% User user = (User) request.getAttribute("user"); %>
        <table style="width:60%; height:auto;" border = "1">
    <form action='<%=request.getContextPath()%>/list' method='post'>
        <tr>
            <td>Name : <input type='text' name='name' value='<%=user.getName()%>'/></td>
            <td>Login : <input type='text' name='login' value='<%=user.getLogin()%>'/></td>
            <td>e-mail : <input type='text' name='email' value='<%=user.getEmail()%>'/></td>
            <td><input type='hidden' name='id' value='<%=user.getId()%>'></td>
            <td><input type='submit' name='action' value='update'></td>
        </tr>
    </form>
</table><br>
<form>
    <button formaction='<%=request.getContextPath()%>/list' formmethod='get'>Cancel</button>
</form>
</body>
</html>
