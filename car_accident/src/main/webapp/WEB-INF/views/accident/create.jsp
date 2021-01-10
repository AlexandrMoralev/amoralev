<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
      integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">

<title>Save accident</title>

<body>

<div class="container">

    <h2>Save accident</h2>

    <form action="<c:url value='/save'/>" method='POST'>
        <div class="form-group">
            <label>Название:</label>
            <input type="text" class="form-control" name="name">
        </div>
        <div class="form-group">
            <label>Тип:</label>
            <select class="form-control" name="typeId">
                <c:forEach var="type" items="${types}" >
                    <option class="form-control" value="${type.id}">${type.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Статьи:</label>
            <select class="form-control" name="rIds" multiple>
                <c:forEach var="rule" items="${rules}" >
                    <option class="form-control" value="${rule.id}">${rule.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>Описание:</label>
            <input type="text" class="form-control" name="text">
        </div>
        <div class="form-group">
            <label>Адрес:</label>
            <input type="text" class="form-control" name="address">
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary">Сохранить</button>
        </div>
    </form>
</div>


<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"
        integrity="sha384-OgVRvuATP1z7JjHLkuOU7Xw704+h835Lr+6QL9UvYjZE3Ipu6Tp75j7Bh/kR0JKI"
        crossorigin="anonymous"></script>

</body>
</html>