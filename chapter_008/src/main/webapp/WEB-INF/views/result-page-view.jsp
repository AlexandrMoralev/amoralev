<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Result</title>
    <meta http-equiv='refresh' content='2; url=${pageContext.servletContext.contextPath}/users'>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
    <style rel="stylesheet" type="text/css">
        <%@include file="/WEB-INF/css/styles.css" %>
    </style>
</head>
<body>
<div class="form-group">
    <c:if test="${not empty param.get('result')}">
        <c:set var="result" value="${param.get('result')}"/>
        <c:if test="${result eq 'Action error'}">
            <div class="alert alert-warning errorMessage alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <c:out value="${result}"/>
            </div>
        </c:if>
        <c:if test="${result ne 'Action error'}">
            <div class="alert alert-warning infoMessage alert-dismissible">
                <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
                <c:out value="${result}"/>
            </div>
        </c:if>
    </c:if>
</div>
</body>
</html>
