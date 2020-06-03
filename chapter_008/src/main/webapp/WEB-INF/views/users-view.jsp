<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All users</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <style rel="stylesheet" type="text/css">
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div class="container">
    <h1>Users web-app</h1>
    <br>
    <h2>All users</h2>

    <table class="table table-hover" id="table">
        <thead>
        <tr>
            <th>id</th>
            <th>name</th>
            <th>login</th>
            <th>country</th>
            <th>city</th>
            <th>role</th>
            <th>photo</th>
            <th colspan="3">action</th>
        </tr>
        </thead>
        <tbody id="datatable">
        <c:forEach items="${requestScope.userList}" var="user">
            <c:set var="hasPhoto" value="${false}" scope="request"/>
            <c:if test="${not empty user.photoId}">
                <c:set var="hasPhoto" value="${true}" scope="request"/>
            </c:if>
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.login}</td>
                <td>${user.address.country}</td>
                <td>${user.address.city}</td>
                <td>${user.role.description}</td>
                <td>
                    <c:if test="${hasPhoto}">
                        <img src="${pageContext.servletContext.contextPath}/download?name=${user.photoId}" width="100px"
                            height="100px"/></td>
                    </c:if>
                    <c:if test="${!hasPhoto}">
                        <span>no photo</span>
                    </c:if>
                <td>
                    <form action="${pageContext.servletContext.contextPath}/update-user" method="GET">
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <button type="submit" class="btn btn-info btn-sm btn-green">Update user</button>
                    </form>
                </td>
                <td>
                    <form action="${pageContext.servletContext.contextPath}/users?action=delete&userId=${user.id}"
                          method="POST">
                        <input type="hidden" name="userId" value="${user.id}"/>
                        <button type="submit" class="btn btn-info btn-sm btn-green">Delete user</button>
                    </form>
                </td>
                <c:if test="${hasPhoto}">
                <td>
                        <a class="btn btn-sm btn-green" href="${pageContext.servletContext.contextPath}/download?name=${user.photoId}">Download
                            photo</a>
                </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="form-group">
        <form action="${pageContext.servletContext.contextPath}/create-user" method="GET">
            <button type="submit" class="btn btn-primary btn-green" onclick="return validate()">Add user</button>
        </form>
    </div>
</div>
</body>
</html>
