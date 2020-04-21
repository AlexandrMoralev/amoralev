<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>User App login page</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script>
        function validate() {
            var result = true;
            var login = $('#login');
            var password = $('#password');
            var message = '';
            if (login.val() === '') {
                message += login.attr('placeholder') + '\n';
                result = false;
            }
            if (password.val() === '') {
                message += password.attr('placeholder') + '\n';
                result = false;
            }
            if (!result) {
                alert(message);
            }
            return result;
        }
    </script>
    <style rel="stylesheet" type="text/css">
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div class="container-fluid">
    <h1>Users web-app</h1>
    <br>
    <h2>Login page</h2>
    <form class="form-horizontal" action="${pageContext.servletContext.contextPath}/login-view" method="POST">
        <div class="form-group">
            <c:if test="${not empty requestScope.error}">
                <div class="alert alert-warning errorMessage alert-dismissible">
                    <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                    <c:out value="${requestScope.error}"/>
                </div>
            </c:if>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="login">Login:</label>
            <div class="col-sm-4">
                <input type="text"
                       class="form-control"
                       name="login"
                       id="login"
                       placeholder="Enter login">
            </div>
        </div>
        <div class="form-group">
            <label class="control-label col-sm-2" for="password">Password:</label>
            <div class="col-sm-4">
                <input type="password"
                       class="form-control"
                       maxlength="16"
                       name="password"
                       id="password"
                       placeholder="Enter password">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="submit"
                       class="btn btn-primary btn-green"
                       name="action"
                       value="Sign in"
                       onclick="return validate()">
                <form>
                    <button class="btn btn-default btn-green"
                            formaction="${pageContext.servletContext.contextPath}/create-user"
                            formmethod="GET">
                        Registration
                    </button>
                </form>
            </div>
        </div>
    </form>
</div>
</body>
</html>
