<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Result</title>
    <meta http-equiv='refresh' content='2; url=${pageContext.servletContext.contextPath}/'>
</head>
<body>
<c:if test="${param.get('result') eq '1'}">
    <c:out value="Action completed successfully"/>
</c:if>
<c:if test="${param.get('result') ne '1'}">
    <c:out value="Action error"/>
</c:if>
</body>
</html>
