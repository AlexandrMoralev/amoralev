<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Update user</title>
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
            var country = "<c:out value="${user.address.country}"/>";
            $.ajax({
                url: "./addresses?countries=true",
                method: "GET",
                data: "",
                complete: function (data) {
                    var result = "<option></option>";
                    var countries = data.responseText.split(',');
                    for (var i = 0; i < countries.length; i++) {
                        var selected = country === countries[i] ? "selected" : "";
                        result += "<option " + selected + " value=" + countries[i] + ">" + countries[i] + "</option>"
                    }
                    document.getElementById("country").innerHTML = result;
                }
            });
            fillCity();
        });

        function fillCity() {
            var country = "<c:out value="${user.address.country}"/>";
            var city = "<c:out value="${user.address.city}"/>";
            $.ajax({
                url: "./addresses?country=${user.address.country}",
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
                        var selected = city === cities[i] ? "selected" : "";
                        result += "<option " + selected + " value=" + cities[i] + ">" + cities[i] + "</option>";
                    }
                    document.getElementById("city").innerHTML = result;
                }
            });
        }

        function getCityByCountry() {
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

        $(function () {
            $('#fileUpload').on('submit', function (e) {
                e.preventDefault();
                var form = $(this);
                var data = new FormData();
                var filesField = form.find('input[type="file"]');
                var file = filesField.prop('files')[0];
                var fileName = file.name;
                data.append(fileName, file);

                $.ajax({
                    url: "<%=request.getContextPath()%>/upload",
                    method: "POST",
                    data: data,
                    async: false,
                    contentType: false,
                    cache: false,
                    processData: false,
                    complete: function (data) {
                        $('input[name="photoId"]').val(fileName);
                    }
                });
                e.preventDefault();
            })
        });
    </script>
    <style rel="stylesheet" type="text/css">
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div class="container">
    <h1>Users web-app</h1>
    <br>
    <h2>Update user</h2>
    <div class="form-group">
        <c:set var="fileId" value="${false}" scope="request"/>
        <c:if test="${not empty user.photoId}">
            <c:set var="fileId" value="${user.photoId}" scope="request"/>
        </c:if>
        <form action="${pageContext.servletContext.contextPath}/users" method="post">
            <input type="hidden" name="userId" value="${user.id}"/>
            <input type="hidden" name="addressId" value="${user.address.id}">
            <input type='hidden' name='action' value='update'>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="name">Name:</label>
                <div class="col-sm-4">
                    <input type="text"
                           class="form-control"
                           name="name"
                           id="name"
                           value="${user.name}"
                           required>
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="login">Login:</label>
                <div class="col-sm-4">
                    <input type="text"
                           class="form-control"
                           name="login" id="login"
                           value="${user.login}"
                           required>
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
                           value="${user.password}"
                           required>
                </div>
            </div>
            <div class="form-group row">
                <c:if test="${sessionScope.role == 'root' || sessionScope.role == 'admin'}">
                    <label class="control-label col-sm-1" for="role">Role:</label>
                    <div class="col-sm-4">
                        <select class="form-control" name="role" id="role" title="Choose role" required>
                            <option value="admin">admin</option>
                            <option value="manager">manager</option>
                            <option value="user">user</option>
                            <option value="maintenance">maintenance</option>
                            <option value="guest">guest</option>
                        </select>
                    </div>
                </c:if>
                <c:if test="${sessionScope.role != 'root' && sessionScope.role != 'admin'}">
                    <input type="hidden" name="role" value="user">
                </c:if>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="country">Country:</label>
                <div class="col-sm-4">
                    <select class="form-control"
                            name="country"
                            id="country"
                            value="${user.address.country}"
                            onchange="getCityByCountry()"
                            required>
                    </select>
                </div>
            </div>
            <div class="form-group row">
                <label class="control-label col-sm-1" for="city">City:</label>
                <div class="col-sm-4">
                    <select class="form-control"
                            name="city"
                            id="city"
                            value="${user.address.city}"
                            required>
                    </select>
                </div>
            </div>

            <div class="form-group row">
                <div class="col-sm-4">
                    <input type="hidden"
                           class="form-control"
                           name="photoId"
                           id="photoId"
                           value="${fileId}">
                </div>
            </div>

            <div class="col-sm-offset-0">
                <button type="submit" class="btn btn-primary btn-green">Update user</button>
            </div>
        </form>
    </div>
    <div class="form-group">
        <form id="fileUpload">
            <div class="checkbox">
                <label for="file"></label>
                <input id="file" type="file" name="file">
            </div>
            <button type="submit" class="btn btn-info">Загрузить файл</button>
        </form>
    </div>
    <div class="form-group">
        <form action="${pageContext.servletContext.contextPath}/users" method="get">
            <div class="col-sm-offset-0">
                <button type="submit" class="btn btn-info btn-green">Return to users list</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>
