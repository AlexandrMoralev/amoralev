<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Register new user</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <script>
        function validate() {
            var result = true;
            var name = $('#name');
            var login = $('#login');
            var password = $('#password');
            var message = '';
            if (name.val() === '') {
                message += name.attr('placeholder') + '\n';
                result = false;
            }
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

        $(document).ready(function () {
            $.ajax({
                url: "./addresses?countries=true",
                method: "GET",
                data: "",
                complete: function (data) {
                    var result = "<option></option>";
                    var countries = data.responseText.split(',');
                    for (var i = 0; i < countries.length; i++) {
                        result += "<option value=" + countries[i] + ">" + countries[i] + "</option>";
                    }
                    document.getElementById("country").innerHTML = result;
                }
            });
        });

        function getCity() {
            $.ajax({
                url: "./addresses?country=" + $("#country").val().trim(),
                method: "GET",
                data: "",
                complete: function (data) {
                    var result = "<option></option>";
                    var cities = data.responseText.split(';')
                        .map(function (addr) {
                            var fromIndex = addr.indexOf("city");
                            var toIndex = addr.indexOf("'}");
                            return addr.slice(fromIndex, toIndex).split("='")[1];
                        });
                    for (var i = 0; i < cities.length; i++) {
                        result += "<option value=" + cities[i] + ">" + cities[i] + "</option>";
                    }
                    document.getElementById("city").innerHTML = result;
                }
            });
        }
    </script>
    <style rel="stylesheet" type="text/css">
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div class="container">
    <h1>Users web-app</h1>
    <br>
    <h2>Register new user</h2>
    <div class="form-group">
        <form action="${pageContext.servletContext.contextPath}/users" method="POST">
            <div class="form-group row">
                <label class="control-label col-sm-1" for="name">Name:</label>
                <div class="col-sm-4">
                    <input type="text"
                           class="form-control"
                           name="name"
                           id="name"
                           title="Enter name">
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="login">Login:</label>
                <div class="col-sm-4">
                    <input type="text"
                           class="form-control"
                           name="login"
                           id="login"
                           title="Enter login">
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="password">Password:</label>
                <div class="col-sm-4">
                    <input type="password"
                           class="form-control"
                           maxlength="16"
                           name="password"
                           id="password"
                           title="Enter password">
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="role">Role:</label>
                <div class="col-sm-4">
                    <select class="form-control" name="role" id="role" title="Choose role">
                        <option value="admin">admin</option>
                        <option value="manager">manager</option>
                        <option value="user">user</option>
                        <option value="maintenance">maintenance</option>
                        <option value="guest">guest</option>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="country">Country:</label>
                <div class="col-sm-4">
                    <select class="form-control"
                            id="country"
                            name="country"
                            onchange="getCity()"
                            required>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="city">City:</label>
                <div class="col-sm-4">
                    <select class="form-control"
                            id="city"
                            name="city"
                            required></select>
                </div>
            </div>
            <div class="col-sm-offset-0">
                <input type="hidden"
                       class="btn btn-primary btn-green"
                       name="action"
                       value="create">
                </input>
                <button type="submit" class="btn btn-primary btn-green" onclick="return validate()">Create new user
                </button>
            </div>
        </form>
    </div>
    <div class="form-group">
        <form action="${pageContext.servletContext.contextPath}/users" method="GET">
            <div class="col-sm-offset-0">
                <button type="submit" class="btn btn-info btn-green">Return to users list</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
