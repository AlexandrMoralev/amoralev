<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!-- Required meta tags -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- Bootstrap CSS -->
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css"
      integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">

<title>Update accident</title>

<body>

<div class="container">

    <h2>Update accident</h2>

    <c:if test="${not empty error}">
        <div class="alert alert-warning errorMessage alert-dismissible">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            <c:out value="${error}"/>
        </div>
    </c:if>

    <form action="<c:url value='/edit'/>" method='POST'>
        <div class="form-group">
            <input type="hidden" class="form-control" name="id" value="${accident.id}">
        </div>
        <div class="form-group">
            <input type="hidden" class="form-control" name="typeId" value="${accident.type.id}">
        </div>
        <div class="form-group">
            <input type="hidden" class="form-control" name="rIds" value="${rIds}">
        </div>
        <div class="form-group">
            <label>Название:</label>
            <input type="text" class="form-control" name="name" value="${accident.name}">
        </div>
        <div class="form-group">
            <label>Описание:</label>
            <input type="text" class="form-control" name="text" value="${accident.text}">
        </div>
        <div class="form-group">
            <label>Адрес:</label>
            <input type="text" class="form-control" name="address" value="${accident.address}">
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